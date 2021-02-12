package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Adapters.AdminOrderAdaptrer;
import com.quantum.myaata.Models.AdminOrderModel;

import java.util.ArrayList;
import java.util.List;

public class AdminConfirmedOrder extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView confirmOrderRecycler;
    private AdminOrderAdaptrer adminOrderAdaptrer;
    private List<AdminOrderModel> adminOrderModelList;
    private TextView coinfirmed, pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_confirmed_order );


        toolbar = findViewById( R.id.confirm_toolbar );
        confirmOrderRecycler = findViewById( R.id.confirmOrderRecycler );
        pending = findViewById( R.id.admin_confirmed_pending_order_txt );
        coinfirmed = findViewById( R.id.admin_confirmed_confirmed_order_txt );


        toolbar.setTitle( "Confirm Order" );
        setSupportActionBar( toolbar );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );

        adminOrderModelList = new ArrayList<>( );

        adminOrderAdaptrer = new AdminOrderAdaptrer( adminOrderModelList );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        confirmOrderRecycler.setLayoutManager( linearLayoutManager );
        confirmOrderRecycler.setAdapter( adminOrderAdaptrer );

        if(DBquaries.is_franchise_Admin){
            int size=DBquaries.franchise_order_list.size();
            for(int i=0;i<size;i++){
                FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document(DBquaries.franchise_order_list.get( i )  ).
                        get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        boolean is_paid = (boolean) task.getResult().get( "is_paid" );
                        if (is_paid) {

                            adminOrderModelList.add( new AdminOrderModel( task.getResult( ).get( "id" ).toString( ),
                                    task.getResult( ).get( "time" ).toString( ),
                                    task.getResult( ).get( "otp" ).toString( ),
                                    task.getResult( ).get( "grand_total" ).toString( ),
                                    (boolean) task.getResult( ).get( "is_paid" ),
                                    (boolean) task.getResult( ).get( "is_pickUp" ),
                                    task.getResult( ).get( "store_address" ).toString( ),
                                    task.getResult( ).get( "store_name" ).toString( ),
                                    task.getResult( ).get( "total_commission" ).toString( ),
                                    task.getResult( ).get( "store_id" ).toString( )
                            ) );
                            adminOrderAdaptrer.notifyDataSetChanged();
                        }
                    }
                } );
            }
            adminOrderAdaptrer.notifyDataSetChanged();

        }else {
            FirebaseFirestore.getInstance( ).collection( "ORDERS" ).orderBy( "id" ).get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful( )) {

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {

                            boolean is_paid = (boolean) documentSnapshot.get( "is_paid" );
                            if (is_paid) {

                                adminOrderModelList.add( new AdminOrderModel( documentSnapshot.get( "id" ).toString( ),
                                        documentSnapshot.get( "time" ).toString( ),
                                        documentSnapshot.get( "otp" ).toString( ),
                                        documentSnapshot.get( "grand_total" ).toString( ),
                                        (boolean) documentSnapshot.get( "is_paid" ),
                                        (boolean) documentSnapshot.get( "is_pickUp" ),
                                        documentSnapshot.get( "store_address" ).toString(),
                                        documentSnapshot.get( "store_name" ).toString(),
                                        documentSnapshot.get( "total_commission" ).toString(),
                                        documentSnapshot.get( "store_id" ).toString()
                                ) );


                            }

                        }
                        adminOrderAdaptrer.notifyDataSetChanged( );


                    }
                }
            } );
        }



        pending.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( AdminConfirmedOrder.this, AdminPendingOrders.class );
                startActivity( intent );
            }
        } );


    }
}