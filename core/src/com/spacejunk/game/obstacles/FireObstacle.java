package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class FireObstacle extends Obstacle {

    public FireObstacle(Level level) {
        this.obstacleTexture = new Texture("fire.png");
        this.level = level;
        this.obstacleType = OBSTACLES.FIRE;

    }

}
