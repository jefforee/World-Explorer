package byow.Core;

public class Position {
    Integer x;
    Integer y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Integer min(Integer X, Integer Y) {
        if (X < Y) {
            return X;
        } else {
            return Y;
        }
    }

    public static Integer max(Integer X, Integer Y) {
        if (X > Y) {
            return X;
        } else {
            return Y;
        }
    }

    public static long dist(Position pos1, Position pos2) {
        int xDist = pos2.x - pos1.x;
        int yDist = pos2.y - pos1.y;
        return Math.round(Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2)));
    }

}
