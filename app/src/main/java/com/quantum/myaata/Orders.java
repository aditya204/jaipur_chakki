package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyOrderGroceryAdapter myOrderGroceryAdapter;
    private List<MyOrderGroceryModel> myOrderGroceryModelList;
    private LinearLayout noItemLL;
    private Dialog loadingDialog;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        id=getIntent().getStringExtra( "id" );
        setContentView( R.layout.activity_orders );


        toolbar = findViewById( R.id.toolbar_grocery_Orders );
        recyclerView = findViewById( R.id.mr_ordrer_grocery_recycler );
        noItemLL = findViewById( R.id.no_item_layout );

        loadingDialog = new Dialog( this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        loadingDialog.show( );

        toolbar.setTitle( "My Orders" );
        setSupportActionBar( toolbar );
        toolbar.setTitleTextColor( Color.parseColor( "#FFFFFF" ) );
        getSupportActionBar( ).setDisplayShowHomeEnabled( true );
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );




        myOrderGroceryModelList = new ArrayList<>( );

        myOrderGroceryAdapter = new MyOrderGroceryAdapter( myOrderGroceryModelList );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( myOrderGroceryAdapter );

        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( id )
                .collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful( )) {

                    long size = (long) task.getResult( ).get( "list_size" );

                    if (size == 0) {
                        loadingDialog.dismiss( );
                        noItemLL.setVisibility( View.VISIBLE );

                    }
                    for (long x =0; x<size; x++) {
                        final String order_id = task.getResult( ).get( "order_id_"+x ).toString( );

                        FirebaseFirestore.getInstance( ).collection( "ORDERS" ).document( order_id ).collection( "ORDER_LIST" ).orderBy( "product_id" )
                                .get( ).addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful( )) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult( )) {

                                        myOrderGroceryModelList.add( new MyOrderGroceryModel(
                                                documentSnapshot.get( "name" ).toString( ),
                                                documentSnapshot.get( "commission" ).toString( ),
                                                documentSnapshot.get( "image" ).toString( ),
                                                documentSnapshot.get( "description" ).toString( ),
                                                order_id,
                                                documentSnapshot.get( "product_id" ).toString( ),
                                                (boolean) documentSnapshot.get( "is_cancled" ),
                                                (boolean) documentSnapshot.get( "delivery_status" ),
                                                documentSnapshot.get( "otp" ).toString( )


                                        ) );


                                        loadingDialog.dismiss( );

                                    }
                                    myOrderGroceryAdapter.notifyDataSetChanged( );
                                }
                            }
                        } );


                    }
                    loadingDialog.dismiss( );


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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId( ) == android.R.id.home) {
            finish( );
        }

        return super.onOptionsItemSelected( item );
    }

    public static class MyOrderGroceryModel {

        private String name, deliveryStat, description, product_id, order_id, otp;
        private String image;
        private boolean is_cancled, is_deliverd;

        public MyOrderGroceryModel(String name, String deliveryStat, String image, String description, String order_id, String product_id,boolean is_cancled, boolean is_deliverd, String otp) {
            this.name = name;
            this.deliveryStat = deliveryStat;
            this.image = image;
            this.description = description;
            this.order_id = order_id;
            this.product_id = product_id;

            this.is_cancled = is_cancled;
            this.is_deliverd = is_deliverd;
            this.otp = otp;

        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public boolean isIs_cancled() {
            return is_cancled;
        }

        public void setIs_cancled(boolean is_cancled) {
            this.is_cancled = is_cancled;
        }

        public boolean isIs_deliverd() {
            return is_deliverd;
        }

        public void setIs_deliverd(boolean is_deliverd) {
            this.is_deliverd = is_deliverd;
        }



        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDeliveryStat() {
            return deliveryStat;
        }

        public void setDeliveryStat(String deliveryStat) {
            this.deliveryStat = deliveryStat;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public static class MyOrderGroceryAdapter extends RecyclerView.Adapter<MyOrderGroceryAdapter.ViewHolder> {


        private List<MyOrderGroceryModel> myOrderGroceryModelList;

        public MyOrderGroceryAdapter(List<MyOrderGroceryModel> myOrderGroceryModelList) {
            this.myOrderGroceryModelList = myOrderGroceryModelList;
        }

        @NonNull
        @Override
        public MyOrderGroceryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.my_order_grocery_item, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull MyOrderGroceryAdapter.ViewHolder holder, int position) {
            String name = myOrderGroceryModelList.get( position ).getName( );
            String DeliceryStatus = myOrderGroceryModelList.get( position ).getDeliveryStat( );
            String image = myOrderGroceryModelList.get( position ).getImage( );
            String description = myOrderGroceryModelList.get( position ).getDescription( );
            String order_id = myOrderGroceryModelList.get( position ).getOrder_id( );
            String productId = myOrderGroceryModelList.get( position ).getProduct_id( );

            boolean is_Deliverd = myOrderGroceryModelList.get( position ).isIs_deliverd( );
            boolean is_Cancled = myOrderGroceryModelList.get( position ).isIs_cancled( );
            String otp = myOrderGroceryModelList.get( position ).getOtp( );


            holder.setDAta( name, image, description, order_id, productId, otp );
            holder.setdeliveryStat( is_Cancled, is_Deliverd, DeliceryStatus );

        }

        @Override
        public int getItemCount() {
            return myOrderGroceryModelList.size( );
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView deliveryStatus, name;
            private ImageView image;
            private ConstraintLayout constraintLayout;
            private TextView blueDot, greenDot, RedDot, description, otp_txt;


            public ViewHolder(@NonNull View itemView) {
                super( itemView );

                image = itemView.findViewById( R.id.myOrder_groceryItemImage );
                deliveryStatus = itemView.findViewById( R.id.myOrder_groceryItemDeliveryStatus );
                name = itemView.findViewById( R.id.myOrder_groceryItemName );
                constraintLayout = itemView.findViewById( R.id.myOrder_groceryLayout );
                description = itemView.findViewById( R.id.myOrder_groceryItemDescription );
                blueDot = itemView.findViewById( R.id.myOrder_groceryItem_BlueDot );
                greenDot = itemView.findViewById( R.id.myOrder_groceryItem_GreenDot );
                RedDot = itemView.findViewById( R.id.myOrder_groceryItem_RedDot );


                otp_txt = itemView.findViewById( R.id.otp_txt );


            }


            public void setDAta(String Name, String res, String desc, final String Oid, final String Pid, String Otp) {

                Glide.with( itemView.getContext( ) ).load( res ).into( image );


                otp_txt.setText( Otp );
                name.setText( Name );
                description.setText( desc );

                constraintLayout.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent( itemView.getContext( ), OrderDetails.class );
                        intent.putExtra( "order_id", Oid );
                        intent.putExtra( "product_id", Pid );
                        itemView.getContext( ).startActivity( intent );


                    }
                } );



            }


            public void setdeliveryStat(boolean is_cancled, boolean is_deliverd, String deliStat) {


                if (is_cancled) {
                    RedDot.setVisibility( View.VISIBLE );
                    deliveryStatus.setText( "Cancelled" );
                    deliveryStatus.setTextColor( Color.parseColor( "#DF4444" ) );
                } else {
                    if (is_deliverd) {
                        greenDot.setVisibility( View.VISIBLE );
                        deliveryStatus.setText( "Delivered on :" + deliStat );

                    } else {
                        blueDot.setVisibility( View.VISIBLE );
                        deliveryStatus.setText( "Commission per kg : ₹" + deliStat  );
                    }

                }


            }
        }





    }



}