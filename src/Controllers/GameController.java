package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class GameController {
    private Connection connection;
    private NBAController nbaController ;

    public GameController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
    }

    public void showGames() throws SQLException, IOException {
        nbaController.showTable("games");
    }

    public void newGame() throws SQLException {
        nbaController.insertNewData("games");
    }
}
