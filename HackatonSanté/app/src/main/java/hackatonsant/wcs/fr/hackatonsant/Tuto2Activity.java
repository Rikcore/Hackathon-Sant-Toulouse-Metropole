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


public class Tuto2Activity extends AppCompatActivity {

    Button buttonSkipTuto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto2);

        buttonSkipTuto2 = (Button) findViewById(R.id.buttonSkipTuto2);

        buttonSkipTuto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tuto3 = new Intent(Tuto2Activity.this, Tuto3Activity.class);

                startActivity(tuto3);
                finish();
            }
        });
    }



}