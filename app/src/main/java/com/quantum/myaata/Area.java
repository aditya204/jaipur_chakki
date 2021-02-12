package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.Adapters.AddressAdapter;
import com.quantum.myaata.Adapters.AreaAdapter;
import com.quantum.myaata.Models.AreaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Area extends AppCompatActivity {
    RecyclerView recyclerView;
    AreaAdapter areaAdapter;
    List<AreaModel> areaModelList;
    LinearLayout add_area_ll,llareatxt;
    TextView add_Area_txt;
    Button add_area_btn;
    EditText area_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_area );


        add_area_ll=findViewById( R.id.add_area_ll );
        add_Area_txt=findViewById( R.id.add_area_txt );
        add_area_btn=findViewById( R.id.add_area_btn );
        area_et=findViewById( R.id.add_area_et );
        recyclerView=findViewById( R.id.area_recycler );
        llareatxt=findViewById( R.id.llareatxt );


        add_Area_txt.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                add_area_ll.setVisibility( View.VISIBLE );
            }
        } );


        areaModelList=new ArrayList<>(  );
        areaAdapter=new AreaAdapter( areaModelList );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( areaAdapter );

        int layout_code=getIntent().getIntExtra( "layout_code",-1 );
        if(layout_code==1){
            llareatxt.setVisibility( View.GONE );
            FirebaseFirestore.getInstance().collection( "AREAS" ).document( DBquaries.user_area ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        areaModelList.add( new AreaModel( task.getResult().get( "name" ).toString(),
                                task.getResult().get( "orders" ).toString()
                        ) );

                    }
                    areaAdapter.notifyDataSetChanged();


                }
            } );

        }else {
            FirebaseFirestore.getInstance().collection( "AREAS" ).orderBy( "name" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            areaModelList.add( new AreaModel( documentSnapshot.get( "name" ).toString(),
                                    documentSnapshot.get( "orders" ).toString()
                            ) );
                        }
                        areaAdapter.notifyDataSetChanged();

                    }
                }
            } );

        }



        add_area_btn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String name=area_et.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText( Area.this, "Can not add Empty Area", Toast.LENGTH_SHORT ).show( );

                }else {
                    Map<String,Object> add=new HashMap<>(  );
                    add.put( "name",name );
                    add.put( "orders","0");

                    FirebaseFirestore.getInstance().collection( "AREAS" ).document( name ).set( add )
                            .addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        onRestart();
                                    }
                                }
                            } );

                }


            }
        } );





    }

    @Override
    public void onRestart() {
        super.onRestart( );
        finish( );
        startActivity( getIntent( ) );
    }
}