package com.spacejunk.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.spacejunk.game.GameScreen;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.consumables.Consumable;

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
    TextureRegion currentFrame;

    Rectangle characterShape;

    Animation<TextureRegion> defaultCharacterAnimation; // Must declare frame type (TextureRegion)
    Texture defaultAnimationSheet;

    Animation<TextureRegion> hammerCharacterAnimation;
    Texture hammerAnimationSheet;

    Animation<TextureRegion> fireSuitCharacterAnimation;
    Texture fireSuitAnimationSheet;

    Animation<TextureRegion> gasMaskCharacterAnimation;
    Texture gasMaskAnimationSheet;

    Animation<TextureRegion> invisibilityCharacterAnimation;
    Texture invisibilityAnimationSheet;

    protected int FRAME_COLS;
    protected int FRAME_ROWS;

    protected Pixmap pixmap;

    // Remaining Lives; how many hits can be taken
    private int remainingLives = GameConstants.MAX_LIVES;


    public void create() {

        FileHandle handle = Gdx.files.internal("astronaut_texture_1.png");
        this.pixmap = new Pixmap(handle);

        characterShape = new Rectangle();

        initialY = Gdx.graphics.getHeight() / 2 - characterTextures[0].getHeight() / 2;
        initialX = Gdx.graphics.getWidth() / 9;

        topPlatformY = 5 * Gdx.graphics.getHeight() / 6;
        middlePlatformY = 3 * Gdx.graphics.getHeight() / 6;
        bottomPlatformY = Gdx.graphics.getHeight() / 6;

        Gdx.app.log("applog", "Top platform y is " + topPlatformY);
        Gdx.app.log("applog", "Middle platform y is " + middlePlatformY);
        Gdx.app.log("applog", "Bottom platform y is " + bottomPlatformY);

        currentY = initialY;
        targetY = middlePlatformY;


        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(defaultAnimationSheet,
                defaultAnimationSheet.getWidth() / FRAME_COLS,
                defaultAnimationSheet.getHeight() / FRAME_ROWS);

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
        defaultCharacterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);


        // Have to do this 4 more times for each equipped Consumable
        characterFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        tmp = TextureRegion.split(hammerAnimationSheet,
                hammerAnimationSheet.getWidth() / FRAME_COLS,
                hammerAnimationSheet.getHeight() / FRAME_ROWS);
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                characterFrames[index++] = tmp[i][j];
            }
        }
        hammerCharacterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);

        // Now for firesuit
        characterFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        tmp = TextureRegion.split(fireSuitAnimationSheet,
                fireSuitAnimationSheet.getWidth() / FRAME_COLS,
                fireSuitAnimationSheet.getHeight() / FRAME_ROWS);
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                characterFrames[index++] = tmp[i][j];
            }
        }
        fireSuitCharacterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);

        // now for gas mask
        characterFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        tmp = TextureRegion.split(gasMaskAnimationSheet,
                gasMaskAnimationSheet.getWidth() / FRAME_COLS,
                gasMaskAnimationSheet.getHeight() / FRAME_ROWS);
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                characterFrames[index++] = tmp[i][j];
            }
        }
        gasMaskCharacterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);


        // now finally for invisibility
        characterFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        tmp = TextureRegion.split(invisibilityAnimationSheet,
                invisibilityAnimationSheet.getWidth() / FRAME_COLS,
                invisibilityAnimationSheet.getHeight() / FRAME_ROWS);
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                characterFrames[index++] = tmp[i][j];
            }
        }
        invisibilityCharacterAnimation = new Animation<TextureRegion>(1f/(FRAME_COLS * FRAME_COLS), characterFrames);

    }

    public Pixmap getPixmap() { return pixmap; }

    public void moveCharacter(int y) {
        // Bottom half of screen tapped
        if (y < (currentGame.getyMax() / 2)) {
            Gdx.app.log("applog", "going down");
            updatePlatform(false);
        }
        // Top half is tapped
        else {
            Gdx.app.log("applog", "going up");
            updatePlatform(true);
            // Making sure we don't go up a platform while already at the top most
        }
    }

    public void updateCharacterShapeCoordinates() {
        this.characterShape.set(this.initialX - this.getCharacterShape().getWidth() /2,
                this.currentY - this.getCharacterShape().getHeight() /2,
                this.characterTextures[0].getWidth(),
                this.characterTextures[0].getHeight()); //XY Coordinate and radius
    }

    public void updateCharacterPosition(boolean toAnimate) {

        if(toAnimate) {
            if (this.currentY < this.targetY) {
                if (this.currentY + GameConstants.VERTICAL_SPEED < this.targetY) {
                    this.currentY += GameConstants.VERTICAL_SPEED;
                } else {
                    this.currentY = this.targetY;
                }
            } else if (this.currentY > this.targetY) {
                if (this.currentY - GameConstants.VERTICAL_SPEED > this.targetY) {
                    this.currentY -= GameConstants.VERTICAL_SPEED;
                } else {
                    this.currentY = this.targetY;
                }
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

    public void render(SpriteBatch canvas, float elapsedTime, ShapeRenderer shapeRenderer, boolean toAnimate, Consumable.CONSUMABLES equipped) {

        if(toAnimate) {
            if (equipped == null) {
                currentFrame = this.defaultCharacterAnimation.getKeyFrame(elapsedTime, true);
            }
            else {
                switch (equipped) {
                    case FIRESUIT:
                        currentFrame = this.fireSuitCharacterAnimation.getKeyFrame(elapsedTime, true);
                        break;

                    case SPACE_HAMMER:
                        currentFrame = this.hammerCharacterAnimation.getKeyFrame(elapsedTime, true);
                        break;

                    case GAS_MASK:
                        currentFrame = this.gasMaskCharacterAnimation.getKeyFrame(elapsedTime, true);
                        break;

                    case INVISIBILITY:
                        currentFrame = this.invisibilityCharacterAnimation.getKeyFrame(elapsedTime, true);
                        break;

                    default:
                        currentFrame = this.defaultCharacterAnimation.getKeyFrame(elapsedTime, true);
                        break;

                }
            }
        }

        canvas.draw(currentFrame,
                this.initialX - currentFrame.getRegionWidth() / 2,
                this.currentY - currentFrame.getRegionHeight() / 2);


        // Only render shapes if on debug mode
        if(GameScreen.DEBUG) {
            shapeRenderer.rect(this.getCharacterShape().getX(),
                    this.getCharacterShape().getY(),
                    this.getCharacterShape().getWidth(),
                    this.getCharacterShape().getHeight());
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

    public int[] getCoordinates() {
        return new int[]{ (int)this.getCharacterShape().getX(), (int)this.getCharacterShape().getY()};
    }

    public int getInitialX() {
        return this.initialX;
    }

    public Texture[] getCharacterTextures() {
        return characterTextures;
    }

    public Animation<TextureRegion> getDefaultCharacterAnimation() {
        return defaultCharacterAnimation;
    }

    public Rectangle getCharacterShape() {
        return characterShape;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    // returns true if the hit took out remaining lives
    // We can add a parameter here for how much of a hit
    public boolean takesHit() {
        return (--this.remainingLives <= 0);
    }

    public boolean giveLife() {
        if (this.remainingLives < GameConstants.MAX_LIVES) {
            this.remainingLives++;
            return true;
        }

        return false;
    }

    public void resetLives() {
        this.remainingLives = GameConstants.MAX_LIVES;
    }

}
