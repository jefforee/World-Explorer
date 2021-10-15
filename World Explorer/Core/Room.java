package byow.Core;

import byow.TileEngine.TETile;

public class Room {
    private static final Integer MAXROOMXLENGTH = 10;
    private static final Integer MAXROOMYLENGTH = 10;
    private static final Integer MINROOMXLENGTH = 5;
    private static final Integer MINROOMYLENGTH = 5;


    private Position pos;
    private Integer xLength;
    private Integer yLength;
    private Position centerPos;
    private Integer maxX;
    private Integer maxY;

    public Room(Position pos, Integer xLength, Integer yLength) {
        this.pos = pos;
        this.xLength = xLength;
        this.yLength = yLength;
        this.centerPos = new Position(pos.x + xLength / 2, pos.y + yLength / 2);
        this.maxX = pos.x + xLength - 1;
        this.maxY = pos.y + yLength - 1;
    }

    /** Creates a rectangular room of size xLength by yLength. */
    public static void makeRoom(Integer xLength, Integer yLength, Position pos, TETile[][] world) {
        makeColumn(xLength, yLength, pos, world);
        makeRow(xLength, yLength, pos, world);
        fillSpace(xLength, yLength, pos, world);
    }

    /** Creates the x boundaries for the room. */
    private static void makeColumn(Integer xLength, Integer yLength,
                                   Position pos, TETile[][] world) {
        for (Integer x = pos.x; x < pos.x + xLength; x++) {
            world[x][pos.y] = World.getBorder();
        }
        for (Integer x = pos.x; x < pos.x + xLength; x++) {
            world[x][pos.y + yLength - 1] = World.getBorder();
        }
    }

    /** Creates the y boundaries for the room. */
    private static void makeRow(Integer xLength, Integer yLength, Position pos, TETile[][] world) {
        for (Integer y = pos.y; y < pos.y + yLength; y++) {
            world[pos.x][y] = World.getBorder();
        }
        for (Integer y = pos.y; y < pos.y + yLength; y++) {
            world[pos.x + xLength - 1][y] = World.getBorder();
        }
    }

    /** Fills in the space between the boundaries within the room. */
    private static void fillSpace(Integer xLength, Integer yLength, Position pos,
                                  TETile[][] world) {
        for (Integer y = pos.y + 1; y < (pos.y + yLength - 1); y++) {
            for (Integer x = pos.x + 1; x < (pos.x + xLength - 1); x++) {
                world[x][y] = World.getSpace();
            }
        }
    }

    public static Room smallerX(Room room1, Room room2) {
        if (room1.getPos().x < room2.getPos().x) {
            return room1;
        } else {
            return room2;
        }
    }

    public static Room biggerX(Room room1, Room room2) {
        if (room1.getPos().x > room2.getPos().x) {
            return room1;
        } else {
            return room2;
        }
    }

    public static Room smallerY(Room room1, Room room2) {
        if (room1.getPos().y < room2.getPos().y) {
            return room1;
        } else {
            return room2;
        }
    }

    public static Room biggerY(Room room1, Room room2) {
        if (room1.getPos().y > room2.getPos().y) {
            return room1;
        } else {
            return room2;
        }
    }



    public static Integer getMaxRoomXLength() {
        return MAXROOMXLENGTH;
    }

    public static Integer getMaxRoomYLength() {
        return MAXROOMYLENGTH;
    }

    public static Integer getMinRoomXLength() {
        return MINROOMXLENGTH;
    }

    public static Integer getMinRoomYLength() {
        return MINROOMYLENGTH;
    }

    public Integer getXLength() {
        return this.xLength;
    }

    public Integer getYLength() {
        return this.yLength;
    }

    public Position getCenterPos() {
        return this.centerPos;
    }

    public Position getPos() {
        return this.pos;
    }

    public Integer getMaxX() {
        return this.maxX;
    }

    public Integer getMaxY() {
        return this.maxY;
    }
}
