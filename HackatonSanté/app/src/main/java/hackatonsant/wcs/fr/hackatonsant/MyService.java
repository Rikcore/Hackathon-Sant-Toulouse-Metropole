package hackatonsant.wcs.fr.hackatonsant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {

    public static final String TAG = "MyService";
    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Alerts");
    AlertModel model;

    public LocationManager locationManager;
    public LocationListener locationListener;

    private Double userLat;
    private Double userLon;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLat = location.getLatitude();
                userLon = location.getLongitude();
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

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
        userLat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        userLon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

        Log.d(TAG, "Service Created");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                boolean knows = PreferenceManager.getDefaultSharedPreferences(MyService.this).getBoolean("Knows", true);
                if (knows) {
                    model = dataSnapshot.getValue(AlertModel.class);

                    Double alertLat = model.getLat();
                    Double alertLon = model.getLon();
                    String alertId = dataSnapshot.getKey();
                    float[] results = new float[1];
                    Location.distanceBetween(alertLat, alertLon, userLat, userLon, results);

                    if (/*results[0] <= 500 && */!model.adressed) {
                        displayNotification(alertId);
                    }
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
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;

    }

    private void displayNotification(String alertId) {


        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        final String finalAlertId = alertId;
        intent.putExtra("Alert", finalAlertId);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Incident en cours")
                .setContentText("Un incident a lieu au : "+ model.getLocation())
                .setSmallIcon(R.mipmap.minidefibrillateur)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .build();

        notifManager.notify(0, noti);

    }

}
