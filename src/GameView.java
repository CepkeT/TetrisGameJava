import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GameView extends JPanel {
    static final int TILE_SIZE = 30;
    private GameController controller;

    public GameView(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(controller.getField().getWidth() * TILE_SIZE,
                controller.getField().getHeight() * TILE_SIZE));
        setFocusable(true);
        addKeyListener(new TAdapter());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawField(g);
        drawFigure(g);
        drawNextFigure(g);
        drawStatus(g);
        drawBorders(g);
        displayLeaderBoard(g);
    }

    public void setController(GameController gameController) {
        this.controller = gameController;
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (controller.isGameRunning() || e.getKeyCode() == KeyEvent.VK_P) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        controller.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        controller.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        controller.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        // Повернуть фигуру на стрелку вверх
                        controller.rotate();
                        break;
                    case KeyEvent.VK_P:
                        // Пауза на P, возобновление игры, если она была приостановлена
                        controller.pauseGame();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        // Выход из игры на ESC
                        controller.quitGame();  // Вместо System.exit(0); лучше вынести в отдельный метод
                        break;
                }
            }
        }
    }

    private void drawField(Graphics g) {
        g.setColor(Color.black);
        Field field = controller.getField();
        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (field.isOccupied(col, row)) {
                    drawTile(g, col * TILE_SIZE, row * TILE_SIZE, field.getValue(col, row));
                }
            }
        }
    }

    private void drawFigure(Graphics g) {
        Figure figure = controller.getCurrentFigure();
        int[][] shape = figure.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int x = (figure.getX() + col) * TILE_SIZE;
                    int y = (figure.getY() + row) * TILE_SIZE;
                    drawTile(g, x, y, shape[row][col]);
                }
            }
        }
    }

    private void drawNextFigure(Graphics g) {
        int x = (controller.getField().getWidth() + 1) * TILE_SIZE;
        int y = TILE_SIZE * 10;

        Figure nextFigure = controller.getNextFigure();
        int[][] shape = nextFigure.getShape();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    drawTile(g, x + col * TILE_SIZE, y + row * TILE_SIZE, shape[row][col]);
                }
            }
        }
    }

    private void drawStatus(Graphics g) {
        int sx = controller.getField().getWidth() * TILE_SIZE + 10;
        int sy = TILE_SIZE;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Tahoma", Font.BOLD, 14));
        g.drawString(STR."Цыферки Очков: \{controller.getScore()}", sx, sy);
        g.drawString(STR."Левель: \{controller.getLevel()}", sx, sy + 20);
        //g.drawString(STR."линии: \{controller.getLinesRemoved()}", sx, sy + 40);
        g.drawString(STR."Скорость:\{controller.getDelay()}", sx, sy + 60);
    }

    private void drawBorders(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, controller.getField().getWidth() * TILE_SIZE,
                controller.getField().getHeight() * TILE_SIZE);
    }

    private void displayLeaderBoard(Graphics g) {
        int sx = controller.getField().getWidth() * TILE_SIZE + 10;
        int sy = TILE_SIZE * 4;

        g.setFont(new Font("Thoma", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString("Пис** РейтинМетр:", sx, sy);
        List<PlayerScore> leaderBoard = controller.getLeaderBoard();
        if (leaderBoard != null) {
            sy += 20;
            for (int i = 0; i < leaderBoard.size() && i < 5; i++) {
                PlayerScore score = leaderBoard.get(i);
                g.drawString(STR."\{score.getName()}: \{score.getScore()}", sx, sy + i * 20);
            }
        }
    }

    private void drawTile(Graphics g, int x, int y, int value) {
        g.setColor(Color.GRAY);
        g.fillRect(x + 1, y + 1, TILE_SIZE - 4, TILE_SIZE - 4);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
    }
}