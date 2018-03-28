package com.spacejunk.game.obstacles;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class ToxicGasObstacle extends Obstacle {

    public ToxicGasObstacle(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.obstacleTexture = manager.get("toxic_gas_green.png");
        this.brokenTexture = manager.get("toxic_gas_green.png");
        this.pixmap = manager.getPixmap("toxic_gas_green.png");
        this.sound = manager.get("sounds/gas_sound.mp3");

        this.level = level;
        this.obstacleType = OBSTACLES.TOXIC_GAS;



        this.breaksOnConsumable = Consumable.CONSUMABLES.GAS_MASK;
    }

}
