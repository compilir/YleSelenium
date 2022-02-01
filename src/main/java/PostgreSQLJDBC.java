import java.sql.*;
import java.util.ArrayList;

public class PostgreSQLJDBC {

    public static Connection connectionToDb() throws ClassNotFoundException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "", "");
            System.out.println("Successfully Connected.");
        } catch (Exception e) {
            System.out.println(e);
        }
        return c;
    }

    public static void addEhdokas(Connection connection, String url) {
        String SQL = "INSERT INTO public.ehdokas(url) "
                + "VALUES(?)";
        try (
                PreparedStatement statement = connection.prepareStatement(SQL);) {
            statement.setString(1, url);
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addKysymys(Connection connection, String kysymys) {
        String SQL = "INSERT INTO public.kysymysehdokkaalle(kysymys) "
                + "VALUES(?)";
        try (
                PreparedStatement statement = connection.prepareStatement(SQL);) {
            statement.setString(1, kysymys);
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
        }
    }

    public static void ehdokkaanvastaus(Connection connection, String kysymys, String vastaus, Integer vastausNumero, String url, String puolue) {
        String SQL = "INSERT INTO public.ehdokaskysymys(kysymys, vastausteksti, vastausnumero, url, puolue) "
                + "VALUES(?,?,?,?,?)";
        try (
                PreparedStatement statement = connection.prepareStatement(SQL);) {
            statement.setString(1, kysymys);
            statement.setString(2, vastaus);
            statement.setInt(3, vastausNumero);
            statement.setString(4, url);
            statement.setString(5, puolue);
            statement.addBatch();
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getEhdokkaat(Connection connection) {
        ArrayList<String> ehdokas = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select distinct on (url) * from ehdokkaat group by url");
            while (rs.next()) {
                ehdokas.add(rs.getString("url"));
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(" Data Retrieved Successfully ..");
        return ehdokas;
    }


}