package com.spacejunk.game.obstacles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.spacejunk.game.GameScreen;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public abstract class Obstacle {

    public enum OBSTACLES {ASTEROID, ALIEN, FIRE, TOXIC_GAS};

    // Initial dummy setting
    protected int x = -1;
    protected int y = -1;

    protected OBSTACLES obstacleType;

    protected Texture obstacleTexture;
    protected Texture brokenTexture;
    protected Level level;

    protected Rectangle obstacleShape;

    protected Sound sound;

    // MYSHIT DELETE IF THIS FAILS!1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    protected Pixmap pixmap;

    /* This field is to prevent collision set off by
    an obstacle that the Astronaut is either taking a hit from (hence walks through)
    or using a consumable on. In both cases, we don't want the collision to be detected.
     */
    private boolean isBroken = false;

    // for matching with a consumable
    protected Consumable.CONSUMABLES breaksOnConsumable;

    public Obstacle() {
        this.obstacleShape = new Rectangle();
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y - (GameScreen.getScaledTextureHeight(obstacleTexture) / 2);
    }

    public int[] getCoordinates() {
        return new int[]{this.x, this.y};
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getType() {
        return String.valueOf(this.obstacleType);
    }

    public void moveLeft() {
        this.x = this.x - this.level.getVelocity();
    }

    public Texture getTexture() {
        if (this.isBroken()) {
            return this.brokenTexture;
        } else {

            return this.obstacleTexture;
        }
    }

    public Rectangle getObstacleShape() {
        return obstacleShape;
    }
    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public Pixmap getPixmap() { return this.pixmap; }

    public void playSound() {
        this.sound.play(1.0f);
    }

    public Consumable.CONSUMABLES getBreaksOnConsumable() {
        return breaksOnConsumable;
    }

    public void setBreaksOnConsumable(Consumable.CONSUMABLES breaksOnConsumable) {
        this.breaksOnConsumable = breaksOnConsumable;
    }

}
