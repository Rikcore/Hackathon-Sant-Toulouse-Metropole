
package hackatonsant.wcs.fr.hackatonsant;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GeoShape  implements Serializable {

    @Key
    private String type;
    @Key
    private List<Double> coordinates = new ArrayList<Double>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
