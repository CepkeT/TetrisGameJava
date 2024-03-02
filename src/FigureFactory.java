import java.util.Random;

public class FigureFactory {
    private static final int[][][] SHAPES = {
            // I-фигура
            {{1, 1, 1, 1}},
            // O-фигура
            {{1, 1}, {1, 1}},
            // T-фигура
            {{0, 1, 0}, {1, 1, 1}},
            // L-фигура
            {{1, 0}, {1, 0}, {1, 1}},
            // J-фигура
            {{0, 1}, {0, 1}, {1, 1}},
            // S-фигура
            {{1, 1, 0}, {0, 1, 1}},
            // Z-фигура
            {{0, 1, 1}, {1, 1, 0}}
    };

    public static Figure createRandomFigure(Field field) {
        Random rand = new Random();
        int type = rand.nextInt(SHAPES.length);
        int[][] shape = SHAPES[type];
        int x = rand.nextInt(field.getWidth() - shape[0].length);
        int y = -shape.length + 2;
        return new Figure(shape, x, y, field);
    }

}