package com.quantum.myaata.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quantum.myaata.DBquaries;
import com.quantum.myaata.Home;
import com.quantum.myaata.Models.UsersModel;
import com.quantum.myaata.Orders;
import com.quantum.myaata.R;
import com.quantum.myaata.UsersDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<UsersModel> usersModelList;

    public UsersAdapter(List<UsersModel> usersModelList) {
        this.usersModelList = usersModelList;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.users_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        String id=usersModelList.get( position ).getUser_id();
        String name=usersModelList.get( position ).getName();
        String address=usersModelList.get( position ).getAddress();



        holder.setData( name,address,id );

    }

    @Override
    public int getItemCount() {
        return usersModelList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,address;
        Button addCommission,send_commission;
        LinearLayout commission_ll,users_ll;
        EditText commission,user_address;


        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            name=itemView.findViewById( R.id.store_name );
            address=itemView.findViewById( R.id.user_store_address );
            addCommission=itemView.findViewById( R.id.add_commission );
            commission_ll=itemView.findViewById( R.id.commission_ll );
            commission=itemView.findViewById( R.id.extra_commison_et );
            user_address=itemView.findViewById( R.id.user_details );
            send_commission=itemView.findViewById( R.id.send_commission );
            users_ll=itemView.findViewById( R.id.user_recycler_item_LL );




        }

        public void setData(String Name,String Adderss,String user_id){

            name.setText( Name );
            address.setText( Adderss );

            users_ll.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent( itemView.getContext(), UsersDetails.class );
                    intent.putExtra( "id",user_id );
                    itemView.getContext().startActivity(intent);
                }
            } );
            addCommission.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {

                    if(DBquaries.is_franchise_Admin){
                        Toast.makeText( itemView.getContext(), "Only for main Admin", Toast.LENGTH_SHORT ).show( );
                    }else {
                        if(commission_ll.getVisibility()==View.VISIBLE){
                            commission_ll.setVisibility( View.GONE );
                        }else {
                            commission_ll.setVisibility( View.VISIBLE );

                        }
                    }

                }
            } );


            send_commission.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    String Commmission=commission.getText().toString();
                    String ADD=user_address.getText().toString();

                    if(Commmission.isEmpty()||ADD.isEmpty()){
                        Toast.makeText( itemView.getContext(), "Enter All Fields", Toast.LENGTH_SHORT ).show( );
                    }else {

                        Date dNow = new Date( );
                        SimpleDateFormat ft = new SimpleDateFormat( "yyMMddhhmmssMs" );
                        String Id = ft.format( dNow );

                        SimpleDateFormat ft1 = new SimpleDateFormat( "yy/MM/dd hh:mm" );
                        String datetime = ft1.format( dNow );

                        Map<String,Object> map=new HashMap<>(  );
                        map.put( "id",Id );
                        map.put( "commission",Commmission );
                        map.put( "user_adderss",ADD );
                        map.put( "time",datetime );

                        final Map<String, Object> Commission_map = new HashMap<>( );
                        Commission_map.put( "commission", String.valueOf( DBquaries.MY_COMMISSION + Integer.parseInt( Commmission )  ) );
                        Commission_map.put( "total_commission",String.valueOf( DBquaries.TOTAL_COMMISSION+Integer.parseInt( Commmission )) );

                        FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).update(Commission_map).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        } );
                        

                        FirebaseFirestore.getInstance().collection( "USERS" ).document( user_id ).collection( "COMMISSION" )
                                .document( Id ).set(map ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    commission_ll.setVisibility( View.GONE );
                                    Toast.makeText( itemView.getContext(), "Commission Added", Toast.LENGTH_SHORT ).show( );
                                }
                            }
                        } );
                    }

                }
            } );

        }
    }
}
