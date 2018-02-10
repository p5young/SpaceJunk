package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class ToxicGasObstacle extends Obstacle {


    public ToxicGasObstacle(Level level) {
        this.obstacleTexture = new Texture("toxic_gas_green.png");
        this.level = level;
    }

}
