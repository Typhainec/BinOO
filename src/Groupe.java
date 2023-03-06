import java.util.ArrayList;
import java.util.Date;

public class Groupe {
    private int idGroupe;
    private String nomGroupe;
    private Date dateDebutProjetGroupe;
    private Date dateFinProjetGroupe;
    private int idProjet;
    private ArrayList<Utilisateur> tabUtilisateur = new ArrayList<>();

    public ArrayList<Utilisateur> getTabUtilisateur() {
        return tabUtilisateur;
    }

    public void setTabUtilisateur(ArrayList<Utilisateur> tabUtilisateur) {
        this.tabUtilisateur = tabUtilisateur;
    }

    public Groupe(int idGroupe, String nomGroupe, Date dateDebutProjetGroupe, Date dateFinProjetGroupe, int idProjet, ArrayList<Utilisateur> tabUtilisateur) {
        this.idGroupe = idGroupe;
        this.nomGroupe = nomGroupe;
        this.dateDebutProjetGroupe = dateDebutProjetGroupe;
        this.dateFinProjetGroupe = dateFinProjetGroupe;
        this.idProjet = idProjet;
        this.tabUtilisateur = tabUtilisateur;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public Date getDateDebutProjetGroupe() {
        return dateDebutProjetGroupe;
    }

    public void setDateDebutProjetGroupe(Date dateDebutProjetGroupe) {
        this.dateDebutProjetGroupe = dateDebutProjetGroupe;
    }

    public Date getDateFinProjetGroupe() {
        return dateFinProjetGroupe;
    }

    public void setDateFinProjetGroupe(Date dateFinProjetGroupe) {
        this.dateFinProjetGroupe = dateFinProjetGroupe;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }
}
