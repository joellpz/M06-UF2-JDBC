import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class NBAMain {

	public static Connection c;
	public static void main(String[] args) throws IOException, SQLException {
		NBAMenu menu = new NBAMenu();
		NBAGeneral nbaGeneral = new NBAGeneral();
		
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		c = connectionFactory.connect();

		TeamController teamController = new TeamController(c);
		PlayerController playerController = new PlayerController(c);
		NBAController nbaController = new NBAController(c);
		PreparedStatement preparedStatement;
		Statement st = c.createStatement();

//		Connection conn = null;
//		Identity identity;
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
			case 1:
				nbaController.showTable();
				// dbaccessor.mostraAutors();
				break;

			case 2:
				// dbaccessor.mostraRevistes();
				break;

			case 3:
				// dbaccessor.mostraRevistesArticlesAutors();
				break;

			case 4:
				// dbaccessor.altaAutor();
				break;

			case 5:
				// dbaccessor.altaRevista();
				break;

			case 6:
				// dbaccessor.altaArticle();
				break;

			case 7:
				// dbaccessor.actualitzarTitolRevistes(conn);
				break;

			case 8:
				// dbaccessor.afegeixArticleARevista(conn);
				break;

			case 9:
				nbaGeneral.resetBBDD("data/EER/nba-db-postgresql.sql");
				nbaGeneral.insertBaseData("data/CSV/players.csv");
				nbaGeneral.insertBaseData("data/CSV/teams.csv");
				nbaGeneral.insertBaseData("data/CSV/seasons.csv");
				nbaGeneral.insertBaseData("data/CSV/games.csv");

				nbaGeneral.insertBaseData("data/CSV/playerSeasons.csv");
				nbaGeneral.insertBaseData("data/CSV/playerPerGame.csv");
				nbaGeneral.insertBaseData("data/CSV/teamPerSeason.csv");
				break;

			case 10:
				// dbaccessor.carregaAutors(conn);
				break;

			case 11:
				// dbaccessor.sortir();
				break;

			default:
				System.out.println("Introdueixi una de les opcions anteriors");
				break;

			}
			option = menu.mainMenu();
		}

	}

}

//TO DO Definir el esquema de la base de datos.
//TO DO Creación de una base de datos. (Esta opción puede realizarse directamente en PostgreSQL). El resto de opciones deberían realizarse mediante un menú de terminal específicamente creado para nuestra práctica.
//TO DO Definir las sentencias de creación de las tablas que guardarán la información. El proyecto deberá incluir un archivo schema.sql con las sentencias de creación de la base de datos.
//TO DO Conexión mediante JDBC con la base de datos.
//TODO Manejo de la conexión mediante un menú de terminal que debe tener:
//TO DO 		Una opción que permita borrar las tablas de la base de datos y su información.
//TO DO 		Una opción que permita crear las tablas de la base de datos.
//TO DO 		Una opción que permita poblar masivamente las tablas de la base de datos leyendo los ficheros generados en la primera práctica.
//TODO 		Diferentes opciones de consulta sobre la información. Ejemplos:
//TODO 			Seleccionar todos los elementos que contengan un texto concreto.
//TODO 			Seleccionar todos los elementos que cumplan una condición.
//TODO 			Seleccionar elementos concretos.
//TODO 		Posibilidad de modificar un registro concreto de información. Ejemplo:
//TODO 			Seleccionar un elemento concreto y permitir su modificación.
//TODO 		Posibilidad de modificar diferentes registros de información.
//TODO 		Posibilidad de eliminar un registro concreto de información.
//TODO 		Posibilidad de eliminar un conjunto de registros de información que cumplan un condición.
