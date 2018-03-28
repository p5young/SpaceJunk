package com.spacejunk.game.obstacles;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class FireObstacle extends Obstacle {

    public FireObstacle(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.obstacleTexture = manager.get("fire.png");
        this.brokenTexture = manager.get("fire.png");
        this.pixmap = manager.getPixmap("fire.png");
        this.sound = manager.get("sounds/fire_sound.mp3");

        this.level = level;
        this.obstacleType = OBSTACLES.FIRE;
        this.breaksOnConsumable = Consumable.CONSUMABLES.FIRESUIT;
    }

}
