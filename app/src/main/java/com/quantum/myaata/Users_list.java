package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Adapters.UsersAdapter;
import com.quantum.myaata.Models.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users_list extends AppCompatActivity {
    private RecyclerView user_recycler;
    UsersAdapter usersAdapter;
    List<UsersModel> usersModelList;
    Toolbar toolbar;
    String area_name="";
    String area_count="";
    Button clear_area_orders;
    TextView order_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_users_list );

        user_recycler=findViewById( R.id.users_recycler);
        toolbar=findViewById( R.id.Users_toolbar );
        order_count=findViewById( R.id.area_order_txt );
        clear_area_orders=findViewById( R.id.clear_area_orders );

        if(DBquaries.is_franchise_Admin){
            clear_area_orders.setVisibility( View.GONE );
        }

        int layout_Code=getIntent().getIntExtra( "layout_code",-1 );

        if(layout_Code==1){

            long count=getIntent().getLongExtra( "count",-1 );
            String uid=getIntent().getStringExtra( "uid" );

            order_count.setText( "Order Count :-" +count );
            clear_area_orders.setVisibility( View.GONE );

            usersModelList = new ArrayList<>( );
            usersAdapter = new UsersAdapter( usersModelList );
            LinearLayoutManager layoutManager = new LinearLayoutManager( this );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            user_recycler.setLayoutManager( layoutManager );
            user_recycler.setAdapter( usersAdapter );
            FirebaseFirestore.getInstance( ).collection( "USERS" ).document( uid ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        usersModelList.add( new UsersModel( task.getResult().get( "store_name" ).toString( ),
                                task.getResult().getId( ),
                                task.getResult().get( "store_address" ).toString( )
                        ) );

                        usersAdapter.notifyDataSetChanged( );

                    }


                }
            } );







        }else {


            area_name = getIntent( ).getStringExtra( "area_name" );
            area_count = getIntent( ).getStringExtra( "count" );
            order_count.setText( "Total Area Order :- " + area_count );

            clear_area_orders.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    Map<String, Object> map = new HashMap<>( );
                    map.put( "orders", "0" );
                    FirebaseFirestore.getInstance( ).collection( "AREAS" ).document( area_name ).update( map )
                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful( )) {
                                        order_count.setText( "Total Area Order :- " + 0 );
                                        Toast.makeText( Users_list.this, "Orders Clear", Toast.LENGTH_SHORT ).show( );
                                    }
                                }
                            } );
                }
            } );


            toolbar.setTitle( "Stores" );
            setSupportActionBar( toolbar );
            toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );


            usersModelList = new ArrayList<>( );
            usersAdapter = new UsersAdapter( usersModelList );
            LinearLayoutManager layoutManager = new LinearLayoutManager( this );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            user_recycler.setLayoutManager( layoutManager );
            user_recycler.setAdapter( usersAdapter );

            FirebaseFirestore.getInstance( ).collection( "USERS" ).orderBy( "store_name" ).get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful( )) {

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {
                            if (area_name.equals( documentSnapshot.get( "area" ).toString( ) )) {
                                usersModelList.add( new UsersModel( documentSnapshot.get( "store_name" ).toString( ),
                                        documentSnapshot.getId( ),
                                        documentSnapshot.get( "store_address" ).toString( )
                                ) );
                            }

                        }
                        usersAdapter.notifyDataSetChanged( );
                    }
                }
            } );
        }

    }
}