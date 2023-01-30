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
		this.connection = connection;
		this.nbaController = new NBAController(this.connection);
		columns = nbaController.getColumnsName(table);
	}

	public void showPlayers() throws SQLException, IOException {
		nbaController.showTable("players");
	}

	public void newPlayer() throws SQLException {
		System.out.println("*** Introduce la siguiente información. ***");
		String sqlBase = "INSERT INTO " + table + "(";
		String aux = ") VALUES (";
		for (String column: columns) {
			sqlBase.concat("\""+column+"\",");
			aux = aux.concat("?,");
		}
		sqlBase = sqlBase.substring(0, sqlBase.length() - 1).concat(aux.substring(0, aux.length() - 1).concat(");"));
		PreparedStatement pst = connection.prepareStatement(sqlBase);


	}
	public void updatePlayer(){

	}

}
