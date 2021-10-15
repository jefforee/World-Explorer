package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.IOException;

// @Source CS61B lab13
public class Menu {
    /**
     * The width of the window of this game.
     */
    private int width;
    /**
     * The height of the window of this game.
     */
    private int height;
    /**
     * Game over.
     */
    private boolean gameOver = false;
    private String avatarName = "avatar";
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File SAVEDFILE = Utils.join(CWD, "loadfile.txt");
    private static final File AVATARNAME = Utils.join(CWD, "avatarName.txt");
    private boolean displayMenu = true;
    private boolean victory = false;
    private boolean autoGrader = false;
    private boolean locked = true;


    public Menu(int width, int height, boolean autoGrader) {
        this.autoGrader = autoGrader;
        this.width = width;
        this.height = height;
        if (!this.autoGrader) {
            StdDraw.setCanvasSize(this.width * 16, this.height * 16);
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setXscale(0, this.width);
            StdDraw.setYscale(0, this.height);
            StdDraw.clear(Color.BLACK);
            StdDraw.enableDoubleBuffering();
        }
    }

    public void drawMenu() {
        if (!this.autoGrader) {
            StdDraw.clear(Color.BLACK);
            Font font = new Font("Arial", Font.BOLD, 80);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(width / 2, height * 0.75, "World Explorer");
            Font font2 = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font2);
            StdDraw.text(width / 2, height * 0.55, "New World (N)");
            StdDraw.text(width / 2, height * 0.50, "Load (L)");
            StdDraw.text(width / 2, height * 0.45, "Quit (Q)");
            StdDraw.text(width / 2, height * 0.40, "Set Avatar Name (A)");
            StdDraw.show();
        }
    }

    public void drawSeed(String seed) {
        if (!this.autoGrader) {
            StdDraw.clear(Color.BLACK);
            this.drawMenu();
            Font font = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.text(width / 2, height * 0.10, "Seed: " + seed);
            StdDraw.show();
        }
    }

    public void drawAvatarName(String name) {
        if (!this.autoGrader) {
            StdDraw.clear(Color.BLACK);
            this.drawMenu();
            Font font = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.RED);
            StdDraw.text(width / 2, height * 0.10, "Avatar: " + name);
            Font font2 = new Font("Arial", Font.BOLD, 25);
            StdDraw.setFont(font2);
            StdDraw.setPenColor(Color.CYAN);
            StdDraw.textRight(width - 0.5, 1, "End name with ;");
            StdDraw.show();
        }
    }

    public String startGame() {
        while (true) {
            this.drawMenu();
            String userInput = "";
            if (StdDraw.hasNextKeyTyped()) {
                String command = String.valueOf(StdDraw.nextKeyTyped());
                if (command.equals("N") || command.equals("n")) {
                    userInput += command;
                    drawSeed("");
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char input = StdDraw.nextKeyTyped();
                            userInput += String.valueOf(input);
                            if (!Character.isDigit(input)) {
                                this.displayMenu = false;
                                return userInput;
                            }
                            drawSeed(userInput.substring(1));
                        }
                    }
                }
                if (command.equals("A") || command.equals("a")) {
                    drawAvatarName("");
                    this.avatarName = "";
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char input = StdDraw.nextKeyTyped();
                            if (String.valueOf(input).equals(";")) {
                                this.drawMenu();
                                break;
                            }
                            this.avatarName += String.valueOf(input);
                            drawAvatarName(this.avatarName);
                        }
                    }
                }
                if (command.equals("L") || command.equals("l")) {
                    this.avatarName = Utils.readContentsAsString(AVATARNAME);
                    this.displayMenu = false;
                    return Utils.readContentsAsString(SAVEDFILE);
                }
                if (command.equals("Q") || command.equals("q")) {
                    if (!autoGrader) {
                        System.exit(0);
                    }
                    return "quit";
                }
            }
        }
    }

    public void coinCounter(Avatar avatar) {
        if (!this.autoGrader) {
            Font font = new Font("Arial", Font.ITALIC, 15);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.textLeft(0, height - 1,
                    "Snowflakes collected: " + (avatar.getCoinAmount() - 1));
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(0, height - 1.5, "Snowflakes collected: " + avatar.getCoinAmount());
            StdDraw.textLeft(0, height - 2.5, "Snowflakes goal: " + World.getMaxcoins());
            if (avatar.getCoinAmount().equals(World.getMaxcoins())) {
                this.locked = false;
            }
            Font font2 = new Font("Monaco", Font.BOLD, 14);
            StdDraw.setFont(font2);
            StdDraw.show();

        }
    }

    public void drawLines() {
        if (!this.autoGrader) {
            Font font3 = new Font("Arial", Font.ROMAN_BASELINE, 15);
            StdDraw.setFont(font3);
            StdDraw.setPenColor(Color.green);
            StdDraw.textLeft(width / 2 - 0.8, height - 0.6, "health:");
            StdDraw.setPenColor(Color.yellow);
            StdDraw.textRight(width - 1.25, height - 3.35, "inventory:");
            StdDraw.setPenColor(Color.orange);
            StdDraw.text(width / 3 - 2, height - 2, "press 'b' to extinguish fire");
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.line(0, this.height - 4, this.width, this.height - 4);
            StdDraw.line(0, this.height - 4.1, this.width, this.height - 4.1);
            StdDraw.line(0, this.height - 4.2, this.width, this.height - 4.15);
            StdDraw.show();
        }
    }

    public void setGameOverTrue() {
        this.gameOver = true;
    }

    public void setVictoryTrue() {
        this.victory = true;
    }

    public void drawVictory() {
        if (!this.autoGrader) {
//            StdDraw.clear(Color.BLACK);
            Font font = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.YELLOW);
            StdDraw.text(width / 2, height * 0.55, "Congratulations!!!");
            StdDraw.text(width / 2, height * 0.50, "You have collected all the snowflakes!");
            StdDraw.show();
        }
    }

    public void drawDefeat() {
        if (!this.autoGrader) {
//            StdDraw.clear(Color.BLACK);
            Font font = new Font("Arial", Font.BOLD, 40);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.red);
            StdDraw.text(width / 2, height * 0.55, "Game Over!");
            StdDraw.text(width / 2, height * 0.50,
                    "You have lost all your health and have melted into water!");
            StdDraw.show();
        }
    }

    public boolean quit(String userInput) {
        if (userInput.contains(":Q") || userInput.contains(":q")) {
            if (!SAVEDFILE.exists()) {
                try {
                    SAVEDFILE.createNewFile();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            Utils.writeContents(SAVEDFILE, userInput);
            return true;
        }
        return false;
    }


    public Position getMousePosition() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        int closestX = (int) Math.round(mouseX);
        int closestY = (int) Math.round(mouseY);
        return new Position(closestX, closestY);
    }

    public void makeHearts(Avatar avatar, TETile[][] world) {
        if (avatar.getHealth() == 0) {
            this.gameOver = true;
        }
        if (!this.autoGrader) {
            for (int i = 0; i < avatar.getHealth(); i++) {
                Position heartPos = avatar.getHeartPos().get(i);
                world[heartPos.x][heartPos.y] = Tileset.HEALTH;
            }
            for (int i = avatar.getHealth(); i < avatar.getMaxHealth(); i++) {
                Position heartPos = avatar.getHeartPos().get(i);
                world[heartPos.x][heartPos.y] = Tileset.NOTHING;
            }
        }
    }


    public void detectBlock(Position pos, TETile[][] world) {
        Integer xPos = pos.x;
        Integer yPos = pos.y;
        Font font = new Font("Arial", Font.BOLD, 25);
        StdDraw.setFont(font);
        TETile currentTile = world[xPos][yPos];
        double cornerX = width;
        double cornerY = height - 1.5;
        if (currentTile.equals(World.getCoin())) {
            StdDraw.setPenColor(new Color(204, 238, 255));
            StdDraw.textRight(cornerX, cornerY, World.getCoin().description());
        }
        if (currentTile.equals(World.getSpace())) {
            StdDraw.setPenColor(new Color(204, 238, 255));
            StdDraw.textRight(cornerX, cornerY, World.getSpace().description());
        }
        if (currentTile.equals(World.getBorder())) {
            StdDraw.setPenColor(new Color(33, 182, 168));
            StdDraw.textRight(cornerX, cornerY, World.getBorder().description());
        }
        if (currentTile.equals(Avatar.getAvatartype())) {
            StdDraw.setPenColor(Color.red);
            StdDraw.textRight(cornerX, cornerY, this.avatarName);
        }
        if (currentTile.equals(World.getEmptyspace())) {
            StdDraw.setPenColor(Color.white);
            StdDraw.textRight(cornerX, cornerY, World.getEmptyspace().description());
        }
        if (currentTile.equals(World.getPortal())) {
            StdDraw.setPenColor(Color.blue);
            StdDraw.textRight(cornerX, cornerY, World.getPortal().description());
        }
        if (currentTile.equals(Tileset.INNERLIGHT)
                || currentTile.equals(Tileset.MIDDLELIGHT)
                || currentTile.equals(Tileset.OUTERLIGHT)
                || currentTile.equals(Tileset.CLOSEDINNERLIGHT)) {
            StdDraw.setPenColor(new Color(255, 153, 153));
            StdDraw.textRight(cornerX, cornerY, "fire");
        }
        if (currentTile.equals((Tileset.HEALTH))) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.textRight(cornerX, cornerY, Tileset.HEALTH.description());
        }
        if (currentTile.equals((Tileset.LOCKED_DOOR))) {
            StdDraw.setPenColor(Color.yellow);
            StdDraw.textRight(cornerX, cornerY, Tileset.LOCKED_DOOR.description());
        }
        if (currentTile.equals((Tileset.UNLOCKED_DOOR))) {
            StdDraw.setPenColor(Color.yellow);
            StdDraw.textRight(cornerX, cornerY, Tileset.UNLOCKED_DOOR.description());
        }
        if (currentTile.equals((Tileset.HEALTHBOOST))) {
            StdDraw.setPenColor(Color.green);
            StdDraw.textRight(cornerX, cornerY, Tileset.HEALTHBOOST.description());
        }
        if (currentTile.equals((Tileset.STEPPEDFLOOR))) {
            StdDraw.setPenColor(new Color(229, 204, 255));
            StdDraw.textRight(cornerX, cornerY, Tileset.STEPPEDFLOOR.description());
        }
        Font font2 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font2);
        StdDraw.show();
    }

    public String getAvatarName() {
        return avatarName;
    }

    public boolean getDisplayMenu() {
        return this.displayMenu;
    }

    public static File getSavedfile() {
        return SAVEDFILE;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isVictory() {
        return this.victory;
    }

    public boolean isAutoGrader() {
        return this.autoGrader;
    }

    public void setTrueAutoGrader() {
        this.autoGrader = true;
    }
}
