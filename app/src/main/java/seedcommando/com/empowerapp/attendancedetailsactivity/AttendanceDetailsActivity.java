package seedcommando.com.empowerapp.attendancedetailsactivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seedcommando.com.empowerapp.Config;
import seedcommando.com.empowerapp.Utilities;
import seedcommando.com.empowerapp.applicationaprovels.ApplicationAprovelsActivity;
import seedcommando.com.empowerapp.constantclass.EmpowerApplication;
import seedcommando.com.empowerapp.R;
import seedcommando.com.empowerapp.manageractivity.CalendarView;
import seedcommando.com.empowerapp.manageractivity.CalenderFragment;
import seedcommando.com.empowerapp.pojos.Data;
import seedcommando.com.empowerapp.pojos.attendancedetails.AttendanceDetails;
import seedcommando.com.empowerapp.rest.ApiClient;
import seedcommando.com.empowerapp.rest.ApiInterface;
import seedcommando.com.empowerapp.service.MyFirebaseMessagingService;
import seedcommando.com.empowerapp.utilitys.NotificationUtils;

/**
 * Created by commando1 on 9/1/2017.
 */

public class AttendanceDetailsActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<AttendanceDetailsPOJO> arrayList;
    ProgressDialog pd;
    private ApiInterface apiService;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // default date format
    private static final String DATE_FORMAT_calender = "dd-MMM-yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();


    //event handling
    private CalendarView.EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
   SimpleDateFormat format12 = new SimpleDateFormat("dd MMM yyyy");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.attendance_detail_layout);

     /*  toolbar decalration....*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setLogo(R.drawable.em_logo);

        apiService = ApiClient.getClient().create(ApiInterface.class);

       /* TextView textView = (TextView) findViewById(R.id.title);
        textView.setText("Attendance Details");*/

      /* ................. */

        listView = findViewById(R.id.list);
        //Log.e("data", MainActivity.arrayList.toString());
       // setUpExpList();
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(c.getTime());



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    invalidateOptionsMenu();//Activity method
                    //updateMenuCounts(MyFirebaseMessagingService.count1);

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }

            }
        };
        //updateAttendanceDetails();
        assignUiElements();
        assignClickHandlers();
        updateAttendanceDetails();




    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        invalidateOptionsMenu();
        MenuItem item = menu.findItem(R.id.action_sync);
        item.setVisible(false);
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable,"200");

        return  super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable   layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable, String.valueOf(MyFirebaseMessagingService.count1));
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, ApplicationAprovelsActivity.class);
            this.startActivity(intent);
            MyFirebaseMessagingService.count1=0;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    public void AttandanceDetails(String id,String date) {

        pd = new ProgressDialog(AttendanceDetailsActivity.this);
        pd.setMessage("Loading....");
        pd.show();

        Map<String,String> attdetails=new HashMap<String,String>();


        attdetails.put("employeeID",id);
        attdetails.put("fromDate",date);
        Log.e("id",id);
        Log.e("fromdate",date);

        arrayList  = new ArrayList<AttendanceDetailsPOJO>();

        Call<AttendanceDetails> call = apiService.getAttendanceDetails(attdetails);
        call.enqueue(new Callback<AttendanceDetails>() {
            @Override
            public void onResponse(retrofit2.Call<AttendanceDetails> call, Response<AttendanceDetails> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    SimpleDateFormat format,format1,format2,format0,format3,format4;

                    if (response.body().getStatus().equals("1")) {

                        for(int i=0;i<response.body().getData().size();i++){
                            AttendanceDetailsPOJO attendanceDetailsPOJO=new AttendanceDetailsPOJO();

                            // if(response.body().getData().get(i).getFromTime().length()==0) {
                            format = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
                            format0 = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
                            format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            format4 = new SimpleDateFormat("dd-MMM");
                            format2 = new SimpleDateFormat("hh:mm aa");
                            format3 = new SimpleDateFormat("hh:mm aa");

                            try {
                                Date date = format.parse(response.body().getData().get(i).getShiftDate());
                                Log.e("id1",response.body().getData().get(i).getShiftDate());
                                attendanceDetailsPOJO.setDate(format4.format(date));
                               // if(response.body().getData().get(i).getInTime()==null && response.body().getData().get(i).getInTime().isEmpty()) {
                                    Date date1 = format.parse(response.body().getData().get(i).getInTime());
                                    Log.e("id1", response.body().getData().get(i).getInTime());
                                Date date3 = format0.parse(response.body().getData().get(i).getOutTime());
                                Log.e("id1",response.body().getData().get(i).getOutTime());

                                    if(format1.format(date1).equals("01-Jan-1900")){
                                        attendanceDetailsPOJO.setIn("-");
                                       // attendanceDetailsPOJO.setOut("");

                                    }else {
                                    attendanceDetailsPOJO.setIn(format2.format(date1));
                                       // attendanceDetailsPOJO.setOut(format3.format(date3));
                                    }
                                if(format1.format(date3).equals("01-Jan-1900")){
                                    //attendanceDetailsPOJO.setIn("");
                                    attendanceDetailsPOJO.setOut("-");

                                }else {
                                    //attendanceDetailsPOJO.setIn(format2.format(date1));
                                    attendanceDetailsPOJO.setOut(format3.format(date3));
                                }
                               // }


                                attendanceDetailsPOJO.setManhrs(formatTime(response.body().getData().get(i).getManHours()));
                                attendanceDetailsPOJO.setStatus(response.body().getData().get(i).getDayStatusCode());


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            arrayList.add(attendanceDetailsPOJO);
                            Log.e("data added",arrayList.get(i).toString());
                        }
                        AttendanceDetailsAdapter adapter = new AttendanceDetailsAdapter(AttendanceDetailsActivity.this, arrayList);
                        Log.e("id1","array adapter called");
                        listView.setAdapter(adapter);

                    }else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), AttendanceDetailsActivity.this);
                    }
                }else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", AttendanceDetailsActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", AttendanceDetailsActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", AttendanceDetailsActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }

            @Override
            public void onFailure(retrofit2.Call<AttendanceDetails> call, Throwable t) {
                // Log error here since request failed
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), AttendanceDetailsActivity.this);
               // Log.e("TAG", t.toString());

            }
        });
    }

    public String formatTime(String manHours){

        String formatTime1="00:00";

        try
        {
            //String srcSource = "14-Mar-18(1.5) , \r15-Mar-18(1.5)";
            String[] Arr1 = manHours.split(":");
            if (Arr1.length > 0)
            {
                Log.e("fString",Arr1[0]);
                Log.e("sString",Arr1[1]);
                String hrstr = Integer.parseInt(Arr1[0]) < 10 ? '0' + Arr1[0] : Arr1[0];
                String minstr = Integer.parseInt(Arr1[1]) < 10 ? '0' + Arr1[1] : Arr1[1];
                Log.e("1fString", String.valueOf(hrstr));
                Log.e("2sString", String.valueOf(minstr));

                formatTime1= hrstr+":"+minstr;
                }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return formatTime1;
    }

    private void assignUiElements()
    {

        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);

    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, 1);
                updateAttendanceDetails();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, -1);
                updateAttendanceDetails();
            }
        });




        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AttendanceDetailsActivity.this, date, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            currentDate.set(Calendar.YEAR, year);
            currentDate.set(Calendar.MONTH, monthOfYear);
            currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //updateLabel();
            SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);
            Log.e("data123456789",sdf1.format(currentDate.getTime()));
            updateAttendanceDetails();
        }

    };

    public void updateAttendanceDetails(){

        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_calender);
        if(Utilities.isNetworkAvailable(AttendanceDetailsActivity.this)) {
            AttandanceDetails(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")),format12.format(currentDate.getTime()));

            // DescripanciesData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId")));
        }else {
            Toast.makeText(AttendanceDetailsActivity.this,"No Internet Connection...",Toast.LENGTH_LONG).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        txtDate.setText(sdf.format(currentDate.getTime()));

    }
}
