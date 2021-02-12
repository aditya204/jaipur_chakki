package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Button login;
    EditText phone_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        login = findViewById( R.id.login );
        phone_et = findViewById( R.id.phone_edit_txt );

        FirebaseUser user = FirebaseAuth.getInstance( ).getCurrentUser( );
        if (user != null) {
            DBquaries.loadGroceryCartList( MainActivity.this );
            startActivity( new Intent( MainActivity.this, Home.class ) );
            finish();
        }


        login.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (phone_et.getText( ).toString( ).length( ) == 10) {
                    Intent intent = new Intent( MainActivity.this, VerifyPhone.class );
                    intent.putExtra( "phone", phone_et.getText( ).toString( ) );
                    startActivity( intent );
                } else {
                    Toast.makeText( MainActivity.this, "Enter a valid Phone Number", Toast.LENGTH_SHORT ).show( );
                }

            }
        } );
    }
}