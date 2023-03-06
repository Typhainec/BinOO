import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataAccess {
    private static Connection connexion;
    private String url = "jdbc:mysql://localhost:3306/binomotron2";
    private String login = "root";
    private String mdp = "root";

    private DataAccess() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connexion = DriverManager.getConnection(this.url, this.login, this.mdp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (connexion == null)
            connexion = new DataAccess().connexion;

        return connexion;
    }

    public static void close() {
        if (connexion != null) {
            try {
                connexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connexion = null;
    }
}
