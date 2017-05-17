package com.danilons.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public EditText userName;
    public EditText password;
    public Button ok,register;
    public Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        userName.setText("admin");
        password.setText("abc123");
        ok = (Button) findViewById(R.id.ok);
// Initialize Realm
        Realm.init(MainActivity.this);
        realm = Realm.getDefaultInstance();
        //check if the DB is empty and add User Admin and the Vendors
        User first = realm.where(User.class).findFirst();
        if(first == null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.createObject(User.class);
                    user.setName("Administrator");
                    user.setAge(60);
                    user.setUserName("admin");
                    user.setPassword("abc123");
                    user.setUserId(0);
                    user.setAdmin(true);
                    System.out.println("Add Admin");
                    addProduct();
                }
            });

        }else{
            System.out.println("Admin was in database");
        }
      /*  Category catFirst = realm.where(Category.class).findFirst();
        if(catFirst == null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Category cat = realm.createObject(Category.class);
                    cat.setCatName("Computer");
                    Category cat1 = realm.createObject(Category.class);
                    cat1.setCatName("Phone");
                }
            });
        }*/
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



// Get a Realm instance for this thread

                final User usrs = realm.where(User.class).equalTo("userName",userName.getText().toString()).equalTo("password",password.getText().toString()).findFirst();//findAll();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                if(usrs != null){
                    //alertDialog.setMessage("Welcome, "+usrs.getName());
                    Intent regnext = new Intent(getApplication(), TabViewAct.class);
                    regnext.putExtra("userKey", usrs.getUserName());
                    startActivity(regnext);
                }else{
                    alertDialog.setMessage("Username or Password is invalid ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }



            }
        });
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regnext = new Intent(getApplication(), Register.class);
                startActivity(regnext);
            }
        });

    }

    public void addProduct(){
        Category cat = realm.createObject(Category.class);
        cat.setCatName("Computer");
        Category cat1 = realm.createObject(Category.class);
        cat1.setCatName("Phone");

        Vendor vd0 = realm.createObject(Vendor.class);
        vd0.setName("Walmart");
        vd0.setId(0);
        vd0.setAddress("900 Dufferin St, Toronto, ON M6H 4A9");
        vd0.setLocation(43.6562371,-79.4357443);
        Vendor vd1 = realm.createObject(Vendor.class);
        vd1.setName("BestBuy");
        vd1.setId(1);
        vd1.setAddress("CF Toronto Eaton Centre, 65 Dundas St W, Toronto, ON M5G 2C3");
        vd1.setLocation(43.6554221,-79.3830228);
        Vendor vd2 = realm.createObject(Vendor.class);
        vd2.setName("The Source");
        vd2.setId(2);
        vd2.setAddress("Hudson's Bay Centre, 20 Bloor St E, Toronto, ON M4W 3G7");
        vd2.setLocation(43.671069,-79.3857479);
        Vendor vd3 = realm.createObject(Vendor.class);
        vd3.setName("Apple Store");
        vd3.setId(3);
        vd3.setAddress("Yorkdale Shopping Centre, 3401 Dufferin Street, Toronto, ON M6A 2T9");
        vd3.setLocation(43.7255218,-79.4531679);

        Product pr1 = realm.createObject(Product.class);
        pr1.setId(0);
        pr1.setProModel("MacBook Pro");
        pr1.setProDesc("New MacBook Pro 2017 with touchbar");
        pr1.setQuantity(10);
        pr1.setProCat(cat);
        pr1.setPrice(2999.99);
        pr1.setImgID(realm,new int[]{R.drawable.mc1,R.drawable.mc2,R.drawable.mc3});
        pr1.setVendors(new RealmList<Vendor>(vd0,vd1));

        Product pr2 = realm.createObject(Product.class);
        pr2.setId(1);
        pr2.setProModel("Galaxy s7");
        pr2.setProDesc("Samsung Galaxy s7 128gb");
        pr2.setQuantity(30);
        pr2.setProCat(cat1);
        pr2.setPrice(699.99);
        pr2.setImgID(realm,new int[]{R.drawable.s71,R.drawable.s72,R.drawable.s73});
        pr2.setVendors(new RealmList<Vendor>(vd2,vd3));

    }
}

