import javax.swing.*;
import java.awt.*;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        GameController gameController = new GameController();
        GameView gameView = new GameView(gameController);

        gameController.initGame(gameView);

        JFrame frame = new JFrame("Tetris");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(gameView, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(
                (gameController.getField().getWidth() + 10) * GameView.TILE_SIZE,
                (gameController.getField().getHeight() + 2) * GameView.TILE_SIZE
        ));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameController.startGame();


    });
}