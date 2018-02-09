package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.SpaceJunk;

/**
 * Created by vidxyz on 2/9/18.
 */

public abstract class Character {


    public enum PLATFORM {TOP, MIDDLE, BOTTOM}

    PLATFORM currentPlatform;
    SpaceJunk currentGame;

    // Positional identifiers
    int currentX, currentY;
    int initialY, initialX;
    int targetY;

    int topPlatformY, middlePlatformY, bottomPlatformY;

    Texture[] characterTextures = new Texture[3];


    public void create() {

        initialY = Gdx.graphics.getHeight() / 2 - characterTextures[0].getWidth() / 2;
        initialX = Gdx.graphics.getWidth() / 9;

        topPlatformY = currentGame.getyMax() / 6;
        middlePlatformY = topPlatformY + currentGame.getyMax() / 3;
        bottomPlatformY = middlePlatformY + currentGame.getyMax()  / 3;

        Gdx.app.log("applog", "Top platform y is " + topPlatformY);
        Gdx.app.log("applog", "Middle platform y is " + middlePlatformY);
        Gdx.app.log("applog", "Bottom platform y is " + bottomPlatformY);

        currentY = initialY;
        targetY = middlePlatformY;
    }

    public void moveCharacter(int y) {

        // Bottom half of screen tapped
        if (y < (currentGame.getyMax() / 2)) {
            updatePlatform(false);
        }
        // Top half is tapped
        else {
            updatePlatform(true);
            // Making sure we don't go up a platform while already at the top most
        }
    }

    public void updateCharacterPosition() {

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

    public int getTopPlatformY() {
        return topPlatformY;
    }

    public int getMiddlePlatformY() {
        return middlePlatformY;
    }

    public int getBottomPlatformY() {
        return bottomPlatformY;
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

    public Texture[] getCharacterTextures() {
        return characterTextures;
    }
}
