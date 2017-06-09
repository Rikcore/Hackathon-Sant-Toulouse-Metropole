package hackatonsant.wcs.fr.hackatonsant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by apprenti on 08/06/17.
 */


public class Tuto1Activity extends AppCompatActivity  {

    Button buttonSkipTuto1;
    TextView textViewTuto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto1);

        buttonSkipTuto1 = (Button) findViewById(R.id.buttonSkipTuto1);
        textViewTuto1 = (TextView)findViewById(R.id.textViewTuto1);
        //textViewTuto1.setText(R.string.textViewTitleStepTuto1);

        buttonSkipTuto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tuto2 = new Intent(Tuto1Activity.this, Tuto2Activity.class);

                startActivity(tuto2);
            }
        });
    }



}

