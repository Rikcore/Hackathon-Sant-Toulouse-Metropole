package hackatonsant.wcs.fr.hackatonsant;


public class DefibrilateurPrivateModel {

    public static final String COMMUNE = "TOULOUSE";

    public String commune;
    public String accessibilite;
    public String implantation;
    public String adresse;
    public String nomSite;
    public String typeStructure;
    public Double lat;
    public Double lon;

    public DefibrilateurPrivateModel(){};

    public DefibrilateurPrivateModel(String adresse, Double lat, Double lon){

        this.adresse = adresse;
        this.commune = COMMUNE;
        this.lat = lat;
        this.lon = lon;
    }


    public String getCommune() {
        return commune;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
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
