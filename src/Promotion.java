import java.util.ArrayList;
import java.util.Date;

public class Promotion {
    private int idPromotion;
    private String nomPromotion;
    private Date dateDebutPromotion;

    @Override
    public String toString() {
        return "Promotion{" +
                "idPromotion=" + idPromotion +
                ", nomPromotion='" + nomPromotion + '\'' +
                ", dateDebutPromotion=" + dateDebutPromotion +
                '}';
    }

    public Promotion(int promotionId, String promotionNom, Date promotionDateDebut) {
        this.idPromotion = promotionId;
        this.nomPromotion = promotionNom;
        this.dateDebutPromotion = promotionDateDebut;
    }

    public int getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(int idPromotion) {
        this.idPromotion = idPromotion;
    }

    public String getNomPromotion() {
        return nomPromotion;
    }

    public void setNomPromotion(String nomPromotion) {
        this.nomPromotion = nomPromotion;
    }

    public Date getDateDebutPromotion() {
        return dateDebutPromotion;
    }

    public void setDateDebutPromotion(Date dateDebutPromotion) {
        this.dateDebutPromotion = dateDebutPromotion;
    }

    public static ArrayList<Promotion> getAllPromotions() {
        MethodeApprenant binomotron = new MethodeApprenant();

        try {
            return binomotron.afficherPromotion();
        } catch (RuntimeException e) {
            System.err.println("Failed to retrieve promotions from database: " + e.getMessage());
            return new ArrayList<>(); // retourne une liste vide en cas d'erreur
        }
    }
}
