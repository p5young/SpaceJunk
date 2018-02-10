package com.spacejunk.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.characters.Astronaut;
import com.spacejunk.game.characters.Character;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/4/18.
 * This is the main game object
 * Holds all metadata pertaining to the current game being played
 */


public class SpaceJunk extends Game {


    public enum DIFFICULTY_LEVEL {EASY, MEDIUM, HARD};


    private int currentYposition;
    private DIFFICULTY_LEVEL currentDifficultyLevel;

    // Canvas size
    int xMax, yMax;

    private double currentGameScore;


    private SpriteBatch canvas;
    private BitmapFont font;

    private Level level;
    private Character character;



    public SpaceJunk(DIFFICULTY_LEVEL level) {
        this.currentDifficultyLevel = level;
        // This can be chnaged as needed
        this.character = new Astronaut(this);
        this.currentGameScore = 0;
    }


    @Override
    public void create () {

        canvas = new SpriteBatch();
        // Use libGDX's default Arial font
        font = new BitmapFont();

        xMax = Gdx.graphics.getWidth();
        yMax = Gdx.graphics.getHeight();

        Gdx.app.log("applog", "create method of SpaceJunk called here");
        Gdx.app.log("applog", "xMax is " + xMax);
        Gdx.app.log("applog", "yMax is " + yMax);

        character.create();


        // Updating the values needed from within the level class
        this.level = new Level(this);

        Gdx.app.log("applog", "Level is initialized in create method of SpaceJunk");

        // We subtract 1/6th of the screen here to make up for the offset we caused earlier
        this.level.setPlatformCoordinates(0, Gdx.graphics.getHeight() / 3, 2 * Gdx.graphics.getHeight() / 3);
        this.level.setMaxCoordinates(xMax, yMax);
        this.level.generateInitialObstacles();

        this.setScreen(new GameScreen(this));

    }

    @Override
    public void render () {
        super.render(); // important!
    }

    public void dispose() {
        canvas.dispose();
        font.dispose();
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


    public double getCurrentGameScore() {
        return currentGameScore;
    }

    public void incrementGameScore() {
        this.currentGameScore += this.level.getScoringRate();
    }


}

