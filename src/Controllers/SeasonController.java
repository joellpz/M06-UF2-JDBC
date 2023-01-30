package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SeasonController {
    private Connection connection;
    private NBAController nbaController ;

    public SeasonController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
    }

    public void showSeasons() throws SQLException, IOException {
        nbaController.showTable("seasons");
    }
}
