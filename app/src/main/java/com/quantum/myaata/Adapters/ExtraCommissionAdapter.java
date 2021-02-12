package com.quantum.myaata.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quantum.myaata.DBquaries;
import com.quantum.myaata.Models.ExtraCommissionModel;
import com.quantum.myaata.R;

import java.util.List;

public class ExtraCommissionAdapter extends RecyclerView.Adapter<ExtraCommissionAdapter.ViewHolder> {
    List<ExtraCommissionModel> extraCommissionModelList;

    public ExtraCommissionAdapter(List<ExtraCommissionModel> extraCommissionModelList) {
        this.extraCommissionModelList = extraCommissionModelList;
    }

    @NonNull
    @Override
    public ExtraCommissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.extra_commisson_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraCommissionAdapter.ViewHolder holder, int position) {

        String details=extraCommissionModelList.get( position ).getDetails();
        String time=extraCommissionModelList.get( position ).getTime();
        String comm=extraCommissionModelList.get( position ).getCommission();

        holder.setData( time,comm,details );
    }

    @Override
    public int getItemCount() {
        return extraCommissionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time,comm,details;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            time=itemView.findViewById( R.id.exc_time );
            comm=itemView.findViewById( R.id.exc_commission );
            details=itemView.findViewById( R.id.exc_details_user );

        }

        public void setData(String Toime,String Comm,String Details){
            time.setText( Toime );
            comm.setText( "₹ "+Comm );
            details.setText( Details );
        }
    }
}
