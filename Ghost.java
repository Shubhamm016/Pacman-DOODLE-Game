import java.awt.Rectangle;

public class Ghost {
    private int x, y, size;
    private int speed = 4;

    public Ghost(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
    public int getSpeed() { return speed; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
