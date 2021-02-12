package com.quantum.myaata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    ImageButton addimge;
    private ImageView displayImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    public static String imageUrl="";
    private Dialog loadingDialog;
    EditText name,price,stock,description,commission;
    Button add_button;
    String product_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_product );

        mStorageRef= FirebaseStorage.getInstance().getReference("USER_VIDEO_DISPLAY_IMAGE");
        displayImage=findViewById( R.id.product_image );
        addimge=findViewById( R.id.addImageBtn );
        name=findViewById( R.id.name_et );
        price=findViewById( R.id.price_et );
        stock=findViewById( R.id.stock_et );
        description=findViewById( R.id.description_et );
        add_button=findViewById( R.id.live_btn );
        commission=findViewById( R.id.commission_et );

        add_button.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()||price.getText().toString().isEmpty()||stock.getText().toString().isEmpty()||description.getText().toString().isEmpty()||commission.getText().toString().isEmpty()||imageUrl.isEmpty()){

                    Toast.makeText( AddProduct.this, "Enter All Details", Toast.LENGTH_SHORT ).show( );
                }else {
                    Map<String,Object> addDetails=new HashMap<>(  );
                    addDetails.put( "name",name.getText().toString());
                    addDetails.put( "price",price.getText().toString());
                    addDetails.put( "in_stock",Integer.parseInt( stock.getText().toString() ));
                    addDetails.put( "description",description.getText().toString());
                    addDetails.put( "image",imageUrl);
                    addDetails.put( "commission",commission.getText().toString());

                    FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( product_id ).set( addDetails ).addOnCompleteListener( new OnCompleteListener<Void>( ) {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( AddProduct.this, "Product_added", Toast.LENGTH_SHORT ).show( );

                            }
                        }
                    } );


                }
            }
        } );


        int layout_code=getIntent().getIntExtra( "layout_code",-1 );

        if(layout_code==1){
             product_id=getIntent().getStringExtra( "product_id" );
            FirebaseFirestore.getInstance().collection( "PRODUCTS" ).document( product_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>( ) {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        name.setText( task.getResult().get( "name" ).toString() );
                        price.setText( task.getResult().get( "price" ).toString() );
                        description.setText( task.getResult().get( "description" ).toString() );
                        stock.setText(String.valueOf( (long) task.getResult().get( "in_stock" )) );
                        commission.setText( task.getResult().get( "commission" ).toString() );
                        imageUrl=task.getResult().get( "image" ).toString();
                        Glide.with( AddProduct.this ).load(imageUrl).into( displayImage );
                        addimge.setVisibility( View.INVISIBLE );
                        displayImage.setVisibility( View.VISIBLE );
                    }
                }
            } );
        }else {
            DocumentReference ref = FirebaseFirestore.getInstance().collection("PRODUCTS").document();
            product_id = ref.getId();
        }

        loadingDialog = new Dialog( AddProduct.this );
        loadingDialog.setContentView( R.layout.loading_progress_dialouge );
        loadingDialog.setCancelable( false );
        loadingDialog.getWindow( ).setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        addimge.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        } );
        displayImage.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        } );
    }


    private void openFileChooser() {

        Intent intent = new Intent( );
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( intent, PICK_IMAGE_REQUEST );


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK
                && data!= null && data.getData() != null   ){


            mImageUri= data.getData();
            displayImage.setVisibility( View.VISIBLE );
            displayImage.setImageURI( mImageUri );
            uploadFile();
            addimge.setVisibility( View.GONE );

        }
    } private String getFileExtention(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( cR.getType( uri ) );
    }

    public void uploadFile(){

        if(mImageUri!=null){
            loadingDialog.show();
            final StorageReference fileref= mStorageRef.child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( System.currentTimeMillis()+"."+getFileExtention( mImageUri ) );

            fileref.putFile( mImageUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>( ) {
                        @Override
                        public void onSuccess(final Uri uri) {

                            imageUrl=uri.toString();
                            loadingDialog.dismiss();
                        }
                    } );




                }
            } ).addOnFailureListener( new OnFailureListener( ) {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT ).show( );
                }
            } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {



                }
            } );
        }else {
            Toast.makeText( this, "no item selected", Toast.LENGTH_SHORT ).show( );

        }



    }

}