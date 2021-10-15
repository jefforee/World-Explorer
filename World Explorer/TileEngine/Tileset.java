package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final Color MAINBACKGROUND = new Color(204, 238, 255); ///new Color(233, 116, 82);

    public static final TETile AVATAR = new TETile('⛄', Color.blue,
            new Color(229, 204, 255), "avatar");
    public static final TETile WALL = new TETile('▣', new Color(33, 182, 168),
            new Color(162, 217, 231), "wall");
    public static final TETile FLOOR = new TETile('·', Color.white,
            MAINBACKGROUND, "floor");
    public static final TETile STEPPEDFLOOR = new TETile('·', Color.white,
            new Color(229, 204, 255), "stepped floor");
    public static final TETile OUTERLIGHT = new TETile('·',
            Color.white, new Color(254, 204, 81), "light");
    public static final TETile MIDDLELIGHT = new TETile('·',
            Color.white, new Color(255, 228, 105), "light");
    public static final TETile INNERLIGHT = new TETile('♨',
            Color.red, new Color(254, 204, 81), "light");
    public static final TETile CLOSEDINNERLIGHT = new TETile('♨',
            Color.black, MAINBACKGROUND, "light");
    public static final TETile NOTHING = new TETile(' ', Color.black,
            Color.black, "empty space");
    public static final TETile GRASS = new TETile('"', Color.green,
            Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue,
            Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta,
            Color.pink, "flower");
    public static final TETile PORTAL = new TETile('◉', new Color(59, 0, 179),
            MAINBACKGROUND, "portal");
    public static final TETile COIN = new TETile('❄', Color.cyan,
            MAINBACKGROUND, "snowflakes");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, MAINBACKGROUND,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow,
            Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray,
            Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green,
            Color.black, "tree");
    public static final TETile HEALTH = new TETile('♥', Color.red,
            Color.black, "health");
    public static final TETile PUDDLE = new TETile('☁', Color.blue,
            MAINBACKGROUND, "puddle");
    public static final TETile KEY = new TETile('❄', new Color(233, 232, 239),
            Color.black, "KEY");
    public static final TETile HEALTHBOOST = new TETile('+', Color.green,
            MAINBACKGROUND, "health bonus");

}


