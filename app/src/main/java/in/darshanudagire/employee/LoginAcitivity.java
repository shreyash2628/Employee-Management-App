package in.darshanudagire.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import in.darshanudagire.employee.dashboard.Attendence;

public class LoginAcitivity extends AppCompatActivity {

    //EditText vemail,vpassword;
    TextInputLayout vemail,vpassword;
    String email,password;
    Button login;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null)
        {
            startActivity(new Intent(LoginAcitivity.this,DashBoard.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);


        vemail = findViewById(R.id.edittextemail);
        vpassword = findViewById(R.id.edittextpassword);
        login = findViewById(R.id.loginbtn_id);



        //progress dialog
        //progrees dialog
        progressDialog = new ProgressDialog(LoginAcitivity.this);
        progressDialog.setMessage("Signing In");


        //  initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        //Login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vemail.getEditText().getText().toString().equals("") && !vemail.getEditText().getText().toString().contains(" "))
                {
                    email = vemail.getEditText().getText().toString();
                    if(!vpassword.getEditText().getText().toString().equals("") && !vpassword.getEditText().getText().toString().contains(" "))
                    {
                        password = vpassword.getEditText().getText().toString();
                        signin(email, password);
                    }
                    else
                    {
                        vpassword.setError("Enter a valid password");
                        vpassword.requestFocus();
                    }

                }
                else
                {
                    vemail.setError("Invalid email Id");
                    vemail.requestFocus();
                }

            }
        });

    }



    //signin Function
    private void signin(String email, final String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LoginAcitivity.this,"Authentication successful",Toast.LENGTH_LONG).show();
                            Intent n =new Intent(LoginAcitivity.this, DashBoard.class);
                            n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(n);
                        } else
                        {
                            String error = task.getException().getMessage();
                            Toast.makeText(LoginAcitivity.this, error, Toast.LENGTH_SHORT).show();

                        }

                    }


                });









    }
}