package Controllers;

import java.sql.Connection;

/**
 * Controlador para la tabla 'teams'
 */
public class TeamController {

    private final Connection connection;
    private final NBAController nbaController;
    private final String table;

    /**
     * Define la conexión del controlador con la tabla Equipos (Teams)
     * @param connection Data Base Connection
     */
    public TeamController(Connection connection) {
        this.connection = connection;
        this.nbaController = new NBAController(this.connection);
        this.table = "teams";
    }

    /**
     * Muestra la información contenida dentro de la tabla Teams
     * 
     */
    public void showTeams() {
        nbaController.showTable(table);
    }


    /**
     * Añade un equipo dentro de la tabla Teams
     * 
     */
    public void newTeam()  {
        nbaController.insertNewData(table);
    }

    /**
     * Actualiza la información sobre un Equipo
     * 
     */
    public void updateTeam()  {
        nbaController.updateData(table);
    }

    /**
     * Elimina la entrada sobre un Equipo
     * 
     */
    public void deleteTeam() {
        nbaController.deleteData(table);
    }

}
