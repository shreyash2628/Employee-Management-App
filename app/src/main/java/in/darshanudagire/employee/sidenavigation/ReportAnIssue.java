package in.darshanudagire.employee.sidenavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import in.darshanudagire.employee.R;

public class ReportAnIssue extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText issue;
    private Button submit;
    private FirebaseFirestore db;
    private String issuetxt,emailid;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_an_issue);



        toolbar = findViewById(R.id.toolbar);
        submit = findViewById(R.id.submitBtn_id);
        issue = findViewById(R.id.issuetxt_id);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(ReportAnIssue.this);
        progressDialog.setMessage("Processing !");

        //firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailid = mAuth.getCurrentUser().getEmail();
                issuetxt = issue.getText().toString();

                HashMap<String,Object> fullissue = new HashMap<>();
                fullissue.put("issueDescription",issuetxt);


                if(!issuetxt.isEmpty())
                {
                    progressDialog.show();
                    db.collection("Issues").document(emailid).set(fullissue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(ReportAnIssue.this,"Your respose is recorded ! Thank You",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(ReportAnIssue.this,"Something went Wrong",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(ReportAnIssue.this,"Enter your Issue",Toast.LENGTH_LONG).show();
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