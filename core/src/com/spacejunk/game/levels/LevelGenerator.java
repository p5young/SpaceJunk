package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
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

    public LevelGenerator(Level level) {
        this.level = level;
        this.randomGenerator = new Random();
    }


    public void generateInitialObstacles() {
        // Initializing what the types of the obstacles are going to be
        for(int i = 0; i < Level.MAX_NUMBER_OF_OBSTACLES; i++) {
            level.getObstaclesList().add(this.getNextRandomObstacle(i));
        }

        this.generateInitialCoordinatesForObstacles();
    }

    /**
     * Generates initial co-ordinates while making sure that they are not impossible
     **/
    public void generateInitialCoordinatesForObstacles() {


        for (int i = 0; i < level.getObstaclesList().size(); i++) {
            int[] coordinates;
            if(i == 0) {
                // This is done so that initially, at the start, the obstacles are off screen
                // Giving the user some time to get accustomed to the in-game physics
                coordinates = this.getCoordinatesForFirstObstacle();
                coordinates[0] += level.getXMax();
            }
            else {
                coordinates = this.getCoordinatesForObstacle(i - 1);
            }

            level.getObstaclesList().get(i).setCoordinates(coordinates[0], coordinates[1]);
        }

        printObstacleCoordinates();

    }

    private void printObstacleCoordinates() {

        Gdx.app.log("applog", "Printing out newly generated coorindates");
        int i = 0;
        for (Obstacle o : level.getObstaclesList()) {
            i++;
            Gdx.app.log("applog",
                    String.format("Obstacle %d is a %s has coordinates (%d, %d)", i, o.getType(),
                            o.getX(), o.getY()));
        }
    }

    /**
     Returns a random obstacle from our list of obstacles
     **/
    private Obstacle getNextRandomObstacle(int obstacleNumber) {

        int randomInt = randomGenerator.nextInt(Level.TOTAL_NUMBER_OF_OBSTACLE_TYPES);

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

    private int[] getCoordinatesForFirstObstacle() {

        int[] coordinates = new int[2];
        int y = generateRandomYCoordinate();
        int x = 0;
        coordinates[0] = x;
        coordinates[1] = y;
        return coordinates;
    }

    public int[] getCoordinatesForObstacle(int previousObstacleIndex) {

        int[] coordinates = new int[2];

        int y = generateRandomYCoordinate();

        int previousX, previousY;

        previousX = level.getObstaclesList().get(previousObstacleIndex).getX();
        previousY = level.getObstaclesList().get(previousObstacleIndex).getY();

        int x;
        // Previous obstacle was on the same platform as currently spawning obstacle
        if(previousY == y) {
            x = generateRandomXCoordinateForObstacleOnSamePlatform(previousX);
        }

        // Previous obstacle was on a different platform to currently spawning obstacle
        else {
            x = generateRandomXCoordinateForObstacleOnDifferentPlatform(previousX);
        }


        coordinates[0] = x;
        coordinates[1] = y;


        return coordinates;
    }

    private int generateRandomXCoordinateForObstacleOnSamePlatform(int previousX) {

        int from = previousX + level.getMinimumDistanceBetweenObstacles();
        int to = from + level.getRandomRegion();

        return randomGenerator.nextInt(Math.abs(to - from)) + from;
    }

    private int generateRandomXCoordinateForObstacleOnDifferentPlatform(int from) {

        int to = from + level.getRandomRegion();

        return randomGenerator.nextInt(Math.abs(to - from)) + from;
    }



    private int generateRandomYCoordinate() {
        int temp = randomGenerator.nextInt(Level.MAX_PLATFORMS);

        switch (temp) {
            case 0:
                return level.getTopPlatformY();
            case 1:
                return level.getMiddlePlatformY();
            case 2:
                return level.getBottomPlatformY();
            default:
                Gdx.app.log("applog", "Error: This shouldn't be happening");
                return level.getMiddlePlatformY();
        }
    }

}
