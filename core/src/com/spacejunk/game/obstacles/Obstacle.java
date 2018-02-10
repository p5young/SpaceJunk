package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public abstract class Obstacle {

    // Initial dummy setting
    protected int x = -1;
    protected int y = -1;

    protected int obstacleNumber;

    protected Texture obstacleTexture;
    protected Level level;


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
        if(this instanceof AsteroidObstacle) {
            return "Asteroid";
        }
        else if (this instanceof FireObstacle) {
            return "Fire";
        }
        else {
            return "Default";
        }
    }

    public void moveLeft() {
        this.x = this.x - this.level.getVelocity();

        // We now wrap around over here
        if(x < -obstacleTexture.getWidth()) {
            int[] coordinates = this.level.getCoordinatesForObstacle(level.getFurthestObstacleIndex());
            level.setFurthestObstacleIndex(this.obstacleNumber);
            this.setCoordinates(coordinates[0], coordinates[1]);
        }


    }

    public Texture getTexture() {
        return this.obstacleTexture;
    }


}
