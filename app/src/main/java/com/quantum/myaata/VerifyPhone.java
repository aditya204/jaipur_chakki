package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    String verificarionCodeBySystem;
    EditText code;
    Button verify;
    private Dialog loadingDialog;
    String phoneNo = "";
    String full_name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_verify_phone );

        loadingDialog = new Dialog( VerifyPhone.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show();

        phoneNo = getIntent( ).getStringExtra( "phone" );

        verify = findViewById( R.id.verify );
        code = findViewById( R.id.enter_otp );


        verify.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                if(code.getText().toString().length()==6){
                    verifyCode( code.getText( ).toString( ) );
                }else {
                    Toast.makeText( VerifyPhone.this, "Check code Again", Toast.LENGTH_SHORT ).show( );
                }


            }
        } );
        sendVerificationCode( phoneNo );

    }


    private void sendVerificationCode(String PhoneNo) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder( FirebaseAuth.getInstance( ) )
                        .setActivity( this )
                        .setPhoneNumber( "+91" + PhoneNo )
                        .setTimeout( 60L, TimeUnit.SECONDS )
                        .setCallbacks( mCallbacks )
                        .build( ) );



    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks( ) {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent( s, forceResendingToken );

            loadingDialog.dismiss();

            verificarionCodeBySystem = s;


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String Code = phoneAuthCredential.getSmsCode( );
            if (Code != null) {
                verifyCode( Code );
                code.setText( Code );

            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText( VerifyPhone.this, e.getMessage( ), Toast.LENGTH_SHORT ).show( );
        }
    };

    private void verifyCode(String verificationCode) {
        loadingDialog.show( );
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential( verificarionCodeBySystem, verificationCode );
        SigninUserByCredential( credential );

    }

    private void SigninUserByCredential(PhoneAuthCredential credential) {



        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance( );

        firebaseAuth.signInWithCredential( credential ).addOnCompleteListener( VerifyPhone.this, new OnCompleteListener<AuthResult>( ) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful( )) {
                    FirebaseUser user = FirebaseAuth.getInstance( ).getCurrentUser( );
                    if (user != null) {
                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( user.getUid( ) ).get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult( );

                                if (documentSnapshot.exists( )) {
                                    loadingDialog.dismiss( );
                                    Intent intent=new Intent( VerifyPhone.this,Home.class );
                                    startActivity( intent );
                                    finish();

                                } else {

                                    FirebaseFirestore.getInstance( ).collection( "FRANCHISE_ADMINS" ).document( user.getUid( ) ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult( );

                                            if(documentSnapshot.exists()){



                                            }else {
                                                loadingDialog.dismiss( );
                                                Intent intent=new Intent( VerifyPhone.this,AraeList.class );
                                                startActivity( intent );
                                                finish();
                                            }

                                        }
                                    } );


                                }


                            }
                        } );


                    } else {
                        Toast.makeText( VerifyPhone.this, "Please Try Again", Toast.LENGTH_SHORT ).show( );
                        finish( );
                    }






                 /*   firebaseAuth.createUserWithEmailAndPassword(mail,password  ).addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){




                            }else {
                                loadingDialog.dismiss();

                                firebaseAuth.signOut();
                                Toast.makeText( VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show( );
                            }
                        }
                    } );*/


                } else {
                    loadingDialog.dismiss( );
                    Toast.makeText( VerifyPhone.this, task.getException( ).getMessage( ), Toast.LENGTH_SHORT ).show( );
                }
            }
        } );
    }
}