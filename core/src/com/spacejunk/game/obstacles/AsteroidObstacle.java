package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public class AsteroidObstacle extends Obstacle {

    public AsteroidObstacle(Level level) {
        this.obstacleTexture = new Texture("asteroid.png");

        this.brokenTexture = new Texture("asteroid_broken.png");

        FileHandle handle = Gdx.files.internal("asteroid.png");
        this.pixmap = new Pixmap(handle);

        this.level = level;
        this.obstacleType = OBSTACLES.ASTEROID;
        this.breaksOnConsumable = Consumable.CONSUMABLES.SPACE_HAMMER;
    }

}

