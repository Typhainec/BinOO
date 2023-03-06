import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class Login {
    private boolean checkLogin(String email, String password) {
        try (PreparedStatement statement = DataAccess.getInstance().prepareStatement(
                "SELECT * FROM utilisateurs WHERE mail_utilisateur = ? AND mdp_utilisateur = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();


            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute login query", e);
        }
    }

    public String systemLogin(String userLogin) {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            String email = userLogin;
            if (email == null) {
                System.out.print("Adresse email : ");
                email = scanner.nextLine();
            }
            System.out.print("Mot de passe : ");
            String password = scanner.nextLine();

            if (checkLogin(email, password)) {
                try (PreparedStatement statement = DataAccess.getInstance().prepareStatement(
                        "SELECT id_role,id_utilisateur,prenom_utilisateur FROM utilisateurs WHERE mail_utilisateur = ?")) {
                    statement.setString(1, email);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        int idRole = resultSet.getInt("id_role");
                        int idUtilisateur = resultSet.getInt("id_utilisateur");
                        String prenomUtilisateur = resultSet.getString("prenom_utilisateur");
                        switch (idRole) {
                            case 1:
                                // Rediriger vers la fenêtre admin
                                MethodAdmin ma = new MethodAdmin();
                                ma.choixAdmin();
                                // appeler une méthode pour les fonctionnalités de l'administrateur
                                break;
                            case 2:
                                // Rediriger vers la fenêtre professeur
                                MethodeProf mP = new MethodeProf();
                                mP.choixProf(idUtilisateur, prenomUtilisateur);
                                // appeler une méthode pour les fonctionnalités du professeur
                                break;
                            case 3:
                                MethodeApprenant mA = new MethodeApprenant();
                                mA.apprenantChoix(email, password, idUtilisateur);
                                break;
                        }

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                loggedIn = true;

            } else {
                System.out.println("Email ou mot de passe incorrect.");
            }
        }

        // Retourne une chaîne de caractères vide car la méthode ne retourne pas de résultat
        return "";
    }
}


