package hackatonsant.wcs.fr.hackatonsant;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.util.Locale;


public class DefibrillateurRequestModel extends GoogleHttpClientSpiceRequest<DefibrillateurPublicModel> {

    public static final String TAG = "DefibRequestModel";

    private Double lat;
    private Double lon;
    private int radius;
    private String url;


    public DefibrillateurRequestModel (Double lat, Double lon, int radius) {
        super( DefibrillateurPublicModel.class);
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.url = String.format(Locale.US, "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=defibrillateurs&rows=50&geofilter.distance=%f,%f,%d",
                lat,
                lon,
                radius);

    }

    @Override
    public DefibrillateurPublicModel loadDataFromNetwork() throws Exception {

        Log.d(TAG, "Calling WebService" + url);

        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));

        request.setParser(new GsonFactory().createJsonObjectParser());
        return request.execute().parseAs(getResultType());
    }
}
