package com.danilons.ecommerce;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by danilons on 2017-04-20.
 */

public class AddProductActivity extends AppCompatActivity {
    public EditText price;
    public EditText desc;
    public EditText model;
    public Spinner cat;
    public EditText qnt;
    public Realm realm;
    public Button submit;
    public Button back;
    public int nextId;
    public LinearLayout checkBoxsVendor;
    public RealmResults<Vendor> vendors;
    public ArrayList<Integer> listVendors = new ArrayList<Integer>();
    public ArrayList<Category> catList = new ArrayList<Category>();
    ArrayList<String> catNames = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        cat = (Spinner) findViewById(R.id.spinnerAddPro);
        price = (EditText) findViewById(R.id.priceAddPro);
        desc = (EditText) findViewById(R.id.descriptionAddPro);
        model = (EditText) findViewById(R.id.modelAddPro);
        qnt = (EditText) findViewById(R.id.quantAddPro);
        submit = (Button) findViewById(R.id.submitAddPro);
        back = (Button) findViewById(R.id.backAddPro);
        checkBoxsVendor = (LinearLayout) findViewById(R.id.linearLayoutVendor);

        Realm.init(AddProductActivity.this);
        realm = Realm.getDefaultInstance();

        RealmResults<Category> cats = realm.where(Category.class).findAll();
        for (Category catg : cats){
            catList.add(catg);
            catNames.add(catg.getCatName());
        }

        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(AddProductActivity.this,android.R.layout.simple_list_item_1,catNames);
        cat.setAdapter(categoryArrayAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgAlert = "";
                if(listVendors.isEmpty() || model.getText().length() == 0 || price.getText().length() == 0 || qnt.getText().length() == 0){
                    msgAlert = "You must fill the fields";
                }else{
                    try {
                        nextId = (int) realm.where(Product.class).count();
                    }catch (Exception e){
                        nextId = 0;
                    }
                    final RealmList<Vendor> realmVendors = new RealmList<Vendor>();
                    Integer[] valuesVendors = new Integer[listVendors.size()];
                    for(int num = 0; num<listVendors.size(); num++){
                        valuesVendors[num] = listVendors.get(num);
                    }
                    RealmResults<Vendor> resultVendors = realm.where(Vendor.class).in("id",valuesVendors).findAll();
                    for (Vendor vend:resultVendors) {
                        realmVendors.add(vend);
                    }
                    //nextId++;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Product product = realm.createObject(Product.class);
                            product.setId(nextId);
                            product.setProModel(model.getText().toString());
                            product.setPrice(Double.parseDouble(price.getText().toString()));
                            product.setProDesc(desc.getText().toString());
                            product.setQuantity(Integer.parseInt(qnt.getText().toString()));
                            product.setVendors(realmVendors);
                            String catSel = cat.getSelectedItem().toString();
                            for(Category catg : catList){
                                if(catg.getCatName().equalsIgnoreCase(catSel)){
                                    product.setProCat(catg);
                                    break;
                                }
                            }
                            product.setImgID(realm,new int[]{R.drawable.iphone7,R.drawable.iphone7a,R.drawable.iphone7b});

                            //
                        }

                    });
                    msgAlert = "Product add with sucessfully";
                }
                AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                alertDialog.setMessage(msgAlert);
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
        //read Vendors from DB and add to a checkbox
         vendors = realm.where(Vendor.class).findAll();

        for (Vendor vendor: vendors) {
            final CheckBox checkBox = new CheckBox(AddProductActivity.this);
            checkBox.setText(vendor.getName());
            checkBox.setId(vendor.getId());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!checkBox.isChecked()){
                        for (int a=0; a<listVendors.size(); a++) {
                            if(listVendors.get(a) == checkBox.getId()){
                                listVendors.remove(a);
                            }
                        }
                    }else{
                        listVendors.add(checkBox.getId());
                    }

                }
            });
            checkBoxsVendor.addView(checkBox);

        }
    }


}
