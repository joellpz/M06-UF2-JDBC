package Controllers;

import java.sql.Connection;

/**
 * Controlador para la tabla 'teams'
 */
public class TeamPerSeasonController {

    private final Connection connection;
    private final NBAController nbaController;
    private final String table;

    /**
     * Define la conexión del controlador con la tabla Equipos (Teams)
     * @param connection Data Base Connection
     */
    public TeamPerSeasonController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
        this.table = "teamperseason";
    }

    /**
     * Muestra la información contenida dentro de la tabla Teams
     * 
     */
    public void showTeamPerSeason() {
        nbaController.showTable(table);
    }


    /**
     * Añade un equipo dentro de la tabla Teams
     * 
     */
    public void newTeamPerSeason()  {
        nbaController.insertNewData(table);
    }

    /**
     * Actualiza la información sobre un Equipo
     * 
     */
    public void updateTeamPerSeason()  {
        nbaController.updateData(table);
    }

    /**
     * Elimina la entrada sobre un Equipo
     * 
     */
    public void deleteTeamPerSeason() {
        nbaController.deleteData(table);
    }

}
