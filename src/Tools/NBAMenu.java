package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase que crea los menus de selección.
 */
public class NBAMenu {
    private int option;
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Menu de NBA
     */
    public NBAMenu() {
        super();
    }

    /**
     * Main Menu
     *
     * @return int
     */
    public int mainMenu() {
        do {
            System.out.println(" \nMENU PRINCIPAL \n");

            System.out.println("1. Mostrar tabla");
            System.out.println("2. Introducir Datos");
            System.out.println("3. Actualizar Datos");
            System.out.println("4. Eliminar Datos");
            System.out.println("5. Mostrar información sobre un jugador");
            System.out.println("6. Borrar y Generar Tablas.");
            System.out.println("7. Salir");
            System.out.println("Escoger opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("valor no válido");
            }
        } while (option < 1 || option > 7);

        return option;
    }

    /**
     * Data Menu
     *
     * @return int
     */
    public int dataMenu() {
        do {
            System.out.println(" \n*** Trabajar los Datos, escoge la tabla: *** \n");
            System.out.println("1. Jugadores");
            System.out.println("2. Equipos");
            System.out.println("3. Temporadas");
            System.out.println("4. Partidos");
            System.out.println("5. Equipos Por Temporada");
            System.out.println("6. Jugadores Por Temporada");
            System.out.println("7. Jugadores Por Partido");
            System.out.println("Escoger opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("valor no válido");
                e.printStackTrace();

            }
        } while (option < 1 && option > 7);

        return option;
    }


    /**
     * Menu en el caso de Tablas Intermedias
     * @return Opción
     */
    public int hugeDataMenu() {
        do {
            System.out.println("*** Quieres ver la información sobre... ***");
            System.out.println("1. Partidos");
            System.out.println("2. Temporadas");
            System.out.println("Escoger opción: ");
            try {
                option = Integer.parseInt(br.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("valor no válido");
                e.printStackTrace();
            }

        } while (option < 1 && option > 2);
        return option;
    }

//    public Identity authenticate(int tries) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("============================ACB=============================");
//        System.out.println("============================================================");
//        System.out.println("Avís: tens " + (3 - tries) + " intents per loginarte");
//        System.out.println("============================================================");
//        System.out.println("Inserta nom del usuari: ");
//        String user = br.readLine();
//        System.out.println("Inserta contrasenya: ");
//        String password = br.readLine();
//
//        Identity identity = new Identity(user, password);
//        return identity;
//
//    }

}
