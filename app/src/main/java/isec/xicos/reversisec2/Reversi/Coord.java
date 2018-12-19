package isec.xicos.reversisec2.Reversi;

import java.io.Serializable;

public class Coord implements Serializable {
    int x, y;
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
