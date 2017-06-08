package hackatonsant.wcs.fr.hackatonsant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextAdresse;
    private EditText editTextAccessibilite;
    private EditText editTextImplantation;
    private EditText editTextNomSite;
    private EditText editTextTypeStructure;

    private Button buttonAddDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        editTextAdresse = (EditText) findViewById(R.id.editTextAdresse);
        editTextAccessibilite = (EditText) findViewById(R.id.editTextAccessibilite);
        editTextImplantation = (EditText) findViewById(R.id.editTextImplantation);
        editTextNomSite = (EditText) findViewById(R.id.editTextNomSite);
        editTextTypeStructure  = (EditText) findViewById(R.id.editTextTypeStructure);

        buttonAddDevice = (Button) findViewById(R.id.buttonAddDevice);
            buttonAddDevice.setOnClickListener(this);


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

    private DefibrilateurPrivate createPrivateDevice(){

        String accessibilite = editTextAccessibilite.getText().toString();
        String implantation = editTextImplantation.getText().toString();
        String adresse = editTextAdresse.getText().toString();
        String nomSite = editTextNomSite.getText().toString();
        String typeStructure = editTextTypeStructure.getText().toString();

        DefibrilateurPrivate defibrilateurPrivate =new DefibrilateurPrivate(adresse);
        defibrilateurPrivate.setAccessibilite(accessibilite);
        defibrilateurPrivate.setImplantation(implantation);
        defibrilateurPrivate.setNomSite(nomSite);
        defibrilateurPrivate.setTypeStructure(typeStructure);
        return defibrilateurPrivate;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonAddDevice:

                DefibrilateurPrivate finalDevice = createPrivateDevice();
                String toto = "toto";
        }
    }
}
