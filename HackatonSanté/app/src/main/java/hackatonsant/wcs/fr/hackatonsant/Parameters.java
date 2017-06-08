
package hackatonsant.wcs.fr.hackatonsant;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parameters  implements Serializable {

    @Key
    private List<String> dataset = new ArrayList<String>();
    @Key
    private String timezone;
    @Key
    private Integer rows;
    @Key
    private String format;
    @Key
    private List<String> geofilterDistance = new ArrayList<String>();

    public List<String> getDataset() {
        return dataset;
    }

    public void setDataset(List<String> dataset) {
        this.dataset = dataset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getGeofilterDistance() {
        return geofilterDistance;
    }

    public void setGeofilterDistance(List<String> geofilterDistance) {
        this.geofilterDistance = geofilterDistance;
    }

}
