package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
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

    Rectangle characterShape;

    Animation<TextureRegion> characterAnimation; // Must declare frame type (TextureRegion)
    Texture animationSheet;

    protected int FRAME_COLS;
    protected int FRAME_ROWS;


    public void create() {

        characterShape = new Rectangle();

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


        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(animationSheet,
                animationSheet.getWidth() / FRAME_COLS,
                animationSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] characterFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                characterFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        characterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);
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

    public void updateCharacterShapeCoordinates() {
        this.characterShape.set(this.initialX,
                this.currentY,
                this.characterTextures[0].getWidth() / 2,
                this.characterTextures[0].getHeight() / 2); //XY Coordinate and radius
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

    public Animation<TextureRegion> getCharacterAnimation() {
        return characterAnimation;
    }

    public Rectangle getCharacterShape() {
        return characterShape;
    }
}
