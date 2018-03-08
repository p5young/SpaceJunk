package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.consumables.GasMaskConsumable;
import com.spacejunk.game.consumables.InvisibilityConsumable;
import com.spacejunk.game.consumables.LifeConsumable;
import com.spacejunk.game.consumables.SpaceHammerConsumable;
import com.spacejunk.game.obstacles.AlienObstacle;
import com.spacejunk.game.obstacles.AsteroidObstacle;
import com.spacejunk.game.obstacles.FireObstacle;
import com.spacejunk.game.obstacles.Obstacle;
import com.spacejunk.game.obstacles.ToxicGasObstacle;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.consumables.FireSuitConsumable;

import java.util.Random;

/**
 * Created by vidxyz on 2/14/18.
 */

public class LevelGenerator {

    private Level level;
    private Random randomGenerator;

    private int MIN_GAP = 430;

    public LevelGenerator(Level level) {
        this.level = level;
        this.randomGenerator = new Random();
    }

    // creates a group of obstacles
    // returns its width plus a gap which is used by level.renderObstacles to delay next group
    public int generateObstacles() {
        Gdx.app.log("applog", "Making new batch of obstacles");
        Gdx.app.log("applog", "MinGap: " + MIN_GAP);
        int randomInt = randomGenerator.nextInt(7);
        switch (randomInt) {
            // layout 0
            case 0:
                Gdx.app.log("applog", "Layout 0");
                makeUnbreakable(0, level.getBottomPlatformY());
                int a0 = makeUnbreakable(30, level.getMiddlePlatformY());
                int b0 = makeUnbreakable(a0 + MIN_GAP, level.getTopPlatformY());
                return b0 + MIN_GAP;
            // layout 1
            case 1:
                Gdx.app.log("applog", "Layout 1");
                makeUnbreakable(0, level.getMiddlePlatformY());
                int a1 = makeUnbreakable(30, level.getTopPlatformY());
                int b1 = makeUnbreakable(a1 + MIN_GAP, level.getBottomPlatformY());
                return b1 + MIN_GAP;
            // layout 2
            case 2:
                Gdx.app.log("applog", "Layout 2");
                int a2 = makeUnbreakable(0, level.getMiddlePlatformY());
                int b2 = makeUnbreakable(a2, level.getMiddlePlatformY());
                makeUnbreakable(a2, level.getTopPlatformY());
                return b2 + MIN_GAP;
            // layout 3
            case 3:
                Gdx.app.log("applog", "Layout 3");
                int a3 = makeUnbreakable(0, level.getMiddlePlatformY());
                int b3 = makeConsumable(a3, level.getMiddlePlatformY());
                makeConsumable(a3, level.getBottomPlatformY());
                return b3 + MIN_GAP;
            // layout 4
            case 4:
                Gdx.app.log("applog", "Layout 4");
                int a4 = makeLife(0, randomLevel());
                return a4 + MIN_GAP;
            // layout 5
            case 5:
                Gdx.app.log("applog", "Layout 5");
                int randomLevel5 = randomLevel();
                int a5 = makeConsumable(0, randomLevel5);
                int b5 = makeUnbreakable(a5, randomLevel5);
                return b5 + MIN_GAP;
            // layout 6
            case 6:
                Gdx.app.log("applog", "Layout 6");
                int a6 = makeConsumable(0, randomLevel());
                return a6 + MIN_GAP;
            default:
                Gdx.app.log("applog", "Error: generateObstacles' switch broke");
                return 500;
        }
    }

    // Called instead of generateObstacles() (above) when Level.java:88 says so
    // Feel free to change the code in this routine to make whatever chunks you desire for testing
    public int generateDEBUGObstacles() {
        // Fire Obstacle (middle)
        Obstacle o = new FireObstacle(level);
        o.setCoordinates(level.getXMax(), level.getMiddlePlatformY());
        level.getObstaclesList().add(o);
        // Fire Consumable (middle)
        Consumable cc = new FireSuitConsumable(level);
        cc.setCoordinates(level.getXMax() + 500, level.getMiddlePlatformY());
        level.getConsumablesList().add(cc);
        // Fire Consumable (bottom)
        Consumable c = new FireSuitConsumable(level);
        c.setCoordinates(level.getXMax(), level.getBottomPlatformY());
        level.getConsumablesList().add(c);
        return 1400;
    }

    /*
    spawns a random obstacle the player DOESN'T have a consumable for
    returns the x coordinate of the right side
     */
    private int makeUnbreakable(int x, int y) {
        Obstacle o = getRandomObstacle();
        o.setCoordinates(level.getXMax() + x, y);
        level.getObstaclesList().add(o);
        return x + o.getTexture().getWidth() + 10;  // NOTE: CHANGE 10 TO NUMBER AFFECTED BY DIFFICULTY
    }

    private Obstacle getRandomObstacle() {

        int randomInt = randomGenerator.nextInt(GameConstants.TOTAL_NUMBER_OF_OBSTACLE_TYPES);

        switch (randomInt) {
            case 0:
                return new AsteroidObstacle(level);
            case 1:
                return new FireObstacle(level);
            case 2:
                return new ToxicGasObstacle(level);
            case 3:
                return new AlienObstacle(level);
            default:
                Gdx.app.log("applog", "Error: getRandomObstacle broke");
                return new AsteroidObstacle(level);
        }
    }

    /*
    spawns a random consumable the player DOESN'T yet have in their inventory
    returns the x coordinate of the right side
     */
    private int makeConsumable(int x, int y) {
        Consumable c = getRandomConsumable();
        c.setCoordinates(level.getXMax() + x, y);
        level.getConsumablesList().add(c);
        return x + c.getTexture().getWidth() + 10;  // NOTE: CHANGE 10 TO NUMBER AFFECTED BY DIFFICULTY
    }

    private Consumable getRandomConsumable() {

        int randomInt = randomGenerator.nextInt(GameConstants.TOTAL_NUMBER_OF_CONSUMABLE_TYPES);

        switch (randomInt) {
            case 0:
                return new FireSuitConsumable(level);
            case 1:
                return new GasMaskConsumable(level);
            case 2:
                return new InvisibilityConsumable(level);
            case 3:
                return new SpaceHammerConsumable(level);
            default:
                Gdx.app.log("applog", "Error: getRandomConsumable broke");
                return new SpaceHammerConsumable(level);
        }
    }

    private int makeLife(int x, int y) {
        Consumable c = new LifeConsumable(level);
        c.setCoordinates(level.getXMax() + x, y);
        level.getConsumablesList().add(c);
        return x + c.getTexture().getWidth() + 10;
    }

    private int randomLevel() {
        switch(randomGenerator.nextInt(3)) {
            case 0:
                return level.getBottomPlatformY();
            case 1:
                return level.getMiddlePlatformY();
            case 2:
                return level.getTopPlatformY();
            default:
                Gdx.app.log("applog", "Error: randomLevel broke");
                return 0;
        }
    }
}
