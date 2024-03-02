import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameController implements ActionListener {
    private Field field;
    private Figure currentFigure;
    private Figure nextFigure;
    private Timer timer;
    private final GameLogic gameLogic;
    private SqlLite db;
    private final ExecutorService dbExecutor;
    private List<PlayerScore> leaderBoard;
    private GameView gameView;
    private boolean gameRunning;

    public GameController() {
        this.field = new Field(10, 20);
        this.gameLogic = new GameLogic();
        dbExecutor = Executors.newSingleThreadExecutor();
        try {
            db = new SqlLite(dbExecutor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initGame(GameView gameView) {
        this.gameView = gameView;
        this.timer = new Timer(gameLogic.getDelay(), this);
        this.gameLogic.reset();
        this.gameRunning = true;
        timer.start();
        asyncLoadLeaderBoard();
    }


    private void asyncLoadLeaderBoard() {
        dbExecutor.submit(() -> {
            leaderBoard = db.getLeaderBoard();
            SwingUtilities.invokeLater(gameView::repaint);
        });
    }


    public void gameOver() {
        timer.stop();
        field.clear();
        gameRunning = false;
        String name = JOptionPane.showInputDialog(null, "Ты продул, назови себя неудачник!!!:");
        if (name != null && !name.trim().isEmpty()) {
            dbExecutor.submit(() -> {
                db.asyncSaveScore(name, gameLogic.getScore());
                asyncLoadLeaderBoard();
            });
        } else {
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
    }

    private void updateGame() {
        if (!currentFigure.moveDown()) {
            field.absorbFigure(currentFigure);
            int lines = field.removeFullLines();
            if (lines > 0) {
                gameLogic.setLinesRemoved(gameLogic.getLinesRemoved() + lines);
                gameLogic.setScore(gameLogic.getScore() + gameLogic.calculateScore(lines));
                gameLogic.checkLevelUp();
            }
            currentFigure = nextFigure;
            nextFigure = FigureFactory.createRandomFigure(field);
            if (!field.canSpawnFigure(currentFigure)) {
                gameOver();
            }
        }
        gameView.repaint();
        timer.setDelay(gameLogic.getDelay());
    }

    public void moveLeft() {
        if (gameRunning && currentFigure.canMove(-1, 0)) {
            currentFigure.moveLeft();
        }
    }

    public void moveRight() {
        if (gameRunning && currentFigure.canMove(1, 0)) {
            currentFigure.moveRight();
        }
    }

    public void moveDown() {
        if (gameRunning && currentFigure.canMove(0, 1)) {
            currentFigure.moveDown();
        } else {
            field.absorbFigure(currentFigure);
            currentFigure = nextFigure;
            nextFigure = FigureFactory.createRandomFigure(field);
            if (!field.canSpawnFigure(currentFigure)) {
                gameRunning = false;
            }
        }
    }


    public void rotate() {
        if (gameRunning) {
            currentFigure.rotate();
        }
    }

    public void startGame() {
        if(gameRunning || field == null) {
            assert field != null;
            field.clear();
        }
        field = new Field(10, 20);
        currentFigure = FigureFactory.createRandomFigure(field);
        nextFigure = FigureFactory.createRandomFigure(field);
        gameRunning = true;
        if (gameView != null) {
            gameView.setController(this);
            timer.start();
        }
        initGame(gameView);
    }

    public void pauseGame() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
        gameRunning = timer.isRunning();
    }

    public Figure getCurrentFigure() {
        return currentFigure;
    }


    public List<PlayerScore> getLeaderBoard() {
        return leaderBoard;
    }

    public Field getField() {
        return field;
    }

    public int getScore() {
        return gameLogic.getScore();
    }

    public int getLevel() {
        return gameLogic.getLevel();
    }

    public int getLinesRemoved() {
        return gameLogic.getLinesRemoved();
    }

    public int getDelay() {
        return gameLogic.getDelay();
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void dispose() {
        dbExecutor.shutdown();
        try {
            if (!dbExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                dbExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            dbExecutor.shutdownNow();
        }
        if (db != null) {
            db.close();
        }
    }

    public Figure getNextFigure() {
        return nextFigure;
    }
}