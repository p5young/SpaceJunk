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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.max;

/**
 * Created by vidxyz on 2/14/18.
 */

public class LevelGenerator {

    private Level level;
    private Random randomGenerator;

    private int MIN_GAP = GameConstants.MIN_GAP;

    private ArrayList<Consumable.CONSUMABLES> Unbreakables;
    private ArrayList<Consumable.CONSUMABLES> Breakables;

    // list of probabilities of each chunk appearing
    // equal numbers means equal probability
    // a weight of 0 means that chunk cannot be generated
    private int[] weights;
    private int weightSum = 0;    // sum of all numbers in 'probabilities'

    private int TOP;
    private int MIDDLE;
    private int BOTTOM;

    public LevelGenerator(Level level) {
        this.level = level;
        this.randomGenerator = new Random();

        // give all layouts equal weight at start
        int NUMBER_OF_LAYOUTS = 10;     // determined by number of cases in generateObstacles()
        weights = new int[NUMBER_OF_LAYOUTS];
        for (int i = 0 ; i < NUMBER_OF_LAYOUTS ; ++i) {
            weights[i] = 2;
            weightSum += 2;
        }

    }

    // adjusts the weight of a certain chunk/ layout
    private void setWeight(int chunk, int newWeight) {
        weightSum += newWeight - weights[chunk];  // add newWeight to sum, subtract oldWeight
        weights[chunk] = newWeight;             // set newWeight
    }

    // takes in a random number (0 < rand <= weightSum) and returns a chunk based on weights
    private int getChunk(int rand) {
        for (int i = 0 ; i < weights.length ; ++i) {
            rand -= weights[i];
            if (rand <= 0) return i;
        }
        Gdx.app.log("applog", "getChunk() failed");
        return -1;
    }

    // creates a group of obstacles
    // returns its width plus a gap which is used by level.renderObstacles to delay next group
    public int generateObstacles() {
        Gdx.app.log("applog", "Making new batch of obstacles");

        // had to put these here because they're incorrect at time of construction
        TOP = level.getTopPlatformY();
        MIDDLE = level.getMiddlePlatformY();
        BOTTOM = level.getBottomPlatformY();

        // assess what consumables the player has and populate Unbreakables and Breakables ArrayLists
        populateBreakableAndUnbreakables();

        // adjust the weights of certain chunks based on lives, consumables, etc...
        adjustWeights();

        // get the platform the player is on (or moving to)
        int charPlatform = level.getCurrentGame().getCharacter().getTargetY();

        // pick which layout to use based on weights and random number
        // note: a layout with weight 0 is impossible
        int selectedLayout = getChunk(randomGenerator.nextInt(weightSum) + 1);

        // LAYOUT NOTATION:
        // R: random obstacle
        // U: unbreakable obstacle
        // B: breakable obstacle
        // C: consumable
        // L: life
        // selectedLayout = 9; // FORCE LAYOUT - FOR TESTING
        switch (selectedLayout) {
            case 0: {
                /*
                layout 0
                    R
                R
                R
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                makeRandomObstacle(0, BOTTOM);
                int a = makeRandomObstacle(30, MIDDLE);
                int b = makeRandomObstacle(a + MIN_GAP, TOP);
                return b + MIN_GAP;
            } case 1: {
                /*
                layout 1
                 R
                R
                    R
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int a = makeRandomObstacle(30, TOP);
                makeRandomObstacle(0, MIDDLE);
                int b = makeRandomObstacle(a + MIN_GAP, BOTTOM);
                return b + MIN_GAP;
            } case 2: {
                /*
                layout 2
                 R (top or bottom)
                R R (middle)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int a = makeRandomObstacle(0, MIDDLE);
                int b = makeRandomObstacle(a, MIDDLE);
                makeRandomObstacle(a / 2, randomLevel(2));
                return b + MIN_GAP;
            } case 3: {
                /*
                layout 3
                  B (top or bottom)
                U C (middle)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int a = makeUnbreakable(0, MIDDLE);
                int b = makeConsumable(a + 20, MIDDLE);
                makeBreakable(a, randomLevel(2));
                return b + MIN_GAP;
            } case 4: {
                /*
                layout 4
                C B (random level)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int randomLevel = randomLevel();
                int a = makeConsumable(0, randomLevel);
                int b = makeBreakable(a, randomLevel);
                return b + MIN_GAP;
            } case 5: {
                /*
                layout 5
                L (random level)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int a = makeLife(0, randomLevel());
                return a + MIN_GAP;
            } case 6: {
                /*
                layout 6
                B C (random level)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int randomLevel = randomLevel();
                int a = makeBreakable(0, randomLevel);
                int b = makeConsumable(a, randomLevel);
                return b + MIN_GAP;
            } case 7: {
                /*
                layout 7
                U L (random level)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int randomLevel = randomLevel();
                int a = makeUnbreakable(0, randomLevel);
                int b = makeLife(a, randomLevel);
                return b + MIN_GAP;
            } case 8: {
                /*
                layout 8 - weight 0 unless they have > 2 consumables
                B
                B
                B
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int a = makeBreakable(0, TOP);
                int b = makeBreakable(0, MIDDLE);
                int c = makeBreakable(0, BOTTOM);
                return max(a,max(b,c)) + MIN_GAP;
            } case 9: {
                /*
                layout 9 - weight 0 unless they have > 2 consumables
                U (player level)
                B (other level)
                B (other level)
                 */
                Gdx.app.log("applog", "Layout " + selectedLayout);
                int[] otherPlatforms = allPlatformsNotThis(charPlatform);

                int a = makeUnbreakable(0, charPlatform);
                int b = makeBreakable(0, otherPlatforms[0]);
                int c = makeBreakable(0, otherPlatforms[1]);
                return max(a,max(b,c)) + MIN_GAP;
            }
            default:
                Gdx.app.log("applog", "generateObstacles() FAILED!" + selectedLayout);
                return 0;
        }
    }

    private void populateBreakableAndUnbreakables() {
        Unbreakables = new ArrayList<Consumable.CONSUMABLES>();
        Breakables = new ArrayList<Consumable.CONSUMABLES>();
        Set<Consumable.CONSUMABLES> inventory = level.getInventory();   // Player inventory

        // Fire suit
        if (inventory.contains(Consumable.CONSUMABLES.FIRESUIT)){
            Breakables.add(Consumable.CONSUMABLES.FIRESUIT);
        } else {
            Unbreakables.add(Consumable.CONSUMABLES.FIRESUIT);
        }

        // Hammer
        if (inventory.contains(Consumable.CONSUMABLES.SPACE_HAMMER)){
            Breakables.add(Consumable.CONSUMABLES.SPACE_HAMMER);
        } else {
            Unbreakables.add(Consumable.CONSUMABLES.SPACE_HAMMER);
        }

        // Invisibility
        if (inventory.contains(Consumable.CONSUMABLES.INVISIBILITY)){
            Breakables.add(Consumable.CONSUMABLES.INVISIBILITY);
        } else {
            Unbreakables.add(Consumable.CONSUMABLES.INVISIBILITY);
        }

        // Gas Mask
        if (inventory.contains(Consumable.CONSUMABLES.GAS_MASK)){
            Breakables.add(Consumable.CONSUMABLES.GAS_MASK);
        } else {
            Unbreakables.add(Consumable.CONSUMABLES.GAS_MASK);
        }
    }

    private void adjustWeights() {
        // layout 5 - (free life) don't spawn unless they're missing a life
        if (level.getCurrentGame().getCharacter().getRemainingLives() >= GameConstants.MAX_LIVES) {
            setWeight(5, 0);
        } else {
            setWeight(5, 2);
        }

        // layout 8 & 9 - (walls) don't spawn unless player has > 2 consumables
        if (level.getInventory().size() > 2) {
            setWeight(8, 3);
            setWeight(9, 3);
        } else {
            setWeight(8, 0);
            setWeight(9, 0);
        }
    }

    /*
    spawns a random obstacle the player DOESN'T have a consumable for
    returns the x coordinate of the right side
    if they have all consumables, spawns a random obstacle
     */
    private int makeUnbreakable(int x, int y) {
        Obstacle o;
        if (Unbreakables.size() == 0) {
            o = getRandomObstacle();
        } else {
            switch (Unbreakables.get(randomGenerator.nextInt(Unbreakables.size()))) {
                case SPACE_HAMMER:
                    o = new AsteroidObstacle(level);
                    break;
                case INVISIBILITY:
                    o = new AlienObstacle(level);
                    break;
                case GAS_MASK:
                    o = new ToxicGasObstacle(level);
                    break;
                case FIRESUIT:
                    o = new FireObstacle(level);
                    break;
                default:
                    Gdx.app.log("applog", "levelGenerator.makeUnbreakable() failed!!!");
                    o = getRandomObstacle();
            }
        }
        o.setCoordinates(level.getXMax() + x, y);
        level.getObstaclesList().add(o);
        return x + o.getTexture().getWidth() + 10;
    }

    /*
    spawns a random obstacle the player HAS a consumable for
    returns the x coordinate of the right side
    if they have no consumables, spawns a random obstacle
     */
    private int makeBreakable(int x, int y) {
        Obstacle o;
        if (Breakables.size() == 0) {
            o = getRandomObstacle();
        } else {
            switch (Breakables.get(randomGenerator.nextInt(Breakables.size()))) {
                case SPACE_HAMMER:
                    o = new AsteroidObstacle(level);
                    break;
                case INVISIBILITY:
                    o = new AlienObstacle(level);
                    break;
                case GAS_MASK:
                    o = new ToxicGasObstacle(level);
                    break;
                case FIRESUIT:
                    o = new FireObstacle(level);
                    break;
                default:
                    Gdx.app.log("applog", "levelGenerator.makeBreakable() failed!!!");
                    o = getRandomObstacle();
            }
        }
        o.setCoordinates(level.getXMax() + x, y);
        level.getObstaclesList().add(o);
        return x + o.getTexture().getWidth() + 10;
    }

    private int makeRandomObstacle(int x, int y) {
        Obstacle o = getRandomObstacle();
        o.setCoordinates(level.getXMax() + x, y);
        level.getObstaclesList().add(o);
        return x + o.getTexture().getWidth() + 10;
    }

    /*
    spawns a random consumable
    returns the x coordinate of the right side
     */
    private int makeConsumable(int x, int y) {
        Consumable c = getRandomConsumable();
        c.setCoordinates(level.getXMax() + x, y);
        level.getConsumablesList().add(c);
        return x + c.getTexture().getWidth() + 10;
    }

    private int makeLife(int x, int y) {
        Consumable c = new LifeConsumable(level);
        c.setCoordinates(level.getXMax() + x, y);
        level.getConsumablesList().add(c);
        return x + c.getTexture().getWidth() + 10;
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

    // provides a random platform (bottom, middle, top)
    private int randomLevel() {
        switch(randomGenerator.nextInt(3)) {
            case 0:
                return BOTTOM;
            case 1:
                return MIDDLE;
            case 2:
                return TOP;
            default:
                Gdx.app.log("applog", "Error: randomLevel() broke");
                return 0;
        }
    }

    // takes in an int: 1 (bottom), 2 (middle), 3 (top), BOTTOM, MIDDLE, TOP
    // and returns a random platform which is NOT the one passed in
    private int randomLevel(int notLevel) {
        int[] possibleLevels = new int[2];

        // Fill possibleLevels array to include every level NOT specified by notLevel
        if (notLevel == 1 || notLevel == level.getBottomPlatformY()) {
            possibleLevels[0] = level.getMiddlePlatformY();
            possibleLevels[1] = level.getTopPlatformY();
        } else if (notLevel == 2 || notLevel == level.getMiddlePlatformY()) {
            possibleLevels[0] = level.getBottomPlatformY();
            possibleLevels[1] = level.getTopPlatformY();
        } else if (notLevel == 3 || notLevel == level.getTopPlatformY()) {
            possibleLevels[0] = level.getBottomPlatformY();
            possibleLevels[1] = level.getMiddlePlatformY();
        } else {
            Gdx.app.log("applog", "Error: randomLevel(int) broke. input: " + notLevel);
            return 0;
        }

        return possibleLevels[randomGenerator.nextInt(2)];
    }

    // takes in an int: 1 (bottom), 2 (middle), 3 (top), BOTTOM, MIDDLE, TOP
    // and returns the two platforms NOT passed in
    private int[] allPlatformsNotThis(int notLevel) {

        if (notLevel == 1 || notLevel == BOTTOM) {
            return new int[] {MIDDLE, TOP};
        } else if (notLevel == 2 || notLevel == MIDDLE) {
            return new int[] {BOTTOM, TOP};
        } else if (notLevel == 3 || notLevel == TOP) {
            return new int[] {BOTTOM, MIDDLE};
        } else {
            Gdx.app.log("applog", "Error: allPlatformsNotThis(int) broke. input: " + notLevel);
            return new int[2];
        }
    }
}
