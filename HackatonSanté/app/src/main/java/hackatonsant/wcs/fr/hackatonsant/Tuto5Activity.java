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


public class Tuto5Activity extends AppCompatActivity {

    Button buttonTutoReturnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuto5);

        buttonTutoReturnMain = (Button) findViewById(R.id.buttonTutoReturnMain);

        buttonTutoReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnmain = new Intent(Tuto5Activity.this, MainActivity.class);

                startActivity(returnmain);
            }
        });
    }



}