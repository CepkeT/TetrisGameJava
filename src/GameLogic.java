public class GameLogic {
    private int score;
    private int level;
    private int linesRemoved;
    private static final int BASE_SCORE_FOR_LEVEL = 100;
    private static final double LEVEL_MULTIPLIER = 2;
    private static final int INITIAL_DELAY = 700;
    private int delay;

    public GameLogic() {
        this.score = 0;
        this.level = 1;
        this.linesRemoved = 0;
        this.delay = INITIAL_DELAY;
    }

    public int calculateScore(int removedLines) {
        return switch (removedLines) {
            case 1 -> 100 * level;
            case 2 -> 300 * level;
            case 3 -> 500 * level;
            case 4 -> 800 * level;
            default -> 0;
        };
    }

    public void checkLevelUp() {
        int nextLevelScore = (int) (BASE_SCORE_FOR_LEVEL * Math.pow(LEVEL_MULTIPLIER, level - 1));
        if (score >= nextLevelScore) {
            level++;
            delay = INITIAL_DELAY;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public void setLinesRemoved(int linesRemoved) {
        this.linesRemoved = linesRemoved;
    }

    public int getDelay() {
        return delay;
    }

    public void reset() {
        this.score = 0;
        this.level = 1;
        this.linesRemoved = 0;
        this.delay = INITIAL_DELAY;
    }
}
