package Controllers;

import Controllers.NBAController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PlayerController {

	private Connection connection;
	private NBAController nbaController ;
	private String table = "players";
	List<String> columns;
	
	public PlayerController(Connection c) throws SQLException {
		this.connection = c;
		this.nbaController = new NBAController(connection);
		columns = nbaController.getColumnsName(table);
	}

	public void showPlayers() throws SQLException, IOException {
		nbaController.showTable("players");
	}

	public void newPlayer() throws SQLException {
		nbaController.insertNewData("players");
	}
	public void updatePlayer(){

	}

}
