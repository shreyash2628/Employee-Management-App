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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAcitivity extends AppCompatActivity {


    EditText vemail,vpassword;
    String email,password;
    Button login;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(LoginAcitivity.this);
        progressDialog.setMessage("Signing In");


        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //login button visibility
        vpassword.addTextChangedListener(loginvisibility);
        vemail.addTextChangedListener(loginvisibility);


        //Login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!vemail.getText().toString().equals("") && !vemail.getText().toString().contains(" "))
                {
                    email = vemail.getText().toString();
                    if(!vpassword.getText().toString().equals("") && !vpassword.getText().toString().contains(" "))
                    {
                        password = vpassword.getText().toString();
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


    //Text watcher
    private TextWatcher loginvisibility = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String username = vemail.getText().toString();
            String password = vpassword.getText().toString();
            if(!username.isEmpty() && !password.isEmpty())
            {
                login.setText("Login");
                login.setAlpha(1);
                login.setEnabled(true);
            }
            else
            {
                login.setText("Enter details");
                login.setAlpha((float) 0.4);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //signin Function
    private void signin(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LoginAcitivity.this,"Authentication successful",Toast.LENGTH_LONG).show();
                            Intent n =new Intent(LoginAcitivity.this,DashBoard.class);
                            n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(n);
                        } else
                        {
                            progressDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(LoginAcitivity.this, error, Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


}