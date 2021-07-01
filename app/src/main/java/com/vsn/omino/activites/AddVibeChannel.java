package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vsn.omino.BottomSheets.BottomSheetFragment;
import com.vsn.omino.R;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddVibeChannel extends AppCompatActivity {

    ImageView Channelbg;
    EditText CHname;
    public Button SelectContent,CreateCH;

    Uri bgUri;
    FirebaseStorage mStorage;
    StorageTask uploadTask;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    BottomSheetFragment fragmentBottomSheetDialogFull;
    List<SavedCollectionModel> selectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vibe_channel);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        fragmentBottomSheetDialogFull = new BottomSheetFragment(AddVibeChannel.this);
        Channelbg = (ImageView)findViewById(R.id.Channelbg);
        CHname = (EditText) findViewById(R.id.CHname);
        SelectContent = (Button) findViewById(R.id.SelectContent);
        CreateCH = (Button) findViewById(R.id.CreateCH);
        selectedList = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        Channelbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Channel Background"), 1);
            }
        });


        SelectContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentBottomSheetDialogFull.show(AddVibeChannel.this.getSupportFragmentManager(), fragmentBottomSheetDialogFull.getTag());
            }
        });

        CreateCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedList = fragmentBottomSheetDialogFull.getAllselecteditemsList();
                if(selectedList != null){
                    final ProgressDialog progressDialog = new ProgressDialog(AddVibeChannel.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Creating Vibe Channel...");
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();


                    if(CHname.getText().toString().isEmpty()){
                        progressDialog.dismiss();
                        Toast.makeText(AddVibeChannel.this, "Fill the Name", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(bgUri != null) {


                            Cursor cursor = getContentResolver().query(bgUri, null, null, null, null);
                            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            cursor.moveToFirst();
                            String namee = cursor.getString(nameIndex);
                            String Filename = System.currentTimeMillis() + namee;
                            final StorageReference storageReference = mStorage.getReference().child("VibeChannelBg").child(prefs.getString("userID",null).toString()).child(Filename);
                            uploadTask = storageReference.putFile(bgUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            final Task<Uri> groupIconUri = taskSnapshot.getStorage().getDownloadUrl();
                                            groupIconUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String bgUrl = uri.toString();
                                                    reference = FirebaseDatabase.getInstance().getReference("VibeChannels").child(prefs.getString("userID",null).toString());
                                                    String id = reference.push().getKey();

                                                    HashMap<String,String> channelMap = new HashMap<>();
                                                    channelMap.put("ChannelName",CHname.getText().toString());
                                                    channelMap.put("ChannelCover",bgUrl);
                                                    channelMap.put("ChannelID",id);
                                                    channelMap.put("userID",prefs.getString("userID",null).toString());
                                                    reference.child(id).setValue(channelMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){

                                                                        DatabaseReference vibRef = FirebaseDatabase.getInstance().getReference("VibeChannelData").child(id);
                                                                        for(int i=0; i<selectedList.size();i++) {
                                                                            String ran_id = vibRef.push().getKey();
                                                                            HashMap<String, String> dataMap = new HashMap<>();
                                                                            dataMap.put("DataUrl", selectedList.get(i).getImageUrl());
                                                                            dataMap.put("DataCover", selectedList.get(i).getCover());
                                                                            dataMap.put("Category", selectedList.get(i).getType());
                                                                            vibRef.child(ran_id).setValue(dataMap)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                //Toast.makeText(AddVibeChannel.this, "Successful Created", Toast.LENGTH_SHORT).show();

                                                                                            }else{
                                                                                                Toast.makeText(AddVibeChannel.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }
                                                                        Channelbg.setImageDrawable(getResources().getDrawable(R.drawable.icons8_add_image_96));
                                                                        CHname.setText("");
                                                                        SelectContent.setText("Attach");
                                                                        progressDialog.dismiss();
                                                                    }
                                                                    else{
                                                                        Toast.makeText(AddVibeChannel.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    progressDialog.dismiss();
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                    });

                        }
                        else{

                                                    reference = FirebaseDatabase.getInstance().getReference("VibeChannels").child(prefs.getString("userID",null).toString());
                                                    String id = reference.push().getKey();

                                                    HashMap<String,String> channelMap = new HashMap<>();
                                                    channelMap.put("ChannelName",CHname.getText().toString());
                                                    channelMap.put("ChannelCover","none");
                                                    channelMap.put("ChannelID",id);
                                                    channelMap.put("userID",prefs.getString("userID",null).toString());
                                                    reference.child(id).setValue(channelMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){

                                                                        DatabaseReference vibRef = FirebaseDatabase.getInstance().getReference("VibeChannelData").child(id);
                                                                        for(int i=0; i<selectedList.size();i++) {
                                                                            String ran_id = vibRef.push().getKey();
                                                                            HashMap<String, String> dataMap = new HashMap<>();
                                                                            dataMap.put("DataUrl", selectedList.get(i).getImageUrl());
                                                                            dataMap.put("DataCover", selectedList.get(i).getCover());
                                                                            dataMap.put("Category", selectedList.get(i).getType());
                                                                            vibRef.child(ran_id).setValue(dataMap)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                //Toast.makeText(AddVibeChannel.this, "Successful Created", Toast.LENGTH_SHORT).show();

                                                                                            }else{
                                                                                                Toast.makeText(AddVibeChannel.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }
                                                                        Channelbg.setImageDrawable(getResources().getDrawable(R.drawable.icons8_add_image_96));
                                                                        CHname.setText("");
                                                                        SelectContent.setText("Attach");
                                                                        progressDialog.dismiss();
                                                                    }
                                                                    else{
                                                                        Toast.makeText(AddVibeChannel.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    progressDialog.dismiss();
                                                                }
                                                            });


                        }

                    }
                }
                else{
                    Toast.makeText(AddVibeChannel.this, "Attachment Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            bgUri = data.getData();
            Uri uri= bgUri;
            Channelbg.setImageURI(uri);
        }
    }



}