package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public class WallObstacle extends Obstacle {

    private Texture obstacleTexture;
    private Level level;

    public WallObstacle(Level level) {
        this.obstacleTexture = new Texture("toptube.png");
        this.level = level;
    }


    @Override
    public void moveLeft() {
        this.x = this.x - this.level.getVelocity();

        // We now wrap around over here
        if(x < 0) {
            int[] coordinates = this.level.getNextCoordinates();
            coordinates[0] -= this.level.getXMax();
            this.setCoordinates(coordinates[0], coordinates[1]);
        }


    }

    @Override
    public Texture getTexture() {
        return this.obstacleTexture;
    }
}

