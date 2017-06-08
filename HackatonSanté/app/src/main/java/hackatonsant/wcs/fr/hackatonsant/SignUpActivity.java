package hackatonsant.wcs.fr.hackatonsant;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {
    CheckBox checkBoxKnow;
    CheckBox checkBoxOwn;
    private FirebaseDatabase database;
    private DatabaseReference refKnows;
    private DatabaseReference refDoesnt;
    Button buttonSignUp;
    private EditText editTextName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkBoxKnow = (CheckBox) findViewById(R.id.checkBoxKnow);
        checkBoxOwn = (CheckBox) findViewById(R.id.checkBoxOwn);
        buttonSignUp = (Button) findViewById(R.id.buttonRegisterUser);
        editTextName = (EditText) findViewById(R.id.editTextName);


        database = FirebaseDatabase.getInstance();
        refKnows = database.getReference("users/knows");
        refDoesnt = database.getReference("users/doesnt");


        buttonSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean owns = checkBoxOwn.isChecked();
                boolean knows = checkBoxKnow.isChecked();
                if (editTextName.getText() == null){
                    Toast.makeText(SignUpActivity.this, "Vous devez renseigner votre nom", Toast.LENGTH_SHORT).show();
                } else {
                    String userName = editTextName.getText().toString();
                    //PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this).edit().putString("UserName", userName).apply();
                    UserModel user = new UserModel(userName, knows);

                    if (knows) {
                        refKnows.push().setValue(user);
                    }
                    else {
                        refDoesnt.push().setValue(user);
                    }
                }


            }
        });

    }

}
