package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class AlienObstacle extends Obstacle {

    public AlienObstacle(Level level, int obstacleNumber) {
        this.obstacleTexture = new Texture("alien_security.png");
        this.level = level;
        this.obstacleNumber = obstacleNumber;
        this.obstacleType = OBSTACLES.ALIEN;

    }

}
