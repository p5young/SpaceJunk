package com.spacejunk.game;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.spacejunk.game.SpaceJunk;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;


		Log.i("applog", "SPACEJUNK IS BEING INITAILIZED HERE");

		initialize(new SpaceJunk(SpaceJunk.DIFFICULTY_LEVEL.EASY), config);
	}
}
