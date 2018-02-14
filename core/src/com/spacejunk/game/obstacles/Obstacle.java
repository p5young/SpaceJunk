package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.spacejunk.game.levels.Level;

import org.w3c.dom.css.Rect;

/**
 * Created by vidxyz on 2/8/18.
 */

public abstract class Obstacle {

    public enum OBSTACLES {ASTEROID, ALIEN, FIRE, TOXIC_GAS};

    // Initial dummy setting
    protected int x = -1;
    protected int y = -1;

    protected int obstacleNumber;
    protected OBSTACLES obstacleType;

    protected Texture obstacleTexture;
    protected Level level;

    protected Rectangle obstacleShape;

    public Obstacle() {
        this.obstacleShape = new Rectangle();
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
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

        // We now wrap around over here
        if(x < -obstacleTexture.getWidth()) {
            int[] coordinates = this.level.getLevelGenerator().getCoordinatesForObstacle(level.getFurthestObstacleIndex());
            level.setFurthestObstacleIndex(this.obstacleNumber);
            this.setCoordinates(coordinates[0], coordinates[1]);
        }


    }

    public Texture getTexture() {
        return this.obstacleTexture;
    }

    public Rectangle getObstacleShape() {
        return obstacleShape;
    }
}
