package com.danilons.ecommerce;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TabHost;
import android.widget.TextView;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by danilons on 2017-04-13.
 */

public class TabViewAct extends AppCompatActivity {

    public SearchView searchView;
    public ListView listViewFull, listViewCart;
    public ArrayList<Product> prods = new ArrayList<Product>();
    public ArrayList<Product> searchAllProducts = new ArrayList<Product>();
    public ArrayList<Product> searchCartProducts = new ArrayList<Product>();
    public RealmResults<Cart> cartList;
    public ArrayList<Integer> qntsCart = new ArrayList<Integer>();
    public User userLog;
    public Realm realm;
    public Button edit;
    public EditText nametb,userNametb,userAgetb;
    boolean isEdit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        nametb = (EditText)findViewById(R.id.NameProfileTb);
        userNametb = (EditText) findViewById(R.id.userNameProfileTb);
        userAgetb = (EditText) findViewById(R.id.userAgeProfileTb);
        Intent intentObj = getIntent();
        Realm.init(TabViewAct.this);
        realm = Realm.getDefaultInstance();
        userLog = realm.where(User.class).equalTo("userName",intentObj.getStringExtra("userKey")).findFirst();
        RealmResults<Product> prodBds = realm.where(Product.class).findAll();
        for (Product proBd : prodBds ){
            prods.add(proBd);
        }

        listViewFull = (ListView) findViewById(R.id.listViewFull);
        listViewCart = (ListView) findViewById(R.id.listViewCart);
        
        searchAllProducts = prods;
        cartList = realm.where(Cart.class).equalTo("userId",userLog.getUserId()).findAll();
        for(Cart item: cartList){
            Product tmppro = realm.where(Product.class).equalTo("Id",item.getProductId()).findFirst();
            searchCartProducts.add(tmppro);
            qntsCart.add(item.getQuant());
        }
        CustomAdapterPro myAdaptCar = new CustomAdapterPro(searchCartProducts, qntsCart, getApplicationContext());
        listViewCart.setAdapter(myAdaptCar);

        CustomAdapterPro myAdapt = new CustomAdapterPro(searchAllProducts, getApplicationContext());
        listViewFull.setAdapter(myAdapt);
        searchView = (SearchView)findViewById(R.id.searchBar);

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Product> searchtp = new ArrayList<Product>();
                ArrayList<Product> searchtpcar = new ArrayList<Product>();
                ArrayList<Integer> qntsCartTb = new ArrayList<Integer>();

                for (Product prod: prods) {
                    if(prod.getProModel().toLowerCase().contains(newText.toLowerCase())){
                        searchtp.add(prod);
                    }
                }
                for(Cart item: cartList){
                    Product tmppro = realm.where(Product.class).equalTo("Id",item.getProductId()).contains("proModel",newText).findFirst();
                    if(tmppro != null){
                        searchtpcar.add(tmppro);
                        qntsCartTb.add(item.getQuant());
                    }

                }

                    searchAllProducts = searchtp;
                    searchCartProducts = searchtpcar;

                CustomAdapterPro myAdapt = new CustomAdapterPro(searchAllProducts, getApplicationContext());
                listViewFull.setAdapter(myAdapt);

                CustomAdapterPro myAdaptCar = new CustomAdapterPro(searchCartProducts, qntsCartTb, getApplicationContext());
                listViewCart.setAdapter(myAdaptCar);
                return false;
            }
        });


        final TabHost host = (TabHost)findViewById(R.id.tabHost);

        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Products");
        spec.setContent(R.id.Products);
        spec.setIndicator("Products");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("My Cart");
        spec.setContent(R.id.Cart);
        spec.setIndicator("My Cart");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Profile");
        spec.setContent(R.id.Profile);
        spec.setIndicator("Profile");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equalsIgnoreCase("Products")){
                    searchView.setVisibility(View.VISIBLE);

                    CustomAdapterPro myAdapt = new CustomAdapterPro(prods, getApplicationContext());
                    listViewFull.setAdapter(myAdapt);
                }else if(tabId.equalsIgnoreCase("My Cart")){
                    CustomAdapterPro myAdaptCart = new CustomAdapterPro(searchCartProducts, qntsCart, getApplicationContext());
                    listViewCart.setAdapter(myAdaptCart);
                    searchView.setVisibility(View.VISIBLE);
                }else{
                    isEdit = false;

                    nametb.setText(userLog.getName());
                    nametb.setEnabled(false);
                    userNametb.setText(userLog.getUserName());
                    userNametb.setEnabled(false);
                    userAgetb.setText(""+userLog.getAge());
                    userAgetb.setEnabled(false);
                    searchView.setVisibility(View.GONE);
                   edit = (Button) findViewById(R.id.editProfile);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isEdit){
                                edit.setText("Edit");
                                userAgetb.setEnabled(false);
                                userNametb.setEnabled(false);
                                nametb.setEnabled(false);
                                isEdit = false;
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        userLog.setName(nametb.getText().toString());
                                        userLog.setAge(Integer.parseInt(userAgetb.getText().toString()));
                                        userLog.setUserName(userNametb.getText().toString());
                                    }
                                });

                            }else{
                                userAgetb.setEnabled(true);
                                userNametb.setEnabled(true);
                                nametb.setEnabled(true);
                                isEdit = true;
                                edit.setText("Submit");
                            }
                        }
                    });
                }
            }
        });
        listViewFull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextProd = new Intent(getApplicationContext(),ProductDetails.class);
                int idProd =prods.get(position).getId();
                nextProd.putExtra("ProductKey",idProd);
                nextProd.putExtra("userKey",userLog.getUserId());
                startActivity(nextProd);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(userLog.getUserName().equalsIgnoreCase("admin")){
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add_product:
                //your code here
                Intent nextAdd = new Intent(getApplication(), AddProductActivity.class);
                startActivity(nextAdd);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prods.clear();
        RealmResults<Product> prodBds = realm.where(Product.class).findAll();
        for (Product proBd : prodBds ){
            prods.add(proBd);
        }
        searchAllProducts = prods;
        searchCartProducts.clear();
        qntsCart.clear();
        for(Cart item: cartList){
            Product tmppro = realm.where(Product.class).equalTo("Id",item.getProductId()).findFirst();
            searchCartProducts.add(tmppro);
            qntsCart.add(item.getQuant());
        }
        if(searchCartProducts != null){
            CustomAdapterPro myAdapt = new CustomAdapterPro(searchAllProducts, getApplicationContext());
            listViewFull.setAdapter(myAdapt);
        }

    }
}
