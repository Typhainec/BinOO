public class Projet {

    private int idProjet;
    private String nomProjet;
    private int idCreatorProjet;
    private String nomCreateurProjet;

    public String getNomCreateurProjet() {
        return nomCreateurProjet;
    }

    public Projet(int idProjet, String nomProjet, String nomCreateurProjet, int idCreatorProjet) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.nomCreateurProjet = nomCreateurProjet;
        this.idCreatorProjet = idCreatorProjet;

    }

    public Projet(int idProjet, String nomProjet, int idCreatorProjet, String nomCreateurProjet) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.idCreatorProjet = idCreatorProjet;
        this.nomCreateurProjet = nomCreateurProjet;
    }

    public void setNomCreateurProjet(String nomCreateurProjet) {
        this.nomCreateurProjet = nomCreateurProjet;
    }

    public Projet(int idProjet, String nomProjet, int idCreatorProjet) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.idCreatorProjet = idCreatorProjet;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public int getIdCreatorProjet() {
        return idCreatorProjet;
    }

    public void setIdCreatorProjet(int idCreatorProjet) {
        this.idCreatorProjet = idCreatorProjet;
    }
}
