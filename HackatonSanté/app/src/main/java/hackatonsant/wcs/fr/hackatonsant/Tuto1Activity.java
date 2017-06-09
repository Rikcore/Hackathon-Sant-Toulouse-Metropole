package hackatonsant.wcs.fr.hackatonsant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by apprenti on 08/06/17.
 */


public class Tuto1Activity extends AppCompatActivity  {

    Button buttonSkipTuto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto1);

        buttonSkipTuto1 = (Button) findViewById(R.id.buttonSkipTuto1);

        buttonSkipTuto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tuto2 = new Intent(Tuto1Activity.this, Tuto2Activity.class);

                startActivity(tuto2);
            }
        });
    }



}

