package com.spacejunk.game.obstacles;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class AlienObstacle extends Obstacle {

    public AlienObstacle(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.obstacleTexture = manager.get("alien.png");
        this.brokenTexture = manager.get("alien.png");
        this.pixmap = manager.getPixmap("alien.png");
        this.sound = manager.get("sounds/alien_sound.wav");

        this.level = level;
        this.obstacleType = OBSTACLES.ALIEN;
        this.breaksOnConsumable = Consumable.CONSUMABLES.INVISIBILITY;
    }

}
