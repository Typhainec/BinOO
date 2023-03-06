import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class MethodeProf {

    public String choixProf(int idUser, String nomCreateur) throws ParseException, SQLException {
        System.out.println("Connecté en tant que professeur.");
        Scanner sc = new Scanner(System.in);
        System.out.println(" ");
        System.out.println("1- Si vous souhaitez voir l'ensemble des projets");
        System.out.println("2- Si vous voulez créer un projet");
        System.out.println(" ");
        System.out.println("Veuillez rentrer votre choix (1 ou 2) :");
        int choixProf = sc.nextInt();
        sc.nextLine();
        if (choixProf == 1) {
            MethodeProf mp = new MethodeProf();
            mp.afficherAllProjet();
            System.out.println("1- Si vous souhaitez choisir un projet afin de l'assigner à une promotion veuillez rentrer");
            System.out.println("2- Si vous souhaitez quitter l'application ");
            int choixProf2 = sc.nextInt();
            if (choixProf2 == 1) {
                mp.methodAssignerProjetPromo(choixProf2);
            }
        }
        if (choixProf == 2) {
            System.out.println("Veuillez renseigner le nom du projet que vous souhaitez créer (ATTENTION LE PROJET SERA DIRECTEMENT RENTRE EN BDD)");
            String nomProjetCree = sc.nextLine();
            int idCreateur = idUser;
            String prenomCreateur = nomCreateur;
            MethodeProf mp = new MethodeProf();
            mp.enregistrerProjetDansBD(idCreateur, nomProjetCree, prenomCreateur);
            System.out.println("Le projet a bien été enregistré en BDD");


        }
        return "";

    }


    public ArrayList<Promotion> getAllPromotion() {
        ArrayList<Promotion> promotionsTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;

        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT id_promotion, nom_promotion, date_entree_promotion FROM promotions ORDER BY date_entree_promotion");
            lecture = requete.executeQuery();
            while (lecture.next()) {
                int idPromotion = lecture.getInt("id_promotion");
                String nomPromotion = lecture.getString("nom_promotion");
                Date datePromotion = lecture.getDate("date_entree_promotion");

                Promotion promotionLue = new Promotion(idPromotion, nomPromotion, datePromotion);
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

    ArrayList<Projet> getAllProjet() {
        ArrayList<Projet> projetTab = new ArrayList<>();
        PreparedStatement requete = null;
        ResultSet lecture = null;
        try {
            requete = DataAccess.getInstance().prepareStatement(
                    "SELECT * FROM projets");
            lecture = requete.executeQuery();
            while (lecture.next()) {
                Projet projetLu = new Projet(lecture.getInt("id_projet"), lecture.getString("nom_projet"), lecture.getInt("id_utilisateur"), lecture.getString("nom_createur"));
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

    public String afficherAllProjet() {
        System.out.println("Voici l'ensemble des projets");
        MethodeProf mp = new MethodeProf();
        for (int i = 0; i < mp.getAllProjet().size(); i++)
            System.out.println("Nom du projet : " + mp.getAllProjet().get(i).getNomProjet() + "  Id du projet : " + mp.getAllProjet().get(i).getIdProjet() + "  Id du créateur du projet : " + mp.getAllProjet().get(i).getIdCreatorProjet() + " Nom du créateur du projet : " + mp.getAllProjet().get(i).getNomCreateurProjet());
        return "";
    }

    public String afficherAllPromotion() {
        System.out.println("Voici l'ensemble des promotions");
        MethodeProf mp = new MethodeProf();
        for (int i = 0; i < mp.getAllPromotion().size(); i++)
            System.out.println("Nom de la promotion : " + mp.getAllPromotion().get(i).getNomPromotion() + "  Id de la promotion : " + mp.getAllPromotion().get(i).getIdPromotion() + "  Date du début de la promotion : " + mp.getAllPromotion().get(i).getDateDebutPromotion());
        return "";
    }

    ArrayList<Utilisateur> getUtilisateurByPromo(String choixPromo) {
        ArrayList<Utilisateur> utilisateurByPromoTab = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet lecture = null;
        try {
            statement = DataAccess.getInstance().prepareStatement(
                    "SELECT * FROM utilisateurs JOIN promotions on utilisateurs.id_promotion = promotions.id_promotion WHERE nom_promotion = ?");
            statement.setString(1, choixPromo);

            lecture = statement.executeQuery();
            while (lecture.next()) {
                // code to read result set and populate utilisateurByPromoTab
                int id = lecture.getInt("id_utilisateur");
                String nom = lecture.getString("nom_utilisateur");
                String prenom = lecture.getString("prenom_utilisateur");
                String email = lecture.getString("mail_utilisateur");
                String mdp = lecture.getString("mdp_utilisateur");
                int idPromotion = lecture.getInt("id_promotion");
                int idRole = lecture.getInt("id_role");
                Utilisateur utilisateur = new Utilisateur(id, nom, prenom, email, mdp, idPromotion, idRole);
                utilisateurByPromoTab.add(utilisateur);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve promotions from database", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                // log error
            }
            DataAccess.close();
        }
        return utilisateurByPromoTab;
    }

    public void enregistrerGroupesDansBD(ArrayList<Groupe> groupes) throws SQLException {
        // Ouvrir une connexion à la base de données
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binomotron2", "root", "root");

        try {
            // Désactiver le mode autocommit pour gérer une transaction manuelle
            conn.setAutoCommit(false);

            // Préparer la requête SQL INSERT pour insérer un groupe
            PreparedStatement pstmtInsertGroupe = conn.prepareStatement(
                    "INSERT INTO groupes (nom_groupe, date_debut_projet, date_fin_projet, id_projet) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            // Préparer la requête SQL INSERT pour insérer un utilisateur dans un groupe
            PreparedStatement pstmtInsertUtilisateurGroupe = conn.prepareStatement(
                    "INSERT INTO utilisateurs_groupes (id_utilisateur, id_groupe) VALUES (?, ?)");

            // Boucler sur tous les groupes
            for (Groupe groupe : groupes) {
                // Insérer le groupe dans la table "groupes"
                pstmtInsertGroupe.setString(1, groupe.getNomGroupe());
                pstmtInsertGroupe.setDate(2, new java.sql.Date(groupe.getDateDebutProjetGroupe().getTime()));
                pstmtInsertGroupe.setDate(3, new java.sql.Date(groupe.getDateFinProjetGroupe().getTime()));
                pstmtInsertGroupe.setInt(4, groupe.getIdProjet());
                pstmtInsertGroupe.executeUpdate();

                // Récupérer l'identifiant généré pour le groupe inséré
                ResultSet rs = pstmtInsertGroupe.getGeneratedKeys();
                rs.next();
                int idGroupe = rs.getInt(1);

                // Boucler sur tous les utilisateurs du groupe
                for (Utilisateur utilisateur : groupe.getTabUtilisateur()) {
                    // Insérer l'utilisateur dans la table "utilisateurs_groupes"
                    pstmtInsertUtilisateurGroupe.setInt(1, utilisateur.getIdUtilisateur());
                    pstmtInsertUtilisateurGroupe.setInt(2, idGroupe);
                    pstmtInsertUtilisateurGroupe.executeUpdate();
                }
            }

            // Valider la transaction
            conn.commit();

            System.out.println("Les groupes ont été enregistrés dans la base de données.");
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            conn.rollback();
            throw e;
        } finally {
            // Fermer la connexion à la base de données
            conn.close();
        }
    }

    public String methodAssignerProjetPromo(int choixProf2) throws ParseException, SQLException {
        Scanner sc = new Scanner(System.in);
        MethodeProf mp = new MethodeProf();
        if (choixProf2 == 1) {
            mp.afficherAllProjet();
            System.out.println("Veuillez saisir l'id du projet que vous souhaitez assigner à une promotion");
            int choixProfProjet = sc.nextInt();
            sc.nextLine();

            mp.afficherAllPromotion();
            System.out.println("veuillez selectionner la promotion désirée");
            String choixPromo = sc.nextLine();

            System.out.println("Veuillez selectionner la taille des groupes");
            int tailleGroupe = sc.nextInt();
            sc.nextLine();

            System.out.println("Veuillez selectionner la date de début du projet format au dd/MM/yyyy");
            String dateDebutStr = sc.nextLine();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dateDebut = formatter.parse(dateDebutStr);

            System.out.println("Veuillez selectionner la date de fin du projet au format dd/MM/yyyy");
            String dateFinStr = sc.nextLine();
            SimpleDateFormat formatterFin = new SimpleDateFormat("dd/MM/yyyy");

            java.util.Date dateFin = formatterFin.parse(dateFinStr);


            System.out.println("Voici un proposition de groupe de " + tailleGroupe + "apprenants.");
            System.out.println(mp.getUtilisateurByPromo(choixPromo));
            ArrayList<Groupe> stockerUtilisateurDansGroupe = new ArrayList<>();
            Random rand = new Random(System.currentTimeMillis());

            ArrayList<Utilisateur> utilisateurByPromo = mp.getUtilisateurByPromo(choixPromo);
            Collections.shuffle(utilisateurByPromo, rand);

            for (int i = 0; i < utilisateurByPromo.size(); i = i + tailleGroupe) {
                String nomGroupe = "groupe de " + utilisateurByPromo.get(i).getNomUtilisateur();
                java.util.Date dateDebutProjetGroupe = dateDebut;
                java.util.Date dateFinProjetGroupe = dateFin;
                int idProjet = choixProfProjet;
                ArrayList<Utilisateur> tabUtilisateur = new ArrayList<>();
                for (int d = 0; d < tailleGroupe && (i + d) < utilisateurByPromo.size(); d++) {
                    tabUtilisateur.add(utilisateurByPromo.get(i + d));
                }
                Groupe groupelu = new Groupe(0, nomGroupe, dateDebutProjetGroupe, dateFinProjetGroupe, idProjet, tabUtilisateur);
                stockerUtilisateurDansGroupe.add(groupelu);
                // Afficher les informations sur le groupe et les utilisateurs qu'il contient
                System.out.println("Groupe créé : " + nomGroupe);
                System.out.println("Membres du groupe : ");
                for (Utilisateur u : tabUtilisateur) {
                    System.out.println("- " + u.getNomUtilisateur());
                }
                System.out.println();
            }

            Scanner sc2 = new Scanner(System.in);
            System.out.println("Souhaitez-vous enregistrer ces groupes ? (oui/non)");
            String reponse = sc2.nextLine();

            while (!reponse.equals("oui")) {
                // Faire quelque chose tant que la réponse n'est pas "oui"
                System.out.println("Les groupes n'ont pas été enregistrés dans la base de données.");
                ArrayList<Utilisateur> boucleTabUser = new ArrayList<>();
                boucleTabUser = mp.getUtilisateurByPromo(choixPromo);
                Collections.shuffle(boucleTabUser, rand);
                ArrayList<Groupe> boucleStockerUtilisateurDansGroupe = new ArrayList<>();

                for (int i = 0; i < boucleTabUser.size(); i = i + tailleGroupe) {
                    String nomGroupe = "groupe de " + boucleTabUser.get(i).getNomUtilisateur();
                    java.util.Date dateDebutProjetGroupe = dateDebut;
                    java.util.Date dateFinProjetGroupe = dateFin;
                    int idProjet = choixProfProjet;
                    ArrayList<Utilisateur> tabUtilisateur = new ArrayList<>();
                    for (int d = 0; d < tailleGroupe && (i + d) < boucleTabUser.size(); d++) {
                        tabUtilisateur.add(boucleTabUser.get(i + d));
                    }
                    Groupe groupelu = new Groupe(0, nomGroupe, dateDebutProjetGroupe, dateFinProjetGroupe, idProjet, tabUtilisateur);
                    boucleStockerUtilisateurDansGroupe.add(groupelu);
                    // Afficher les informations sur le groupe et les utilisateurs qu'il contient
                    System.out.println("Groupe créé : " + nomGroupe);
                    System.out.println("Membres du groupe : ");
                    for (Utilisateur u : tabUtilisateur) {
                        System.out.println("- " + u.getNomUtilisateur());
                    }
                    System.out.println();
                }
                reponse = sc2.nextLine();
            }
            if (reponse.equals("oui")) {
                // Code pour enregistrer les groupes dans la base de données
                mp.enregistrerGroupesDansBD(stockerUtilisateurDansGroupe);
                System.out.println("Les groupes ont été enregistrés dans la base de données.");
            }

        }
        return "";
    }

    public Projet enregistrerProjetDansBD(int idCreateur, String nomProjet, String prenomCreateur) throws SQLException {
        // Ouvrir une connexion à la base de données
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binomotron2", "root", "root");
        Projet result = null;
        try {
            // Désactiver le mode autocommit pour gérer une transaction manuelle
            conn.setAutoCommit(false);

            // Préparer la requête SQL INSERT pour insérer un groupe
            PreparedStatement pstmtInsertProjet = conn.prepareStatement(
                    "INSERT INTO projets (nom_projet, nom_createur, id_utilisateur) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);


            pstmtInsertProjet.setString(1, nomProjet);
            pstmtInsertProjet.setString(2, prenomCreateur);
            pstmtInsertProjet.setInt(3, idCreateur);
            if (pstmtInsertProjet.executeUpdate() == 1) {
                ResultSet generatedKeys = pstmtInsertProjet.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idProjet = generatedKeys.getInt(1);
                    result = new Projet(idProjet, nomProjet, prenomCreateur, idCreateur);
                }

            }
            ;

            // Valider la transaction
            conn.commit();

            System.out.println("Le projet a bien était rentré en base de données.");
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            conn.rollback();
            throw e;
        } finally {
            // Fermer la connexion à la base de données
            conn.close();
        }
        return result;
    }


}


