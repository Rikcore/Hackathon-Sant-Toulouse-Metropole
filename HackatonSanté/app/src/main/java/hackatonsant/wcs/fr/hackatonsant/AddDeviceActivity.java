package hackatonsant.wcs.fr.hackatonsant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "AddDeviceActivity";

    private EditText editTextAdresse;
    private EditText editTextAccessibilite;
    private EditText editTextImplantation;
    private EditText editTextNomSite;
    private EditText editTextTypeStructure;

    private Double lat;
    private Double lon;

    private Button buttonAddDevice;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editTextAdresse = (EditText) findViewById(R.id.editTextAdresse);
        editTextAccessibilite = (EditText) findViewById(R.id.editTextAccessibilite);
        editTextImplantation = (EditText) findViewById(R.id.editTextImplantation);
        editTextNomSite = (EditText) findViewById(R.id.editTextNomSite);
        editTextTypeStructure  = (EditText) findViewById(R.id.editTextTypeStructure);

        buttonAddDevice = (Button) findViewById(R.id.buttonAddDevice);
            buttonAddDevice.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Devices");

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();
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

        final AlertDialog needLocation= new AlertDialog.Builder(AddDeviceActivity.this)
       .setMessage("Veuillez vous placer près du défibrillateur pour faciliter sa géolocalisation, puis appuyer sur Continuer")
       .setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AddDeviceActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .show();
    }

    private DefibrilateurPrivateModel createPrivateDevice(){

        String accessibilite = editTextAccessibilite.getText().toString();
        String implantation = editTextImplantation.getText().toString();
        String adresse = editTextAdresse.getText().toString();
        String nomSite = editTextNomSite.getText().toString();
        String typeStructure = editTextTypeStructure.getText().toString();

        DefibrilateurPrivateModel defibrilateurPrivateModel =new DefibrilateurPrivateModel(adresse, lat, lon);
        defibrilateurPrivateModel.setAccessibilite(accessibilite);
        defibrilateurPrivateModel.setImplantation(implantation);
        defibrilateurPrivateModel.setNomSite(nomSite);
        defibrilateurPrivateModel.setTypeStructure(typeStructure);
        return defibrilateurPrivateModel;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(AddDeviceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Location Permission refused, asking user");

            ActivityCompat.requestPermissions(AddDeviceActivity.this,
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, 0);
        } else {

            Log.d(TAG, "Location Permission Already Granted");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonAddDevice:
                if(editTextAdresse.getText().length() == 0){
                    Toast.makeText(this, "Veuillez renseigner une adresse.", Toast.LENGTH_SHORT).show();
                } else {


                    DefibrilateurPrivateModel finalDevice = createPrivateDevice();
                    ref.push().setValue(finalDevice);
                    startActivity(new Intent(AddDeviceActivity.this, MainActivity.class));
                }

        }
    }
}
