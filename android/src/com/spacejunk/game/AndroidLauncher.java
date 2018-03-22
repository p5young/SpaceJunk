package com.spacejunk.game;

import android.Manifest;
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
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.interfaces.SystemServices;

import java.io.File;
import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements SystemServices {


	private static String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
	private static final int PERMS_REQUEST_CODE = 200;

	private static final String SCREEN_SHARE_FILE_PATH = "/sdcard/Android/data/screen_capture.mp4";

	public static final String TAG = "AndroidLauncher";

	public static final int WRITE_REQUEST_CODE = 7;

	private static boolean hasRecordingStarted;
	private static boolean hasRecordingStopped;

	private static boolean writeAccepted = false;


	private static final int PERMISSION_CODE = 1;
	private static final int FACEBOOK_CODE = 3;
	private int mScreenDensity;
	private MediaProjectionManager mProjectionManager;


	private static final int DISPLAY_WIDTH = 480;
	private static final int DISPLAY_HEIGHT = 640;


	CallbackManager callbackManager;
	ShareDialog shareDialog;


	private MediaProjection mMediaProjection;
	private VirtualDisplay mVirtualDisplay;
	private MediaProjectionCallback mMediaProjectionCallback;
	private ToggleButton mToggleButton;
	private MediaRecorder mMediaRecorder;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		hasRecordingStarted = false;

		Log.i("androidlog", "About to ask permissions");
		ContextCompat.checkSelfPermission(this, perms[0]);
		Log.i("androidlog", "About to ask initialzie recording tools");
		initializeScreenRecordingTools();
		initializeFacebookSDK();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new SpaceJunk(SpaceJunk.DIFFICULTY_LEVEL.EASY, this), config);


//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//				this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
//			}
//
//
//		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMediaProjection != null) {
			mMediaProjection.stop();
			mMediaProjection = null;
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {


		if (requestCode == FACEBOOK_CODE) {


			if(resultCode != RESULT_OK) {
				Log.i("facebooklog", "BAD RESULT RETURN FACEBOOK");
				return;
			}

			callbackManager.onActivityResult(requestCode, resultCode, data);

			return;
		}


		if(requestCode == PERMS_REQUEST_CODE) {

			if (resultCode != RESULT_OK) {
				Toast.makeText(this,
						"Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
				mToggleButton.setChecked(false);
				return;
			}

			Toast.makeText(this, "on Activity result here", Toast.LENGTH_SHORT).show();
			mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
			mMediaProjection.registerCallback(mMediaProjectionCallback, null);
			mVirtualDisplay = createVirtualDisplay();
			mMediaRecorder.start();
		}

	}


	@Override
	public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

		switch(permsRequestCode){

			case PERMS_REQUEST_CODE:
				writeAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
				break;
			default:

		}

	}


	@Override
	public void startRecording(String filePath) {

		if(!hasRecordingStarted) {

			Log.i("androidlog", "In android mode now baby with filepath: " + filePath);
			hasRecordingStarted = true;

			shareScreen();

		}

	}

	@Override
	public void stopRecording() {

		if(!hasRecordingStopped) {
			Log.i("androidlog", "In android mode now stopRecodring: ");
			hasRecordingStopped = true;
			stopScreenSharing();

			beginVideoSharing();
		}
	}


	private void beginVideoSharing() {


		File dir = Environment.getExternalStorageDirectory();
		File dcim = new File(dir.getAbsolutePath() + "/Android/data/screen_capture.mp4");
		Uri videoUri = Uri.fromFile(dcim);
		Log.i("facebooklog", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android");
		Uri shareFileUri = Uri.parse("/storage/emulated/0/Android/data/screen_capture.mp4");


		if (ShareDialog.canShow(ShareLinkContent.class)) {

			ShareVideo shareVideo = new ShareVideo.Builder()
					.setLocalUrl(videoUri)
					.build();

			ShareVideoContent videoContent = new ShareVideoContent.Builder()
					.setVideo(shareVideo)
					.setContentDescription("description of the video")
					.setContentTitle("content title")
					.build();

			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle("Some title")
					.setContentDescription("some description")
					.setImageUrl(Uri.parse("http://png-3.findicons.com/files/icons/1782/classic_blue/256/classic_blue_android.png"))
					.setContentUrl(Uri.parse("https://fb.me/390376541148098"))
					.build();

			Log.i("facebooklog", "About to show share dialog");
//            shareDialog.show(content);

			ShareDialog.show(this, videoContent);
		}


	}





	private void initializeFacebookSDK() {
		callbackManager = CallbackManager.Factory.create();
//		shareDialog = new ShareDialog(this);


		// this part is optional
//		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//			@Override
//			public void onSuccess(Sharer.Result result) {
//				Log.i("facebooklog","Successsss!");
//			}
//
//			@Override
//			public void onCancel() {
//
//			}
//
//			@Override
//			public void onError(FacebookException error) {
//
//			}
//		});
	}


	private VirtualDisplay createVirtualDisplay() {
		return mMediaProjection.createVirtualDisplay("MainActivity",
				DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
				mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
	}

	private void initializeScreenRecordingTools() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenDensity = metrics.densityDpi;

		mMediaRecorder = new MediaRecorder();
		initRecorder();
		prepareRecorder();

		mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

		mMediaProjectionCallback = new MediaProjectionCallback();
	}


	private void stopScreenSharing() {
		if (mVirtualDisplay == null) {
			return;
		}
		mVirtualDisplay.release();
		//mMediaRecorder.release();
	}


	private void shareScreen() {
		if (mMediaProjection == null) {
			startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
			return;
		}
		mVirtualDisplay = createVirtualDisplay();
		mMediaRecorder.start();
	}


	private boolean shouldAskPermission(){

		return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

	}





	//---------------------------------------------
	//---------------------------------------------

	private class MediaProjectionCallback extends MediaProjection.Callback {
		@Override
		public void onStop() {
			if (hasRecordingStarted) {
				mMediaRecorder.stop();
				mMediaRecorder.reset();
				Log.v(TAG, "Recording Stopped");
				initRecorder();
				prepareRecorder();
			}
			mMediaProjection = null;
			stopScreenSharing();
			Log.i(TAG, "MediaProjection Stopped");
		}
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

	private void initRecorder() {
//		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
		mMediaRecorder.setVideoFrameRate(30);
		mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		mMediaRecorder.setOutputFile(SCREEN_SHARE_FILE_PATH);
	}


}
