package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.obstacles.AlienObstacle;
import com.spacejunk.game.obstacles.AsteroidObstacle;
import com.spacejunk.game.obstacles.FireObstacle;
import com.spacejunk.game.obstacles.Obstacle;
import com.spacejunk.game.obstacles.ToxicGasObstacle;

import java.util.Random;

/**
 * Created by vidxyz on 2/14/18.
 */

public class LevelGenerator {

    private Level level;
    private Random randomGenerator;

    private int MIN_GAP = 200;

    public LevelGenerator(Level level) {
        this.level = level;
        this.randomGenerator = new Random();
    }

    // creates a group of obstacles
    // returns its width plus a gap which is used by level.renderObstacles to delay next group
    public int generateObstacles() {
        Gdx.app.log("applog", "Making new batch of obstacles");
        Gdx.app.log("applog", "MinGap: " + MIN_GAP);
        int randomInt = randomGenerator.nextInt(GameConstants.MAX_LAYOUTS);
        switch (randomInt) {
            // layout 0
            case 0:
                Gdx.app.log("applog", "Layout 0");
                makeUnbreakable(0, level.getBottomPlatformY());
                makeUnbreakable(20, level.getMiddlePlatformY());
                makeUnbreakable(650, level.getTopPlatformY());
                return 140;
            // layout 1
            case 1:
                Gdx.app.log("applog", "Layout 1");
                makeUnbreakable(0, level.getMiddlePlatformY());
                makeUnbreakable(20, level.getTopPlatformY());
                makeUnbreakable(650, level.getBottomPlatformY());
                return 140;
            // layout 2
            case 2:
                Gdx.app.log("applog", "Layout 2");
                makeUnbreakable(0, level.getMiddlePlatformY());
                makeUnbreakable(300, level.getMiddlePlatformY());
                makeUnbreakable(300, level.getTopPlatformY());
                return 140;
            default:
                Gdx.app.log("applog", "Error: generateObstacles' switch broke");
                return 500;
        }
    }

    private void makeUnbreakable(int x, int y) {
        int size = level.getObstaclesList().size();
        level.getObstaclesList().add(getRandomObstacle(size));
        level.getObstaclesList().get(size).setCoordinates(level.getXMax() + x, y);
    }

    private Obstacle getRandomObstacle(int obstacleNumber) {

        int randomInt = randomGenerator.nextInt(GameConstants.TOTAL_NUMBER_OF_OBSTACLE_TYPES);

        switch (randomInt) {
            case 0:
                return new AsteroidObstacle(level, obstacleNumber);
            case 1:
                return new FireObstacle(level, obstacleNumber);
            case 2:
                return new ToxicGasObstacle(level, obstacleNumber);
            case 3:
                return new AlienObstacle(level, obstacleNumber);
            default:
                Gdx.app.log("applog", "Error: This should'nt happen");
                return null;
        }
    }

}
