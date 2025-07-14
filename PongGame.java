import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 650);
        frame.setLocationRelativeTo(null);
        frame.add(new GamePanel());
        frame.setVisible(true);
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Paddle player1, player2;
    private Ball ball;
    private Timer timer;
    private int score1 = 0, score2 = 0; // Placar

    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        player1 = new Paddle(50, 250, 10, 100, KeyEvent.VK_W, KeyEvent.VK_S);
        player2 = new Paddle(740, 250, 10, 100, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        ball = new Ball(390, 290, 20);

        timer = new Timer(16, this); // Aproximadamente 60 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Desenha os elementos do jogo
        player1.draw(g);
        player2.draw(g);
        ball.draw(g);

        // Desenha a linha central
        g.setColor(Color.WHITE);
        g.fillRect(390, 0, 2, 600);

        // Exibir pontuação
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(score1), 350, 50);
        g.drawString(String.valueOf(score2), 430, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.move();
        player2.move();
        ball.move();
        ball.checkCollision(player1, player2);

        int scored = ball.checkScore();
        if (scored == 1) {
            score1++;
            ball.reset();
        } else if (scored == 2) {
            score2++;
            ball.reset();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player1.keyPressed(e.getKeyCode());
        player2.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player1.keyReleased(e.getKeyCode());
        player2.keyReleased(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

class Paddle {
    private int x, y, width, height;
    private int speed = 10;
    private int upKey, downKey;
    private boolean moveUp = false, moveDown = false;

    public Paddle(int x, int y, int width, int height, int upKey, int downKey) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.upKey = upKey;
        this.downKey = downKey;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public void move() {
        if (moveUp && y > 0)
            y -= speed;
        if (moveDown && y + height < 600)
            y += speed;
    }

    public void keyPressed(int keyCode) {
        if (keyCode == upKey)
            moveUp = true;
        if (keyCode == downKey)
            moveDown = true;
    }

    public void keyReleased(int keyCode) {
        if (keyCode == upKey)
            moveUp = false;
        if (keyCode == downKey)
            moveDown = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class Ball {
    private int x, y, diameter;
    private int xSpeed = 5, ySpeed = 5;

    public Ball(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }

    public void move() {
        x += xSpeed;
        y += ySpeed;

        if (y <= 0 || y >= 600 - diameter) {
            ySpeed = -ySpeed;
        }
    }

    public void checkCollision(Paddle p1, Paddle p2) {
        if (x <= p1.getX() + p1.getWidth() && y >= p1.getY() && y <= p1.getY() + p1.getHeight()) {
            xSpeed = -xSpeed;
            speedUp();
        }

        if (x + diameter >= p2.getX() && y >= p2.getY() && y <= p2.getY() + p2.getHeight()) {
            xSpeed = -xSpeed;
            speedUp();
        }
    }

    private void speedUp() {
        if (xSpeed > 0)
            xSpeed += 1;
        else
            xSpeed -= 1;

        if (ySpeed > 0)
            ySpeed += 1;
        else
            ySpeed -= 1;
    }

    public int checkScore() {
        if (x < 0)
            return 2; // Jogador 2 pontua
        if (x > 800)
            return 1; // Jogador 1 pontua
        return 0;
    }

    public void reset() {
        x = 390;
        y = 290;
        xSpeed = (Math.random() > 0.5) ? 5 : -5;
        ySpeed = (Math.random() > 0.5) ? 5 : -5;
    }
}
