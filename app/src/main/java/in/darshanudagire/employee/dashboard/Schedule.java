package in.darshanudagire.employee.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.darshanudagire.employee.R;

public class Schedule extends AppCompatActivity {

    //variables
    CalendarView calendarView;
    TextView textView;
    String date_selected,event_name,event_description;
    Button addEvent;
    Bundle bundle;
    private Toolbar toolbar;
    private final int REQUEST_CODE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        calendarView = findViewById(R.id.calender);
        textView = findViewById(R.id.my_date);
        addEvent = findViewById(R.id.add_event_buton);
        toolbar = findViewById(R.id.toolbar_schedule);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                date_selected=d + "/" + m + "/" + y;
                textView.setText(date_selected);
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Schedule.this,"Under development",Toast.LENGTH_LONG).show();
            }
        });


        bundle=getIntent().getExtras();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE)
        {
            if (requestCode==RESULT_OK)
            {
                event_name=data.getStringExtra("eventname");
                event_description = data.getStringExtra("descriptionname");
            }
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }




}