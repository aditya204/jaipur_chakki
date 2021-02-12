package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminEditAddress extends AppCompatActivity {

    EditText name,number,whatsapp,details;
    CheckBox home,office;
    private static String type = "";
    Button upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_edit_address );


        name=findViewById( R.id.name_et );
        number=findViewById( R.id.number_et );
        whatsapp=findViewById( R.id.whats_app_no_et );
        details=findViewById( R.id. address_details_et);
        home=findViewById( R.id.address_type_Home );
        office=findViewById( R.id.address_type_office );
        upload=findViewById( R.id. save_address_btn);

        String order_id=getIntent().getStringExtra( "order_id");
        String product_id=getIntent().getStringExtra( "product_id");


        FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).collection( "ORDER_LIST" ).document( product_id ).get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            name.setText( task.getResult().get( "user_name" ).toString() );
                            number.setText( task.getResult().get( "user_phn" ).toString() );
                            whatsapp.setText( task.getResult().get( "user_whatsapp" ).toString() );
                            details.setText( task.getResult().get( "user_address_details" ).toString() );
                           type=( task.getResult().get( "user_address_type" ).toString() );

                           if(type.equals( "HOME" )){
                               office.setChecked( false );
                               home.setChecked( true );
                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                   home.setCompoundDrawableTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                               }
                           }else {
                               home.setChecked( false );
                               office.setChecked( true );
                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                   office.setCompoundDrawableTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                               }
                           }


                        }
                    }
                } );
        home.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (home.isChecked( )) {
                    type = "HOME";
                    office.setChecked( false );
                    home.setChecked( true );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        home.setCompoundDrawableTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                    }
                }
            }
        } );

        office.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (office.isChecked( )) {
                    type = "OFFICE";
                    home.setChecked( false );
                    office.setChecked( true );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        office.setCompoundDrawableTintList( ColorStateList.valueOf( Color.parseColor( "#DF4444" ) ) );
                    }
                }
            }
        } );

        upload.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().isEmpty()||number.getText().toString().isEmpty()||whatsapp.getText().toString().isEmpty()||details.getText().toString().isEmpty()){
                    Toast.makeText( AdminEditAddress.this, "Enter all fields", Toast.LENGTH_SHORT ).show( );

                }else {
                    upload.setVisibility( View.GONE );
                    final Map<String, Object> Data = new HashMap<>( );
                    Data.put( "user_name", name.getText().toString() );
                    Data.put( "user_phn", number.getText().toString() );
                    Data.put( "user_whatsapp", whatsapp.getText().toString() );
                    Data.put( "user_address_details", details.getText().toString() );
                    Data.put( "user_address_type", type );

                    FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).collection( "ORDER_LIST" ).orderBy( "product_id" ).get()
                            .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                            String id =documentSnapshot.getId();
                                            FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).collection( "ORDER_LIST" ).document( id )
                                                    .update( Data ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText( AdminEditAddress.this, "Address Changed", Toast.LENGTH_SHORT ).show( );
                                                        Intent intent=new Intent( AdminEditAddress.this,OrderDetails.class );
                                                        intent.putExtra( "order_id",order_id );
                                                        intent.putExtra( "product_id",product_id );
                                                        startActivity( intent );
                                                        finish();
                                                    }

                                                }
                                            } );

                                        }



                                    }
                                }
                            } );
                }

            }
        } );





    }
}