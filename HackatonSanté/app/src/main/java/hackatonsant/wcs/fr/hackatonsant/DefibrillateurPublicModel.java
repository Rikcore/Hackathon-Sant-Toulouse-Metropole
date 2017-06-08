
package hackatonsant.wcs.fr.hackatonsant;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefibrillateurPublicModel  implements Serializable{


    @Key
    private Integer nhits;
    @Key
    private Parameters parameters;
    @Key
    private List<Record> records = new ArrayList<Record>();

    public Integer getNhits() {
        return nhits;
    }

    public void setNhits(Integer nhits) {
        this.nhits = nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

}
