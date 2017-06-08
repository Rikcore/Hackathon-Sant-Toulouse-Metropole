
package hackatonsant.wcs.fr.hackatonsant;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fields implements Serializable {

    @Key
    private String commune;
    @Key
    private String nomSite;
    @Key
    private String dist;
    @Key
    private String implantation;
    @Key
    private String adresse;
    @Key
    private List<Double> geoPoint2d = new ArrayList<Double>();
    @Key
    private String accessibilite;
    @Key
    private GeoShape geoShape;
    @Key
    private String typeStructure;
    /*@Key
    private Integer id;*/

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getNomSite() {
        return nomSite;
    }

    public void setNomSite(String nomSite) {
        this.nomSite = nomSite;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
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

    public List<Double> getGeoPoint2d() {
        return geoPoint2d;
    }

    public void setGeoPoint2d(List<Double> geoPoint2d) {
        this.geoPoint2d = geoPoint2d;
    }

    public String getAccessibilite() {
        return accessibilite;
    }

    public void setAccessibilite(String accessibilite) {
        this.accessibilite = accessibilite;
    }

    public GeoShape getGeoShape() {
        return geoShape;
    }

    public void setGeoShape(GeoShape geoShape) {
        this.geoShape = geoShape;
    }

    public String getTypeStructure() {
        return typeStructure;
    }

    public void setTypeStructure(String typeStructure) {
        this.typeStructure = typeStructure;
    }

    /*public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }*/

}
