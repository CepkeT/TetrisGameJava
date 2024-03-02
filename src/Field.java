import java.util.Arrays;

public class Field {
    private final int[][] grid;

    public Field(int width, int height) {
        grid = new int[height][width];
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public boolean isOccupied(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && grid[y][x] != 0;
    }

    public void setValue(int x, int y, int value) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            grid[y][x] = value;
        }else System.err.println(STR."setValue: \{x}, \{y}");
    }

    public int removeFullLines() {
        int numFullLines = 0;

        for (int row = 0; row < getHeight(); row++) {
            if (isLineFull(row)) {
                numFullLines++;
                removeLine(row);
            }
        }
        return numFullLines;
    }

    public void absorbFigure(Figure figure) {
        for (int row = 0; row < figure.getShape().length; row++) {
            for (int col = 0; col < figure.getShape()[0].length; col++) {
                int val = figure.getShape()[row][col];
                if (val != 0) {
                    int newX = figure.getX() + col;
                    int newY = figure.getY() + row;
                    if (newX >= 0 && newX < getWidth() && newY >= 0 && newY < getHeight()) {
                        setValue(newX, newY, val);
                    }
                }
            }
        }
    }

    private void removeLine(int lineRow) {
        for (int r = lineRow; r > 0; r--) {
            System.arraycopy(grid[r - 1], 0, grid[r], 0, getWidth());
        }
        Arrays.fill(grid[0], 0);
    }

    private boolean isLineFull(int row) {
        return Arrays.stream(grid[row]).allMatch(val -> val != 0);
    }

    public boolean canSpawnFigure(Figure figure) {
        for (int row = 0; row < figure.getShape().length; row++) {
            for (int col = 0; col < figure.getShape()[0].length; col++) {
                if (figure.getShape()[row][col] != 0) {
                    if (isOccupied(col, row)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void clear() {
        for (int[] row : grid) {
            Arrays.fill(row, 0);
        }
    }

    public int getValue(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return grid[y][x];
        } else {
            return 0;
        }
    }
}