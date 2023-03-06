import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MethodeApprenant {
    ArrayList<Utilisateur> getAllUtilisateur() {
        ArrayList<Utilisateur> utilisateurTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;
        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT * FROM utilisateurs");
            lecture = requete.executeQuery();
            while (lecture.next()) {

                int idUtilisateur = lecture.getInt("id_utilisateur");
                String nomUtilisateur = lecture.getString("nom_utilisateur");
                String prenomUtilisateur = lecture.getString("prenom_utilisateur");
                String mailUtilisateur = lecture.getString("mail_utilisateur");
                String mdpUtilisateur = lecture.getString("mdp_utilisateur");
                int idPromotion = lecture.getInt("id_promotion");
                int idRole = lecture.getInt("id_role");


                Utilisateur utilisateurLu = new Utilisateur(idUtilisateur, nomUtilisateur, prenomUtilisateur, mailUtilisateur, mdpUtilisateur, idPromotion, idRole);
                utilisateurTab.add(utilisateurLu);
            }


        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve promotions from database", e);
        } finally {
            try {
                if (lecture != null) {
                    lecture.close();
                }
                if (requete != null) {
                    requete.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }
        return utilisateurTab;
    }


    public ArrayList<Promotion> afficherPromotion() {
        ArrayList<Promotion> promotionsTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;

        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT id_promotion, nom_promotion, date_entree_promotion FROM promotions");
            lecture = requete.executeQuery();
            while (lecture.next()) {
                String nomPromotion = lecture.getString("nom_promotion");
                Promotion promotionLue = new Promotion(0, nomPromotion, null);
                promotionsTab.add(promotionLue);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve promotions from database", e);
        } finally {
            try {
                if (lecture != null) {
                    lecture.close();
                }
                if (requete != null) {
                    requete.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }

        return promotionsTab;
    }

    public String apprenantChoix(String mailUser, String mdpUser, int idUser) {
        // Rediriger vers la fenêtre élève
        System.out.println("Connecté en tant qu'élève.");
        // appeler une méthode pour les fonctionnalités de l'étudiant
        Scanner sc = new Scanner(System.in);
        System.out.println(" ");
        System.out.println("1- Si vous souhaitez voir les projets auxquels vous avez participé");
        System.out.println("2- Si vous souhaitez consulter les groupes d'un projet particulier");

        System.out.println(" ");
        System.out.println("Veuillez rentrer votre choix (1 ou 2) :");
        int choixUser = sc.nextInt();
        if (choixUser == 1) {
            MethodeApprenant idgetbd = new MethodeApprenant();
            idgetbd.afficherMesProjets(idUser);
        }
        //rentrer methode pour mesProjets
        else if (choixUser == 2) {
            MethodeApprenant db = new MethodeApprenant();
            db.afficherGroupeUserProjet();
        } else {
            System.out.println("Veuillez entrer un chiffre valide");
        }

        return "";
    }

    public ArrayList<Projet> afficherProjet(String apprenantChoixPromotion) {
        ArrayList<Projet> projetTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;

        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT DISTINCT nom_projet FROM projets\n" +
                            "JOIN groupes ON projets.id_projet = groupes.id_projet\n" +
                            "JOIN utilisateurs_groupes ON groupes.id_groupe = utilisateurs_groupes.id_groupe\n" +
                            "JOIN utilisateurs ON utilisateurs_groupes.id_utilisateur = utilisateurs.id_utilisateur\n" +
                            "JOIN promotions ON utilisateurs.id_promotion = promotions.id_promotion\n" +
                            "WHERE promotions.nom_promotion = ?");
            requete.setString(1, apprenantChoixPromotion);


            lecture = requete.executeQuery();
            while (lecture.next()) {
                String nomProjet = lecture.getString("nom_projet");
                Projet projetLu = new Projet(0, nomProjet, 0);
                projetTab.add(projetLu);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve promotions from database", e);
        } finally {
            try {
                if (lecture != null) {
                    lecture.close();
                }
                if (requete != null) {
                    requete.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }

        return projetTab;
    }

    public ArrayList<Groupe> afficherGroupe(String apprenantChoixPromotion, String apprenantChoixProket) {
        ArrayList<Groupe> groupeTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;

        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT nom_groupe, nom_utilisateur, prenom_utilisateur FROM projets\n" +
                            "JOIN groupes ON projets.id_projet = groupes.id_projet\n" +
                            "JOIN utilisateurs_groupes ON groupes.id_groupe = utilisateurs_groupes.id_groupe\n" +
                            "JOIN utilisateurs ON utilisateurs_groupes.id_utilisateur = utilisateurs.id_utilisateur\n" +
                            "JOIN promotions ON utilisateurs.id_promotion = promotions.id_promotion\n" +
                            "WHERE promotions.nom_promotion = ? AND projets.nom_projet = ? \n" +
                            "ORDER BY nom_groupe;");
            requete.setString(1, apprenantChoixPromotion);
            requete.setString(2, apprenantChoixProket);


            lecture = requete.executeQuery();

            Map<String, ArrayList<Utilisateur>> mapUsersByGroupeName = new HashMap<>();
            while (lecture.next()) {
                String nomGroupe = lecture.getString("nom_groupe");

                // On regarde si un groupe de ce nom existe dans la map
                ArrayList<Utilisateur> listGroupUsers = null;
                if (mapUsersByGroupeName.containsKey(nomGroupe)) {
                    listGroupUsers = mapUsersByGroupeName.get(nomGroupe);
                } else {
                    listGroupUsers = new ArrayList<>();
                    mapUsersByGroupeName.put(nomGroupe, listGroupUsers);
                }
                // Ici listGroupUsers contient la liste des utilisateurs du groupe "nomGroupe"
                String nomUtilisateur = lecture.getString("nom_utilisateur");
                String prenomUtilisateurs = lecture.getString("prenom_utilisateur");

                Utilisateur u = new Utilisateur(0, nomUtilisateur, prenomUtilisateurs, "", null, 0, 0);
                listGroupUsers.add(u);
            }
            Set<String> nomsGroupes = mapUsersByGroupeName.keySet();
            for (String nomGroupe : nomsGroupes) {
                ArrayList<Utilisateur> utilisateursDuGroupe = mapUsersByGroupeName.get(nomGroupe);
                Groupe groupeLu;
                groupeLu = new Groupe(0, nomGroupe, null, null, 0, utilisateursDuGroupe);
                groupeTab.add(groupeLu);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Nous n'avons pas réussi à accéder à la table groupe", e);
        } finally {
            try {
                if (lecture != null) {
                    lecture.close();
                }
                if (requete != null) {
                    requete.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }

        return groupeTab;
    }

    public String afficherGroupeUserProjet() {
        // rentrer methode pour voirToutProjet
        MethodeApprenant db = new MethodeApprenant();

        System.out.println("Voici les différentes promotions :");

        for (int i = 0; i < db.afficherPromotion().size(); i++)
            System.out.println(db.afficherPromotion().get(i).getNomPromotion());

        System.out.println("Veuillez rentrer le nom de la promotion qui vous interesse");
        Scanner nsc = new Scanner(System.in);
        String apprenantChoixPromotion = nsc.nextLine();

        for (int i = 0; i < db.afficherProjet(apprenantChoixPromotion).size(); i++) {
            System.out.println(db.afficherProjet(apprenantChoixPromotion).get(i).getNomProjet());
        }
        System.out.println("Veuillez rentrer le nom du projet qui vous interesse");
        String apprenantChoixProjet = nsc.nextLine();

        ArrayList<Groupe> groupes = db.afficherGroupe(apprenantChoixPromotion, apprenantChoixProjet);
        System.out.println("Voici les différents groupes");
        for (int i = 0; i < groupes.size(); i++) {
            Groupe groupe = groupes.get(i);
            ArrayList<Utilisateur> tabUtilisateur = groupe.getTabUtilisateur();
            List<String> nomsUtilisateurs = tabUtilisateur.stream().map(u -> u.getNomComplet()).collect(Collectors.toList());
            System.out.println(groupe.getNomGroupe() + nomsUtilisateurs);
        }
        return "";
    }

    public String afficherMesProjets(int idUser) {
        MethodeApprenant db = new MethodeApprenant();
        db.tecAfficherMesProjets(idUser);
        System.out.println("Voici les projets auxquels vous avez participé");
        for (int i = 0; i < db.tecAfficherMesProjets(idUser).size(); i++)
            System.out.println(db.tecAfficherMesProjets(idUser).get(i).getNomProjet());
        return "";
    }

    public ArrayList<Projet> tecAfficherMesProjets(int idUser) {
        ArrayList<Projet> nomMesProjets = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;
        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT distinct nom_projet FROM projets " +
                            "JOIN groupes ON projets.id_projet = groupes.id_projet " +
                            "JOIN utilisateurs_groupes ON groupes.id_groupe = utilisateurs_groupes.id_groupe " +
                            "JOIN utilisateurs ON utilisateurs_groupes.id_utilisateur = utilisateurs.id_utilisateur " +
                            "WHERE utilisateurs.id_utilisateur = ?");

            requete.setInt(1, idUser);


            lecture = requete.executeQuery();

            while (lecture.next()) {

                String nomProjet = lecture.getString("nom_projet");


                Projet projetLu = new Projet(0, nomProjet, 0);
                nomMesProjets.add(projetLu);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nomMesProjets;
    }


    public ArrayList<Utilisateur> getUtilisateurId(String email, String password) {
        ArrayList<Utilisateur> utilisateuridTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;

        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT id_utilisateur FROM utilisateurs\n" +
                            "WHERE mail_utilisateur = ? AND mdp_utilisateur = ?");

            requete.setString(1, email);
            requete.setString(2, password);


            lecture = requete.executeQuery();
            while (lecture.next()) {

                int idUtilisateur = lecture.getInt("id_utilisateur");


                Utilisateur utilisateurLu = new Utilisateur(idUtilisateur, "", "", "", "", 0, 0);
                utilisateuridTab.add(utilisateurLu);
            }


        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve utilisateur from database", e);
        } finally {
            try {
                if (lecture != null) {
                    lecture.close();
                }
                if (requete != null) {
                    requete.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }
        return utilisateuridTab;
    }

}
