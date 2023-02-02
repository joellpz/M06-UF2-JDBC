package Controllers;

import java.sql.Connection;

/**
 * Controlador para la tabla 'games'
 */
public class PlayerPerGameController {
    private final Connection connection;
    private final NBAController nbaController;
    private final String table;

    /**
     * Define la conexión del controlador con la tabla Equipos (Teams)
     *
     * @param connection Data Base Connection
     */
    public PlayerPerGameController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
        this.table = "playerpergame";
    }


    /**
     * Muestra la información contenida dentro de la tabla Games
     */
    public void showPlayerPerGames()  {
        nbaController.showTable(table);
    }

    /**
     * Añade un partido dentro de la tabla
     */
    public void newPlayerPerGame()  {
        nbaController.insertNewData(table);
    }


    /**
     * Actualiza la información sobre un Partido
     */
    public void updatePlayerPerGame()  {
        nbaController.updateData(table);
    }

    /**
     * Elimina la entrada sobre un Partido
     */
    public void deletePlayerPerGame()  {
        nbaController.deleteData(table);
    }

    /**
     * Muestra la información de esta tabla en relación al jugador deseado.
     */
    public void showDataFromPlayers(){ nbaController.showDataPerPlayer(table); }
}
