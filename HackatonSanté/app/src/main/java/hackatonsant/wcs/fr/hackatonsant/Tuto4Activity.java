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


public class Tuto4Activity extends AppCompatActivity {

    Button buttonSkipTuto4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto4);

        buttonSkipTuto4 = (Button) findViewById(R.id.buttonSkipTuto4);

        buttonSkipTuto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tuto5 = new Intent(Tuto4Activity.this, Tuto5Activity.class);

                startActivity(tuto5);
            }
        });
    }



}