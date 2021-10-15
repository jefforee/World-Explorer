package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    private TETile[][] world;
    private Avatar avatar;
    private String input;
    private Menu mainMenu;
    private boolean autoGrader = false;
    private boolean usingKeyboard = false;
    private World worldClass;



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        mainMenu = new Menu(Engine.HEIGHT, Engine.WIDTH, false);
        this.usingKeyboard = true;
        Menu game = this.mainMenu;
        while (this.mainMenu.getDisplayMenu()) {
            this.input = this.mainMenu.startGame();
            String userInput = this.input;
            if (userInput.equals("quit")) {
                this.mainMenu.quit(userInput);
                return;
            }
            if (!this.autoGrader) {
                Font font = new Font("Monaco", Font.BOLD, 14);
                StdDraw.setFont(font);
                ter.renderFrame(this.interactWithInputString(userInput));
            }
            this.getAvatar().saveAvatar(game.getAvatarName());
        }
        while (!game.isGameOver()) {
            game.drawLines();
            game.makeHearts(avatar, this.getWorld());
            if (this.getAvatar().isOnCoin()) {
                this.getAvatar().setOnCoinFalse();
            }
            game.coinCounter(this.getAvatar());
            if (this.avatar.getCoinAmount() == World.getMaxcoins()) {
                world[worldClass.getDoorPos().x][worldClass.getDoorPos().y] = Tileset.UNLOCKED_DOOR;
                world[WIDTH - 1][HEIGHT - 4] = Tileset.KEY;
            }
            if (game.getMousePosition().x < WIDTH && game.getMousePosition().y < HEIGHT
                    && 0 < game.getMousePosition().x && 0 < game.getMousePosition().y) {
                game.detectBlock(game.getMousePosition(), this.getWorld());
            }
            if (this.avatar.getHealth() == 0) {
                world[avatar.getCurrentPos().x][avatar.getCurrentPos().y] = Tileset.PUDDLE;
                game.setGameOverTrue();
            }
            if (game.quit(this.input)) {
                game.setGameOverTrue();
                if (!this.autoGrader) {
                    System.exit(0);
                }
            }
            worldClass.lightOff(world);
            if (StdDraw.hasNextKeyTyped()) {
                char userInput = StdDraw.nextKeyTyped();
                if (userInput == 'W' || userInput == 'w') {
                    this.input += 'w';
                    this.avatar.moveUp(this.world);
                } else if (userInput == 'S' || userInput == 's') {
                    this.input += 's';
                    this.avatar.moveDown(this.world);
                } else if (userInput == 'A' || userInput == 'a') {
                    this.input += 'a';
                    this.avatar.moveLeft(this.world);
                } else if (userInput == 'D' || userInput == 'd') {
                    this.input += 'd';
                    this.avatar.moveRight(this.world);
                } else if (userInput == ':') {
                    this.input += ':';
                } else if (userInput == 'Q' || userInput == 'q') {
                    this.input += 'q';
                } else if (userInput == 'B' || userInput == 'b') {
                    this.input += 'b';
                    worldClass.lightSwitch(this.avatar, this.avatar.getCurrentPos(), world);
                }
            }
            if (!this.autoGrader) {
                ter.renderFrame(this.getWorld());
            }
        }
        if (avatar.getCoinAmount() ==  World.getMaxcoins() && avatar.getHealth() != 0) {
            game.setVictoryTrue();
        }
        if (game.isVictory()) {
            game.drawVictory();
        } else {
            game.drawDefeat();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param ///input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String userInput) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if (this.mainMenu == null) {
            this.mainMenu = new Menu(Engine.HEIGHT, Engine.WIDTH, true);
        }
        if (!this.usingKeyboard) {
            this.autoGrader = true;
            this.mainMenu.setTrueAutoGrader();
        }
        int randomInt = 0;
        String restOfString = "";
        this.input = userInput;
        if (userInput.contains("l") || userInput.contains("L")) {
            this.input = Utils.readContentsAsString(Menu.getSavedfile());
            if (this.input.contains(":q") || this.input.contains(":Q")) {
                this.fixInput();
            }
            this.input += userInput.substring(1);
        }
        String alteredUserInput = this.input.substring(1);
        try {
            while (0 < userInput.length()) {
                randomInt = randomInt * 10 + Integer.parseInt(alteredUserInput.substring(0, 1));
                alteredUserInput = alteredUserInput.substring(1);
            }
        } catch (NumberFormatException e) {
            restOfString = alteredUserInput.substring(1);
        }
        if (this.input.contains(":q") || this.input.contains(":Q")) {
            this.mainMenu.quit(this.input);
            this.fixInput();
        }
        if (restOfString.contains(":q") || restOfString.contains(":Q")) {
            restOfString = restOfString.substring(0, restOfString.length() - 2);
        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        World newWorld = new World(randomInt);
        this.worldClass = newWorld;
        newWorld.worldGenerator(finalWorldFrame);
        this.world = finalWorldFrame;
        avatar = new Avatar(newWorld.getAvatarPos(), world, newWorld.getPortalPos(), this.mainMenu);
        avatar.loadMovements(restOfString, finalWorldFrame, this.worldClass);
        return finalWorldFrame;
    }

    public TETile[][] getWorld() {
        return this.world;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public String getInput() {
        return this.input;
    }

    public Menu getMainMenu() {
        return this.mainMenu;
    }

    public void fixInput() {
        this.input = this.input.substring(0, input.length() - 2);
    }
}
