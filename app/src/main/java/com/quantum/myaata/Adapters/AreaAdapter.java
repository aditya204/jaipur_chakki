package com.quantum.myaata.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quantum.myaata.Models.AreaModel;
import com.quantum.myaata.R;
import com.quantum.myaata.Users_list;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    List<AreaModel> areaModelList;

    public AreaAdapter(List<AreaModel> areaModelList) {
        this.areaModelList = areaModelList;
    }


    @NonNull
    @Override
    public AreaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.area_item, parent, false );
        return new ViewHolder( view );
    }



    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.ViewHolder holder, int position) {

        String Nmae=areaModelList.get( position ).getName();
        String Count=areaModelList.get( position ).getOrder();

        holder.name.setText( Nmae );
        holder.count.setText( Count );
        holder.setName( Nmae ,Count);



    }

    @Override
    public int getItemCount() {
        return areaModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,count;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            name=itemView.findViewById( R.id.area_txt );
            count=itemView.findViewById( R.id.order_count_txt );




        }
        private  void  setName(String Nmae,String Count){
            name.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent( itemView.getContext(), Users_list.class );
                    intent.putExtra( "area_name",Nmae );
                    intent.putExtra( "count",Count );
                    itemView.getContext().startActivity(intent);
                }
            } );
        }
    }
}
