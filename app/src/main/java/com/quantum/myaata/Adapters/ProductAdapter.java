package com.quantum.myaata.Adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quantum.myaata.AddProduct;
import com.quantum.myaata.Cart;
import com.quantum.myaata.DBquaries;
import com.quantum.myaata.Models.ProductModel;
import com.quantum.myaata.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<ProductModel> productModelList;

    public ProductAdapter(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.grocery_cart_product_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        String name=productModelList.get( position ).getName();
        String image=productModelList.get( position ).getImage();
        String price=productModelList.get( position ).getPrice();
        String descrip=productModelList.get( position ).getDescriptin();
        String id=productModelList.get( position ).getProduct_id();
        String commission=productModelList.get( position ).getCommision();
        holder.setDate( name,price,descrip,commission,image,id );

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price,decri,commision;
        ImageView image;
        Button addtoCart,goto_cart;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            name=itemView.findViewById( R.id.grocery_cart_product_title );
            price=itemView.findViewById( R.id.grocery_cart_productPrice );
            decri=itemView.findViewById( R.id.grocery_cart_productDescription );
            commision=itemView.findViewById( R.id.grocery_cart_productcommision );
            image=itemView.findViewById( R.id.grocery_cart_productImage );
            addtoCart=itemView.findViewById( R.id.add_product_btn );
            goto_cart=itemView.findViewById( R.id.goto_cart_product_btn );
            constraintLayout=itemView.findViewById( R.id.grocery_cart_product_item_Layout );


        }

        private void setDate(String Name,String Price,String Desc,String Comm,String Image,String product_id){

            if(Image.isEmpty()){
                image.setImageResource( R.drawable.images );
            }else {
                Glide.with( itemView.getContext() ).load( Image ).into( image );
            }

            if(DBquaries.IS_ADMIN||DBquaries.is_franchise_Admin){
                addtoCart.setVisibility( View.GONE );
                goto_cart.setVisibility( View.GONE );

                constraintLayout.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        if(DBquaries.IS_ADMIN) {
                            Intent intent = new Intent( itemView.getContext( ), AddProduct.class );
                            intent.putExtra( "layout_code", 1 );
                            intent.putExtra( "product_id", product_id );
                            itemView.getContext( ).startActivity( intent );
                        }
                    }
                } );
            }else {


                    if(DBquaries.grocery_CartList_product_id.contains( product_id )){
                        addtoCart.setVisibility( View.GONE );
                        goto_cart.setVisibility( View.VISIBLE );
                    }else {
                        addtoCart.setVisibility( View.VISIBLE );
                        goto_cart.setVisibility( View.GONE );
                    }


            }




            name.setText( Name );
            price.setText("₹ "+Price+"/-" );
            decri.setText( Desc );
            commision.setText( "Commission per kg :- ₹ "+Comm );



            goto_cart.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity( new Intent( itemView.getContext(), Cart.class) );
                }
            } );
            addtoCart.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View view) {
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null){

                        addtoCart.setClickable( false );
                        final Map<String, Object> updateListSize = new HashMap<>( );
                        updateListSize.put( "list_size", (long) DBquaries.grocery_CartList_product_id.size( ) + 1 );

                        Map<String, Object> product_Id = new HashMap<>( );
                        product_Id.put( "id_" + (long) DBquaries.grocery_CartList_product_id.size( ), product_id );

                        FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).
                                update( product_Id ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                            .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" ).
                                            update( updateListSize ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                DBquaries.grocery_CartList_product_id.add( product_id );
                                                addtoCart.setVisibility( View.GONE );
                                                goto_cart.setVisibility( View.VISIBLE );
                                                Toast.makeText( itemView.getContext(), "Added to cart", Toast.LENGTH_SHORT ).show( );
                                                Map<String, Object> Count = new HashMap<>( );
                                                Count.put( product_id,1 );

                                                FirebaseFirestore.getInstance( ).collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                                        .collection( "USER_DATA" ).document( "MY_GROCERY_CARTITEMCOUNT" ).
                                                        update( Count ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                } );

                                            }

                                        }
                                    } );

                                }
                            }
                        } );
                    }
                }
            } );
        }
    }
}
