package hackatonsant.wcs.fr.hackatonsant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final Double DEFAULT_LAT_TLSE = 43.608316;  //Lattitude St Sernin Tlse
    public static final Double DEFAULT_LON_TLSE = 1.441804;   //Longitude St Sernin Tlse
    public static final int DEFAULT_RADIUS = 5000;
    public static final String TAG = "MainActivity";

    private MapView mapView;
    private GoogleMap map;
    private Marker userMarker;
    private CustomInfoWindowAdapter mapAdapter;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double userLat;
    private Double userLon;

    private Button buttonAddDeviceMain;
    private Button buttonEmergency;
    private Button buttonTutorial;

    private FloatingActionButton fab;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private Intent intent;

    private SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userName = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("UserName", null);

        if (userName == null) {

            Log.d(TAG, "No User Logged, Starting SignUp");
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }
        Log.d(TAG, userName + " Logged In");
        buttonAddDeviceMain = (Button) findViewById(R.id.buttonAddDeviceMain);
        buttonAddDeviceMain.setOnClickListener(this);
        buttonEmergency = (Button)findViewById(R.id.buttonCallEmergency);
        buttonEmergency.setOnClickListener(this);
        buttonTutorial= (Button)findViewById(R.id.buttonTutorial);
        buttonTutorial.setOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonMain);
        fab.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Devices");

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapAdapter = new CustomInfoWindowAdapter(MainActivity.this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "Position Changed");
                userLat = location.getLatitude();
                userLon = location.getLongitude();

                if (userLat != null) {
                    LatLng userLocation = new LatLng(userLat, userLon);
                    updateMapWithUserPosition(userLocation);
                    performRequest(userLat, userLon);
                    Log.d(TAG, "Performed Request");
                }
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
                map.setInfoWindowAdapter(mapAdapter);
                LatLng defautlPos = new LatLng(DEFAULT_LAT_TLSE, DEFAULT_LON_TLSE);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_man);
                userMarker = map.addMarker(new MarkerOptions()
                        .position(defautlPos)
                        .title("Vous êtes ici")
                        .icon(icon));
                if (userLat != null & userLon != null) {
                    LatLng userPosition = new LatLng(userLat, userLon);
                    updateMapWithUserPosition(userPosition);
                }
            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DefibrilateurPrivateModel model = dataSnapshot.getValue(DefibrilateurPrivateModel.class);

                Double currentLat = model.getLat();
                Double currentLon = model.getLon();
                String adress = model.getAdresse();
                String implantation = model.getImplantation();
                String info = model.getAccessibilite();
                String title;
                if(implantation != null){
                    title = String.format("%s%n%s",
                            adress,
                            implantation);
                } else {
                    title = adress;
                }

                if (info == null){
                    info = "Pas d'information d'accessibilité";
                }

                if (currentLat != null) {
                    LatLng latLng = new LatLng(currentLat, currentLon);
                    Log.d(TAG, latLng.toString());
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.mini_housse);
                    map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(info).icon(icon));
                    mapView.onResume();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = getIntent();
        if (intent.hasExtra("Alert")) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Allez vous intervenir sur cet incident ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String alertId = intent.getStringExtra("Alert");

                            DatabaseReference boolRef = FirebaseDatabase.getInstance()
                                    .getReference("Alerts")
                                    .child(alertId);

                            boolRef.setValue(null);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                userLat = lastLocation.getLatitude();
                userLon = lastLocation.getLongitude();
            }

        }
        startService(new Intent(MainActivity.this, MyService.class));
    }

    @Override
    protected void onStop() {
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
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                , 0, 10, locationListener);
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

    private void updateMapWithUserPosition(LatLng latLng) {

        userMarker.setPosition(latLng);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.d(TAG, "Map Updated");
        mapView.onResume();
    }

    private void performRequest(Double lat, Double lon) {

        String logMessage = String.format(Locale.US, "Performing request for %f, %f , waiting for callback.",
                lat,
                lon);
        Log.d(TAG, logMessage);
        DefibrillateurRequestModel request = new DefibrillateurRequestModel(lat, lon, DEFAULT_RADIUS);
        spiceManager.execute(request, new defibRequestListener());
    }

    private class defibRequestListener implements RequestListener<DefibrillateurPublicModel> {

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
            for (int i = 0; i < records.size(); i++) {
                double lon = defibrillateurPublicModel.getRecords().get(i).getGeometry().getCoordinates().get(0);
                double lat = defibrillateurPublicModel.getRecords().get(i).getGeometry().getCoordinates().get(1);
                LatLng coordinates = new LatLng(lat, lon);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.minidefibrillateur);

                final String implantation = defibrillateurPublicModel.getRecords().get(i).getFields().getImplantation();
                final String adress = defibrillateurPublicModel.getRecords().get(i).getFields().getAdresse();
                String title;
                if (implantation != null) {
                    title = String.format("%s%n%s",
                            adress,
                            implantation);
                } else {
                    title = adress;
                }
                final String info = defibrillateurPublicModel.getRecords().get(i).getFields().getAccessibilite();
                map.addMarker(new MarkerOptions().position(coordinates).icon(icon).title(title).snippet(info));
                mapView.onResume();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonAddDeviceMain:

                startActivity(new Intent(MainActivity.this, AddDeviceActivity.class));
                break;
            
            case R.id.buttonCallEmergency :

                startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
                break;

            case R.id.buttonTutorial :
            
                startActivity(new Intent(MainActivity.this, Tuto1Activity.class));
                break;
            
            case R.id.floatingActionButtonMain :
            
                LatLng userPosition = new LatLng(userLat, userLon);
                map.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                break;
        }
    }
}
