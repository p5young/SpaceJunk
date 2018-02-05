package com.spacejunk.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by vidxyz on 2/4/18.
 * This is the main game object
 * Holds all metadata pertaining to the current game being played
 */


public class SpaceJunk extends Game {


    public enum DIFFICULTY_LEVELS {EASY, MEDIUM, HARD};

    private int currentYposition;
    private DIFFICULTY_LEVELS currentDifficultyLevel;

    private SpriteBatch canvas;
    private BitmapFont font;

    public SpaceJunk(DIFFICULTY_LEVELS level) {
        this.currentDifficultyLevel = level;
    }


    @Override
    public void create () {
        canvas = new SpriteBatch();
        // Use libGDX's default Arial font
        font = new BitmapFont();
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


}

