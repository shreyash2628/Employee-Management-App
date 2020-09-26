package in.darshanudagire.employee.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import in.darshanudagire.employee.Models.ApplyLeaveInfo;
import in.darshanudagire.employee.R;
import in.darshanudagire.employee.sidenavigation.ReportAnIssue;

public class AppyForLeave extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseFirestore db;
    private String emailid;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private EditText start_date_et,end_date_et,absence_name_et,comments_et,totaldays;
    private TextView availableLeaves;
    private String start_date,end_date,absence_name,comments,totalsdaysstring;
    private int availableLeavestxt,currentLeaves;
    private Button button;
    private DocumentReference doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appy_for_leave);

        toolbar = findViewById(R.id.apply_leavetoolbar);
        start_date_et = findViewById(R.id.start_dateinfo_id);
        end_date_et = findViewById(R.id.end_dateinfo_id);
        absence_name_et = findViewById(R.id.absence_nameinfo_id);
        comments_et = findViewById(R.id.req_comments_id);
        button = findViewById(R.id.apply_id);
        totaldays = findViewById(R.id.totalsdays_id);
        availableLeaves = findViewById(R.id.availableLeaves_id);


        progressDialog = new ProgressDialog(AppyForLeave.this);
        progressDialog.setMessage("Processing !");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //check for leaves
        checkLeaveAvailable();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_date=start_date_et.getText().toString();
                end_date=end_date_et.getText().toString();
                absence_name=absence_name_et.getText().toString();
                comments=comments_et.getText().toString();
                emailid = mAuth.getCurrentUser().getEmail();
                totalsdaysstring =  totaldays.getText().toString();


                if(!start_date.isEmpty() && !end_date.isEmpty() && !absence_name.isEmpty() && !comments.isEmpty())
                {
                    if(isLeaveAvailable())
                    {
                        progressDialog.show();
                        doc = db.collection("Leaves").document(emailid);
                        String uniqueid = db.collection("Leaves").document(emailid).collection("September").document().getId();
                        ApplyLeaveInfo applyLeaveInfo = new ApplyLeaveInfo(start_date,end_date,totalsdaysstring,absence_name,comments);
                        db.collection("Leaves").document(emailid).collection("September").document(uniqueid).set(applyLeaveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    db.collection("Leaves").document(emailid).update("availableLeaves",currentLeaves).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(AppyForLeave.this, "Applied for Leave",Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(AppyForLeave.this, "Something went wrong",Toast.LENGTH_LONG).show();
                                            }
                                            finish();
                                        }
                                    });
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(AppyForLeave.this, "Something went wrong",Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(AppyForLeave.this, "No leaves available",Toast.LENGTH_LONG).show();
                    }

                }
                else
                    {
                        Toast.makeText(AppyForLeave.this, "Enter all details",Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private boolean isLeaveAvailable() {
        currentLeaves  = availableLeavestxt - Integer.parseInt(totalsdaysstring);
        if(currentLeaves>=0)
        {
            return true;
        }

        return false;
    }

    private void checkLeaveAvailable() {
        progressDialog.show();
        emailid = mAuth.getCurrentUser().getEmail();
        db.collection("Leaves").document(emailid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if(task.isSuccessful())
           {
               progressDialog.dismiss();
               availableLeavestxt = Integer.parseInt(task.getResult().get("availableLeaves").toString());
               availableLeaves.setText("Available Leaves : "+ String.valueOf(availableLeavestxt));
           }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}