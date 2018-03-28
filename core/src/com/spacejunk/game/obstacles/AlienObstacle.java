package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
        this.obstacleTexture = level.getCurrentGame().getManager().get("alien.png");
        this.brokenTexture = level.getCurrentGame().getManager().get("alien.png");

        this.pixmap = level.getCurrentGame().getManager().getPixmap("alien.png");

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/alien_sound.wav"));

        this.level = level;
        this.obstacleType = OBSTACLES.ALIEN;
        this.breaksOnConsumable = Consumable.CONSUMABLES.INVISIBILITY;
    }

}
