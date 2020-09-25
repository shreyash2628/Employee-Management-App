package in.darshanudagire.employee.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.darshanudagire.employee.Holders.AttendenceViewHolder;
import in.darshanudagire.employee.Models.AttendenceInfo;
import in.darshanudagire.employee.R;

public class Attendence extends AppCompatActivity {

    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    FirestoreRecyclerAdapter<AttendenceInfo, AttendenceViewHolder> adapter;;
    private RecyclerView recyclerView;
    private TextView empty;
    private Toolbar toolbar;


    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        recyclerView = findViewById(R.id.recycler);
        empty = findViewById(R.id.emptytxt);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //progrees dialog
        progressDialog = new ProgressDialog(Attendence.this);
        progressDialog.setMessage("Please wait");

        //linear layout
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //instance
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        updateUi();
    }



    private void updateUi() {
        progressDialog.show();
        final String email = auth.getCurrentUser().getEmail();
        Query query = db.collection("Attendence").document(email).collection("September").orderBy("date", Query.Direction.DESCENDING);



                FirestoreRecyclerOptions<AttendenceInfo> options = new FirestoreRecyclerOptions.Builder<AttendenceInfo>()
                        .setQuery(query, AttendenceInfo.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<AttendenceInfo, AttendenceViewHolder>(options) {

                    @NonNull
                    @Override
                    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attendence, parent, false);
                        return new AttendenceViewHolder(view);
                    }


                    @Override
                    public void onDataChanged()
                    {
                        if(getItemCount() == 0)
                        {
                            progressDialog.dismiss();
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AttendenceViewHolder holder, int position, @NonNull final AttendenceInfo model) {

                        holder.date.setText(model.getDate());
                        holder.startTime.setText("Start Time :- " + model.getStartTime());
                        holder.endTime.setText("End Time :- "+ model.getEndTime());
                        progressDialog.dismiss();
                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}