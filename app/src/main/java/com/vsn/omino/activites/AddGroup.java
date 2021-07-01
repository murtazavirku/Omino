package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vsn.omino.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGroup extends AppCompatActivity {

    CircleImageView groupIcon;
    EditText name;
    EditText desc;
    Button create;
    Uri iconUri;
    FirebaseStorage mStorage;
    StorageTask uploadTask;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mStorage = FirebaseStorage.getInstance();
        groupIcon = (CircleImageView) findViewById(R.id.groupIcon);
        name= (EditText) findViewById(R.id.groupName);
        desc = (EditText) findViewById(R.id.groupDesc);
        create = (Button) findViewById(R.id.groupCreate);
        reference = FirebaseDatabase.getInstance().getReference("Groups").child(prefs.getString("userID", null).toString());
        groupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Group Icon"), 1);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(AddGroup.this);
                progressDialog.setCancelable(false);
                progressDialog.show();


                if(name.getText().toString().isEmpty() || desc.getText().toString().isEmpty()){
                    Toast.makeText(AddGroup.this, "Fill the Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(iconUri != null) {


                        Cursor cursor = getContentResolver().query(iconUri, null, null, null, null);
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        cursor.moveToFirst();
                        String namee = cursor.getString(nameIndex);
                        String Filename = System.currentTimeMillis() + namee;
                        final StorageReference storageReference = mStorage.getReference().child("GroupIcons").child(Filename);
                        uploadTask = storageReference.putFile(iconUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final Task<Uri> groupIconUri = taskSnapshot.getStorage().getDownloadUrl();
                                        groupIconUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String IconUrl = uri.toString();
                                                String id = reference.push().getKey();
                                                HashMap<String,String> groupMap = new HashMap<>();
                                                groupMap.put("GroupIcon",IconUrl);
                                                groupMap.put("GroupName",name.getText().toString());
                                                groupMap.put("GroupDesc",desc.getText().toString());
                                                groupMap.put("GroupID",id);
                                                groupMap.put("Subscribers","0");
                                                reference.child(id).setValue(groupMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(AddGroup.this, "Successful Created", Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                                else{
                                                                    Toast.makeText(AddGroup.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                }progressDialog.dismiss();
                                                            }
                                                        });
                                            }
                                        });
                                            }
                                        });

                    }
                    else{
                        //Toast.makeText(AddGroup.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        String id = reference.push().getKey();
                        HashMap<String,String> groupMap = new HashMap<>();
                        groupMap.put("GroupIcon","https://firebasestorage.googleapis.com/v0/b/instaclone-94522.appspot.com/o/user.png?alt=media&token=cb2eebc6-3908-40b7-bf06-22f074b0d871");
                        groupMap.put("GroupName",name.getText().toString());
                        groupMap.put("GroupDesc",desc.getText().toString());
                        groupMap.put("GroupID",id);
                        groupMap.put("Subscribers","0");
                        reference.child(id).setValue(groupMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(AddGroup.this, "Successful Created", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                        else{
                                            Toast.makeText(AddGroup.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                        }progressDialog.dismiss();
                                    }
                                });
                    }

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            iconUri = data.getData();
            Uri uri= iconUri;
            groupIcon.setImageURI(uri);
        }
    }
}