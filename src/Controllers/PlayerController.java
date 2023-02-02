package Controllers;

import java.sql.Connection;
import java.util.List;

/**
 * Controlador para la tabla 'players'
 */
public class PlayerController {

	private final Connection connection;
	private final NBAController nbaController ;
	private final String table = "players";
	List<String> columns;

	/**
	 * Define la conexión del controlador con la tabla Jugaodres (players)
	 * @param connection Data Base Connection
	 * 
	 */
	public PlayerController(Connection connection)  {
		this.connection = connection;
		this.nbaController = new NBAController(this.connection);
		columns = nbaController.getColumnsName(table);
	}

	/**
	 * Muestra la información contenida dentro de la tabla Players
	 * 
	 */
	public void showPlayers(){
		nbaController.showTable(table);
	}

	/**
	 * Añade un Jugador dentro de la tabla Players
	 * 
	 */
	public void newPlayer()  {
		nbaController.insertNewData(table);
	}
	/**
	 * Actualiza la información sobre un Jugador
	 * 
	 */
	public void updatePlayer()  {
		nbaController.updateData(table);
	}

	/**
	 * Elimina la entrada sobre un Jugador
	 * 
	 */
	public void deletePlayer() {
		nbaController.deleteData(table);
	}

}
