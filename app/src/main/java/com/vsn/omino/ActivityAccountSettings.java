package com.vsn.omino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.vsn.omino.models.User;
import com.vsn.omino.models.UserAccountSettings;
import com.vsn.omino.models.UserSettings;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAccountSettings extends AppCompatActivity {


    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef,reference;
    private String userID;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    String username;


    //EditProfile Fragment widgets
    private EditText mUsername, mTag, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;
    ProgressDialog pdg;

    //vars
    private UserSettings mUserSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        //mDisplayName = (EditText) findViewById(R.id.display_name);
        mUsername = (EditText) findViewById(R.id.username);

        mTag = (EditText) findViewById(R.id.tag);
        mDescription = (EditText) findViewById(R.id.catagory);
        mEmail = (EditText) findViewById(R.id.email);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = (TextView) findViewById(R.id.changeProfilePhoto);


        //setProfileImage();
        pdg = new ProgressDialog(ActivityAccountSettings.this);
        pdg.setMessage("loading...");
        pdg.show();
        setupFirebaseAuth();


        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d(TAG, "onClick: navigating back to ProfileActivity");
                finish();
            }
        });

        ImageView checkmark = (ImageView) findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
                finish();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Email = snapshot.child("Users").child(prefs.getString("userID", null)).child("email").getValue().toString();
                String Phone = snapshot.child("Users").child(prefs.getString("userID", null)).child("phone_number").getValue().toString();
                String Userid = snapshot.child("Users").child(prefs.getString("userID", null)).child("user_id").getValue().toString();
                String Name = snapshot.child("Users").child(prefs.getString("userID", null)).child("username").getValue().toString();

                String description = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("description").getValue().toString();
                String display_name = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("display_name").getValue().toString();
                String profile_photo = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("profile_photo").getValue().toString();
                String tag = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("tag").getValue().toString();

                username = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("username").getValue().toString();
//                    String Phone = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("phone_number").getValue().toString();
//                    String Userid = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("user_id").getValue().toString();
//                    String Name = snapshot.child("UserAccountSettings").child(prefs.getString("userID", null)).child("username").getValue().toString();

                if(!(profile_photo.isEmpty())) {
                    Picasso.get().load(profile_photo).into(mProfilePhoto);
                }
                mUsername.setText(username);
                mTag.setText(tag);
                mDescription.setText(description);
                mEmail.setText(Email);
                mPhoneNumber.setText(Phone);


                mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: changing profile photo");
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, 102);
                    }
                });
                pdg.dismiss();



         //       Toast.makeText(ActivityAccountSettings.this, "called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityAccountSettings.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void saveProfileSettings(){
        //final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String tag = mTag.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

        if(!(this.username.equals(username))) {
            checkIfUsernameExists(username);
        }
        if(tag != null) {
            myRef.child(getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(getString(R.string.field_tag))
                    .setValue(tag);
        }

        if(description != null) {
            myRef.child(getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(getString(R.string.field_description))
                    .setValue(description);
        }

        if(phoneNumber != 0) {
            myRef.child(getString(R.string.dbname_users))
                    .child(userID)
                    .child(getString(R.string.field_phone_number))
                    .setValue(String.valueOf(phoneNumber));
        }


    }




    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    //add the username
                    updateUsername(username);
                    Toast.makeText(ActivityAccountSettings.this, "saved username.", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(ActivityAccountSettings.this, "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                ProgressDialog pd = new ProgressDialog(ActivityAccountSettings.this);
                pd.setMessage("uploading...");
                pd.show();
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mProfilePhoto.setImageBitmap(selectedImage);

                mProfilePhoto.setDrawingCacheEnabled(true);
                mProfilePhoto.buildDrawingCache();
                Bitmap bitmap = mProfilePhoto.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();


                FirebaseStorage storage = FirebaseStorage.getInstance();

                final StorageReference ref = storage.getReference().child("profileImages/"+userID+".jpg");
                UploadTask uploadTask = ref.putBytes(data1);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            myRef.child(getString(R.string.dbname_user_account_settings))
                                    .child(userID)
                                    .child("profile_photo")
                                    .setValue(downloadUri.toString());
                            pd.dismiss();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ActivityAccountSettings.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(ActivityAccountSettings.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
       /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: upadting username to: " + username);

        myRef.child(getString(R.string.dbname_users))
                .child(userID)
                .child(getString(R.string.field_username))
                .setValue(username);

        myRef.child(getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(getString(R.string.field_username))
                .setValue(username);
    }



}