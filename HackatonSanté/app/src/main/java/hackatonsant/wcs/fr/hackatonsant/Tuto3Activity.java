package hackatonsant.wcs.fr.hackatonsant;

/**
 * Created by apprenti on 08/06/17.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by apprenti on 08/06/17.
 */


public class Tuto3Activity extends AppCompatActivity {

    Button buttonSkipTuto3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto3);

        buttonSkipTuto3 = (Button) findViewById(R.id.buttonSkipTuto3);

        buttonSkipTuto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tuto4 = new Intent(Tuto3Activity.this, Tuto4Activity.class);

                startActivity(tuto4);
            }
        });
    }



}