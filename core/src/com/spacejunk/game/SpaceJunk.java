package com.spacejunk.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by vidxyz on 2/4/18.
 * This is the main game object
 * Holds all metadata pertaining to the current game being played
 */


public class SpaceJunk extends Game {


    public enum DIFFICULTY_LEVEL {EASY, MEDIUM, HARD};
    public enum PLATFORM {TOP, MIDDLE, BOTTOM}

    private int currentYposition;
    private DIFFICULTY_LEVEL currentDifficultyLevel;
    private PLATFORM currentPlatform;

    // Canvas size
    int xMax, yMax;

    // Positional identifiers
    int currentX, currentY;
    int initialY, initialX;
    int targetY;

    int topPlatformY, middlePlatformY, bottomPlatformY;

    private SpriteBatch canvas;
    private BitmapFont font;


    public SpaceJunk(DIFFICULTY_LEVEL level) {
        this.currentDifficultyLevel = level;
        this.currentPlatform = PLATFORM.MIDDLE;
    }


    @Override
    public void create () {


        canvas = new SpriteBatch();
        // Use libGDX's default Arial font
        font = new BitmapFont();

        xMax = Gdx.graphics.getWidth();
        yMax = Gdx.graphics.getHeight();

        Gdx.app.log("applog", "create method of SpaceJunk.java called here " + String.valueOf(xMax));
        Gdx.app.log("applog", "yMax is " + String.valueOf(yMax));


        Texture[] astronauts = new Texture[3];
        astronauts[0] = new Texture("astronaut_texture_1.png");
        astronauts[1] = new Texture("astronaut_texture_2.png");
        astronauts[2] = new Texture("astronaut_texture_3.png");

        initialY = Gdx.graphics.getHeight() / 2 - astronauts[0].getWidth() / 2;
        initialX = Gdx.graphics.getWidth() / 2;

        topPlatformY = yMax / 6;
        middlePlatformY = topPlatformY + yMax / 3;
        bottomPlatformY = middlePlatformY + yMax / 3;

        currentY = initialY;
        targetY = middlePlatformY;

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


    private void updatePlatform(boolean isGoingUp) {
        if(isGoingUp) {
            if(this.currentPlatform == PLATFORM.BOTTOM) {
                this.currentPlatform = PLATFORM.MIDDLE;
                this.targetY = middlePlatformY;
            }
            else if (this.currentPlatform == PLATFORM.MIDDLE) {
                this.currentPlatform = PLATFORM.TOP;
                this.targetY = topPlatformY;
            }
            // Else we are already on the top platform, so no point going up anymore
        }
        else {
            if(this.currentPlatform == PLATFORM.TOP) {
                this.currentPlatform = PLATFORM.MIDDLE;
                this.targetY = middlePlatformY;
            }
            else if(this.currentPlatform == PLATFORM.MIDDLE) {
                this.currentPlatform = PLATFORM.BOTTOM;
                this.targetY = bottomPlatformY;
            }
            // Else we already at the bottom, no point going futher down
        }
    }

    public void moveAstronaut(int y) {


        Gdx.app.log("applog", String.valueOf(y));

        // Bottom half of screen tapped
        if (y < (yMax / 2)) {
            updatePlatform(false);
        }
        // Top half is tapped
        else {
            updatePlatform(true);
            // Making sure we don't go up a platform while already at the top most
        }
    }

    public void updateAstronautPosition() {

        if(this.currentY < this.targetY) {
            if(this.currentY + 10 < this.targetY) {
                this.currentY += 10;
            }
            else {
                this.currentY = this.targetY;
            }
        }
        else if (this.currentY > this.targetY){
            if(this.currentY - 10 > this.targetY) {
                this.currentY -= 10;
            }
            else {
                this.currentY = this.targetY;
            }
        }
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getInitialX() {
        return this.initialX;
    }



}

