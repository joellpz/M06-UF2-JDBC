import Controllers.*;
import DBConnection.ConnectionFactory;
import Tools.NBAImport;
import Tools.NBAMenu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main
 */
public class NBAMain {


	/**
	 * Connection
	 */
	public static Connection c;

	/**
	 * Main
	 * @param args args
	 * @throws IOException Exception
	 * @throws SQLException Exception
	 */
	public static void main(String[] args) throws IOException, SQLException {
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		c = connectionFactory.connect();

		NBAMenu menu = new NBAMenu();
		NBAImport nbaImport = new NBAImport(c);


		TeamController teamController = new TeamController(c);
		PlayerController playerController = new PlayerController(c);
		SeasonController seasonController = new SeasonController(c);
		GameController gameController = new GameController(c);
		PlayerPerGameController pgameController = new PlayerPerGameController(c);
		PlayerPerSesonController pseasonController = new PlayerPerSesonController(c);
		TeamPerSeasonController tseasonController = new TeamPerSeasonController(c);

//		Connection conn = null;
//		DBConnection.Identity identity;
//		int option;
//		int intents = 0;
//		DBAccessor dbaccessor = new DBAccessor();
//		dbaccessor.init();
//		while (intents < 3 && conn == null) {
//			identity = menu.authenticate(intents);
//			// prova de test
//			identity.toString();
//
//			conn = dbaccessor.getConnection(identity);
//			intents++;
//		}

		int option = menu.mainMenu();
		while (option > 0 && option < 12) {
			switch (option) {
				case 1 -> {
					//Show Tables
					switch (menu.dataMenu()) {
						case 1 -> playerController.showPlayers();
						case 2 -> teamController.showTeams();
						case 3 -> seasonController.showSeasons();
						case 4 -> gameController.showGames();
						case 5 -> tseasonController.showTeamPerSeason();
						case 6 -> pseasonController.showPlayerSeasons();
						case 7 -> pgameController.showPlayerPerGames();
					}
				}
				case 2 -> {
					//Insert Data
					switch (menu.dataMenu()) {
						case 1 -> playerController.newPlayer();
						case 2 -> teamController.newTeam();
						case 3 -> seasonController.newSeason();
						case 4 -> gameController.newGame();
						case 5 -> tseasonController.newTeamPerSeason();
						case 6 -> pseasonController.newPlayerSeasons();
						case 7 -> pgameController.newPlayerPerGame();
					}
				}
				case 3 -> {
					//Update Data
					switch (menu.dataMenu()) {
						case 1 -> playerController.updatePlayer();
						case 2 -> teamController.updateTeam();
						case 3 -> seasonController.updateSeason();
						case 4 -> gameController.updateGame();
						case 5 -> tseasonController.updateTeamPerSeason();
						case 6 -> pseasonController.updatePlayerSeasons();
						case 7 -> pgameController.updatePlayerPerGame();
					}
				}
				case 4 -> {
					//Delete data
					switch (menu.dataMenu()) {
						case 1 -> playerController.deletePlayer();
						case 2 -> teamController.deleteTeam();
						case 3 -> seasonController.deleteSeason();
						case 4 -> gameController.deleteGame();
						case 5 -> tseasonController.deleteTeamPerSeason();
						case 6 -> pseasonController.deletePlayerSeasons();
						case 7 -> pgameController.deletePlayerPerGame();
					}
				}
				case 5 -> {
					//Search info from a player into another tables
					switch (menu.hugeDataMenu()) {
						case 1 -> pgameController.showDataFromPlayers();
						case 2 -> pseasonController.showDataFromPlayers();
					}
				}
				case 6 -> nbaImport.resetDataBase();
				case 7 -> System.exit(0);

				default -> System.out.println("*** Introduce un valor v??lido! ***");
			}
			option = menu.mainMenu();
		}

	}

}

//TO DO Definir el esquema de la base de datos.
//TO DO Creaci??n de una base de datos. (Esta opci??n puede realizarse directamente en PostgreSQL). El resto de opciones deber??an realizarse mediante un men?? de terminal espec??ficamente creado para nuestra pr??ctica.
//TO DO Definir las sentencias de creaci??n de las tablas que guardar??n la informaci??n. El proyecto deber?? incluir un archivo schema.sql con las sentencias de creaci??n de la base de datos.
//TO DO Conexi??n mediante JDBC con la base de datos.
//TODO Manejo de la conexi??n mediante un men?? de terminal que debe tener:
//TO DO 		Una opci??n que permita borrar las tablas de la base de datos y su informaci??n.
//TO DO 		Una opci??n que permita crear las tablas de la base de datos.
//TO DO 		Una opci??n que permita poblar masivamente las tablas de la base de datos leyendo los ficheros generados en la primera pr??ctica.
//TODO 		Diferentes opciones de consulta sobre la informaci??n. Ejemplos:
//TO DO 			Seleccionar todos los elementos que contengan un texto concreto.
//TO DO 			Seleccionar todos los elementos que cumplan una condici??n.
//TO DO 			Seleccionar elementos concretos.
//TO DO 		Posibilidad de modificar un registro concreto de informaci??n. Ejemplo:
//TO DO 			Seleccionar un elemento concreto y permitir su modificaci??n.
//TODO 		Posibilidad de modificar diferentes registros de informaci??n.
//TO DO 		Posibilidad de eliminar un registro concreto de informaci??n.
//TO DO 		Posibilidad de eliminar un conjunto de registros de informaci??n que cumplan un condici??n.
