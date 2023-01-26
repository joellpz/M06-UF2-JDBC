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
				teamController.showTeams();
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
