package hackatonsant.wcs.fr.hackatonsant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
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

    public MyService() {}

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service Created");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                boolean knows = PreferenceManager.getDefaultSharedPreferences(MyService.this).getBoolean("Knows", true);
                if (knows) {
                    model = dataSnapshot.getValue(AlertModel.class);

                    displayNotification();
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

    private void displayNotification() {


        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Incident en cours")
                .setContentText("Un incident a lieu au : "+ model.getLocation())
                .setSmallIcon(R.mipmap.minidefibrillateur)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .build();

        notifManager.notify(0, noti);

    }

}
