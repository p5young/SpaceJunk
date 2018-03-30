package com.spacejunk.game.constants;

/**
 * Created by Salar on 26-Feb-18.
 * This class holds all the constants together
 * so fine tuning the game can be done in one place
 */

public class GameConstants {
    /* Level Constants */

    public static final int TOTAL_NUMBER_OF_OBSTACLE_TYPES = 4;
    public static final int TOTAL_NUMBER_OF_CONSUMABLE_TYPES = 4;

    public static final double SCORING_RATE = 0.1;
    public static final double SINGLE_CORRECT_USE_BONUS = 100;
    public static final int MAX_LIVES = 3;

    /* Background Speed Constant */
    public static final int BACKGROUND_SPEED = 4;
    public static final int MAIN_MENU_BACKGROUND_SPEED = 2;

    /* Speed Constants */
    /* 15 is the middle of our speed range (5 to 25) */
    public static final int VERTICAL_SPEED = 15;
    public static final int VELOCITY = 15;
    /* Speed modifier is added to VELOCITY to produce actual playing speed
        The default value is -5, because a playing speed of 10 is good for beginners
     */
    public static final int DEFAULT_SPEED_MODIFIER = -5;

    public static final int BORDER_WIDTH = 40;
    /* Maximum count of an item in inventory */
    public static final int MAX_INVENTORY_COUNT = 4;

    public static final int MIN_GAP = 500;

    public static final float BACKGROUND_MUSIC_VOLUME = 0.25f;


    public static final int X_AXIS_CONSTANT = 2392;
    public static final int Y_AXIS_CONSTANT = 1440;

}
