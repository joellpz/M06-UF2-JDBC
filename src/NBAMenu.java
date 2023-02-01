import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NBAMenu {
	private int option;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public NBAMenu() {
		super();
	}

	public int mainMenu() {


		do {

			System.out.println(" \nMENU PRINCIPAL \n");

			System.out.println("1. Mostrar información de alguna tabla");
			System.out.println("2. Mostrar información sobre un jugador");
			System.out.println("3. Introducir Datos");
			System.out.println("3. Crear Equipo");
			System.out.println("4. Crear Jugador");
			System.out.println("5. Crear Partido");
			System.out.println("6. Crear Temporada");
			System.out.println("7. Assigna jugador a un equip");
			System.out.println("8. Desvincula jugador d'un equip");
			System.out.println("9. Borrar y Generar Tablas.");
			System.out.println("10. Salir");
			System.out.println("Escoger opción: ");
			try {
				option = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("valor no vàlid");
				e.printStackTrace();

			}

		} while (option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 7
				&& option != 8 && option != 9 && option != 10);

		return option;
	}

	public int insertMenu(){
		do {
			System.out.println(" \n*** Insertar Datos, escoge la tabla: *** \n");
			System.out.println("1. Jugadores");
			System.out.println("2. Equipos");
			System.out.println("3. Temporadas");
			System.out.println("3. Equipos");
			System.out.println("Escoger opción: ");
			try {
				option = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("valor no vàlid");
				e.printStackTrace();

			}
		}while (option <1 && option>4);

        return option;
	}

	public Identity authenticate(int tries) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("============================ACB=============================");
		System.out.println("============================================================");
		System.out.println("Avís: tens " + (3 - tries) + " intents per loginarte");
		System.out.println("============================================================");
		System.out.println("Inserta nom del usuari: ");
		String user = br.readLine();
		System.out.println("Inserta contrasenya: ");
		String password = br.readLine();

		Identity identity = new Identity(user, password);
		return identity;

	}

}
