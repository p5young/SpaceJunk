package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public class AsteroidObstacle extends Obstacle {

    public AsteroidObstacle(Level level) {
        this.obstacleTexture = new Texture("asteroid.png");
        this.level = level;
        this.obstacleType = OBSTACLES.ASTEROID;

    }

}

