package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.OpenableColumns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vsn.omino.R;

import java.util.Calendar;
import java.util.HashMap;

public class GroupCreateEvent extends AppCompatActivity {

    ImageView Eventbg;
    EditText EventName,EventDate;
    Button CreateEventbtn;
    Uri bgUri;
    DatePickerDialog datePickerDialog;
    FirebaseStorage mStorage;
    StorageTask uploadTask;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_event);
        ActivityCompat.requestPermissions(GroupCreateEvent.this,
                new String[]{Manifest.permission.WRITE_CALENDAR},
                1);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mStorage = FirebaseStorage.getInstance();
        datePickerDialog =  new DatePickerDialog(GroupCreateEvent.this);
        Eventbg =  (ImageView)findViewById(R.id.Eventbg);
        EventName =  (EditText)findViewById(R.id.EventName);
        EventDate =  (EditText)findViewById(R.id.EventDate);
        CreateEventbtn =  (Button)findViewById(R.id.CreateEventbtn);

        Eventbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Event Background"), 1);
            }
        });


//        EventDate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(GroupCreateEvent.this);
//                datePickerDialog.setTitle("Select Event Date");
//                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        EventDate.setText(dayOfMonth+"/"+month+"/"+year);
//                        datePickerDialog.dismiss();
//                    }
//                });
//                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        datePickerDialog.dismiss();
//                    }
//                });
//                datePickerDialog.show();
//                return true;
//            }
//        });


        EventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               datePickerDialog.show();
            }
        });

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String m = String.valueOf(month+1);
                String d = String.valueOf(dayOfMonth);
                String y = String.valueOf(year);
                EventDate.setText(d+"/"+m+"/"+y);
            }
        });










        CreateEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bgUri != null){
                    if(EventName.getText().toString().isEmpty()){
                        Toast.makeText(GroupCreateEvent.this, "Enter Event Name", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(EventDate.getText().toString().isEmpty()){
                            Toast.makeText(GroupCreateEvent.this, "Enter Date", Toast.LENGTH_SHORT).show();
                        }
                        else{


                            if (ActivityCompat.checkSelfPermission(GroupCreateEvent.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(GroupCreateEvent.this,
                                        new String[]{Manifest.permission.WRITE_CALENDAR},
                                        1);
                            }
                            else{
                                final ProgressDialog progressDialog = new ProgressDialog(GroupCreateEvent.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Creating Event...");
                                progressDialog.setTitle("Please Wait");
                                progressDialog.show();
                                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events").child(getIntent().getStringExtra("id"));

                                Cursor cursor = getContentResolver().query(bgUri, null, null, null, null);
                                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                cursor.moveToFirst();
                                String namee = cursor.getString(nameIndex);
                                String Filename = System.currentTimeMillis() + namee;
                                final StorageReference storageReference = mStorage.getReference().child("Events").child(prefs.getString("userID",null).toString()).child(Filename);
                                uploadTask = storageReference.putFile(bgUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                final Task<Uri> EventbgUri = taskSnapshot.getStorage().getDownloadUrl();
                                                EventbgUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String bgUrl = uri.toString();
                                                        reference = FirebaseDatabase.getInstance().getReference("Events").child(getIntent().getStringExtra("id"));
                                                        String id = reference.push().getKey();

                                                        HashMap<String,String> EventMap = new HashMap<>();
                                                        EventMap.put("Background",bgUrl);
                                                        EventMap.put("EventName",EventName.getText().toString());
                                                        EventMap.put("EventDate",EventDate.getText().toString());
                                                        EventMap.put("EventID",id);
                                                        reference.child(id).setValue(EventMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            String[] splitString = EventDate.getText().toString().split("/");
                                                                            int day = Integer.parseInt(splitString[0]);
                                                                            int month = Integer.parseInt(splitString[1]);
                                                                            int year = Integer.parseInt(splitString[2]);

                                                                            String hourofday = "09";
                                                                            String minute = "00";

                                                                            addReminder(EventName.getText().toString(),year,month,day,year,month,day,Integer.valueOf(hourofday),Integer.valueOf(minute),Integer.valueOf(hourofday),Integer.valueOf(minute));
                                                                            progressDialog.dismiss();
                                                                        }
                                                                        else{
                                                                            Toast.makeText(GroupCreateEvent.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        });
                            }


                        }
                    }
                }
                else{
                    Toast.makeText(GroupCreateEvent.this, "Select Background Image", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    private void addReminder(String EventTitle, int EventStartDateYear, int EventStartDateMonth, int EventStartDateDay, int EventEndDateYear, int EventEndDateMonth, int EventEndDateDay, int BeginHour, int BeginMin, int EndHour, int EndMin) {
        // TODO Auto-generated method stub


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GroupCreateEvent.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    1);
        }
        else{
            ContentResolver cr = this.getContentResolver();
            Calendar beginTime = Calendar.getInstance();

            beginTime.set(EventStartDateYear, EventStartDateMonth - 1, EventStartDateDay, BeginHour, BeginMin);

            long startTime = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();

            endTime.set(EventEndDateYear, EventEndDateMonth - 1, EventEndDateDay, EndHour, EndMin);
            long end1 = endTime.getTimeInMillis();
            ContentValues calEvent = new ContentValues();
            calEvent.put(CalendarContract.Events.CALENDAR_ID, 1);
            calEvent.put(CalendarContract.Events.TITLE, EventTitle);
            calEvent.put(CalendarContract.Events.DTSTART, startTime);
            calEvent.put(CalendarContract.Events.DTEND, end1);
            calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
            calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);

            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);

            // The returned Uri contains the content-retriever URI for
            // the newly-inserted event, including its id
            int id = Integer.parseInt(uri.getLastPathSegment());

            // String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, id);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 0);

            Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
            EventName.setText("");
            EventDate.setText("");
            Eventbg.setImageURI(null);
        }


//        Toast.makeText(getApplicationContext(), "Created Calendar Event " + id,
//                Toast.LENGTH_SHORT).show();
//

        //Toast.makeText(getApplicationContext(), "Payable Reminder have been saved successfully", Toast.LENGTH_SHORT).show();
        //return id;

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(GroupCreateEvent.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(GroupCreateEvent.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){

            bgUri = data.getData();
            Uri uri= bgUri;
            Eventbg.setImageURI(uri);
        }
    }
}