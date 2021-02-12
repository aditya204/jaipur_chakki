package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersDetails extends AppCompatActivity {

    TextView name,addreaa,phone,gpay,wapp,edit,commission,total_commission;
    Button order,clear,make_franchise;
    Boolean is_Franchise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_users_details );

        name=findViewById( R.id.user_name );
        addreaa=findViewById( R.id.textView11 );
        phone=findViewById( R.id.textView14 );
        gpay=findViewById( R.id.textView17 );
        wapp=findViewById( R.id.textView16 );
        edit=findViewById( R.id.textView18 );
        clear=findViewById( R.id.clear_commission );
        order=findViewById( R.id.see_orders );
        total_commission=findViewById( R.id.total_commission );
        commission=findViewById( R.id.commission );

        make_franchise=findViewById( R.id.make_admin );




       String user_id=getIntent().getStringExtra( "id" );

        FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    name.setText( task.getResult().get( "store_name" ).toString() );
                    addreaa.setText( task.getResult().get( "store_address" ).toString() );
                    phone.setText( "Phone no:- "+task.getResult().get( "permanent_phone" ).toString() );
                    gpay.setText( "Google / phone pay :- "+task.getResult().get( "google_pay" ).toString() );
                    wapp.setText( "whatsApp :- "+task.getResult().get( "whats_app" ).toString() );
                    commission.setText("Commission :- ₹"+ task.getResult().get( "commission" ).toString() );
                    is_Franchise=(boolean)task.getResult().get( "is_franchise" );
                    total_commission.setText("Total commission till Now :- "+ task.getResult().get( "total_commission" ).toString());

                    if(is_Franchise){
                        make_franchise.setText( "Remove From Admin" );
                        make_franchise.setVisibility( View.GONE );
                    } else {
                        make_franchise.setVisibility( View.VISIBLE );
                    }
                }
            }
        } );

        make_franchise.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(is_Franchise){
                    Map<String,Object> map=new HashMap<>(  );
                    map.put( "is_franchise",false );

                    FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).update( map )
                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        is_Franchise=false;
                                        Toast.makeText( UsersDetails.this, "Removed From Admin", Toast.LENGTH_SHORT ).show( );
                                        make_franchise.setText( "Make Franchise Admin" );

                                    }
                                }
                            } );




                }else {
                    Map<String,Object> map=new HashMap<>(  );
                    map.put( "is_franchise",true );

                    FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).update( map )
                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        is_Franchise=true;
                                        Toast.makeText( UsersDetails.this, "Admin Created", Toast.LENGTH_SHORT ).show( );
                                        make_franchise.setText( "Remove From Admin" );

                                    }
                                }
                            } );

                }
            }
        } );

        clear.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                Map<String,Object> map=new HashMap<>(  );
                map.put("commission", "0" );
                FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).update( map ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DBquaries.MY_COMMISSION=0;
                            commission.setText("Commission :- ₹"+ 0 );

                            Toast.makeText( UsersDetails.this, "Cleared", Toast.LENGTH_SHORT ).show( );
                        }
                    }
                } );
            }
        } );

        order.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent( UsersDetails.this, Orders.class );
                intent.putExtra( "id",user_id );
                startActivity(intent);
            }
        } );

        if (DBquaries.is_franchise_Admin){
            edit.setVisibility( View.GONE );
            clear.setVisibility( View.GONE );
        }

        edit.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent( UsersDetails.this, Signup.class );
                intent.putExtra( "layout_code",1 );
                intent.putExtra( "id",user_id );
                startActivity(intent);
            }
        } );


    }
}