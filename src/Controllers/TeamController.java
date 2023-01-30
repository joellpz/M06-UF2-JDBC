package Controllers;

import Controllers.NBAController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class TeamController {

	private Connection connection;
	private NBAController nbaController ;

	public TeamController(Connection connection) {
		this.connection = connection;
		this.nbaController = new NBAController(this.connection);
	}

	public void showTeams() throws SQLException, IOException {
		nbaController.showTable("teams");
	}

}
