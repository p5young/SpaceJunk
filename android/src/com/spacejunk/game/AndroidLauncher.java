package com.spacejunk.game;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.spacejunk.game.interfaces.SystemServices;

import java.io.File;
import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements SystemServices {


	private static String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE",
			"android.permission.READ_EXTERNAL_STORAGE",
			"android.permission.RECORD_AUDIO"};

	private static final String SCREEN_SHARE_FILE_PATH = "/temp_facebook_video.mp4";

	public static final String TAG = "AndroidLauncher";

	public static final int WRITE_REQUEST_CODE = 7;
	public static final int AUDIO_REQUEST_CODE = 13;

	private static boolean writePermissionAccepted = false;
	private static boolean writePermissionRequested = false;
	private static boolean recordAudioPermissionAccepted = false;
	private static boolean recordAudioPermissionRequested = false;

	boolean userRequestedRecordAudioSetting = false;



	private static final int PERMISSION_CODE = 1;
	private static final int FACEBOOK_CODE = 3;
	private static int numberOfPermissionsRequested = 0;

	private int mScreenDensity;
	private MediaProjectionManager mProjectionManager;


	private static int DISPLAY_WIDTH = 640;
	private static int DISPLAY_HEIGHT = 480;


	CallbackManager callbackManager;

	private MediaProjection mMediaProjection;
	private VirtualDisplay mVirtualDisplay;
	private MediaRecorder mMediaRecorder;

	private SpaceJunk game;

	private void initializeGame() {

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		game = new SpaceJunk(SpaceJunk.DIFFICULTY_LEVEL.EASY, this);
		initialize(game, config);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeFacebookSDK();
		initializeGame();

	}


	@Override
	public void onDestroy() {
		super.onDestroy();


		if (mMediaProjection != null) {
			mMediaProjection.stop();
			mMediaProjection = null;
		}

		deleteTempFile();

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == FACEBOOK_CODE) {

			if(resultCode != RESULT_OK) {
				return;
			}

			callbackManager.onActivityResult(requestCode, resultCode, data);
			return;
		}


		if(requestCode == PERMISSION_CODE) {

			if (resultCode != RESULT_OK) {
				Toast.makeText(this, getString(R.string.screen_record_denied), Toast.LENGTH_SHORT).show();
				// Uh-oh all permissions went through but screen record denied
				// Let the game know so it can stop flashing red record
				game.stopScreenFlashing();
				return;
			}


			// Start recording now, everything is success. All (3) permissions have been granted
			mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
			mVirtualDisplay = createVirtualDisplay();
			mMediaRecorder.start();
			Toast.makeText(this, getString(R.string.screen_record_accepted), Toast.LENGTH_SHORT).show();

		}

	}


	@Override
	public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

		switch(permsRequestCode){

			case WRITE_REQUEST_CODE:
				writePermissionAccepted = (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED);
				writePermissionRequested = true;
				numberOfPermissionsRequested++;
				break;
			case AUDIO_REQUEST_CODE:
				recordAudioPermissionAccepted = (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED);
				recordAudioPermissionRequested = true;
				numberOfPermissionsRequested++;
				break;
			default:
				break;

		}


		if(writePermissionAccepted &&  (recordAudioPermissionAccepted || recordAudioPermissionRequested)) {
			initializeScreenRecordingTools(userRequestedRecordAudioSetting);
			shareScreen();
		}
		else {
			// If write permission is not accepted, then we can't record anything anyway
			if(!writePermissionAccepted) {
				// Let user know
				Toast.makeText(this, getString(R.string.screen_record_denied), Toast.LENGTH_SHORT).show();
				// Let the game know recording isn't happening so it can stop blinking red
				game.stopScreenFlashing();
			}
		}

	}


	@Override
	public void startRecording(int xMax, int yMax, boolean recordAudioSetting) {
//		DISPLAY_HEIGHT = yMax;
//		DISPLAY_WIDTH = xMax;
		userRequestedRecordAudioSetting = recordAudioSetting;

		// Permissions only a problem android M and above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			handlePermissionsAndProceed();
		}
		// Else no problem, permissions already given. Start sharing screen now
		else {
			initializeScreenRecordingTools(userRequestedRecordAudioSetting);
			shareScreen();
		}


	}

	@Override
	public void stopRecording() {
		stopScreenSharing();
		beginVideoSharing();
	}

	private void handlePermissionsAndProceed() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// If files cant be accessed, audio permission makes no sense
			// So we ask for write access first to store the temp video file we record
			if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


				// If record audio permission doesn't exist, add this to the top of the stack
				if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
				} else {
					recordAudioPermissionAccepted = true;
				}

				// Request file handling permissions now
				// This asks for request asynchronously
				this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);

				// If we reach here, we have the audio permission, but not file write. It's useless
				// Handle result in callback of permissions request
				return;
			}


			// If we already have write access, we ask now only for record audio permission
			else {

				writePermissionAccepted = true;

				// If record audio permission doesn't exist, add ask for it
				if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
					this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);

					// We now handle the recording in the callback again, as we are waiting on a permission request/deny
					return;
				} else {
					recordAudioPermissionAccepted = true;
				}
			}

			// If we have reached here, this means that both permissions are approved
			initializeScreenRecordingTools(userRequestedRecordAudioSetting);
			shareScreen();
		}

	}


	private void deleteTempFile() {

		File fdelete = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + SCREEN_SHARE_FILE_PATH);

		if (fdelete.exists()) {
			fdelete.delete();
		}

	}

	private void beginVideoSharing() {

		File dcim = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + SCREEN_SHARE_FILE_PATH);
		Uri videoUri = Uri.fromFile(dcim);

		if (ShareDialog.canShow(ShareLinkContent.class)) {

			ShareVideo shareVideo = new ShareVideo.Builder()
					.setLocalUrl(videoUri)
					.build();

			ShareVideoContent videoContent = new ShareVideoContent.Builder()
					.setVideo(shareVideo)
					.build();


			ShareDialog.show(this, videoContent);
		}

	}

	private void initializeFacebookSDK() {
		callbackManager = CallbackManager.Factory.create();
	}


	private VirtualDisplay createVirtualDisplay() {
		return mMediaProjection.createVirtualDisplay("AndroidLauncher",
				DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
				mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
	}

	private void initializeScreenRecordingTools(boolean recordAudioSetting) {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenDensity = metrics.densityDpi;

		mMediaRecorder = new MediaRecorder();
		initRecorder(recordAudioSetting);
		prepareRecorder();

		mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

	}


	private void stopScreenSharing() {

		if (mVirtualDisplay == null) {
			return;
		}


		mVirtualDisplay.release();
		mMediaRecorder.release();

		// Preparing for any future recordings
		mMediaRecorder = new MediaRecorder();
	}


	private void shareScreen() {

		if (mMediaProjection == null) {
			startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
			return;
		}

		mVirtualDisplay = createVirtualDisplay();
		mMediaRecorder.start();
	}


	private void prepareRecorder() {
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			finish();
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
	}

	private void initRecorder(boolean recordAudioSetting) {

		if (recordAudioPermissionAccepted && recordAudioSetting) {
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		}

		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

		if (recordAudioPermissionAccepted && recordAudioSetting) {
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		}

		mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
		mMediaRecorder.setVideoFrameRate(30);
		mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + SCREEN_SHARE_FILE_PATH);
	}


}
