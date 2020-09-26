package in.darshanudagire.employee.sidenavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import in.darshanudagire.employee.DashBoard;
import in.darshanudagire.employee.Models.UserInfo;
import in.darshanudagire.employee.R;

public class MyProfile extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText name,id,designation,number,location,email;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String emailid,nametxt,numbertxt,locationtxt,emailtxt;
    private Button edit,applyChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        name = findViewById(R.id.name_et);
        id = findViewById(R.id.id_et);
        designation= findViewById(R.id.designation_et);
        number = findViewById(R.id.contact_et);
        location = findViewById(R.id.location_et);
        email = findViewById(R.id.email_et);
        edit = findViewById(R.id.editBtn_id);
        applyChanges = findViewById(R.id.applychangesBtn_id);
        toolbar = findViewById(R.id.toolbar_myprofile);

        //progress dialog
        progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Loading");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firestore instance
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //get email id
        emailid = mAuth.getCurrentUser().getEmail();


        //get data
        progressDialog.show();
        db.collection("Users").document(emailid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                    name.setText(userInfo.getName());
                    id.setText(userInfo.getEmpID());
                    designation.setText(userInfo.getDesignation());
                    number.setText(userInfo.getNumber());
                    location.setText(userInfo.getLocation());
                    email.setText(userInfo.getEmail());
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(MyProfile.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                name.setFocusable(true);
                number.setEnabled(true);
                number.setInputType(InputType.TYPE_CLASS_TEXT);
                number.setFocusable(true);
                location.setEnabled(true);
                location.setInputType(InputType.TYPE_CLASS_TEXT);
                location.setFocusable(true);
                email.setEnabled(true);
                email.setInputType(InputType.TYPE_CLASS_TEXT);
                email.setFocusable(true);
                edit.setVisibility(View.GONE);
                edit.setEnabled(false);
                applyChanges.setVisibility(View.VISIBLE);
                applyChanges.setEnabled(true);
            }
        });


        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nametxt = name.getText().toString();
                locationtxt = location.getText().toString();
                numbertxt = number.getText().toString();
                emailtxt = email.getText().toString();
                edit.setVisibility(View.VISIBLE);
                edit.setEnabled(true);

                if(!nametxt.isEmpty() && !locationtxt.isEmpty() && !numbertxt.isEmpty() && !emailtxt.isEmpty())
                {
                    db.collection("Users").document(emailid).update("name",nametxt,"email",emailtxt,"location",locationtxt,"number",numbertxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(MyProfile.this,"Data updated",Toast.LENGTH_LONG).show();
                           name.setEnabled(false);
                           name.setInputType(InputType.TYPE_NULL);
                           name.setFocusable(false);
                           number.setEnabled(false);
                           number.setInputType(InputType.TYPE_NULL);
                           number.setFocusable(false);
                           location.setEnabled(false);
                           location.setInputType(InputType.TYPE_NULL);
                           location.setFocusable(false);
                           email.setEnabled(false);
                           email.setInputType(InputType.TYPE_NULL);
                           email.setFocusable(false);
                           edit.setVisibility(View.VISIBLE);
                           edit.setEnabled(true);
                           applyChanges.setVisibility(View.GONE);
                           applyChanges.setEnabled(false);
                       }
                       else
                       {
                           Toast.makeText(MyProfile.this,"Something went wrong",Toast.LENGTH_LONG).show();
                       }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MyProfile.this,"Fill all details",Toast.LENGTH_LONG).show();
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