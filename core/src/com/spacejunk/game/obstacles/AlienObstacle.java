package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class AlienObstacle extends Obstacle {

    public AlienObstacle(Level level) {
        super();
        this.obstacleTexture = new Texture("alien.png");
        this.brokenTexture = new Texture("alien.png");

        FileHandle handle = Gdx.files.internal("alien.png");
        this.pixmap = new Pixmap(handle);

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/alien_sound.mp3"));

        this.level = level;
        this.obstacleType = OBSTACLES.ALIEN;
        this.breaksOnConsumable = Consumable.CONSUMABLES.INVISIBILITY;
    }

}
