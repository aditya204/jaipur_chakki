package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quantum.myaata.Adapters.grocery_cart_product_Adapter;
import com.quantum.myaata.Models.grocery_cart_product_Model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Cart extends AppCompatActivity {

    public static TextView priceIncart, tax, disccount, grandTotal;
    private TextView payInCash, editAddress;
    private static String Order_Id = "";
    private long order_no[] = {0};
    private FirebaseFirestore firebaseFirestore;
    public static List<String> product_price_grocery = new ArrayList<>( );
    private Toolbar toolbar;
    public static grocery_cart_product_Adapter grocery_cart_product_adapter;
    private final String CHANNEL_ID = "ai";
    private int Tax[] = {0};
    public static TextView totalSave, payAmount;

    private LinearLayout noItemLL, payment_layout;
    private Dialog loadingDialog;
    private ConstraintLayout address_layout,cart_activity;
    public  static CheckBox pickUpCheck;

    long noCount=0;

    public static List<grocery_cart_product_Model> grocery_cart_product_modelList;
    private RecyclerView productRecyeler;


    private static String otp = new DecimalFormat( "000000" ).format( new Random( ).nextInt( 999999 ) );

    String store_name="";
    String store_address="";

    ///address

    private TextView addresss_name, address_details, address_phn, edit_address, adderss_type,adderss_whatsapp;
    private String user_address_details = "";
    private String user_name = "";
    private long previous_position = 0;
    private String user_phn = "";
    private String user_whatsapp = "";
    private String user_address_type = "";
    ///address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cart );

        cart_activity=findViewById( R.id.cart_activity );
        addresss_name = findViewById( R.id.grocery_cart_address_name );
        address_details = findViewById( R.id.grocery_cart_address_hostel_room );
        address_phn = findViewById( R.id.grocery_cart_address_phone );
        edit_address = findViewById( R.id.grocery_cart_address_editTxt );
        adderss_type = findViewById( R.id.grocery_cart_address_type );
        address_layout = findViewById( R.id.address_layout );
        cart_activity.setVisibility( View.INVISIBLE );
        payment_layout = findViewById( R.id.PaymentLayout );

        totalSave = findViewById( R.id.grocery_cart_totalSave );
        priceIncart = findViewById( R.id.itemTotalPrice );
        tax = findViewById( R.id.taxChargesPrice );
        disccount = findViewById( R.id.discount_price );
        grandTotal = findViewById( R.id.grandTotalPrice );
        payInCash = findViewById( R.id.myCartGroceryPayinCashPayment );
        firebaseFirestore = FirebaseFirestore.getInstance( );
        toolbar = findViewById( R.id.toolbar );
        noItemLL = findViewById( R.id.no_item_layout );
        payAmount = findViewById( R.id.grocery_cart_payAmount );
        edit_address = findViewById( R.id.grocery_cart_address_editTxt );
        adderss_whatsapp=findViewById( R.id.textView8 );


        pickUpCheck = findViewById( R.id.pickUpCheck );

        loadingDialog = new Dialog( Cart.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show( );

        productRecyeler = findViewById( R.id.grocery_cart_product_recycler );
        DBquaries.grocery_CartList_product_OutOfStock.clear( );
        int size = DBquaries.grocery_CartList_product_id.size( );

        if (size == 0) {
            noItemLL.setVisibility( View.VISIBLE );
            cart_activity.setVisibility( View.VISIBLE );

            payment_layout.setVisibility( View.INVISIBLE );
            loadingDialog.dismiss( );
        }


        toolbar.setTitle( "My Cart" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );


        DBquaries.PRICE_IN_CART_GROCERY = 0;
        DBquaries.TOTAL_Commission = 0;
        // createNotificationChanel( );

        pickUpCheck.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DBquaries.setDeliveryCharges();
            }
        } );



        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful( )) {
                            user_address_details = task.getResult( ).get( "address_details" ).toString( );
                            user_name = task.getResult( ).get( "fullname" ).toString( );
                            previous_position = (long) task.getResult( ).get( "previous_position" );
                            user_phn = task.getResult( ).get( "phone" ).toString( );
                            user_whatsapp = task.getResult( ).get( "buyer_whatsapp" ).toString( );
                            user_address_type = task.getResult( ).get( "address_type" ).toString( );
                            store_name=task.getResult( ).get( "store_name" ).toString( );
                            store_address=task.getResult().get( "store_address" ).toString();


                            addresss_name.setText( user_name );
                            address_phn.setText( user_phn );
                            address_details.setText( user_address_details );
                            adderss_type.setText( user_address_type );
                            adderss_whatsapp.setText("whats app :-"+ user_whatsapp );

                            if (user_address_details.isEmpty()) {
                                address_layout.setVisibility( View.GONE );
                            }


                        }


                    }
                } );


        grocery_cart_product_modelList=new ArrayList<>(  );


        for (int x = 0; x < size; x++) {



            final String id = DBquaries.grocery_CartList_product_id.get( x );
            final int finalX = x;
            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTITEMCOUNT" ).get( )
                    .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful( )) {

                                final String count = task.getResult( ).get( id ).toString( );
                                DBquaries.grocery_CartList_product_count.add( count );

                                loadingDialog.show( );
                                firebaseFirestore.collection( "PRODUCTS" ).document( id ).get( )
                                        .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful( )) {
                                                    DBquaries.PRICE_IN_CART_GROCERY = DBquaries.PRICE_IN_CART_GROCERY + Integer.parseInt( count ) * Integer.parseInt( task.getResult( ).get( "price" ).toString( ) );
                                                    DBquaries.TOTAL_Commission = DBquaries.TOTAL_Commission + Integer.parseInt( count ) * (Integer.parseInt( task.getResult( ).get( "commission" ).toString( ) ) );
                                                    priceIncart.setText( String.valueOf( DBquaries.PRICE_IN_CART_GROCERY ) );
                                                    totalSave.setText( "₹" + String.valueOf( DBquaries.TOTAL_Commission ) );
                                                    DBquaries.setDeliveryCharges( );
                                                    if ((long) task.getResult( ).get( "in_stock" ) == 0) {
                                                        DBquaries.grocery_CartList_product_OutOfStock.add( id );
                                                    }

                                                    grocery_cart_product_modelList.add( new grocery_cart_product_Model(
                                                            task.getResult( ).get( "name" ).toString( ),
                                                            task.getResult( ).get( "description" ).toString( ),
                                                            task.getResult( ).get( "price" ).toString( ),
                                                            id,
                                                            count,
                                                            task.getResult( ).get( "image" ).toString( ),
                                                            (long) task.getResult( ).get( "in_stock" ),
                                                            finalX,
                                                            task.getResult( ).get( "commission" ).toString( )



                                                    ) );


                                                }



                                                cart_activity.setVisibility( View.VISIBLE );
                                                loadingDialog.dismiss( );
                                                grocery_cart_product_adapter.notifyDataSetChanged( );

                                            }
                                        } );


                            }
                        }
                    } );


        }

        grocery_cart_product_adapter = new grocery_cart_product_Adapter( grocery_cart_product_modelList );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        productRecyeler.setLayoutManager( linearLayoutManager );
        productRecyeler.setAdapter( grocery_cart_product_adapter );
        grocery_cart_product_adapter.notifyDataSetChanged( );


        edit_address.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, MyAddress.class );
                intent.putExtra( "previous_position", previous_position );
                startActivity( intent );


            }
        } );


        payInCash.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if (address_layout.getVisibility( ) == View.GONE) {

                    Intent intent=new Intent( Cart.this, Address.class);
                    intent.putExtra( "position", 0 );
                    startActivity( intent );

                } else {


                    if(DBquaries.MIN_ORDER_AMOUNT>DBquaries.PRICE_IN_CART_GROCERY+DBquaries.DELIVERY_CHARGES){
                        Toast.makeText( Cart.this, "Min Order Amount is ₹"+String.valueOf( DBquaries.MIN_ORDER_AMOUNT ), Toast.LENGTH_SHORT ).show( );
                    }
                    else {
                        if (DBquaries.grocery_CartList_product_OutOfStock.size( ) == 0) {
                            loadingDialog.show( );
                            Date dNow = new Date( );
                            SimpleDateFormat ft = new SimpleDateFormat( "yyMMddhhmmssMs" );
                            String datetime = ft.format( dNow );
                            Order_Id = datetime;

                            addOrderDetails( Order_Id, "POD", false );
                        } else {
                            Toast.makeText( Cart.this, "Remove the out of Stock product", Toast.LENGTH_SHORT ).show( );
                        }
                    }


                }
            }
        } );
    }

    private void addOrderDetails(final String OrderId, String Payment_Mode, boolean Payment_Status) {


        final Map<String, Object> order_update = new HashMap<>( );
        order_update.put( "orders",  String.valueOf( DBquaries.area_order+1 ) );
        FirebaseFirestore.getInstance().collection( "AREAS" ).document( DBquaries.user_area ).update( order_update ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        } );

        FirebaseFirestore.getInstance().collection( "SEARCH" ).document( user_phn ).get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            noCount=(long)task.getResult().get( "count" );

                            final Map<String, Object> COunt = new HashMap<>( );
                            COunt.put( "count",  noCount+1 );
                            COunt.put( "uid",  FirebaseAuth.getInstance().getCurrentUser().getUid() );


                            FirebaseFirestore.getInstance().collection( "SEARCH" ).document( user_phn ).update( COunt ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }
                                }
                            } );
                        }
                    }
                } );








        String pid = "";
        int grandToatal = 0;
        for (grocery_cart_product_Model groceryCartProductModel : grocery_cart_product_modelList) {

            Map<String, Object> OrderDetails = new HashMap<>( );
            OrderDetails.put( "name", groceryCartProductModel.getName( ) );
            OrderDetails.put( "product_id", groceryCartProductModel.getProduct_id( ) );
            OrderDetails.put( "price", groceryCartProductModel.getPrice( ) );
            OrderDetails.put( "description", groceryCartProductModel.getDescription( ) );
            OrderDetails.put( "image", groceryCartProductModel.getImage( ) );
            OrderDetails.put( "item_count", DBquaries.grocery_CartList_product_count.get( groceryCartProductModel.getInde( ) ) );
            OrderDetails.put( "user_name", user_name );
            OrderDetails.put( "user_phn", user_phn );
            OrderDetails.put( "user_address_details", user_address_details );
            OrderDetails.put( "user_address_type", user_address_type );
            OrderDetails.put( "user_whatsapp", user_whatsapp );
            OrderDetails.put( "user_id", FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) );
            OrderDetails.put( "payment_mode", Payment_Mode );
            OrderDetails.put( "delivery_status", false );
            OrderDetails.put( "is_cancled", false );
            OrderDetails.put( "delivery_time", "1" );
            OrderDetails.put( "payment_status", Payment_Status );
            OrderDetails.put( "otp", otp );
            OrderDetails.put( "commission",groceryCartProductModel.getCommision()  );
            OrderDetails.put( "is_pickUp",pickUpCheck.isChecked() );
            pid = groceryCartProductModel.getProduct_id( );

            String stock = String.valueOf( groceryCartProductModel.getIn_stock( ) );

            grandToatal = grandToatal + Integer.parseInt( groceryCartProductModel.getPrice( ) );
            final Map<String, Object> Stock = new HashMap<>( );
            Stock.put( "in_stock", Integer.parseInt( stock ) - Integer.parseInt( DBquaries.grocery_CartList_product_count.get( groceryCartProductModel.getInde( ) ) ) );


            firebaseFirestore.collection( "PRODUCTS" ).document( pid )
                    .update( Stock ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            } );





            final String finalPid = pid;
            final int finalGrandToatal = grandToatal;
            FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( OrderId ).collection( "ORDER_LIST" )
                    .document( groceryCartProductModel.getProduct_id( ) ).set( OrderDetails ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful( )) {


                        Date dNow = new Date( );
                        SimpleDateFormat ft = new SimpleDateFormat( "yy/MM/dd hh:mm" );
                        String datetime = ft.format( dNow );
                        final Map<String, Object> details = new HashMap<>( );
                        details.put( "grand_total", DBquaries.PRICE_IN_CART_GROCERY );
                        details.put( "id", OrderId );
                        details.put( "otp", otp );
                        details.put( "area",DBquaries.user_area );
                        details.put( "total_commission",DBquaries.TOTAL_Commission );
                        details.put( "tax", DBquaries.DELIVERY_CHARGES );
                        details.put( "time", datetime );
                        details.put( "is_paid", false );
                        details.put( "store_id", FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) );
                        details.put( "store_name",store_name );
                        details.put( "store_address",store_address );
                        details.put( "is_pickUp",pickUpCheck.isChecked() );
                        firebaseFirestore.collection( "ORDERS" ).document( OrderId ).set( details ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful( )) {

                                }
                            }
                        } );


                        Toast.makeText( Cart.this, "Your Order Is Placed", Toast.LENGTH_SHORT ).show( );

                        final Map<String, Object> orderId = new HashMap<>( );
                        orderId.put( "order_id_" + DBquaries.grocery_OrderList.size( ), OrderId );
                        orderId.put( "list_size", DBquaries.grocery_OrderList.size( ) + 1 );

                        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).update( orderId )
                                .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful( )) {
                                            loadingDialog.dismiss( );
                                            DBquaries.grocery_OrderList.add( OrderId );
                                            Map<String, Object> Size = new HashMap<>( );
                                            Size.put( "list_size", 0 );
                                            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                                                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).set( Size )
                                                    .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful( )) {

                                                                DBquaries.grocery_CartList_product_id.clear( );
                                                                DBquaries.grocery_CartList_product_count.clear( );

                                                                Intent intent = new Intent( Cart.this, Home.class );
                                                                startActivity( intent );
                                                            }

                                                        }
                                                    } );


                                        }
                                    }
                                } );


                    }
                }
            } );


        }


    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId( ) == android.R.id.home) {
            finish( );

        }

        return super.onOptionsItemSelected( item );
    }
}