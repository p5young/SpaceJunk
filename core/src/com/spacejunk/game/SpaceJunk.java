package com.spacejunk.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.characters.Astronaut;
import com.spacejunk.game.characters.Character;
import com.spacejunk.game.interfaces.GameServices;
import com.spacejunk.game.interfaces.SystemServices;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/4/18.
 * This is the main game object
 * Holds all metadata pertaining to the current game being played
 */


public class SpaceJunk extends Game implements ApplicationListener, GameServices {


    public enum DIFFICULTY_LEVEL {EASY, MEDIUM, HARD};

    private DIFFICULTY_LEVEL currentDifficultyLevel;

    // Canvas size
    int xMax, yMax;

    private double currentGameScore;
    private SystemServices systemServices;

    private Level level;
    private Character character;

    public MyAssetManager manager;
    private GameScreen currentGameScreen;

    public SpaceJunk(DIFFICULTY_LEVEL level, SystemServices systemServices) {

        this.currentDifficultyLevel = level;
        this.systemServices = systemServices;
        // This can be changed as needed
        this.character = new Astronaut(this);
        this.currentGameScore = 0;
    }

    @Override
    public void stopScreenFlashing() {
        currentGameScreen.stopScreenFlashing();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        // reload all assets in assetManager
        manager.reload();
        // pause the game so the player has time to look at approaching obstacles before continuing
        if (this.currentGameScreen != null) {
            // Only pause if no recording is in progress, else we want to start recording right away
            if(!this.currentGameScreen.isRecordingInProgress()) {
                this.currentGameScreen.pause();
            }
        }
        //Gdx.app.log("applog", "RESUMING!!!!!!!!!!!!!!!!!");
    }


    @Override
    public void create () {

        setUpGame();
        currentGameScreen = new GameScreen(this);
        this.setScreen(currentGameScreen);
    }


    public void setUpGame() {

        manager = new MyAssetManager();

        xMax = Gdx.graphics.getWidth();
        yMax = Gdx.graphics.getHeight();

//        Gdx.app.log("applog", "xMax is " + xMax);
//        Gdx.app.log("applog", "yMax is " + yMax);

        character.create();

        // Updating the values needed from within the level class
        this.level = new Level(this);

        // We subtract 1/6th of the screen here to make up for the offset we caused earlier
        this.level.setPlatformCoordinates(
                5 * yMax / 6,
                3 * yMax / 6,
                1 * yMax/ 6);

        this.level.setMaxCoordinates(xMax, yMax);
        this.currentGameScore = 0;
    }

    @Override
    public void render () {
        super.render(); // important!
    }

    @Override
    public void dispose() {

    }


    public Level getLevel() {
        return this.level;
    }

    public int getxMax() {
        return this.xMax;
    }

    public int getyMax() {
        return this.yMax;
    }

    public Character getCharacter() {
        return this.character;
    }

    public MyAssetManager getManager() {
        return manager;
    }

    public double getCurrentGameScore() {
        return currentGameScore;
    }

    public void incrementGameScore() {
        this.currentGameScore += (this.level.getScoreRateMultiplier() * this.level.getScoringRate());
    }

    public void incrementGameScoreByBonus(double bonus) {
        this.currentGameScore += bonus;
    }

    public DIFFICULTY_LEVEL getCurrentDifficultyLevel() {
        return currentDifficultyLevel;
    }

    public SystemServices getSystemServices() {
        return systemServices;
    }

    // Class which holds all Textures and Pixmaps
    public class MyAssetManager extends AssetManager {
        private Pixmap asteroid;
        private Pixmap fire;
        private Pixmap alien;
        private Pixmap gas;

        public MyAssetManager(){
            reload();
        }

        // reloads all Pixmaps and Textures
        // called by constructor and resume function
        private void reload() {
            asteroid = new Pixmap(Gdx.files.internal("asteroid.png"));
            fire = new Pixmap(Gdx.files.internal("fire.png"));
            alien = new Pixmap(Gdx.files.internal("alien.png"));
            gas = new Pixmap(Gdx.files.internal("toxic_gas_green.png"));
            load("asteroid.png", Texture.class);
            load("asteroid_broken.png", Texture.class);
            load("fire.png", Texture.class);
            load("alien.png", Texture.class);
            load("toxic_gas_green.png", Texture.class);
            load("space_hammer.png", Texture.class);
            load("space_hammer_small.png", Texture.class);
            load("invisibility.png", Texture.class);
            load("invisibility_small.png", Texture.class);
            load("gas_mask.png", Texture.class);
            load("gas_mask_small.png", Texture.class);
            load("firesuit.png", Texture.class);
            load("firesuit_small.png", Texture.class);
            load("heart.png", Texture.class);
            finishLoading();
        }

        public Pixmap getPixmap(String filename) {
            if (filename.equals("asteroid.png")){
                return asteroid;
            } else if (filename.equals("fire.png")) {
                return fire;
            } else if (filename.equals("alien.png")) {
                return alien;
            } else {
                return gas;
            }
        }
    }


}

