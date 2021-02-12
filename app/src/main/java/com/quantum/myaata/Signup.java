package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextInputLayout name,adderss,phone,gpay,wapp;
    Button continue_btn;
    private Dialog loadingDialog;
    String area_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );

        firebaseAuth=firebaseAuth.getInstance();

        continue_btn=findViewById( R.id.sign_up );
        name=findViewById( R.id.namelayout );
        adderss=findViewById( R.id.mail_layout );
        phone=findViewById( R.id. password_layout);
        gpay=findViewById( R.id.google_pay_layout );
        wapp=findViewById( R.id.whats_app );

        int layout_code= getIntent().getIntExtra( "layout_code",-1 );
        loadingDialog = new Dialog( Signup.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        if(layout_code==1){
            String user_id=getIntent().getStringExtra( "id" );

            FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    name.getEditText().setText( task.getResult().get( "store_name" ).toString() );
                    adderss.getEditText().setText(  task.getResult().get( "store_address" ).toString() );
                    gpay.getEditText().setText(  task.getResult().get( "google_pay" ).toString() );
                    wapp.getEditText().setText(  task.getResult().get( "whats_app" ).toString() );
                    phone.getEditText().setText( task.getResult().get( "store_phone" ).toString()  );



                }
            } );

            continue_btn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {

                    if(validatPhone() && validatNmae() && validatAddress() && validatGpay()&& validatWapp()){
                        loadingDialog.show();

                        continue_btn.setClickable( false );
                        Map<String, Object> updateuserData = new HashMap<>( );
                        updateuserData.put( "store_name",name.getEditText().getText().toString() );
                        updateuserData.put( "whats_app", wapp.getEditText().getText().toString()  );
                        updateuserData.put( "google_pay", gpay.getEditText().getText().toString()  );
                        updateuserData.put( "store_phone", phone.getEditText().getText().toString()  );
                        updateuserData.put( "store_address", adderss.getEditText().getText().toString()  );


                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( user_id ).update( updateuserData ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent=new Intent( Signup.this,UsersDetails.class );
                                    intent.putExtra( "id",user_id );
                                    startActivity( intent );
                                    finish();

                                }else {
                                    loadingDialog.dismiss();
                                }


                            }
                        } );

                    }

                }
            } );

        }else {
            area_name=getIntent().getStringExtra( "area_name" );

            continue_btn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {

                    if(validatPhone() && validatNmae() && validatAddress() && validatGpay()&& validatWapp()){
                        loadingDialog.show();

                        continue_btn.setClickable( false );
                        Map<String, Object> userData = new HashMap<>( );
                        userData.put( "store_name",name.getEditText().getText().toString() );
                        userData.put( "permanent_phone", phone.getEditText().getText().toString()  );
                        userData.put( "whats_app", wapp.getEditText().getText().toString()  );
                        userData.put( "google_pay", gpay.getEditText().getText().toString()  );
                        userData.put( "phone", ""  );
                        userData.put( "is_franchise", false  );
                        userData.put( "area", area_name  );
                        userData.put( "fullname", ""  );
                        userData.put( "buyer_whatsapp", ""  );
                        userData.put( "store_phone", phone.getEditText().getText().toString()  );
                        userData.put( "store_address", adderss.getEditText().getText().toString()  );
                        userData.put( "previous_position", 0 );
                        userData.put( "address_details", "" );
                        userData.put( "commission", "0" );
                        userData.put( "total_commission", "0" );
                        userData.put( "address_type", "" );

                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( firebaseAuth.getCurrentUser( ).getUid( ) ).set( userData ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Map<String, Object> listSize = new HashMap<>( );
                                    listSize.put( "list_size", 0 );
                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( firebaseAuth.getCurrentUser( ).getUid( ) ).collection( "USER_DATA" ).document( "MY_ADDRESS" ).set( listSize )
                                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            } );
                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( firebaseAuth.getCurrentUser( ).getUid( ) ).collection( "USER_DATA" ).document( "MY_GROCERY_CARTITEMCOUNT" ).set( listSize )
                                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            } );
                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( firebaseAuth.getCurrentUser( ).getUid( ) ).collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).set( listSize )
                                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            } );
                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( firebaseAuth.getCurrentUser( ).getUid( ) ).collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).set( listSize )
                                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    continue_btn.setClickable( true );
                                                    Intent intent=new Intent( Signup.this,Home.class );
                                                    startActivity( intent );
                                                    loadingDialog.dismiss();
                                                    finish();
                                                    DBquaries.chechAdmin( );
                                                    DBquaries.checkAreaOrder();
                                                    DBquaries.loadGroceryOrders();
                                                    DBquaries.loadNumbers();
                                                    DBquaries.loadCommissions();


                                                }
                                            } );
                                }else {
                                    loadingDialog.dismiss();
                                }


                            }
                        } );

                    }

                }
            } );

        }


















    }
    private boolean validatNmae(){
        String NAME=name.getEditText().getText().toString();
        name.setErrorEnabled( true );

        if(NAME.isEmpty()){
            name.setError( "Can't be empty " );

            return false;
        }else
            name.setErrorEnabled( false );
        return  true;



    }
    private boolean validatAddress(){
        String NAME=adderss.getEditText().getText().toString();
        adderss.setErrorEnabled( true );

        if(NAME.isEmpty()){
            adderss.setError( "Can't be empty " );

            return false;
        }else
            adderss.setErrorEnabled( false );
        return  true;



    }



    private boolean validatPhone(){
        String NAME=phone.getEditText().getText().toString();
        phone.setErrorEnabled( true );

        if(NAME.isEmpty()){
            phone.setError( "Can't be empty " );

            return false;
        }else{
            if(NAME.length()!=10){
                phone.setError( "Enter a valid Phone number " );
                return false;
            }else {
                phone.setErrorEnabled( false );
                return  true;

            }


        }

    }

    private boolean validatGpay(){
        String NAME=gpay.getEditText().getText().toString();
        gpay.setErrorEnabled( true );

        if(NAME.isEmpty()){
            gpay.setError( "Can't be empty " );

            return false;
        }else{
            if(NAME.length()!=10){
                gpay.setError( "Enter a valid Phone number " );
                return false;
            }else {
                gpay.setErrorEnabled( false );
                return  true;

            }


        }

    }

    private boolean validatWapp(){
        String NAME=wapp.getEditText().getText().toString();
        wapp.setErrorEnabled( true );

        if(NAME.isEmpty()){
            wapp.setError( "Can't be empty " );

            return false;
        }else{
            if(NAME.length()!=10){
                wapp.setError( "Enter a valid Phone number " );
                return false;
            }else {
                wapp.setErrorEnabled( false );
                return  true;

            }


        }

    }




}