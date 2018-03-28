package com.spacejunk.game.obstacles;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public class AsteroidObstacle extends Obstacle {

    public AsteroidObstacle(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.obstacleTexture = manager.get("asteroid.png");
        this.brokenTexture = manager.get("asteroid_broken.png");
        this.pixmap = manager.getPixmap("asteroid.png");
        this.sound = manager.get("sounds/asteroid_sound.mp3");

        this.level = level;
        this.obstacleType = OBSTACLES.ASTEROID;
        this.breaksOnConsumable = Consumable.CONSUMABLES.SPACE_HAMMER;
    }

}

