package hackatonsant.wcs.fr.hackatonsant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity {

    public static final String TAG = "EmergencyActivity";
    public static final int DEFAULT_RADIUS = 5000;

    private TextView textViewAddress;
    private TextView textViewTest;
    private Button callEmergency;
    private ConstraintLayout addressLayout;

    private MapView mapView;
    private GoogleMap map;
    private Marker userMarker;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String address;

    private FirebaseDatabase database;
    private DatabaseReference refDevices;
    private DatabaseReference refAlert;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double userLat;
    private Double userLon;

    private SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        geocoder = new Geocoder(this, Locale.getDefault());

        database = FirebaseDatabase.getInstance();
        refDevices = database.getReference("Devices");
        refAlert = database.getReference("Alerts");


        mapView = (MapView) findViewById(R.id.mapViewEmergency);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        callEmergency = (Button) findViewById(R.id.buttonEmergency);
        addressLayout = (ConstraintLayout) findViewById(R.id.addressLayout);
        addressLayout.setVisibility(View.INVISIBLE);


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

                try {
                    addresses = geocoder.getFromLocation(userLat, userLon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                addressLayout.setVisibility(View.VISIBLE);
                textViewAddress.setText(String.format("Adresse à donner aux secours :%n%s%n%s %s",address,postalCode,city));

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
                LatLng defautlPos = new LatLng(MainActivity.DEFAULT_LAT_TLSE, MainActivity.DEFAULT_LON_TLSE);
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

        refDevices.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DefibrilateurPrivateModel model = dataSnapshot.getValue(DefibrilateurPrivateModel.class);

                Double currentLat = model.getLat();
                Double currentLon = model.getLon();
                String title = model.getImplantation();

                if (currentLat != null) {
                    LatLng latLng = new LatLng(currentLat, currentLon);
                    Log.d(TAG, latLng.toString());
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.mini_housse);
                    map.addMarker(new MarkerOptions().position(latLng).title(title));
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

        callEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertModel alertModel = new AlertModel(userLat, userLon, false, address);
                refAlert.push().setValue(alertModel);


                String number = "+33698631580";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "Location Permission refused, asking user");

                        ActivityCompat.requestPermissions(EmergencyActivity.this,
                                new String[]{
                                        Manifest.permission.CALL_PHONE
                                }, 1);
                    }

                    return;
                }
                startActivity(intent);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

        spiceManager.start(this);

        if (ContextCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Location Permission refused, asking user");

            ActivityCompat.requestPermissions(EmergencyActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 0);
        } else {

            Log.d(TAG, "Location Permission Already Granted");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                userLat = lastLocation.getLatitude();
                userLon = lastLocation.getLongitude();
            }
        }
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
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                , 3000, 0, locationListener);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLat = lastLocation.getLatitude();
                        userLon = lastLocation.getLongitude();
                    }

                } else {

                    Log.d(TAG, "User Refused Location");
                    final AlertDialog needGPSDialog = new AlertDialog.Builder(EmergencyActivity.this)
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
            case 1 :

                String number = "+33698631580";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);


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
                String titleAndAdress = defibrillateurPublicModel.getRecords().get(i).getFields().getImplantation() + " " + defibrillateurPublicModel.getRecords().get(i).getFields().getAdresse();
                String info = defibrillateurPublicModel.getRecords().get(i).getFields().getAccessibilite();
                map.addMarker(new MarkerOptions().position(coordinates).icon(icon).title(titleAndAdress).snippet(info));
                mapView.onResume();
            }
        }
    }
}
