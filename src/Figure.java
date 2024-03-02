public class Figure {
    private int[][] shape;
    private int x, y;
    private final Field field;

    public Figure(int[][] shape, int x, int y, Field field) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.field = field;
    }

    public void rotate() {
        int[][] rotatedShape = getRotatedShape();

        if (canPlaceAt(rotatedShape, x, y)) {
            shape = rotatedShape;
        }
    }

    private int[][] getRotatedShape() {
        int n = shape.length;
        int m = shape[0].length;
        int[][] rotatedShape = new int[m][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotatedShape[j][n - 1 - i] = shape[i][j];
            }
        }
        return rotatedShape;
    }

    public boolean canMove(int deltaX, int deltaY) {
        return canPlaceAt(shape, x + deltaX, y + deltaY);
    }

    public boolean moveDown() {
        if (canMove(0, 1)) {
            y++;
            return true;
        } else {
            return false;
        }
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }


    private boolean canPlaceAt(int[][] candidateShape, int candidateX, int candidateY) {
        for (int row = 0; row < candidateShape.length; row++) {
            for (int col = 0; col < candidateShape[row].length; col++) {
                if (candidateShape[row][col] != 0) {
                    int newX = candidateX + col;
                    int newY = candidateY + row;
                    if (newX < 0 || newX >= field.getWidth() || newY < 0 || newY >= field.getHeight() || field.isOccupied(newX, newY)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}