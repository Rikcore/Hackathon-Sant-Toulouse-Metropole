package hackatonsant.wcs.fr.hackatonsant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int DEFAULT_RADIUS = 5000;
    public static final String TAG = "MainActivity";

    private MapView mapView;
    private GoogleMap map;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double userLat;
    private Double userLon;

    private Button buttonAddDeviceMain;

    private SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonAddDeviceMain = (Button) findViewById(R.id.buttonAddDeviceMain);
            buttonAddDeviceMain.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(TAG, "Position Changed");
                userLat = location.getLatitude();
                userLon = location.getLongitude();
                LatLng userLocation = new LatLng(userLat, userLon);
                updateMapWithUserPosition(userLocation);
                performRequest(userLat, userLon);
                Log.d(TAG, "Performed Request");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "Map ready");

                map = googleMap;
                if (userLat != null & userLon != null) {
                    LatLng userPosition = new LatLng(userLat, userLon);
                    updateMapWithUserPosition(userPosition);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        spiceManager.start(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Location Permission refused, asking user");

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 0);
        } else {

            Log.d(TAG, "Location Permission Already Granted");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                userLat = lastLocation.getLatitude();
                userLon = lastLocation.getLongitude();
            }

        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        spiceManager.shouldStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "User Permission for Location Granted");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                , 0, 0, locationListener);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLat = lastLocation.getLatitude();
                        userLon = lastLocation.getLongitude();
                    }

                } else {

                    Log.d(TAG, "User Refused Location");
                    final AlertDialog needGPSDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Veuillez activer la géolocalisation")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }
    }

    private void updateMapWithUserPosition(LatLng latLng){

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("vous êtes ici")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.d(TAG, "Map Updated");
        mapView.onResume();
    }

    private void performRequest(Double lat, Double lon){

        String logMessage = String.format(Locale.US, "Performing request for %f, %f , waiting for callback.",
                lat,
                lon);
        Log.d(TAG, logMessage);
        DefibrillateurRequestModel request = new DefibrillateurRequestModel(lat, lon, DEFAULT_RADIUS);
        spiceManager.execute(request, new defibRequestListener());
    }

    private class defibRequestListener implements RequestListener<DefibrillateurPublicModel>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {

            Log.d(TAG, "Request failed");

        }

        @Override
        public void onRequestSuccess(DefibrillateurPublicModel defibrillateurPublicModel) {

            Log.d(TAG, "Request Success");
            List records = defibrillateurPublicModel.getRecords();
            int size = records.size();
            Log.d(TAG, "Got " + size + " items from API");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonAddDeviceMain:

                startActivity(new Intent(MainActivity.this, AddDeviceActivity.class));
                break;
        }
    }
}
