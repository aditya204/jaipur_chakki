package com.quantum.myaata;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBquaries {


    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance( );
    ////grocery

    public static List<String> grocery_CartList_product_id = new ArrayList<>( );
    public static List<String> grocery_CartList_product_count = new ArrayList<>( );
    public static List<String> grocery_CartList_product_OutOfStock = new ArrayList<>( );
    public static List<String> grocery_OrderList = new ArrayList<>( );


    public static int PRICE_IN_CART_GROCERY = 0;
    public static int TOTAL_Commission = 0;
    public static int DELIVERY_CHARGES = 0;
    public static int DELIVERY_FREE_ABOVE = 0;
    public static boolean IS_ADMIN = false;
    public static int MIN_ORDER_AMOUNT = 0;
    public static String ADMIN_NO;





    public static List<String> numbers_list = new ArrayList<>( );
    public static List<String> uid_list = new ArrayList<>( );
    public static List<Long> count_list = new ArrayList<>( );
    public static int MY_COMMISSION=0;
    public static int TOTAL_COMMISSION=0;

    public static void loadCommissions(){
        FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    MY_COMMISSION=Integer.parseInt(  task.getResult().get( "commission" ).toString());
                    TOTAL_COMMISSION=Integer.parseInt(  task.getResult().get( "total_commission" ).toString());
                }
            }
        } );
    }

    public static void loadNumbers(){
        numbers_list.clear();
        uid_list.clear();
        count_list.clear();
        FirebaseFirestore.getInstance().collection( "SEARCH" ).orderBy( "phone" ).get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                numbers_list.add(documentSnapshot.get("phone").toString() );
                                uid_list.add(documentSnapshot.get("uid").toString() );
                                count_list.add((long)documentSnapshot.get("count") );

                            }
                        }
                    }
                } );
    }


   public static void loadGroceryOrders() {
        grocery_OrderList.clear( );
        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ) )
                .collection( "USER_DATA" ).document( "MY_GROCERY_ORDERS" ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            long size = (long) task.getResult( ).get( "list_size" );
                            for (long x = 0; x < size; x++) {
                                grocery_OrderList.add( task.getResult( ).get( "order_id_" + x ).toString( ) );
                            }


                        }

                    }
                } );

    }

    public static void loadGroceryCartList(final Context context) {
        grocery_CartList_product_id.clear( );
        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                .get( ).addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful( )) {
                    for (long x = 0; x < (long) task.getResult( ).get( "list_size" ); x++) {

                        String id = task.getResult( ).get( "id_" + x ).toString( );
                        grocery_CartList_product_id.add( id );
                    }
                } else {
                    String error = task.getException( ).getMessage( );
                    Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );
                }
            }
        } );

    }


    public static int area_order=0;
    public static String user_area="";
    public static boolean is_franchise_Admin=false;
    public  static void checkAreaOrder(){
       FirebaseFirestore.getInstance().collection( "USERS" ).document( FirebaseAuth.getInstance().getCurrentUser().getUid() ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                    user_area=task.getResult().get( "area" ).toString();
                    if((boolean)task.getResult().get( "is_franchise" )){
                        is_franchise_Admin=true;
                        Home.admin_ll.setVisibility( View.VISIBLE );
                        Home.users_LL.setVisibility( View.GONE );
                        loadFranchiseOrders( user_area );
                    }else {
                        is_franchise_Admin=false;
                    }

                    FirebaseFirestore.getInstance().collection( "AREAS" ).document( user_area ).get().
                           addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   if(task.isSuccessful()){
                                       area_order=Integer.parseInt( task.getResult().get( "orders" ).toString() );
                                   }
                               }
                           } );
               }
           }
       } );
    }
    public static void calcualtePriceGrocery(String todo, String amount) {
        if (todo.equals( "+" )) {
            PRICE_IN_CART_GROCERY =PRICE_IN_CART_GROCERY + Integer.parseInt( amount );
            Cart.priceIncart.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );

        } else {
            PRICE_IN_CART_GROCERY = PRICE_IN_CART_GROCERY - Integer.parseInt( amount );
            Cart.priceIncart.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );

        }


    }
    public static void calculateTotalSave(String todo, String Commsission) {

        if (todo.equals( "+" )) {
            TOTAL_Commission = TOTAL_Commission + Integer.parseInt( Commsission );
            Cart.totalSave.setText( "₹" + TOTAL_Commission );
        } else {
            TOTAL_Commission = TOTAL_Commission - Integer.parseInt( Commsission );
            Cart.totalSave.setText( "₹" + String.valueOf( TOTAL_Commission ) );

        }
    }

   public static void chechAdmin() {
        FirebaseFirestore.getInstance( ).collection( "ADMIN" ).document( "admin" ).get( )
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {

                            MIN_ORDER_AMOUNT = Integer.parseInt( task.getResult( ).get( "min_order_amount" ).toString( ) );
                            DELIVERY_CHARGES= Integer.parseInt( task.getResult().get( "delivery_charge" ).toString() );
                            DELIVERY_FREE_ABOVE= Integer.parseInt( task.getResult().get( "delivery_free_above" ).toString() );
                            ADMIN_NO = task.getResult( ).get( "phone_no" ).toString( );
                            String id = task.getResult( ).get( "id" ).toString( );
                            if (id.equals( FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ))) {
                                IS_ADMIN = true;
                                Home.admin_ll.setVisibility( View.VISIBLE );
                                Home.users_LL.setVisibility( View.INVISIBLE );

                            } else {
                                IS_ADMIN = false;

                            }
                        }
                    }
                } );
    }


   public static  void setDeliveryCharges(){
        if (Cart.pickUpCheck.isChecked( )) {
            Cart.tax.setText( "0" );

            Cart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );
            Cart.payAmount.setText( "₹" + PRICE_IN_CART_GROCERY );
        }else {

            if(PRICE_IN_CART_GROCERY< DELIVERY_FREE_ABOVE){

                Cart.tax.setText( String.valueOf(DELIVERY_CHARGES ) );
                Cart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );
                Cart.payAmount.setText( "₹" +( PRICE_IN_CART_GROCERY + DELIVERY_CHARGES ) );

            }else {
                Cart.tax.setText( "0" );

                Cart.grandTotal.setText( String.valueOf( PRICE_IN_CART_GROCERY ) );
                Cart.payAmount.setText( "₹" + PRICE_IN_CART_GROCERY );

            }

        }


    }

   public static void removeFromGroceryCartList(final String id, final Context context) {
        grocery_CartList_product_id.remove( id );

        final int size = grocery_CartList_product_id.size( );

        if (size == 0) {

            Map<String, Object> Size = new HashMap<>( );
            Size.put( "list_size", 0 );


            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                    .set( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {
                        grocery_CartList_product_id.clear( );
                        grocery_CartList_product_count.clear( );
                        Toast.makeText( context, "removed from cart", Toast.LENGTH_SHORT ).show( );
                        grocery_CartList_product_OutOfStock.remove( id );



                    }


                }
            } );

        }
        for (int x = 0; x < size; x++) {
            Map<String, Object> updateWishList = new HashMap<>( );
            updateWishList.put( "id_" + x, grocery_CartList_product_id.get( x ) );

            firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                    .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                    .set( updateWishList ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful( )) {
                        Map<String, Object> Size = new HashMap<>( );
                        Size.put( "list_size", size );

                        firebaseFirestore.collection( "USERS" ).document( FirebaseAuth.getInstance( ).getUid( ) )
                                .collection( "USER_DATA" ).document( "MY_GROCERY_CARTLIST" )
                                .update( Size ).addOnCompleteListener( new OnCompleteListener<Void>( ) {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful( )) {
                                    Toast.makeText( context, "removed from cart", Toast.LENGTH_SHORT ).show( );
                                    grocery_CartList_product_OutOfStock.remove( id );


                                }


                            }
                        } );

                    } else {
                        String error = task.getException( ).getMessage( );
                        Toast.makeText( context, error, Toast.LENGTH_SHORT ).show( );


                    }
                }
            } );


        }


    }
   public static List<String> franchise_order_list = new ArrayList<>( );

   public static void  loadFranchiseOrders(String area_code){
        franchise_order_list.clear();
        FirebaseFirestore.getInstance().collection( "ORDERS" ).orderBy( "id" ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>( ) {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        if(area_code.equals( documentSnapshot.get( "area" ).toString() )){
                            franchise_order_list.add( documentSnapshot.getId());
                        }
                    }


                }
            }
        } );
    }









}
