package com.spacejunk.game.interfaces;

/**
 * Created by vidxyz on 3/21/18.
 */

public interface SystemServices {

    public void setSpeed(int speed);
    public int getSpeed();

    public void setSettings(boolean sound, boolean record, boolean vibrate);
    public boolean[] getSettings();

    public void startRecording(int xMax, int yMax, boolean recordAudioSetting);
    public void stopRecording();
}
