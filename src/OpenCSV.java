import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OpenCSV {

    /**
     * Imprime linea por línea String[], separando por comas cada posición.
     *
     * @param list Lista Strings[]
     * @param path Dirección donde Guardarla
     */
    public static void writeToCSV(ArrayList<String[]> list, @NotNull String path) {
        try {
            String[] intro;
            switch (path) {
                case "players.csv" ->
                        intro = new String[]{"Name", "Position", "College", "Draft Team", "Draft Position", "Birthday", "Age", "Draft Year", "Career Experience"};
                case "playerSeasons.csv" ->
                        intro = new String[]{"Player", "Season", "Age", "Team", "League", "Position", "Games", "Games Starter",
                                "Minutes Played", "Field Goals", "Field Attempts", "Field Percent", "3PTS Goals",
                                "3PTS Attempts", "3PTS Percent", "2PTS Goals", "2PTS Attempts", "2PTS Percent",
                                "Effective Goal Percent", "Free Throw", "Free Throw Attempts", "Free Throw Percent",
                                "Offensive Rebounds", "Defensive Rebounds", "Total Rebounds", "Assists", "Steals",
                                "Blocks", "Turnovers", "Fouls", "Points", "Triples Dobles"};
                case "seasons.csv" ->
                        intro = new String[]{"Link", "Years", "League", "Champion", "MVP", "RookieOTY", "Points Leader", "Rebounds Leader", "Assists Leader", "Win Shares Leader"};
                case "teamPerSeasons.csv" ->
                        intro = new String[]{"Season", "Team", "Wins", "Loses", "Win Rate", "Games Behind", "Points Per Game", "Opponents PTSxGame", "Team Rating", "Conference"};
                case "teams.csv" ->
                        intro = new String[]{"Name", "Location", "Games", "Wins", "Loses", "Playoff Appearances", "Conference Champions", "NBA Champions", "Conference"};
                case "games.csv" ->
                        intro = new String[]{"Game ID", "Date", "Visitor", "Points Visitor", "Local", "Points Local", "Arena", "Link"};
                case "playerPerGame.csv" ->
                        intro = new String[]{"Game ID", "Team ID", "Player", "Minutes Played", "Field Goals", "FG Attempts", "FG %", "3P Field Goals",
                                "3PFG Attempts", "3PFG %", "Free Throws", "FT Attempts", "FT %", "Offensive Rebounds", "Defensive Rebounds"
                                , "Total Rebounds", "Assists", "Steals", "Blocks", "Turnovers", "Personal Fouls", "Points", "Punctuation"};
                default -> intro = new String[]{"Not defined..."};
            }
            CSVWriter writer = new CSVWriter(new FileWriter("data/CSV/" + path));
            writer.writeNext(intro);
            writer.writeAll(list);
            System.out.println("********Import Finished*****+");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Leer archivo CVS para pasarlo a List
     *
     * @param path Path Archivo
     * @return List de String[]
     */
    /* Si queremos leer algo y transformarlo en un ArrayList de Objetos.
   List<String[]> playerList = readCSV("seasons.csv");
   ArrayList<Season> playerListObj = new ArrayList<>();
       playerList.forEach(player -> playerListObj.add(new Season(player[1],player[2],player[3],player[4],player[5],player[6],player[7],player[8],player[9],player[0])));
   writeToXML(playerListObj,"seasons.xml");*/
    public static List<String[]> readCSV(@NotNull String path) {
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            CSVReader csvReader = new CSVReader(reader);
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
