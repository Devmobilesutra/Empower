package seedcommando.com.empowerapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.LinkedHashMap;

import seedcommando.com.empowerapp.applicationaprovels.ApplicationAprovelsActivity;
import seedcommando.com.empowerapp.applicationstatus.ApplicationStatusActivity;
import seedcommando.com.empowerapp.attendancedetailsactivity.AttendanceDetailsActivity;
import seedcommando.com.empowerapp.constantclass.EmpowerApplication;
import seedcommando.com.empowerapp.discrepanciesactivity.DiscrepanciesActivity;
import seedcommando.com.empowerapp.service.MyFirebaseMessagingService;
import seedcommando.com.empowerapp.shiftallocation.ShiftAllocationActivity;
import seedcommando.com.empowerapp.utilitys.NotificationUtils;

/**
 * Created by commando1 on 8/11/2017.
 */

public class ManagerActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar ;
    //private Menu menu;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

   // public   static LayerDrawable layerDrawable;
    TextView username,designation,email,gender,dateofbirth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
       // FirebaseCrash.report(new Exception("My first Android ` error"));


        toolbar= findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Manager");
        toolbar.setLogo(R.drawable.em_logo);
      // toolbar.setNavigationIcon(R.drawable.ic_action_toolbarleft_arrow);
        drawer = findViewById(R.id.drawer_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(Gravity.LEFT);

            }
        });

        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        username=header.findViewById(R.id.username);
        gender=header.findViewById(R.id.gendertxt);
        designation=header. findViewById(R.id.professiontxt);
        dateofbirth=header. findViewById(R.id.dobtxt);
        email=header. findViewById(R.id.emailtext);
        HomeFragment fragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fr_frame, fragment, "Manager");
        ft.commit();
        setUpNavigationView();
        LinkedHashMap<String, String> data=  ((EmpowerApplication) getApplication()).PersonalDetails(EmpowerApplication.get_session("employeeId"));
      //for(int i=0;i<data.size();i++) {
        if(data !=null) {
            username.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("FirstName")) + " " + EmpowerApplication.aesAlgorithm.Decrypt(data.get("Last_Name")) + "(" + EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode")) + ")");
            designation.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("Designation_Name")));
            email.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("Department_Name")));
            gender.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("Grade_Name")));
            designation.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("Designation_Name")));
            dateofbirth.setText(EmpowerApplication.aesAlgorithm.Decrypt(data.get("Category_Name")));
            EmpowerApplication.set_session("Gender",EmpowerApplication.aesAlgorithm.Decrypt(data.get("Gender")));
        }
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

                   // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {



            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case 1:
                        // launch new intent instead of loading fragment
                        if(HomeFragment.key.size() > 0){
                        startActivity(new Intent(ManagerActivity.this, ApplicationStatusActivity.class));
                        drawer.closeDrawers();
                        return true;
                        }else {

                            EmpowerApplication.alertdialog("No Application available",ManagerActivity.this);
                            return true;
                        }
                    case 2:
                        // launch new intent instead of loading fragment

                        startActivity(new Intent(ManagerActivity.this, AttendanceDetailsActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case 3:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(ManagerActivity.this, ShiftAllocationActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case 4:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(ManagerActivity.this, DiscrepanciesActivity.class));
                        drawer.closeDrawers();
                        return true;

                    //for logout  functionalties.....
                    case 7:

                        if(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("AllowLoginUsingOTP")).equals("1")){
                        Intent i = new Intent(getApplicationContext(), OTPLoginActivity.class);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        EmpowerApplication.set_session("isLogout", "Yes");
                            EmpowerApplication.set_session("data", "");

                        // Staring Login Activity
                        startActivity(i);
                        // startActivity(intent);
                        drawer.closeDrawers();
                        finish();
                        return true;
                    }else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |

                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                            EmpowerApplication.set_session("isLogout", "Yes");
                            EmpowerApplication.set_session("data", "");

                            // Staring Login Activity
                            startActivity(i);
                            // startActivity(intent);
                            drawer.closeDrawers();
                            finish();
                            return true;
                        }
                        //for logout  functionalties.....
                    case 5:
                        startActivity(new Intent(ManagerActivity.this, AboutUs.class));
                        drawer.closeDrawers();
                        return true;


                    case 6:
                        startActivity(new Intent(ManagerActivity.this, ChangepwdActivity.class));
                        drawer.closeDrawers();
                        return true;


                }


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
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
        if (id == R.id.action_sync) {

            return false;
        }
        return false;
    }

   /* public static void badgecount(Context context){
      MyFirebaseMessagingService.setBadgeCount(context,layerDrawable, String.valueOf(MyFirebaseMessagingService.count1));

    }*/
   /* private  void updatemenu(int count) {
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable   layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable, String.valueOf(count));



    }*/

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
    private String getFirebaseCount() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("Count", null);

       // Log.e( "count: " , regId);

        return regId;


    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable   layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable,"200");

        return  super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemCart = menu.findItem(R.id.action_notification);
        LayerDrawable   layerDrawable = (LayerDrawable) itemCart.getIcon();
        MyFirebaseMessagingService.setBadgeCount(this,layerDrawable, String.valueOf(MyFirebaseMessagingService.count1));

        return true;
    }


}

