package in.darshanudagire.employee.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;

import in.darshanudagire.employee.R;

public class DashBoard extends AppCompatActivity {

    CardView schedule,team,leave,attendance,poll,my_profile;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);

        //Hooks

//        drawerLayout = findViewById(R.id.drawer_Layout);
//        navigationView = findViewById(R.id.navigation_view);

        schedule= findViewById(R.id.schedule_cardview);
        team = findViewById(R.id.team_cardview);
        leave = findViewById(R.id.team_cardview);
        attendance = findViewById(R.id.attendance_cardview);
        poll = findViewById(R.id.poll_cardview);
        my_profile=findViewById(R.id.poll_cardview);



        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_schedule = new Intent(DashBoard.this,Schedule.class);
                startActivity(intent_schedule);
            }
        });

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_team = new Intent(DashBoard.this,Team.class);
                startActivity(intent_team);

            }
        });


        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_leave = new Intent(DashBoard.this,Team.class);
                startActivity(intent_leave);

            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_attendance = new Intent(DashBoard.this,Attendence.class);
                startActivity(intent_attendance);
            }
        });


        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_poll = new Intent(DashBoard.this,Poll.class);
                startActivity(intent_poll);
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, MyProfileDashBoard.class);
                startActivity(intent);
            }
        });


















    }
}