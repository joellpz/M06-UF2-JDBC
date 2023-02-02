package Controllers;

import java.sql.Connection;

/**
 * Controlador para la tabla 'games'
 */
public class GameController {
    private final Connection connection;
    private final NBAController nbaController;
    private final String table;

    /**
     * Define la conexión del controlador con la tabla Equipos (Teams)
     *
     * @param connection Data Base Connection
     */
    public GameController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
        this.table = "games";
    }


    /**
     * Muestra la información contenida dentro de la tabla Games
     */
    public void showGames()  {
        nbaController.showTable(table);
    }

    /**
     * Añade un partido dentro de la tabla
     */
    public void newGame()  {
        nbaController.insertNewData(table);
    }


    /**
     * Actualiza la información sobre un Partido
     */
    public void updateGame()  {
        nbaController.updateData(table);
    }

    /**
     * Elimina la entrada sobre un Partido
     */
    public void deleteGame()  {
        nbaController.deleteData(table);
    }
}
