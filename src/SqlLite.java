import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.concurrent.ExecutorService;

public class SqlLite implements AutoCloseable {
    private final Connection connection;
    private final ExecutorService executorService;

    public SqlLite(ExecutorService executorService) throws ClassNotFoundException, SQLException {
        this.executorService = executorService;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        System.out.println("DB connected");

        executorService.submit(this::initializeDatabase);
    }

    private void initializeDatabase() {

        String sql = "CREATE TABLE IF NOT EXISTS LeaderBoard (" +
                "Name TEXT NOT NULL," +
                "Score INTEGER NOT NULL);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PlayerScore> getLeaderBoard() {
        List<PlayerScore> leaderBoard = new ArrayList<>();
        String sql = "SELECT Name, Score FROM LeaderBoard ORDER BY Score DESC;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("Name");
                int score = rs.getInt("Score");
                leaderBoard.add(new PlayerScore(name, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderBoard;
    }

    public void saveScore(String name, int score) {
        String sql = "INSERT INTO LeaderBoard (Name, Score) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void asyncSaveScore(String name, int score) {
        executorService.submit(() -> saveScore(name, score));
    }


    @Override
    public void close() {
        executorService.shutdown();
        if (connection != null) {
            try {
                connection.close();
                System.out.println("DB disconnected");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}