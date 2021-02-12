package com.quantum.myaata.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Models.ExtraCommissionModel;
import com.quantum.myaata.R;

import java.util.ArrayList;
import java.util.List;

public class ExtraCommission extends AppCompatActivity {
    RecyclerView recyclerView;
    ExtraCommissionAdapter extraCommissionAdapter;
    List<ExtraCommissionModel> extraCommissionModelList;
    TextView no_commission_txt;
    private Toolbar toolbar;
    TextView commission,total_commission;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_extra_commission );


        recyclerView=findViewById( R.id.commision_recycler );
        no_commission_txt=findViewById( R.id.no_commission_txt );
        toolbar=findViewById( R.id.ec_toolbar );
        total_commission=findViewById( R.id.total_commission );
        commission=findViewById( R.id.commission );

        toolbar.setTitle( "Extra Commission" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );



        extraCommissionModelList=new ArrayList<>(  );
        extraCommissionAdapter = new ExtraCommissionAdapter( extraCommissionModelList );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( extraCommissionAdapter );

        FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                .collection( "COMMISSION" ).orderBy( "id" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        extraCommissionModelList.add( new ExtraCommissionModel(
                                documentSnapshot.get("user_adderss").toString(),
                                documentSnapshot.get("time").toString(),
                                documentSnapshot.get("commission").toString()

                        ) );

                        if (extraCommissionModelList.size()==0){
                            no_commission_txt.setVisibility( View.VISIBLE );
                            recyclerView.setVisibility( View.INVISIBLE );
                        }else {
                            no_commission_txt.setVisibility( View.INVISIBLE );
                            recyclerView.setVisibility( View.VISIBLE );
                        }
                    }
                    if (extraCommissionModelList.size()==0){
                        no_commission_txt.setVisibility( View.VISIBLE );
                        recyclerView.setVisibility( View.INVISIBLE );
                    }else {
                        no_commission_txt.setVisibility( View.INVISIBLE );
                        recyclerView.setVisibility( View.VISIBLE );
                    }

                    extraCommissionAdapter.notifyDataSetChanged();
                }


            }
        } );

        FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    commission.setText("Commission :- ₹"+ task.getResult().get( "commission" ).toString() );
                    total_commission.setText("Total commission till Now :- "+ task.getResult().get( "total_commission" ).toString());


                }
            }
        } );

    }
}