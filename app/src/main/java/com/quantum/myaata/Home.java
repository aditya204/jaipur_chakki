package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Adapters.ExtraCommission;
import com.quantum.myaata.Adapters.ProductAdapter;
import com.quantum.myaata.Adapters.UsersAdapter;
import com.quantum.myaata.Models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    List<ProductModel> productModelList;
    private ImageButton cart, orders, extre_commission,search;
    private Button pending_order, add_product, confiremed_orders;
    private Toolbar toolbar;
    public static LinearLayout users_LL, admin_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        recyclerView = findViewById( R.id.product_recycler );
        cart = findViewById( R.id.goto_cart );
        orders = findViewById( R.id.goto_orders );
        toolbar = findViewById( R.id.toolbar_home );
        users_LL = findViewById( R.id.linearLayout );
        admin_ll = findViewById( R.id.linearLayout1 );
        pending_order = findViewById( R.id.pending_orders );
        confiremed_orders = findViewById( R.id.confirmed_orders );
        add_product = findViewById( R.id.add_product );
        extre_commission = findViewById( R.id.add_extra_commission );
        search=findViewById( R.id.search );

        search.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Home.this,Search.class ) );
            }
        } );

        String id = FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( );

        DBquaries.chechAdmin( );
        DBquaries.checkAreaOrder();
        DBquaries.loadGroceryOrders();
        DBquaries.loadNumbers();
        DBquaries.loadCommissions();

        extre_commission.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (DBquaries.IS_ADMIN)
                    startActivity( new Intent( Home.this, Area.class ) );
                else if(DBquaries.is_franchise_Admin){
                    Intent intent=new Intent( Home.this, Area.class );
                    intent.putExtra( "layout_code",1 );
                    startActivity( intent );
                }
                else {
                    startActivity( new Intent( Home.this, ExtraCommission.class ) );

                }

            }
        } );

        if(DBquaries.IS_ADMIN){
            search.setVisibility( View.VISIBLE);
        }






        productModelList = new ArrayList<>( );
        productAdapter = new ProductAdapter( productModelList );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( productAdapter );


        toolbar.setTitle( "Products" );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );

        setSupportActionBar( toolbar );


        FirebaseFirestore.getInstance( ).collection( "PRODUCTS" ).orderBy( "name" ).get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful( )) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {

                        productModelList.add( new ProductModel( documentSnapshot.get( "image" ).toString( ),
                                documentSnapshot.get( "name" ).toString( ),
                                documentSnapshot.get( "price" ).toString( ),
                                documentSnapshot.get( "description" ).toString( ),
                                documentSnapshot.get( "commission" ).toString( ),
                                documentSnapshot.getId( ) ) );

                    }
                    productAdapter.notifyDataSetChanged( );
                }
            }
        } );


        cart.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Home.this, Cart.class ) );
            }
        } );
        pending_order.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Home.this, AdminPendingOrders.class ) );
            }
        } );
        confiremed_orders.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Home.this, AdminConfirmedOrder.class ) );
            }
        } );
        add_product.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(DBquaries.is_franchise_Admin){
                    Toast.makeText( Home.this, "This field is only for MainAdmin", Toast.LENGTH_SHORT ).show( );
                }else {
                    startActivity( new Intent( Home.this, AddProduct.class ) );
                }
            }
        } );
        orders.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Home.this, Orders.class );
                intent.putExtra( "id", id );
                startActivity( intent );
            }
        } );

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}