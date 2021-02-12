package com.quantum.myaata.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.quantum.myaata.AdminOrderProduct;
import com.quantum.myaata.DBquaries;
import com.quantum.myaata.Models.AdminOrderModel;
import com.quantum.myaata.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminOrderAdaptrer extends RecyclerView.Adapter<AdminOrderAdaptrer.ViewHolder> {

    private List<AdminOrderModel> adminOrderModelList;

    public AdminOrderAdaptrer(List<AdminOrderModel> adminOrderModelList) {
        this.adminOrderModelList = adminOrderModelList;
    }

    @NonNull
    @Override
    public AdminOrderAdaptrer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.admin_pending_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderAdaptrer.ViewHolder holder, int position) {
        String id=adminOrderModelList.get( position ).getOrder_id();
        String time=adminOrderModelList.get( position ).getTime();
        String otp=adminOrderModelList.get( position ).getOtp();
        String total=adminOrderModelList.get( position ).getGrandTotal();
        boolean is_paid=adminOrderModelList.get( position ).isIs_paid();
        boolean is_pickup=adminOrderModelList.get( position ).isIs_pickup();
        String store_id=adminOrderModelList.get( position ).getStore_id();

        String store_name=adminOrderModelList.get( position ).getStore_name();
        String store_address=adminOrderModelList.get( position ).getStore_address();
        String totalCommision=adminOrderModelList.get( position ).getTotal_commission();


        holder.setDate( id,time,total,is_paid ,otp,is_pickup,store_name,store_address,totalCommision,store_id);


    }

    @Override
    public int getItemCount() {
       return   adminOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView order_id,date,time,price,paymentStatus_txt,otp,store_name,store_address,total_commission;
        private CheckBox is_paid_check;
        private TextView is_pickUptxt;


        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            order_id=itemView.findViewById( R.id.admin_pending_order_id );
            date=itemView.findViewById( R.id.admin_pending_order_date );
            time=itemView.findViewById( R.id.admin_pending_order_time );
            price=itemView.findViewById( R.id.admin_pending_order_grand_total );
            paymentStatus_txt=itemView.findViewById( R.id.textView73 );
            is_paid_check=itemView.findViewById( R.id.admin_pending_order_paid_check );
            otp=itemView.findViewById( R.id.otptxt);
            is_pickUptxt=itemView.findViewById( R.id.pickUptxt );
            store_name=itemView.findViewById( R.id.store_name );
            store_address=itemView.findViewById( R.id.store_address );
            total_commission=itemView.findViewById( R.id.textView6 );



        }

        private void setDate(final String id, String Time, String Price, final boolean is_paid, String Otp, boolean is_pickUp,String storeName,String storeAddress,String Commission,String store_id){


            order_id.setText( id );
            otp.setText( Otp );
            price.setText( "₹"+Price );
            date.setText( Time.substring( 0,8 ) );
            time.setText( Time.substring( 8 ));
            store_name.setText( storeName );
            store_address.setText( storeAddress );
            total_commission.setText( "₹ " +Commission );

            if(is_pickUp){
                is_pickUptxt.setVisibility( View.VISIBLE ); }

            if(is_paid){
                paymentStatus_txt.setVisibility( View.GONE );
                is_paid_check.setVisibility( View.GONE );
            }
            otp.setOnClickListener( new View.OnClickListener( ) {
                @Override
          public void onClick(View view) {

                Intent intent=new Intent( itemView.getContext(), AdminOrderProduct.class );
                intent.putExtra( "order_id",id );
                itemView.getContext().startActivity( intent );

                }
            } );

            is_paid_check.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener( ) {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (is_paid_check.isChecked( )) {

                        final Map<String, Object> ISPAID = new HashMap<>( );
                        ISPAID.put( "is_paid", true );

                        final Map<String, Object> Commission_map = new HashMap<>( );
                        Commission_map.put( "commission", String.valueOf( DBquaries.MY_COMMISSION + Integer.parseInt( Commission )  ) );
                        Commission_map.put( "total_commission",String.valueOf( DBquaries.TOTAL_COMMISSION+Integer.parseInt( Commission )) );

                        FirebaseFirestore.getInstance().collection( "USERS" ).document( store_id ).update(Commission_map).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        } );
                        FirebaseFirestore.getInstance().collection( "ORDERS" ).document(id).update( ISPAID ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    FirebaseFirestore.getInstance().collection( "ORDERS" ).document(id).collection( "ORDER_LIST").orderBy( "product_id" )
                                            .get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){


                                                for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                                                    Date dNow = new Date( );
                                                    SimpleDateFormat ft = new SimpleDateFormat( "dd MMM yy" );
                                                    String datetime = ft.format( dNow );


                                                    Map<String, Object> UpdateStat = new HashMap<>( );
                                                    UpdateStat.put( "is_paid", true );
                                                    UpdateStat.put( "delivery_status", true );
                                                    UpdateStat.put( "delivery_time", datetime );



                                                    String pid=documentSnapshot.get( "product_id" ).toString();
                                                    FirebaseFirestore.getInstance().collection( "ORDERS" ).document(id).collection( "ORDER_LIST").document(pid).update( UpdateStat ).
                                                            addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            } );



                                                }



                                            }
                                        }
                                    } );


                                }
                            }
                        } );



                    } else {
                        is_paid_check.setChecked( true );

                    }
                }
            } );


        }
    }
}
