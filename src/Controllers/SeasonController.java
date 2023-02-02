package Controllers;

import java.sql.Connection;

/**
 * Controlador para la tabla 'seasons'
 */
public class SeasonController {
    private final Connection connection;
    private final NBAController nbaController ;
    private final String table;
    /**
     * Define la conexión del controlador con la tabla Temporadas (Seasons)
     * @param connection Data Base Connection
     */
    public SeasonController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
        this.table = "seasons";
    }

    /**
     * Muestra la información contenida dentro de la tabla Seasons
     * 
     */
    public void showSeasons()  {
        nbaController.showTable(table);
    }

    /**
     * Añade una Temporada dentro de la tabla Seasons
     * 
     */
    public void newSeason()  {
        nbaController.insertNewData(table);
    }

    /**
     * Actualiza la información sobre una Temporada
     * 
     */
    public void updateSeason()  {
        nbaController.updateData(table);
    }
    /**
     * Elimina la entrada sobre una Temporada
     * 
     */
    public void deleteSeason() {
        nbaController.deleteData(table);
    }
}
