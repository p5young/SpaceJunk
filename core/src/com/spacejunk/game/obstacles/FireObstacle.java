package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class FireObstacle extends Obstacle {


    private Texture obstacleTexture;
    private Level level;

    public FireObstacle(Level level) {
        this.obstacleTexture = new Texture("fire_texture.png");
        this.level = level;
    }


    @Override
    public void moveLeft() {
        this.x = this.x - this.level.getVelocity();

        // We now wrap around over here
        if(x < -obstacleTexture.getWidth()) {
            int[] coordinates = this.level.getNextCoordinatesForObstacle();
            this.setCoordinates(coordinates[0], coordinates[1]);
        }


    }

    @Override
    public Texture getTexture() {
        return this.obstacleTexture;
    }

}
