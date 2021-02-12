package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Adapters.AreaAdapter;
import com.quantum.myaata.Adapters.AreaListAdapter;
import com.quantum.myaata.Models.AreaModel;

import java.util.ArrayList;
import java.util.List;

public class AraeList extends AppCompatActivity {
    private RecyclerView area_list_recycler;
    AreaListAdapter areaListAdapter;
    List<AreaModel> areaModelList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_arae_list );

        area_list_recycler=findViewById( R.id.area_list_recycler );

        areaModelList=new ArrayList<>(  );
        areaListAdapter=new AreaListAdapter( areaModelList );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        area_list_recycler.setLayoutManager( layoutManager );
        area_list_recycler.setAdapter( areaListAdapter );
        FirebaseFirestore.getInstance().collection( "AREAS" ).orderBy( "name" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        areaModelList.add( new AreaModel( documentSnapshot.get( "name" ).toString(),
                                documentSnapshot.get( "orders" ).toString()
                        ) );
                    }
                    areaListAdapter.notifyDataSetChanged();

                }
            }
        } );
    }
}