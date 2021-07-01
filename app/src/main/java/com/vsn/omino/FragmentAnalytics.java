package com.vsn.omino;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.AnalyticsNewsFeedAdapter;
import com.vsn.omino.Adapters.CommentAdapter;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.InsideFragments.NewsFeedFragment;
import com.vsn.omino.activites.ImageSpecificPost;
import com.vsn.omino.models.CommentModel;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class FragmentAnalytics extends Fragment {

   // private LineChart lineChart;
    private TextInputLayout stockTickerTextInputLayout;
    private RadioGroup typeRadioGroup, intervalRadioGroup;
    private CheckBox highCheckBox, lowCheckBox, closeCheckBox;
    List<CommentModel> commentModelList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    DatabaseReference databaseReference;
    public static NestedScrollView nestedScrollView;
    public static TextInputLayout tIL1,tIL2;
    public static String selectedPostId;
    Button refButton;


    RecyclerView listView;
    List<OmnioPosts> OmnioPostsList;
    List<String> timeLists;
    AnalyticsNewsFeedAdapter analyticsNewsFeedAdapter;
    ProgressBar postProgress;
    CheckInternet checkInternet;
    LinearLayout noInternet;
    TextView TryAgain;
    MediaPlayer mediaPlayer;


    //...
    LineChart lineChart;
    LineData lineData;
    List<Entry> entryList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        timeLists= new ArrayList<>();
        typeRadioGroup = view.findViewById(R.id.activity_main_period_radiogroup);
        intervalRadioGroup = view.findViewById(R.id.activity_main_priceinterval);
        lineChart = view.findViewById(R.id.activity_main_linechart);
        commentModelList = new ArrayList<>();
        listView = (RecyclerView) view.findViewById(R.id.news_feed_List);
        tIL1 = (TextInputLayout) view.findViewById(R.id.fa_et_nameOfPiece);
        tIL2 = (TextInputLayout) view.findViewById(R.id.fa_et_catagory);
        postProgress = (ProgressBar)view.findViewById(R.id.postProgress);
        postProgress.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        OmnioPostsList = new ArrayList<>();
        checkInternet = new CheckInternet();
        refButton = view.findViewById(R.id.activity_main_getprices);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollview_fragment_analytics);
        noInternet = (LinearLayout)view.findViewById(R.id.lnI);
        TryAgain = (TextView)view.findViewById(R.id.tryagain);
        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPosts getPosts = new GetPosts();
                getPosts.execute(prefs.getString("userID", null).toString());
            }
        });

        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(getContext(), "Press Refresh to obtain data", Toast.LENGTH_SHORT).show();

//                switch(checkedId){
//                    case R.id.activity_main_periodV:
//                        // do operations specific to this selection
//                        break;
//                    case R.id.activity_main_periodS:
//                        // do operations specific to this selection
//                        break;
//                    case R.id.activity_main_periodB:
//                        // do operations specific to this selection
//                        break;
//                    case R.id.activity_main_periodC:
//                        // do operations specific to this selection
//                        break;
//                }
            }
        });
        intervalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(getContext(), "Press Refresh to obtain data", Toast.LENGTH_SHORT).show();

//                switch(checkedId){
//                    Toast.makeText(getContext(), "Please Select the Post First", Toast.LENGTH_SHORT).show();
//
//                    case R.id.activity_main_interval1h:
//                        getEntryMinute();
//                        break;
//                    case R.id.activity_main_interval1d:
//                        getEntryHour();
//                        break;
//                    case R.id.activity_main_interval1w:
//                        getEntryWeek();
//                        break;
//                    case R.id.activity_main_interval1m:
//                        getEntryDays();
//                        break;
//
//                    case R.id.activity_main_interval1y:
//                        getEntryMonth();
//                        break;
//                }
            }
        });

        refButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPostId == null)
                {
                    Toast.makeText(getContext(), "Please Select the Post First", Toast.LENGTH_SHORT).show();
                }
                else if(intervalRadioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getContext(), "Please Select Time", Toast.LENGTH_SHORT).show();

                }
                else if(typeRadioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getContext(), "Please Select Engagements", Toast.LENGTH_SHORT).show();

                }
                else {
                    //yyyy_MM_dd_HH_mm_ss

                    if(typeRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_periodV)
                    {
                        ProgressDialog pd = new ProgressDialog(getContext());
                        pd.setMessage("fetching...");
                        pd.show();
                        final List<String> returning = new ArrayList<>();
                        databaseReference = FirebaseDatabase.getInstance().getReference("PostEngagements").child("Views").child(selectedPostId);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                returning.clear();
                                for (DataSnapshot ss : snapshot.getChildren()) {
                                    returning.add(ss.getValue().toString());
                                }
                                if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1h)
                                { hourCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1d)
                                {  dayCondition(returning); }
//                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1w)
//                                { weekCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1m)
                                { monthCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1y)
                                {yearCondition(returning);}
                                else
                                {}

                                pd.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pd.dismiss();
                            }
                        });
                    }
                    else if(typeRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_periodB)
                    {
                        ProgressDialog pd = new ProgressDialog(getContext());
                        pd.setMessage("fetching...");
                        pd.show();
                        final List<String> returning = new ArrayList<>();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(selectedPostId);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                returning.clear();
                                for (DataSnapshot ss : snapshot.getChildren()) {
                                    returning.add(ss.child("usercommenttime").getValue().toString());
                                }
                                if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1h)
                                { hourCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1d)
                                {  dayCondition(returning); }
//                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1w)
//                                {}
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1m)
                                { monthCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1y)
                                {yearCondition(returning);}
                                else
                                {}
                                pd.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pd.dismiss();
                            }
                        });
                    }
                    else if(typeRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_periodS)
                    {
                        ProgressDialog pd = new ProgressDialog(getContext());
                        pd.setMessage("fetching...");
                        pd.show();
                        final List<String> returning = new ArrayList<>();
                        databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions").child(selectedPostId);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                returning.clear();
                                for (DataSnapshot ss : snapshot.getChildren()) {
                                    returning.add(ss.child("userReactTime").getValue().toString());
                                }
                                if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1h)
                                { hourCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1d)
                                {  dayCondition(returning); }
//                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1w)
//                                {}
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1m)
                                { monthCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1y)
                                {yearCondition(returning);}
                                else
                                {}

                                pd.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pd.dismiss();
                            }
                        });
                    }
                    else if(typeRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_periodC)
                    {
                        ProgressDialog pd = new ProgressDialog(getContext());
                        pd.setMessage("fetching...");
                        pd.show();
                        final List<String> returning = new ArrayList<>();
                        databaseReference = FirebaseDatabase.getInstance().getReference("PostEngagements").child("Clicks").child(selectedPostId);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                returning.clear();
                                for (DataSnapshot ss : snapshot.getChildren()) {
                                    returning.add(ss.getValue().toString());
                                }
                                if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1h)
                                { hourCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1d)
                                {  dayCondition(returning); }
//                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1w)
//                                {}
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1m)
                                { monthCondition(returning); }
                                else if(intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1y)
                                {}
                                else
                                {yearCondition(returning);}

                                pd.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pd.dismiss();
                            }
                        });
                    }
                    else{}

//                   if((intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_interval1h) || (intervalRadioGroup.getCheckedRadioButtonId() == R.id.activity_main_periodV))
//                    {
//                       // List<String> returning = new ArrayList<>();
//                       // getViews();
//                        ProgressDialog pd = new ProgressDialog(getContext());
//                        pd.setMessage("fetching...");
//                        pd.show();
//
//                        final List<String> returning = new ArrayList<>();
//
//                        databaseReference = FirebaseDatabase.getInstance().getReference("PostEngagements").child("Views").child(selectedPostId);
//                        databaseReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                returning.clear();
//                                ///String xyz = "";
//                                for (DataSnapshot ss : snapshot.getChildren()) {
//                                    returning.add(ss.getValue().toString());
//                                    // Log.d("PPPPPP.......PPPPP",ss.getValue().toString());
//                                    // xyz = xyz + ss.getValue().toString();
//                                }
//                                hourCondition(returning);
//                                pd.dismiss();
//
//                                //Toast.makeText(getContext(),returning.get(0), Toast.LENGTH_SHORT).show();
//
//                               // setListOfViews(returning);
//
////                                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                                Date date = new Date();
////                               // System.out.println("Current Date " + sdf.format(date));
////
////                                // Convert Date to Calendar
////                                Calendar ci = Calendar.getInstance();
////                                ci.setTime(date);
////                                // Perform addition/subtraction
////                                ci.add(Calendar.YEAR, 0);ci.add(Calendar.MONTH, 0);ci.add(Calendar.DATE, 0);
////                                ci.add(Calendar.HOUR, -1);ci.add(Calendar.MINUTE, 0);ci.add(Calendar.SECOND, 0);
////
////                                // Convert calendar back to Date
////                                Date currentDatePlusOne = ci.getTime();
////
////                                String subtractedDateAndTime = sdf.format(currentDatePlusOne);
////
////                               // System.out.println("Updated Date " + sdf.format(currentDatePlusOne));
//
//                                //Toast.makeText(getContext(), selectedPostId+a+b+c+d+e+f, Toast.LENGTH_SHORT).show();
//                                //Toast.makeText(getContext(),returning.size(), Toast.LENGTH_SHORT).show();
//
//                                // return returning[0];
//                                //  Log.d("PPPPPP.......PPPPP",returning.get(0));
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                pd.dismiss();
//                            }
//                        });
//                        // Toast.makeText(getContext(),returning.get(0), Toast.LENGTH_SHORT).show();
//
//
//
//                    }


                }



            }
        });


//        stockTickerTextInputLayout = view.findViewById(R.id.activity_main_stockticker);
//        periodRadioGroup = view.findViewById(R.id.activity_main_period_radiogroup);
//        intervalRadioGroup = view.findViewById(R.id.activity_main_priceinterval);
//
//        highCheckBox = view.findViewById(R.id.activity_main_high);
//        lowCheckBox = view.findViewById(R.id.activity_main_low);
//        closeCheckBox = view.findViewById(R.id.activity_main_close);

       // configureLineChart();

        GetPosts getPosts = new GetPosts();
        getPosts.execute(prefs.getString("userID", null).toString());
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Clicked ", Toast.LENGTH_SHORT).show();
//
//             //   nestedScrollView.scrollTo(0, 0);
////                Runnable runnable=new Runnable() {
////                    @Override
////                    public void run() {
////                        nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
////                    }
////                };
////                nestedScrollView.post(runnable);
////                //nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
//        List<Entry> valsComp1 = new ArrayList<Entry>();
//        List<Entry> valsComp2 = new ArrayList<Entry>();
//
//        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
//        valsComp1.add(c1e1);
//        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
//        valsComp1.add(c1e2);
//        // and so on ...
//        Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
//        valsComp2.add(c2e1);
//        Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
//        valsComp2.add(c2e2);
//
//        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
//        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
//        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(setComp1);
//        dataSets.add(setComp2);
//        LineData data = new LineData(dataSets);
//        lineChart.setData(data);
//        lineChart.invalidate(); // refresh
//
//        // the labels that should be drawn on the XAxis
//        final String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4" };
//        ValueFormatter formatter = new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                return quarters[(int) value];
//            }
//        };
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//        xAxis.setValueFormatter(formatter);
//

        lineChart = view.findViewById(R.id.activity_main_linechart);

        entryList.add(new Entry(10,20));
        entryList.add(new Entry(5,10));
        entryList.add(new Entry(7,31));
        entryList.add(new Entry(3,14));
        LineDataSet lineDataSet = new LineDataSet(entryList,"country");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(10);
        lineChart.invalidate();


        return view;
    }

    private void hourCondition(List<String> returning) {
        int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0;
        //  Toast.makeText(getContext(),String.valueOf(timeLists.size()) , Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(), currentDateAndTime, Toast.LENGTH_SHORT).show();
        // Log.d("PPPPPP......",currentDateAndTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        for(int i = 0 ; i < returning.size() ; i++)
        {

            //   Log.d("PPPPPP.......PPPPP",timeLists.get(i));
            if(returning.get(i).contains(currentDateAndTime))
            {

                String value = returning.get(i).substring(14,16);
                if(Integer.valueOf(value)<=10)
                {
                    a++;
                }
                if((Integer.valueOf(value)>10) && (Integer.valueOf(value)<=20))
                {
                    b++;
                }
                if((Integer.valueOf(value)>20) && (Integer.valueOf(value)<=30))
                {
                    c++;
                }
                if((Integer.valueOf(value)>30) && (Integer.valueOf(value)<=40))
                {
                    d++;
                }
                if((Integer.valueOf(value)>40) && (Integer.valueOf(value)<=50))
                {
                    e++;
                }
                if((Integer.valueOf(value)>50) && (Integer.valueOf(value)<=60))
                {
                    f++;
                }
            }
        }
        getEntryMinute(a,b,c,d,e,f);
    }

    private void dayCondition(List<String> returning) {
        int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0,g=0,h=0,j=0,k=0,l=0,m=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        for(int i = 0 ; i < returning.size() ; i++)
        {
            if(returning.get(i).contains(currentDateAndTime))
            {
                String value = returning.get(i).substring(11,13);
                if(Integer.valueOf(value)<=2)
                { a++; }
                if((Integer.valueOf(value)>2) && (Integer.valueOf(value)<=4))
                { b++; }
                if((Integer.valueOf(value)>4) && (Integer.valueOf(value)<=6))
                { c++; }
                if((Integer.valueOf(value)>6) && (Integer.valueOf(value)<=8))
                { d++; }
                if((Integer.valueOf(value)>8) && (Integer.valueOf(value)<=10))
                { e++; }
                if((Integer.valueOf(value)>10) && (Integer.valueOf(value)<=12))
                { f++; }
                if((Integer.valueOf(value)>12) && (Integer.valueOf(value)<=14))
                { g++; }
                if((Integer.valueOf(value)>14) && (Integer.valueOf(value)<=16))
                { h++; }
                if((Integer.valueOf(value)>16) && (Integer.valueOf(value)<=18))
                { j++; }
                if((Integer.valueOf(value)>18) && (Integer.valueOf(value)<=20))
                { k++; }
                if((Integer.valueOf(value)>20) && (Integer.valueOf(value)<=22))
                { l++; }
                if((Integer.valueOf(value)>22) && (Integer.valueOf(value)<=24))
                { m++; }
            }
        }
        getEntryHour(a,b,c,d,e,f,g,h,j,k,l,m);
    }

    void getEntryHour(int a, int b, int c, int d, int e, int f, int g, int h, int j, int k, int l, int m)
    {
        entryList.clear();
        entryList.add(new Entry(2,a));
        entryList.add(new Entry(4,b));
        entryList.add(new Entry(6,c));
        entryList.add(new Entry(8,d));
        entryList.add(new Entry(10,e));
        entryList.add(new Entry(12,f));
        entryList.add(new Entry(14,g));
        entryList.add(new Entry(16,h));
        entryList.add(new Entry(18,j));
        entryList.add(new Entry(20,k));
        entryList.add(new Entry(22,l));
        entryList.add(new Entry(24,m));
        LineDataSet lineDataSet = new LineDataSet(entryList,"Day");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(32);
        lineChart.invalidate();

    }
    private void monthCondition(List<String> returning) {
        int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0,g=0,h=0,j=0,k=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        for(int i = 0 ; i < returning.size() ; i++)
        {
            if(returning.get(i).contains(currentDateAndTime))
            {
                String value = returning.get(i).substring(8,10);
                if(Integer.valueOf(value)<=3)
                { a++; }
                if((Integer.valueOf(value)>3) && (Integer.valueOf(value)<=6))
                { b++; }
                if((Integer.valueOf(value)>6) && (Integer.valueOf(value)<=9))
                { c++; }
                if((Integer.valueOf(value)>9) && (Integer.valueOf(value)<=12))
                { d++; }
                if((Integer.valueOf(value)>12) && (Integer.valueOf(value)<=15))
                { e++; }
                if((Integer.valueOf(value)>15) && (Integer.valueOf(value)<=18))
                { f++; }
                if((Integer.valueOf(value)>18) && (Integer.valueOf(value)<=21))
                { g++; }
                if((Integer.valueOf(value)>21) && (Integer.valueOf(value)<=24))
                { h++; }
                if((Integer.valueOf(value)>24) && (Integer.valueOf(value)<=27))
                { j++; }
                if((Integer.valueOf(value)>27))
                { k++; }

            }
        }
        getEntryDays(a,b,c,d,e,f,g,h,j,k);
    }
    void getEntryDays(int a, int b, int c, int d, int e, int f, int g, int h, int j, int k)
    {
        entryList.clear();
        entryList.add(new Entry(3,a));
        entryList.add(new Entry(6,b));
        entryList.add(new Entry(9,c));
        entryList.add(new Entry(12,d));
        entryList.add(new Entry(15,e));
        entryList.add(new Entry(18,f));
        entryList.add(new Entry(21,g));
        entryList.add(new Entry(24,h));
        entryList.add(new Entry(27,j));
        entryList.add(new Entry(30,k));
        LineDataSet lineDataSet = new LineDataSet(entryList,"Month");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(28);
        lineChart.invalidate();

    }
    private void yearCondition(List<String> returning) {
        int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0,g=0,h=0,j=0,k=0,l=0,m=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        for(int i = 0 ; i < returning.size() ; i++)
        {
            if(returning.get(i).contains(currentDateAndTime))
            {
                String value = returning.get(i).substring(5,7);
                if(Integer.valueOf(value)==1)
                { a++; }
                if((Integer.valueOf(value)==2))
                { b++; }
                if((Integer.valueOf(value)==3))
                { c++; }
                if((Integer.valueOf(value)==4))
                { d++; }
                if((Integer.valueOf(value)==5))
                { e++; }
                if((Integer.valueOf(value)==6))
                { f++; }
                if((Integer.valueOf(value)==7))
                { g++; }
                if((Integer.valueOf(value)==8))
                { h++; }
                if((Integer.valueOf(value)==9))
                { j++; }
                if((Integer.valueOf(value)==10))
                { k++; }
                if((Integer.valueOf(value)==11))
                { l++; }
                if((Integer.valueOf(value)==12))
                { m++; }
            }
        }
        getEntryMonth(a,b,c,d,e,f,g,h,j,k,l,m);
    }
    void getEntryMonth(int a, int b, int c, int d, int e, int f, int g, int h, int j, int k, int l, int m)
    {
        entryList.clear();
        entryList.add(new Entry(1,a));
        entryList.add(new Entry(2,b));
        entryList.add(new Entry(3,c));
        entryList.add(new Entry(4,d));
        entryList.add(new Entry(5,e));
        entryList.add(new Entry(6,f));
        entryList.add(new Entry(7,g));
        entryList.add(new Entry(8,h));
        entryList.add(new Entry(9,j));
        entryList.add(new Entry(10,k));
        entryList.add(new Entry(11,l));
        entryList.add(new Entry(12,m));
        LineDataSet lineDataSet = new LineDataSet(entryList,"Year");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(20);
        lineChart.invalidate();

    }
    private void weekCondition(List<String> returning) {
        int a = 0,b = 0,c = 0,d = 0,e = 0,f = 0,g=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        // System.out.println("Current Date " + sdf.format(date));

        // Convert Date to Calendar
        Calendar ci = Calendar.getInstance();
        ci.setTime(date);
        // Perform addition/subtraction
        ci.add(Calendar.YEAR, 0);ci.add(Calendar.MONTH, 0);ci.add(Calendar.DATE, -7);
        ci.add(Calendar.HOUR, 0);ci.add(Calendar.MINUTE, 0);ci.add(Calendar.SECOND, 0);
        // Convert calendar back to Date
        Date currentDatePlusOne = ci.getTime();
        String subtractedDateAndTime = sdf.format(currentDatePlusOne);

        //Condition6
        Calendar ci2 = Calendar.getInstance();
        ci2.setTime(date);
        ci2.add(Calendar.DATE, -6);
        Date currentDatePlusOne2 = ci2.getTime();
        String subtractedDateAndTime2 = sdf.format(currentDatePlusOne2);
        //Condition5
        Calendar ci3 = Calendar.getInstance();
        ci3.setTime(date);
        ci3.add(Calendar.DATE, -5);
        Date currentDatePlusOne3 = ci3.getTime();
        String subtractedDateAndTime3 = sdf.format(currentDatePlusOne3);
        //Condition4
        Calendar ci4 = Calendar.getInstance();
        ci4.setTime(date);
        ci4.add(Calendar.DATE, -4);
        Date currentDatePlusOne4 = ci4.getTime();
        String subtractedDateAndTime4 = sdf.format(currentDatePlusOne4);
        //Condition3
        Calendar ci5 = Calendar.getInstance();
        ci5.setTime(date);
        ci5.add(Calendar.DATE, -3);
        Date currentDatePlusOne5 = ci5.getTime();
        String subtractedDateAndTime5 = sdf.format(currentDatePlusOne5);
        //Condition2
        Calendar ci6 = Calendar.getInstance();
        ci6.setTime(date);
        ci6.add(Calendar.DATE, -2);
        Date currentDatePlusOne6 = ci6.getTime();
        String subtractedDateAndTime6 = sdf.format(currentDatePlusOne6);
        //Condition1
        Calendar ci7 = Calendar.getInstance();
        ci7.setTime(date);
        ci7.add(Calendar.DATE, -1);
        Date currentDatePlusOne7 = ci7.getTime();
        String subtractedDateAndTime7 = sdf.format(currentDatePlusOne7);


        // System.out.println("Updated Date " + sdf.format(currentDatePlusOne));

        for(int i = 0 ; i < returning.size() ; i++)
        {
                String value = returning.get(i).substring(5,7);
                if(subtractedDateAndTime.contains(value))
                { a++; }
                if((subtractedDateAndTime2.contains(value)))
                { b++; }
                if((subtractedDateAndTime3.contains(value)))
                { c++; }
                if((subtractedDateAndTime4.contains(value)))
                { d++; }
                if((subtractedDateAndTime5.contains(value)))
                { e++; }
                if((subtractedDateAndTime6.contains(value)))
                { f++; }
                if((subtractedDateAndTime7.contains(value)))
                { g++; }

        }
        getEntryWeek(a,b,c,d,e,f,g);
    }
    void getEntryWeek(int a, int b, int c, int d, int e, int f, int g)
    {
        entryList.clear();
        entryList.add(new Entry(1,a));
        entryList.add(new Entry(2,b));
        entryList.add(new Entry(3,c));
        entryList.add(new Entry(4,d));
        entryList.add(new Entry(5,e));
        entryList.add(new Entry(6,f));
        entryList.add(new Entry(7,g));
        LineDataSet lineDataSet = new LineDataSet(entryList,"Week");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(40);
        lineChart.invalidate();

    }
    void getEntryMinute(int a,int b,int c,int d,int e,int f)
    {
        entryList.clear();
        entryList.add(new Entry(10,a));
        entryList.add(new Entry(20,b));
        entryList.add(new Entry(30,c));
        entryList.add(new Entry(40,d));
        entryList.add(new Entry(50,e));
        entryList.add(new Entry(60,f));
        LineDataSet lineDataSet = new LineDataSet(entryList,"Hour");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.white)); //Colors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.white));
        lineChart.setVisibleXRangeMaximum(40);
        lineChart.invalidate();

    }

//    List<String> getViews()
//    {
//
//
//        return returning;
//    }
    void setListOfViews(List<String> returning)
    {
        this.timeLists = returning;
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Stock Price History");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }

    private void LoadAllComments() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child("");//getIntent().getStringExtra("postid"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    String user = snapshot1.child("usercommentid").getValue().toString();
                    String comment = snapshot1.child("usercomment").getValue().toString();
                    commentModelList.add(new CommentModel(user,comment));
                }
//                recyclerView.setLayoutManager(new LinearLayoutManager(ImageSpecificPost.this));
//                adapter = new CommentAdapter(ImageSpecificPost.this,commentModelList,userData);
//                recyclerView.setAdapter(adapter);
//                if(recyclerView.getAdapter().getItemCount()>0) {
//                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
//                }
//                try {
//                    Author.setText(frontUser.get(3));
//                }
//                catch (IndexOutOfBoundsException e){
//                    Log.e("out of bound",e.getMessage());
//
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    private class GetPosts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if(checkInternet.checkInternetConnection(getContext())){
                listView.setVisibility(View.VISIBLE);
                noInternet.setVisibility(View.GONE);
                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        OmnioPostsList.clear();

                        for(DataSnapshot ss : snapshot.getChildren()) {
                            String userid= ss.child("userid").getValue().toString();

//                            if(snapshot.child(userid).exists()){
//                                if(snapshot.child(userid).equals(params[0])){

                            String caption = ss.child("caption").getValue().toString();
                            String category= ss.child("category").getValue().toString();
                            String color= ss.child("color").getValue().toString();
                            String dataURL= ss.child("dataURL").getValue().toString();
                            String datetimepost= ss.child("datetimepost").getValue().toString();
                            String isanalytics= ss.child("isanalytics").getValue().toString();
                            String isnft= ss.child("isnft").getValue().toString();
                            String tags= ss.child("tags").getValue().toString();
                            String usermail= ss.child("usermail").getValue().toString();
                            String usernamee= ss.child("usernamee").getValue().toString();
                            String coverart = ss.child("coverart").getValue().toString();
                            String postid = ss.child("postid").getValue().toString();
                            String groupid = ss.child("Groupid").getValue().toString();
                            OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));


//                                }
//                            }
                        }
                        //Toast.makeText(getContext(), String.valueOf(OmnioPostsList.size()), Toast.LENGTH_SHORT).show();
                        analyticsNewsFeedAdapter = new AnalyticsNewsFeedAdapter(getContext(),OmnioPostsList,mediaPlayer);
                        listView.setLayoutManager(new LinearLayoutManager(getContext()));
                        listView.setAdapter(analyticsNewsFeedAdapter);
                        // Set layout manager to position the items

                        postProgress.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else{
                listView.setVisibility(View.GONE);
                postProgress.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
            postProgress.setVisibility(View.VISIBLE);
        }

    }
    public void arrayListValues(List<String> listString){
        this.timeLists = listString;
    }


}