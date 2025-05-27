import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 24;
    private final int NUM_TILES_X = 20;
    private final int NUM_TILES_Y = 20;
    private final int PANEL_WIDTH = TILE_SIZE * NUM_TILES_X;
    private final int PANEL_HEIGHT = TILE_SIZE * NUM_TILES_Y;

    private Timer timer;
    private Pacman pacman;
    private ArrayList<Wall> walls;
    private ArrayList<Food> foods;
    private ArrayList<Ghost> ghosts;

    private int score = 0;
    private int lives = 3;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();
    }

    private void initGame() {
        pacman = new Pacman(TILE_SIZE, TILE_SIZE, TILE_SIZE);
        walls = new ArrayList<>();
        foods = new ArrayList<>();
        ghosts = new ArrayList<>();

        // Create simple maze walls (border)
        for (int i = 0; i < NUM_TILES_X; i++) {
            walls.add(new Wall(i * TILE_SIZE, 0, TILE_SIZE));
            walls.add(new Wall(i * TILE_SIZE, (NUM_TILES_Y - 1) * TILE_SIZE, TILE_SIZE));
        }
        for (int i = 1; i < NUM_TILES_Y - 1; i++) {
            walls.add(new Wall(0, i * TILE_SIZE, TILE_SIZE));
            walls.add(new Wall((NUM_TILES_X - 1) * TILE_SIZE, i * TILE_SIZE, TILE_SIZE));
        }

        // Add some inner walls
        for (int i = 5; i < 15; i++) {
            walls.add(new Wall(i * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE));
        }

        // Add food dots
        for (int x = 1; x < NUM_TILES_X - 1; x++) {
            for (int y = 1; y < NUM_TILES_Y - 1; y++) {
                boolean isWall = false;
                for (Wall wall : walls) {
                    if (wall.getX() == x * TILE_SIZE && wall.getY() == y * TILE_SIZE) {
                        isWall = true;
                        break;
                    }
                }
                if (!isWall) {
                    foods.add(new Food(x * TILE_SIZE + TILE_SIZE / 3, y * TILE_SIZE + TILE_SIZE / 3, TILE_SIZE / 3));
                }
            }
        }

        // Add ghosts
        ghosts.add(new Ghost(18 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE));
        ghosts.add(new Ghost(1 * TILE_SIZE, 18 * TILE_SIZE, TILE_SIZE));

        timer = new Timer(100, this);
    }

    public void startGame() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw walls
        g.setColor(Color.BLUE);
        for (Wall wall : walls) {
            g.fillRect(wall.getX(), wall.getY(), wall.getSize(), wall.getSize());
        }

        // Draw food
        g.setColor(Color.YELLOW);
        for (Food food : foods) {
            g.fillOval(food.getX(), food.getY(), food.getSize(), food.getSize());
        }

        // Draw ghosts
        g.setColor(Color.RED);
        for (Ghost ghost : ghosts) {
            g.fillOval(ghost.getX(), ghost.getY(), ghost.getSize(), ghost.getSize());
        }

        // Draw Pacman
        g.setColor(Color.ORANGE);
        g.fillOval(pacman.getX(), pacman.getY(), pacman.getSize(), pacman.getSize());

        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, 10, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePacman();
        moveGhosts();
        checkCollisions();
        repaint();
    }

    private void movePacman() {
        int nextX = pacman.getX() + pacman.getDx();
        int nextY = pacman.getY() + pacman.getDy();

        if (!isCollisionWithWall(nextX, nextY)) {
            pacman.move();
        }
    }

    private void moveGhosts() {
        Random rand = new Random();
        for (Ghost ghost : ghosts) {
            int dir = rand.nextInt(4);
            int nextX = ghost.getX();
            int nextY = ghost.getY();

            switch (dir) {
                case 0 -> nextX += ghost.getSpeed();
                case 1 -> nextX -= ghost.getSpeed();
                case 2 -> nextY += ghost.getSpeed();
                case 3 -> nextY -= ghost.getSpeed();
            }

            if (!isCollisionWithWall(nextX, nextY)) {
                ghost.setX(nextX);
                ghost.setY(nextY);
            }
        }
    }

    private void checkCollisions() {
        // Check collision with food
        Food eatenFood = null;
        for (Food food : foods) {
            if (pacman.getBounds().intersects(food.getBounds())) {
                eatenFood = food;
                score += 10;
            }
        }
        if (eatenFood != null) {
            foods.remove(eatenFood);
        }

        // Check collision with ghosts
        for (Ghost ghost : ghosts) {
            if (pacman.getBounds().intersects(ghost.getBounds())) {
                lives--;
                resetPositions();
                if (lives <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
                }
                break;
            }
        }
    }

    private void resetPositions() {
        pacman.setX(TILE_SIZE);
        pacman.setY(TILE_SIZE);
        for (Ghost ghost : ghosts) {
            ghost.setX(18 * TILE_SIZE);
            ghost.setY(1 * TILE_SIZE);
        }
    }

    private boolean isCollisionWithWall(int x, int y) {
        Rectangle nextPos = new Rectangle(x, y, pacman.getSize(), pacman.getSize());
        for (Wall wall : walls) {
            if (nextPos.intersects(wall.getBounds())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                pacman.setDx(0);
                pacman.setDy(-pacman.getSpeed());
            }
            case KeyEvent.VK_DOWN -> {
                pacman.setDx(0);
                pacman.setDy(pacman.getSpeed());
            }
            case KeyEvent.VK_LEFT -> {
                pacman.setDx(-pacman.getSpeed());
                pacman.setDy(0);
            }
            case KeyEvent.VK_RIGHT -> {
                pacman.setDx(pacman.getSpeed());
                pacman.setDy(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pacman.setDx(0);
        pacman.setDy(0);
    }
}
