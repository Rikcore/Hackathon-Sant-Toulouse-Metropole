package hackatonsant.wcs.fr.hackatonsant;


public class DefibrilateurPrivate {

    public static final String COMMUNE = "TOULOUSE";

    private String commune;
    private String accessibilite;
    private String implantation;
    private String adresse;
    private String nomSite;
    private String typeStructure;

    public DefibrilateurPrivate(String adresse){

        this.adresse = adresse;
        this.commune = COMMUNE;
    }


    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getAccessibilite() {
        return accessibilite;
    }

    public void setAccessibilite(String accessibilite) {
        this.accessibilite = accessibilite;
    }

    public String getImplantation() {
        return implantation;
    }

    public void setImplantation(String implantation) {
        this.implantation = implantation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getTypeStructure() {
        return typeStructure;
    }

    public void setTypeStructure(String typeStructure) {
        this.typeStructure = typeStructure;
    }
}
