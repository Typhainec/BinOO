import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MethodAdmin {
    public static void supprimerProjet(Projet projet) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Voulez vous supprimer le projet ? oui/non");
        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("oui")) {
            System.out.println("Supression annulée.");
            return;
        }
        /* Connexion à la base de données */
        Connection connexion = DataAccess.getInstance();

        /* On supprime tous les groupes associés au projet choisi*/

        String deleteGroupQuery = "DELETE FROM groupes WHERE id_projet = ?";
        PreparedStatement deleteGroupStatement = connexion.prepareStatement(deleteGroupQuery);
        deleteGroupStatement.setInt(1, projet.getIdProjet());
        deleteGroupStatement.executeUpdate();

        /* On supprime le projet en lui même */

        String deleteProjectQuery = "DELETE FROM projets WHERE nom_projet = ?";
        PreparedStatement deleteProjectStatement = connexion.prepareStatement(deleteProjectQuery);
        deleteProjectStatement.setString(1, projet.getNomProjet());
        deleteProjectStatement.executeUpdate();
        System.out.println("Le projet " + projet.getNomProjet() + " et les groupes associés ont été supprimés avec succès !");

    }

    public Projet chooseProjet(List<Projet> projets) {

        /* Ici, on gère la possibilité qu'il n'y ai aucun projet dans la liste, un message d'erreur est renvoyé si
        la liste est vide lorsque l'utilisateur tape un nom de projet*/

        if (projets.isEmpty()) {
            throw new IllegalArgumentException("la liste des projets est vide");
        }
        /* On affiche la liste des projets */

        System.out.println("Liste des projets : ");
        for (int i = 0; i < projets.size(); i++) {
            Projet projet = projets.get(i);
            System.out.println((i + 1) + ". " + projet.getNomProjet());
        }

        /* On rend possible la saisie du nom d'un projet pour le choisir. On ajoute un ignoreCase pour ne pas
         * avoir de message d'erreur à cause des majuscules/minucules dans le nom du projet*/

        Projet chosenProjet = null;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrez le nom du projet que vous souhaitez supprimer");
            String choice = scanner.nextLine();
            for (Projet projet : projets) {
                if (projet.getNomProjet().equalsIgnoreCase(choice)) {
                    chosenProjet = projet;
                    break;
                }
            }

            /* Si l'utilisateur rentre un nom de projet qui n'existe pas un message l'en informera et lui indiquera
            de réessayer. Une fois qu'un nom valide a été choisi, il sera stocké dans la variable chosenProjet et
            apparaitra dans la console. */

            if (chosenProjet == null)
                System.out.println("Le projet " + choice + " n'existe pas, veuillez réessayer.");
        } while (chosenProjet == null);

        return chosenProjet;
    }

    public void chooseAndDeleteProjet(ArrayList<Projet> projets) throws SQLException {
        Projet projet = chooseProjet(projets);
        MethodAdmin.supprimerProjet(projet);
    }

    public String choixAdmin() {
        System.out.println("Connecté en tant qu'administrateur.");
        MethodAdmin ma = new MethodAdmin();
        MethodeProf mp = new MethodeProf();
        ArrayList<Projet> projets = mp.getAllProjet();
        try {
            ma.chooseAndDeleteProjet(projets);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
