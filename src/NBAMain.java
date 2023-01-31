

import Controllers.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class NBAMain {

	public static Connection c;
	public static void main(String[] args) throws IOException, SQLException {
		NBAMenu menu = new NBAMenu();
		NBAImport nbaImport = new NBAImport();
		
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		c = connectionFactory.connect();

		TeamController teamController = new TeamController(c);
		PlayerController playerController = new PlayerController(c);
		SeasonController seasonController = new SeasonController(c);
		GameController gameController = new GameController(c);
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
				nbaController.ask4Table();
				break;

			case 2:
				nbaController.showDataPerPlayer();
				break;

			case 3:
				teamController.newTeam();
				// dbaccessor.mostraRevistesArticlesAutors();
				break;

			case 4:
				playerController.newPlayer();
				// dbaccessor.altaAutor();
				break;

			case 5:
				gameController.newGame();
				// dbaccessor.altaRevista();
				break;

			case 6:
				seasonController.newSeason();
				// dbaccessor.altaArticle();
				break;

			case 7:
				// dbaccessor.actualitzarTitolRevistes(conn);
				break;

			case 8:
				// dbaccessor.afegeixArticleARevista(conn);
				break;

			case 9:
				nbaImport.resetDataBase();
				break;

			case 10:
				// dbaccessor.carregaAutors(conn);
				break;

			case 11:
				// dbaccessor.sortir();
				break;

			default:
				System.out.println("*** Introduce un valor válido! ***");
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
//TO DO 			Seleccionar todos los elementos que contengan un texto concreto.
//TO DO 			Seleccionar todos los elementos que cumplan una condición.
//TO DO 			Seleccionar elementos concretos.
//TODO 		Posibilidad de modificar un registro concreto de información. Ejemplo:
//TODO 			Seleccionar un elemento concreto y permitir su modificación.
//TODO 		Posibilidad de modificar diferentes registros de información.
//TODO 		Posibilidad de eliminar un registro concreto de información.
//TODO 		Posibilidad de eliminar un conjunto de registros de información que cumplan un condición.
