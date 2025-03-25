package Demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JPanel implements ActionListener {
    final int TILE_SIZE = 20;
    final int WIDTH = 600;
    final int HEIGHT = 600;
    final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    final int x[] = new int[TOTAL_TILES];
    final int y[] = new int[TOTAL_TILES];
    int bodyParts = 3;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = true;
    Timer timer;
    int score = 0;
    final int POINTS_PER_FOOD = 5;
    JFrame frame;
    JLabel scoreLabel;

    public Main(JFrame frame, JLabel scoreLabel) {
        this.frame = frame;
        this.scoreLabel = scoreLabel;
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new KeyHandler());
        startGame();
    }

    public void startGame() {
        spawnFood();
        timer = new Timer(100, this);
        timer.start();
    }

    public void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break;
            case 'D': y[0] += TILE_SIZE; break;
            case 'L': x[0] -= TILE_SIZE; break;
            case 'R': x[0] += TILE_SIZE; break;
        }
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                timer.stop();
                repaint();
                return;
            }
        }
        if (x[0] < 0) {
            x[0] = WIDTH - TILE_SIZE;
        } else if (x[0] >= WIDTH) {
            x[0] = 0;
        } else if (y[0] < 0) {
            y[0] = HEIGHT - TILE_SIZE;
        } else if (y[0] >= HEIGHT) {
            y[0] = 0;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            bodyParts++;
            score += POINTS_PER_FOOD;
            scoreLabel.setText("Score: " + score);
            spawnFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
                case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
                case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
                case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(scoreLabel, BorderLayout.NORTH);
        
        Main game = new Main(frame, scoreLabel);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
