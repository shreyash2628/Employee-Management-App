package in.darshanudagire.employee.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.security.auth.Subject;

import in.darshanudagire.employee.Models.FeedbackInfo;
import in.darshanudagire.employee.R;

public class Feedback extends AppCompatActivity {

    private RadioGroup exp,des;
    private String experience,type,descriptiontxt,emailid,name;
    private Button submit;
    private EditText description;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //find view by Id
        exp = findViewById(R.id.radioexpgrp_id);
        des = findViewById(R.id.radiodesgrp_id);
        submit = findViewById(R.id.submitBtn_id);
        description = findViewById(R.id.edittxtdes);
        toolbar = findViewById(R.id.toolbar);

        //firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress dialog
        progressDialog = new ProgressDialog(Feedback.this);
        progressDialog.setMessage("Processing");




        exp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.good_id:
                        experience = "Good";
                        break;
                    case R.id.satify_id:
                        experience = "Satifying";
                        break;
                    case R.id.bad_id:
                        experience = "Bad";
                        break;

                }
            }
        });

        des.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.bug_id:
                        type = "Bug";
                        break;
                    case R.id.suggestion_id:
                        type = "Suggestion";
                        break;
                    case R.id.others_id:
                        type = "Other";
                        break;

                }
            }
       });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailid = mAuth.getCurrentUser().getEmail();
                descriptiontxt = description.getText().toString();
                if(!descriptiontxt.isEmpty() && !experience.isEmpty() && !type.isEmpty())
                {
                    progressDialog.show();
                    FeedbackInfo feedbackInfo = new FeedbackInfo(descriptiontxt,type,experience);
                    db.collection("Feedback").document(emailid).set(feedbackInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Feedback.this,"Feedback Recorded",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Feedback.this,"Something went wrong",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Feedback.this,"Enter all Details",Toast.LENGTH_LONG).show();
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