package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.*;

public class World {
    private Integer SEED;
    private Random r;
    private static final TETile BORDER = Tileset.WALL;
    private static final TETile SPACE = Tileset.FLOOR;
    private static final TETile COIN = Tileset.COIN;
    private static final TETile EMPTYSPACE = Tileset.NOTHING;
    private static final TETile PORTAL = Tileset.PORTAL;

    private static final int MAXCOINS = 10;
    private static final Integer MAXROOM = 15;
    private Position avatarPos;
    private ArrayList<Room> roomList;
    private int coins = 0;
    private ArrayList<Position> portalPos = new ArrayList<>();
    private ArrayList<Position> lightPos = new ArrayList<>();
    private boolean[] lightStatus;
    private Position doorPos;



    public static TETile getBorder() {
        return BORDER;
    }

    public static TETile getSpace() {
        return SPACE;
    }

    World(Integer seed) {
        this.SEED = seed;
        int i = 0;
        this.r = new Random(this.SEED);
    }

    public void worldGenerator(TETile[][] world) {
        HashMap<ArrayList<Room>, HashMap<Integer, HashSet<Integer>>> roomGraphTemp
                = this.randomRoomGenerator(world);
        HashMap<Integer, HashSet<Integer>> filledSpace = new HashMap<>();
        for (Object i : roomGraphTemp.keySet()) {
            filledSpace = roomGraphTemp.get(i);
            roomList = (ArrayList<Room>) i;
        }
        connectRooms(world, roomList, filledSpace);
        lightMaker(world);
        coinMaker(world);
        portalMaker(world);
        setAvatarPos(world, roomList);
        makeLockedDoor(world);
        healthBoostMaker(world);
    }


    public void coinMaker(TETile[][] world) {
        while (coins < MAXCOINS) {
            Integer randomRoomNum = r.nextInt(roomList.size());
            Room randomRoom = roomList.get(randomRoomNum);
            Integer randomX = randomRoom.getPos().x + r.nextInt(randomRoom.getXLength() - 1) + 1;
            Integer randomY = randomRoom.getPos().y + r.nextInt(randomRoom.getYLength() - 1) + 1;
            if (world[randomX][randomY].equals(SPACE)) {
                world[randomX][randomY] = COIN;
                coins += 1;
            }
        }
    }

    public void connectRooms(TETile[][] world, ArrayList<Room> roomLists, HashMap<Integer,
            HashSet<Integer>> filledSpace) {
        for (int i = 0; i < roomLists.size() - 1; i++) {
            this.joinRoom(roomLists.get(i), roomLists.get(i + 1), world, filledSpace);
        }
    }

    public void joinRoom(Room room1, Room room2, TETile[][] world,
                         HashMap<Integer, HashSet<Integer>> filledSpace) {
        Integer horOnlyY = onlyHorizontal(room1, room2);
        Integer verOnlyX = onlyVertical(room1, room2);
        if (0 < horOnlyY) {
            createHorizontal(room1, room2, world, horOnlyY, filledSpace);
        } else if (0 < verOnlyX) {
            createVertical(room1, room2, world, verOnlyX, filledSpace);
        } else {
            this.createLWay(room1, room2, world, filledSpace);
        }
    }

    public void createLWay(Room room1, Room room2, TETile[][] world,
                           HashMap<Integer, HashSet<Integer>> filledSpace) {
        Room smallerXRoom = Room.smallerX(room1, room2);
        Room biggerXRoom = Room.biggerX(room1, room2);
        Room smallerYRoom = Room.smallerY(room1, room2);
        Room biggerYRoom = Room.biggerY(room1, room2);
        int dirRandom = this.r.nextInt(2);
        switch (dirRandom) {
            case 0: {
                Integer xStart = smallerXRoom.getMaxX();
                Integer yStart = smallerXRoom.getPos().y + 1;
                Integer xLength = biggerXRoom.getPos().x - smallerXRoom.getMaxX() + 1;
                Integer yLength;
                if (biggerYRoom.getPos().y.equals(yStart - 1)) {
                    yLength = yStart - smallerYRoom.getPos().y - 1; //down

                } else {
                    yLength = biggerYRoom.getPos().y - yStart; //up
                }
                horizontalWay(xStart, yStart, xLength, world);
                verticalWay(xStart + xLength, smallerYRoom.getPos().y + 1, yLength, world);
                if (smallerXRoom.equals(smallerYRoom)) {
                    addCorner(new Position(xStart + xLength + 1, yStart - 1), world);
                } else {
                    addCorner(new Position(xStart + xLength + 1, yStart + 1), world);
                }
                break;
            }
            case 1: {
                Integer xStart = smallerYRoom.getCenterPos().x;
                Integer yStart = smallerYRoom.getMaxY();
                Integer yLength = biggerYRoom.getPos().y - smallerYRoom.getMaxY() + 1;
                Integer xLength;
                verticalWay(xStart, yStart, yLength, world);
                if (smallerXRoom.equals(smallerYRoom)) { // right
                    xLength = biggerXRoom.getPos().x - xStart;
                    addCorner(new Position(xStart - 1, yStart + yLength + 1), world);
                } else { // left
                    addCorner(new Position(xStart + 1, yStart + yLength + 1), world);
                    xLength = xStart - smallerXRoom.getMaxX();
                    xStart = smallerXRoom.getMaxX();
                }
                horizontalWay(xStart, yStart + yLength, xLength, world);
                break;
            }
            default: {
                return;
            }
        }
    }

    public static void verticalWay(Integer xStart, Integer yStart, Integer yLength,
                                   TETile[][] world) {
        for (Integer yPos = yStart; yPos <= yStart + yLength; yPos++) {
            world[xStart][yPos] = SPACE;
            if (world[xStart + 1][yPos].equals(EMPTYSPACE)) {
                world[xStart + 1][yPos] = BORDER;
            }
            if (world[xStart - 1][yPos].equals(EMPTYSPACE)) {
                world[xStart - 1][yPos] = BORDER;
            }
        }
    }

    public static void horizontalWay(Integer xStart, Integer yStart, Integer xLength,
                                     TETile[][] world) {
        for (Integer xPos = xStart; xPos <= xStart + xLength; xPos++) {
            world[xPos][yStart] = SPACE;
            if (world[xPos][yStart + 1].equals(EMPTYSPACE)) {
                world[xPos][yStart + 1] = BORDER;
            }
            if (world[xPos][yStart - 1].equals(EMPTYSPACE)) {
                world[xPos][yStart - 1] = BORDER;
            }
        }
    }

    public static void addCorner(Position pos, TETile[][] world) {
        if (world[pos.x][pos.y].equals(EMPTYSPACE)) {
            world[pos.x][pos.y] = BORDER;
        }
    }

    public static void createHorizontal(Room room1, Room room2, TETile[][] world, Integer horOnlyY,
                                        HashMap<Integer, HashSet<Integer>> filledSpace) {
        Integer potentialX;
        Integer sideLength;
        if (room1.getMaxX() < room2.getMaxX()) {
            potentialX = room1.getMaxX();
            sideLength = room2.getPos().x - room1.getMaxX();
        } else {
            potentialX = room2.getMaxX();
            sideLength = room1.getPos().x - room2.getMaxX();
        }
        horizontalWay(potentialX, horOnlyY, sideLength, world);
    }

    public static void createVertical(Room room1, Room room2, TETile[][] world, Integer verOnlyX,
                                      HashMap<Integer, HashSet<Integer>> filledSpace) {
        Integer potentialY;
        Integer sideLength;
        if (room1.getMaxY() < room2.getMaxY()) {
            potentialY = room1.getMaxY();
            sideLength = room2.getPos().y - room1.getMaxY();
        } else {
            potentialY = room2.getMaxY();
            sideLength = room1.getPos().y - room2.getMaxY();
        }
        verticalWay(verOnlyX, potentialY, sideLength, world);
    }


    public static Integer onlyHorizontal(Room room1, Room room2) {
        ArrayList<Integer> room1YValues = new ArrayList<>();
        Integer room1y = room1.getPos().y;
        for (Integer y = room1y + 1; y < room1.getMaxY(); y++) {
            room1YValues.add(y);
        }
        Integer room2y = room2.getPos().y;
        for (Integer y2 = room2y + 1; y2 < room2.getMaxY(); y2++) {
            if (room1YValues.contains(y2)) {
                return y2;
            }
        }
        return 0;
    }

    public static Integer onlyVertical(Room room1, Room room2) {
        ArrayList<Integer> room1XValues = new ArrayList<>();
        Integer room1x = room1.getPos().x;
        for (Integer x = room1x + 1; x < room1.getMaxX(); x++) {
            room1XValues.add(x);
        }
        Integer room2x = room2.getPos().x;
        for (Integer x2 = room2x + 1; x2 < room2.getMaxX(); x2++) {
            if (room1XValues.contains(x2)) {
                return x2;
            }
        }
        return 0;
    }

    public HashMap<ArrayList<Room>, HashMap<Integer,
            HashSet<Integer>>> randomRoomGenerator(TETile[][] world) {
        HashMap<Integer, HashSet<Integer>> filledSpace = new HashMap<>();
        HashMap<ArrayList<Room>, HashMap<Integer, HashSet<Integer>>> returnMap = new HashMap<>();
        ArrayList<Room> roomLists = new ArrayList<>();
        for (Integer i = 0; i < MAXROOM; i++) {
            Integer xCord = this.r.nextInt(Engine.WIDTH - 5 - Room.getMaxRoomXLength());
            Integer yCord = this.r.nextInt(Engine.HEIGHT - 5 - Room.getMaxRoomYLength());
            Integer xLength = this.r.nextInt(Room.getMaxRoomXLength()) + 2;
            Integer yLength = this.r.nextInt(Room.getMaxRoomYLength()) + 2;
            if (xLength < Room.getMinRoomXLength()) {
                xLength += 4;
            }
            if (yLength < Room.getMinRoomYLength()) {
                yLength += 4;
            }
            Position pos = new Position(xCord, yCord);
            if (!availableSpace(pos, xLength, yLength, filledSpace)) {
                continue;
            }
            Room room = new Room(pos, xLength, yLength);
            room.makeRoom(xLength, yLength, pos, world);
            addToFilledSpace(pos, room.getXLength(), room.getYLength(), filledSpace);
            roomLists.add(room);
        }
        returnMap.put(roomLists, filledSpace);
        return returnMap;
    }

    public void healthBoostMaker(TETile[][] world) {
        int healthBoost = 0;
        while (healthBoost < 5) {
            Integer randomRoomNum = r.nextInt(roomList.size());
            Room randomRoom = roomList.get(randomRoomNum);
            Integer randomX = randomRoom.getPos().x + r.nextInt(randomRoom.getXLength() - 2) + 1;
            Integer randomY = randomRoom.getPos().y + r.nextInt(randomRoom.getYLength() - 2) + 1;
            if (world[randomX][randomY].equals(SPACE)
                    && goodLightPosition(randomX, randomY, world)) {
                world[randomX][randomY] = Tileset.HEALTHBOOST;
                healthBoost += 1;
            }
        }
    }


    public void portalMaker(TETile[][] world) {
        int portals = 0;
        while (portals < 2) {
            Integer randomRoomNum = r.nextInt(roomList.size());
            Room randomRoom = roomList.get(randomRoomNum);
            Integer randomX = randomRoom.getPos().x + r.nextInt(randomRoom.getXLength() - 2) + 2;
            Integer randomY = randomRoom.getPos().y + r.nextInt(randomRoom.getYLength() - 2) + 2;
            if (world[randomX][randomY].equals(SPACE)
                    && goodLightPosition(randomX, randomY, world)
                    && !roomContainsPortal(randomRoom, world)) {
                world[randomX][randomY] = PORTAL;
                this.portalPos.add(new Position(randomX, randomY));
                portals += 1;
            }
        }
    }

    private boolean roomContainsPortal(Room room, TETile[][] world) {
        for (int x = room.getPos().x; x < room.getMaxX(); x++) {
            for (int y = room.getPos().y; y < room.getMaxY(); y++) {
                if (world[x][y].equals(PORTAL)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void makeLockedDoor(TETile[][] world) {
        boolean madeDoor = false;
        while (!madeDoor) {
            Integer randomRoomNum = r.nextInt(roomList.size());
            Room randomRoom = roomList.get(randomRoomNum);
            Integer randomY = randomRoom.getPos().y + r.nextInt(randomRoom.getYLength() - 2) + 1;
            if (world[randomRoom.getPos().x][randomY].equals(BORDER)
                    && !world[randomRoom.getPos().x][randomY].equals(PORTAL)) {
                world[randomRoom.getPos().x][randomY] = Tileset.LOCKED_DOOR;
                this.doorPos = new Position(randomRoom.getPos().x, randomY);
                madeDoor = true;
            }
        }
    }

    public void lightOff(TETile[][] world) {
        for (int i = 0; i < lightStatus.length; i++) {
            if (!lightStatus[i]) {
                Position closestLight = this.lightPos.get(i);
                ArrayList<Position> middleLightsPos
                        = middleLights(world, closestLight,
                        roomList.get(lightPos.indexOf(closestLight)),
                        Tileset.FLOOR, Tileset.MIDDLELIGHT, Tileset.MIDDLELIGHT);
                outerLights(world, closestLight, middleLightsPos,
                        roomList.get(lightPos.indexOf(closestLight)),
                        Tileset.FLOOR, Tileset.OUTERLIGHT, Tileset.OUTERLIGHT);
            } else {
                Position closestLight = this.lightPos.get(i);
                ArrayList<Position> middleLightsPos
                        = middleLights(world, closestLight,
                        roomList.get(lightPos.indexOf(closestLight)),
                        Tileset.MIDDLELIGHT, World.getSpace(), Tileset.STEPPEDFLOOR);
                outerLights(world, closestLight, middleLightsPos,
                        roomList.get(lightPos.indexOf(closestLight)),
                        Tileset.OUTERLIGHT, World.getSpace(), Tileset.STEPPEDFLOOR);
            }
        }
    }

    public boolean goodLightPosition(int x, int y, TETile[][] world) {
        if (world[x + 1][y].description().equals("light")) {
            return false;
        } else if (world[x - 1][y].description().equals("light")) {
            return false;
        } else if (world[x][y + 1].description().equals("light")) {
            return false;
        } else if (world[x][y - 1].description().equals("light")) {
            return false;
        }
        return true;
    }

    public void lightSwitch(Avatar avatar, Position currentPos, TETile[][] world) {
        Position closestLight = this.lightPos.get(0);
        long closestDist = 100000;
        for (Position pos: this.lightPos) {
            if (Position.dist(pos, currentPos) < closestDist) {
                closestDist = Position.dist(pos, currentPos);
                closestLight = pos;
            }
        }
        if (this.lightStatus[(lightPos.indexOf(closestLight))]) { // Close Lights
            world[closestLight.x][closestLight.y] = Tileset.CLOSEDINNERLIGHT;
            ArrayList<Position> middleLightsPos
                    = middleLights(world, closestLight,
                    roomList.get(lightPos.indexOf(closestLight)),
                    Tileset.FLOOR, Tileset.MIDDLELIGHT, Tileset.MIDDLELIGHT);
            outerLights(world, closestLight, middleLightsPos,
                    roomList.get(lightPos.indexOf(closestLight)),
                    Tileset.FLOOR, Tileset.OUTERLIGHT, Tileset.OUTERLIGHT);
            this.lightStatus[(this.lightPos.indexOf(closestLight))] = false;
        } else { // Open Lights
            checkAvatarOnLight(avatar, closestLight, world);
            world[closestLight.x][closestLight.y] = Tileset.INNERLIGHT;
            ArrayList<Position> middleLightsPos
                    = middleLights(world, closestLight,
                    roomList.get(lightPos.indexOf(closestLight)),
                    Tileset.MIDDLELIGHT, World.getSpace(), Tileset.STEPPEDFLOOR);
            outerLights(world, closestLight, middleLightsPos,
                    roomList.get(lightPos.indexOf(closestLight)),
                    Tileset.OUTERLIGHT, World.getSpace(), Tileset.STEPPEDFLOOR);
            this.lightStatus[(lightPos.indexOf(closestLight))] = true;
        }
    }

    private void checkAvatarOnLight(Avatar avatar, Position closestLight, TETile[][] world) {
        boolean done = false;
        for (int x = -2; x < 3; x++) {
            if (done) {
                break;
            }
            for (int y = -2; y < 3; y++) {
                if (world[closestLight.x + x][closestLight.y + y].equals(Tileset.AVATAR)) {
                    avatar.subtractHealth();
                    done = true;
                    break;
                }
            }
        }
    }


    public void lightMaker(TETile[][] world) {
        int i = 0;
        this.lightStatus = new boolean[roomList.size()];
        for (Room room : roomList) {
            boolean roomMade = false;
            while (!roomMade) {
                Integer randomX = room.getPos().x + r.nextInt(room.getXLength() - 2) + 2;
                Integer randomY = room.getPos().y + r.nextInt(room.getYLength() - 2) + 2;
                while (randomX - 3 < 0 || randomY - 3 < 0) {
                    randomX = room.getPos().x + r.nextInt(room.getXLength() - 3) + 3;
                    randomY = room.getPos().y + r.nextInt(room.getYLength() - 3) + 3;
                }
                if (world[randomX][randomY].equals(SPACE)
                        || world[randomX][randomY].equals(Tileset.STEPPEDFLOOR)) {
                    world[randomX][randomY] = Tileset.INNERLIGHT;
                    this.lightPos.add(new Position(randomX, randomY));
                    this.lightStatus[i] = true;
                    roomMade = true;
                    i += 1;
                    ArrayList<Position> middleLightsPos
                            = middleLights(world, new Position(randomX, randomY), room,
                            Tileset.MIDDLELIGHT, World.getSpace(), World.getSpace());
                    outerLights(world, new Position(randomX, randomY), middleLightsPos, room,
                            Tileset.OUTERLIGHT, World.getSpace(), World.getSpace());
                }
            }
        }
    }

    public ArrayList<Position> middleLights(TETile[][] world, Position centerPos, Room room,
                                            TETile lightType, TETile replaced, TETile replaced2) {
        ArrayList<Position> middleLights = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (room.getPos().x <= centerPos.x + x
                        && centerPos.x + x <= room.getMaxX()
                        && room.getPos().y <= centerPos.y + y
                        && centerPos.y + y <= room.getMaxY()) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    if (world[centerPos.x + x][centerPos.y + y].equals(replaced)
                            || world[centerPos.x + x][centerPos.y + y].equals(replaced2)) {
                        world[centerPos.x + x][centerPos.y + y] = lightType;
                        middleLights.add(new Position(centerPos.x + x, centerPos.y + y));
                    }
                }
            }
        }
        return middleLights;
    }

    public void outerLights(TETile[][] world, Position centerPos,
                            ArrayList<Position> middleLightsPos, Room room,
                            TETile lightType, TETile replaced, TETile replaced2) {
        for (int x = -2; x < 3; x++) {
            for (int y = -2; y < 3; y++) {
                if (room.getPos().x <= centerPos.x + x
                        && centerPos.x + x <= room.getMaxX()
                        && room.getPos().y <= centerPos.y + y
                        && centerPos.y + y <= room.getMaxY()) {
                    if (middleLightsPos.contains(new Position(centerPos.x + x, centerPos.y + y))) {
                        continue;
                    }
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    if (world[centerPos.x + x][centerPos.y + y].equals(replaced)
                            || world[centerPos.x + x][centerPos.y + y].equals(replaced2)) {
                        world[centerPos.x + x][centerPos.y + y] = lightType;
                    }
                }
            }
        }
    }


    public static boolean availableSpace(Position pos, Integer xLength, Integer yLength,
                                         HashMap<Integer, HashSet<Integer>> filledSpace) {
        for (Integer x = pos.x; x < pos.x + xLength; x++) {
            for (Integer y = pos.y; y < pos.y + yLength; y++) {
                if (filledSpace.containsKey(x) && filledSpace.get(x).contains(y)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void addToFilledSpace(Position pos, Integer xLength, Integer yLength,
                                        HashMap<Integer, HashSet<Integer>> filledSpace) {
        for (Integer x = pos.x; x < pos.x + xLength; x++) {
            for (Integer y = pos.y; y < pos.y + yLength; y++) {
                if (!filledSpace.containsKey(x)) {
                    filledSpace.put(x, new HashSet<Integer>());
                }
                filledSpace.get(x).add(y);
            }
        }
    }


    public static HashMap<ArrayList<Room>, Integer> edgeToDistance(Graph<Room> roomGraph) {
        HashMap<ArrayList<Room>, Integer> edgeToDistanceMap = new HashMap<>();
        for (Room room : roomGraph.getMap().keySet()) {
            for (Room connectedRooms : roomGraph.getMap().get(room)) {
                ArrayList<Room> edgeDist = new ArrayList<>();
                Integer xDistance = room.getCenterPos().x - connectedRooms.getCenterPos().x;
                Integer yDistance = room.getCenterPos().y - connectedRooms.getCenterPos().y;
                Integer distance = (int) Math.round(Math.sqrt(Math.pow(xDistance, 2)
                        + Math.pow(yDistance, 2)));
                edgeDist.add(room);
                edgeDist.add(connectedRooms);
                edgeToDistanceMap.put(edgeDist, distance);
            }
        }
        return edgeToDistanceMap;
    }

    public void setAvatarPos(TETile[][] world, ArrayList<Room> roomLists) {
        boolean foundPos = false;
        while (!foundPos) {
            Integer randomRoomNum = r.nextInt(roomLists.size());
            Room randomRoom = roomLists.get(randomRoomNum);
            Integer randomX = randomRoom.getPos().x + r.nextInt(randomRoom.getXLength() - 1) + 1;
            Integer randomY = randomRoom.getPos().y + r.nextInt(randomRoom.getYLength() - 1) + 1;
            if (world[randomX][randomY].equals(SPACE)) {
                this.avatarPos = new Position(randomX, randomY);
                foundPos = true;
            }
        }
    }

    public Position getAvatarPos() {
        return this.avatarPos;
    }

    public static TETile getCoin() {
        return COIN;
    }

    public static TETile getEmptyspace() {
        return EMPTYSPACE;
    }

    public static TETile getPortal() {
        return PORTAL;
    }

    public ArrayList<Position> getPortalPos() {
        return this.portalPos;
    }

    public static int getMaxcoins() {
        return MAXCOINS;
    }

    public Position getDoorPos() {
        return this.doorPos;
    }
}
