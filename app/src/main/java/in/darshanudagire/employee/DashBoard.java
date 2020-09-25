package in.darshanudagire.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import in.darshanudagire.employee.Models.AttendenceInfo;
import in.darshanudagire.employee.dashboard.AppyForLeave;
import in.darshanudagire.employee.dashboard.Attendence;
import in.darshanudagire.employee.dashboard.Feedback;
import in.darshanudagire.employee.dashboard.Schedule;
import in.darshanudagire.employee.dashboard.Team;
import in.darshanudagire.employee.sidenavigation.AboutCompany;
import in.darshanudagire.employee.sidenavigation.MyProfile;
import in.darshanudagire.employee.sidenavigation.ReportAnIssue;
import in.darshanudagire.employee.sidenavigation.Share;

public class DashBoard extends AppCompatActivity {


    Drawer drawer = null;
    AccountHeader accountHeader = null;
    private Context context;
    private DisplayMetrics displayMetrics;
    private int widthScreen;
    private int heightScreen;
    private Toolbar toolbar;
    private TextView name,id;
    private String empName,empId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String emailid;
    private Button checkin,checkout;
    private TextView time;
    private String currentime;
    BroadcastReceiver _broadcastReceiver;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private ProgressDialog progressDialog;
    private CardView attendence,team,schedule,leave,myprofile,feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        //find view by Id
        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.empName_id);
        id = findViewById(R.id.empID_id);
        checkin = findViewById(R.id.check_in_button);
        checkout = findViewById(R.id.check_out_button);
        time = findViewById(R.id.timer);
        attendence = findViewById(R.id.attendance_cardview);
        team = findViewById(R.id.team_cardview);
        leave = findViewById(R.id.apply_leave_cardview);
        schedule = findViewById(R.id.schedule_cardview);
        myprofile = findViewById(R.id.my_profile_cardview);
        feedback = findViewById(R.id.feedback_cardview);


        final SharedPreferences pref = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(pref.getBoolean("started",false))
        {

            checkin.setEnabled(false);
            checkin.setAlpha((float) 0.3);
            checkout.setEnabled(true);
            checkout.setAlpha(1);
        }
        else
        {
            checkin.setEnabled(true);
            checkin.setAlpha(1);
            checkout.setEnabled(false);
            checkout.setAlpha((float) 0.3);
        }
        //get app context
        context = getApplicationContext();
        displayMetrics = new DisplayMetrics();
        displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        //progress dialog
        progressDialog = new ProgressDialog(DashBoard.this);
        progressDialog.setMessage("Loading");


        //firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //get email id
        emailid = mAuth.getCurrentUser().getEmail();

        //clock
        final Handler handler = new Handler();
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Do your refreshing
                DateFormat df = new SimpleDateFormat("hh:mm aa");
                 currentime = df.format(Calendar.getInstance().getTime());
                time.setText(currentime);
                //This basically reruns this runnable in 5 seconds
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);



        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.MainMyToolbarStyle);
        widthScreen = (int) Math.abs(dpWidth * 0.7);
        heightScreen = (int) Math.abs(dpHeight * 0.3);



        //get data
        db.collection("Users").document(emailid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    empName = task.getResult().get("name").toString();
                    empId = task.getResult().get("empID").toString();
                    name.setText(empName);
                    id.setText("ID - "+empId);
                    //built side navigation Bar
                    buildDrawer();
                }
                else
                {
                    Toast.makeText(context,"Somethings went Wrong !",Toast.LENGTH_LONG).show();
                }
            }
        });

        //attendence
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this,Attendence.class);
                startActivity(intent);
            }
        });


        //team
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, Team.class);
                startActivity(intent);
            }
        });


        //leave
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppyForLeave.class);
                startActivity(intent);
            }
        });

        //feedback
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Feedback.class);
                startActivity(intent);
            }
        });

        //my profile
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MyProfile.class);
                startActivity(intent);
            }
        });

        //schedule
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Schedule.class);
                startActivity(intent);
            }
        });

        //check in
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout.setAlpha(1);
                checkout.setEnabled(true);
                checkin.setAlpha((float) 0.3);
                checkin.setEnabled(false);

                pref.edit().putString("startTime",String.valueOf(currentime)).apply();
                pref.edit().putBoolean("started",true).apply();
            }
        });


        checkout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                checkout.setAlpha((float) 0.3);
                checkout.setEnabled(false);
                checkin.setAlpha(1);
                checkin.setEnabled(true);
                pref.edit().putBoolean("started",false).apply();
                String date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());
                AttendenceInfo attendenceInfo = new AttendenceInfo(pref.getString("startTime",null),currentime,date);
                DateFormat dateFormat = new SimpleDateFormat("MM");
                Date month = new Date();
                db.collection("Attendence").document(emailid).collection("September").document(date).set(attendenceInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Attendence Registered",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });





    }


    private void buildDrawer() {


        PrimaryDrawerItem profile = new PrimaryDrawerItem().withName("My Profile").withIcon(R.drawable.ic_baseline_account_circle_black_24).withSelectable(false);
        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().withName("About Us").withIcon(R.drawable.about_us).withSelectable(false);
        PrimaryDrawerItem issue = new PrimaryDrawerItem().withName("Report an Issue").withIcon(R.drawable.ic_bug_report_black_24dp).withSelectable(false);
        PrimaryDrawerItem share = new PrimaryDrawerItem().withName("Share").withIcon(R.drawable.ic_baseline_share_24).withSelectable(false);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout).withSelectable(false);

        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withEmail(emailid).withIcon(R.drawable.ic_baseline_account_circle_24).withName(empName);

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .addProfiles(profileDrawerItem)
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .withTextColor(getResources().getColor(R.color.md_white_1000))
                .withHeightDp(180)
                .withSelectionListEnabled(false)
                .withProfileImagesClickable(false)
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(profile,aboutUs,issue,share,logout)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggleAnimated(false)
                .withDrawerWidthDp(widthScreen)
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 1:
                                //my profile
                                Intent intent = new Intent(DashBoard.this, MyProfile.class);
                                startActivity(intent);
                                return true;
                            case 2:
                                //About us
                                Intent intent1 = new Intent(DashBoard.this, AboutCompany.class);
                                startActivity(intent1);
                                return true;

                            case 3:
                                //Report a issue
                                Intent intent2 = new Intent(DashBoard.this, ReportAnIssue.class);
                                startActivity(intent2);
                                return true;

                            case 4:
                                //share
                                Intent intent3=new Intent(Intent.ACTION_SEND);
                                intent3.putExtra(Intent.EXTRA_TEXT,"The Employer App \n Link:- https://drive.google.com/drive/folders/1cDkL-2x2q3YKBvZELpAOWSn6CcAatd8K?usp=sharing");          //inside value appp name
                                        intent3.setType("text/plain");
                                startActivity(Intent.createChooser(intent3,"share via"));
                                return true;

                            case 5:
                                //logout
                                mAuth.signOut();
                                Intent intent4 = new Intent(DashBoard.this,LoginAcitivity.class);
                                startActivity(intent4);
                                finish();
                                Toast.makeText(DashBoard.this, "Signed Out", Toast.LENGTH_LONG).show();


                        }
                        return false;
                    }
                })
                .build();

    }
}