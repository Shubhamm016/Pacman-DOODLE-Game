public class Pacman {
    private int x, y, size;
    private int dx, dy;
    private int speed = 6;

    public Pacman(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = 0;
        this.dy = 0;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(x, y, size, size);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }
    public int getSpeed() { return speed; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
}
