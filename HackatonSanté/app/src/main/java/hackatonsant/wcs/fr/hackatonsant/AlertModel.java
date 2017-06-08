package hackatonsant.wcs.fr.hackatonsant;


public class AlertModel {

    public Double lat;
    public Double lon;
    public Boolean adressed;
    public String location;

    public AlertModel(){}

    public AlertModel(Double lat, Double lon, Boolean adressed) {

        this.lat = lat;
        this.lon = lon;
        this.adressed = adressed;
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

    public Boolean getAdressed() {
        return adressed;
    }

    public void setAdressed(Boolean adressed) {
        this.adressed = adressed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
