package com.danilons.ecommerce;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

/**
 * Created by danilons on 2017-04-11.
 */

public class Register extends AppCompatActivity {
     EditText username, age, name, password;
     Button submit, back;
    int numOfUsers;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.userNameRegister);
        name = (EditText) findViewById(R.id.nameRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        age = (EditText) findViewById(R.id.ageRegister);
        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();

        numOfUsers =  realm.where(User.class).findAll().size();
    back = (Button) findViewById(R.id.backRegister);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    submit = (Button) findViewById(R.id.submitRegister);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty()
                        || age.getText().toString().isEmpty()){

                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            User user = realm.createObject(User.class);
                            user.setName(name.getText().toString());
                            user.setAge(Integer.parseInt(age.getText().toString()));
                            user.setUserName(username.getText().toString());
                            user.setPassword(password.getText().toString());
                            user.setUserId(numOfUsers+1);
                            user.setAdmin(false);
                            AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                            alertDialog.setMessage("User add with sucessfully");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                    });

                }

            }
        });
    }
}
