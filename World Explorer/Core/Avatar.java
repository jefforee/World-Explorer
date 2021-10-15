package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Avatar {

    private static final TETile AVATARTYPE = Tileset.AVATAR;
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File AVATARNAME = Utils.join(CWD, "avatarName.txt");

    private Position currentPos;
    private Integer coinAmount = 0;
    private boolean onCoin = false;
    private ArrayList<Position> portalPos;
    private TETile previousTile = Tileset.FLOOR;
    private Position previousPos = currentPos;
    private Integer health;
    private ArrayList<Position> heartPos = new ArrayList<>();
    private Integer maxHealth = 10;
    private Menu mainMenu;

    public Avatar(Position startPos, TETile[][] world, ArrayList<Position> portalPos, Menu menu) {
        world[startPos.x][startPos.y] = AVATARTYPE;
        this.currentPos = startPos;
        this.portalPos = portalPos;
        this.health = 10;
        this.makeHeartPos();
        this.mainMenu = menu;
    }

    private void makeHeartPos() {
        for (int i = -2; i < 3; i++) {
            this.heartPos.add(new Position(Engine.WIDTH / 2 + i, Engine.HEIGHT - 2));
        }
        for (int i = -2; i < 3; i++) {
            this.heartPos.add(new Position(Engine.WIDTH / 2 + i, Engine.HEIGHT - 3));
        }
    }

    public void moveUp(TETile[][] world) {
        if (availablePath(this.currentPos.x, this.currentPos.y + 1, world)) {
            if (world[this.currentPos.x][this.currentPos.y].equals(Tileset.PORTAL)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.PORTAL;
            } else if (this.previousTile.equals(Tileset.COIN)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else if (this.previousTile.equals(Tileset.FLOOR)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else {
                world[this.currentPos.x][this.currentPos.y] = this.previousTile;
            }
            this.previousTile = world[this.currentPos.x][this.currentPos.y + 1];
            world[this.currentPos.x][this.currentPos.y + 1] = AVATARTYPE;
            this.previousPos = new Position(this.currentPos.x, this.currentPos.y);
            this.currentPos = new Position(this.currentPos.x, this.currentPos.y + 1);
        }
    }

    public void moveDown(TETile[][] world) {
        if (availablePath(this.currentPos.x, this.currentPos.y - 1, world)) {
            if (world[this.currentPos.x][this.currentPos.y].equals(Tileset.PORTAL)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.PORTAL;
            } else if (this.previousTile.equals(Tileset.COIN)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else if (this.previousTile.equals(Tileset.FLOOR)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else {
                world[this.currentPos.x][this.currentPos.y] = this.previousTile;
            }
            this.previousTile = world[this.currentPos.x][this.currentPos.y - 1];
            world[this.currentPos.x][this.currentPos.y - 1] = AVATARTYPE;
            this.previousPos = new Position(this.currentPos.x, this.currentPos.y);
            this.currentPos = new Position(this.currentPos.x, this.currentPos.y - 1);
        }
    }

    public void moveLeft(TETile[][] world) {
        if (availablePath(this.currentPos.x - 1, this.currentPos.y, world)) {
            if (world[this.currentPos.x][this.currentPos.y].equals(Tileset.PORTAL)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.PORTAL;
            } else if (this.previousTile.equals(Tileset.COIN)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else if (this.previousTile.equals(Tileset.FLOOR)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else {
                world[this.currentPos.x][this.currentPos.y] = this.previousTile;
            }
            this.previousTile = world[this.currentPos.x - 1][this.currentPos.y];
            world[this.currentPos.x - 1][this.currentPos.y] = AVATARTYPE;
            this.previousPos = new Position(this.currentPos.x, this.currentPos.y);
            this.currentPos = new Position(this.currentPos.x - 1, this.currentPos.y);
        }
    }

    public void moveRight(TETile[][] world) {
        if (availablePath(this.currentPos.x + 1, this.currentPos.y, world)) {
            if (world[this.currentPos.x][this.currentPos.y].equals(Tileset.PORTAL)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.PORTAL;
            } else if (this.previousTile.equals(Tileset.COIN)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else if (this.previousTile.equals(Tileset.FLOOR)) {
                world[this.currentPos.x][this.currentPos.y] = Tileset.STEPPEDFLOOR;
            } else {
                world[this.currentPos.x][this.currentPos.y] = this.previousTile;
            }
            this.previousTile = world[this.currentPos.x + 1][this.currentPos.y];
            world[this.currentPos.x + 1][this.currentPos.y] = AVATARTYPE;
            this.previousPos = new Position(this.currentPos.x, this.currentPos.y);
            this.currentPos = new Position(this.currentPos.x + 1, this.currentPos.y);
        }
    }


    public boolean availablePath(Integer x, Integer y, TETile[][] world) {
        TETile currentTile = world[x][y];
        if (currentTile.equals(Tileset.UNLOCKED_DOOR)) {
            this.mainMenu.setGameOverTrue();
            return true;
        }
        if (currentTile.equals(World.getSpace()) || currentTile.equals(Tileset.STEPPEDFLOOR)
                || currentTile.equals(Tileset.CLOSEDINNERLIGHT)) {
            return true;
        }

        if (currentTile.equals(Tileset.HEALTHBOOST)) {
            if (this.health < maxHealth) {
                this.health += 1;
            }
            world[currentPos.x][currentPos.y] = Tileset.STEPPEDFLOOR;
            world[x][y] = AVATARTYPE;
            this.currentPos = new Position(x, y);
            return false;
        }
        if (currentTile.equals(Tileset.INNERLIGHT)
                || currentTile.equals(Tileset.MIDDLELIGHT)
                || currentTile.equals(Tileset.OUTERLIGHT)) {
            this.health -= 1;
            return true;
        }
        if (currentTile.equals(World.getCoin())) {
            coinAmount += 1;
            onCoin = true;
            return true;
        }
        if (world[x][y].equals(World.getPortal())) {
            world[currentPos.x][currentPos.y] = World.getSpace();
            Position portalPos1 = this.portalPos.get(0);
            Position portalPos2 = this.portalPos.get(1);
            if (portalPos1.x.equals(x) && portalPos1.y.equals(y)) {
                this.currentPos = portalPos2;
            } else {
                this.currentPos = portalPos1;
            }

            if (world[x + 1][y].equals(World.getSpace())) {
                moveRight(world);
            } else {
                moveLeft(world);
            }
            return false;
        }

        return false;
    }

    public void loadMovements(String userMovements, TETile[][] world, World worldClass) {
        for (int i = 0; i < userMovements.length(); i++) {
            String wasd = userMovements.substring(i, i + 1);
            if (wasd.equals("w")) {
                moveUp(world);
            } else if (wasd.equals("s")) {
                moveDown(world);
            } else if (wasd.equals("a")) {
                moveLeft(world);
            } else if (wasd.equals("d")) {
                moveRight(world);
            } else if (wasd.equals("b")) {
                worldClass.lightSwitch(this, this.getCurrentPos(), world);
            }
        }
    }

    public Integer getCoinAmount() {
        return this.coinAmount;
    }

    public Position getCurrentPos() {
        return this.currentPos;
    }

    public boolean isOnCoin() {
        return this.onCoin;
    }

    public void setOnCoinFalse() {
        this.onCoin = false;
    }

    public ArrayList<Position> getHeartPos() {
        return this.heartPos;
    }

    public Integer getHealth() {
        return this.health;
    }

    public Integer getMaxHealth() {
        return this.maxHealth;
    }

    public void subtractHealth() {
        this.health -= 1;
    }

    public static void saveAvatar(String userInput) {
        if (!AVATARNAME.exists()) {
            try {
                AVATARNAME.createNewFile();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        Utils.writeContents(AVATARNAME, userInput);
    }



    public static TETile getAvatartype() {
        return AVATARTYPE;
    }


}
