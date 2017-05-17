package com.danilons.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by danilons on 2017-04-20.
 */

public class ProductDetails extends AppCompatActivity {
    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    public Product productDetail;
    public TextView model,price,desc;
    public ListView vendorsPro;
    public RealmResults<Cart> currentCart;
    public Button addCart;
    public ArrayList<Product> products = new ArrayList<Product>();
    public Product newProduct = new Product();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intentObj = getIntent();
        final int productId = intentObj.getIntExtra("ProductKey",99);
        final int userIdc = intentObj.getIntExtra("userKey",99);
        vendorsPro = (ListView) findViewById(R.id.listVendorsDetail);
        model = (TextView) findViewById(R.id.modelProDetail);
        price = (TextView) findViewById(R.id.priceProDetail);
        desc = (TextView) findViewById(R.id.descProDetail);
        addCart = (Button) findViewById(R.id.addCar);
        final Realm realm = Realm.getDefaultInstance();
        productDetail = realm.where(Product.class).equalTo("Id",productId).findFirst();
        currentCart = realm.where(Cart.class).equalTo("userId",userIdc).findAll();
        if(products.size()>0){
            products.clear();
        }

        model.setText("Model: "+productDetail.getProModel());
        price.setText("Price: CAD"+productDetail.getPrice());
        desc.setText("Description: "+productDetail.getProDesc());
        String[] vendorsList = new String[productDetail.getVendors().size()];

        for (int a=0; a < productDetail.getVendors().size(); a++) {
            vendorsList[a] = productDetail.getVendors().get(a).getName();
        }
        CustomAdapterVendor customAdapterVendor = new CustomAdapterVendor(this,vendorsList);
        vendorsPro.setAdapter(customAdapterVendor);
        vendorsPro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextVendorIntent = new Intent(getApplication(),VendorDetails.class);
                RealmList<Vendor> vendorRealmList = productDetail.getVendors();
                Vendor finalVendor = vendorRealmList.get(position);
                nextVendorIntent.putExtra("vendorKey",finalVendor.getId());
                startActivity(nextVendorIntent);
            }
        });


        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this, productDetail.getImgID());
        mViewPager.setAdapter(adapterView);
        NUM_PAGES = adapterView.getCount();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        //addcart buttom action

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                final int qntLeft = productDetail.getQuantity();
                if(qntLeft == 0){
                    msg = "Product Sold out";
                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(currentCart.size() >0){
                                boolean exist = false;
                                for(int a = 0; a< currentCart.size(); a++) {
                                    if (currentCart.get(a).getProductId() == productId && currentCart.get(a).getUserId() == userIdc) {
                                        currentCart.get(a).setQuant(currentCart.get(a).getQuant() + 1);
                                        exist = true;
                                    }
                                }
                                if(!exist){
                                    Cart newItem = realm.createObject(Cart.class);
                                    newItem.setQuant(1);
                                    newItem.setUserId(userIdc);
                                    newItem.setProductId(productId);
                                }
                            }else{

                                Cart newItem = realm.createObject(Cart.class);
                                newItem.setQuant(1);
                                newItem.setUserId(userIdc);
                                newItem.setProductId(productId);

                            }
                            //currentUser.setCart(products);
                            // products.get(products.size()-1).setQuantity(1);
                            productDetail.setQuantity(qntLeft-1);
                        }
                    });
                    msg= "Item added in the car with success";
                }


                AlertDialog alertDialog = new AlertDialog.Builder(ProductDetails.this).create();
                alertDialog.setMessage(msg);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        });

    }
}
