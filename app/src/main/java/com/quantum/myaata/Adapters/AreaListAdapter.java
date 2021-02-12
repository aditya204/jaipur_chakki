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
import com.quantum.myaata.Signup;
import com.quantum.myaata.Users_list;

import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.ViewHolder> {
    List<AreaModel> areaModelList;

    public AreaListAdapter(List<AreaModel> areaModelList) {
        this.areaModelList = areaModelList;
    }

    @NonNull
    @Override
    public AreaListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.area_item, parent, false );
        return new ViewHolder( view );    }

    @Override
    public void onBindViewHolder(@NonNull AreaListAdapter.ViewHolder holder, int position) {
        String Nmae=areaModelList.get( position ).getName();

        holder.name.setText( Nmae );
        holder.setName( Nmae );
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
            count.setVisibility( View.GONE );

        }
        private  void  setName(String Nmae){
            name.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent( itemView.getContext(), Signup.class );
                    intent.putExtra( "area_name",Nmae );
                    itemView.getContext().startActivity(intent);
                }
            } );
        }
    }
}
