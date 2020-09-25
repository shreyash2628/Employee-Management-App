package in.darshanudagire.employee.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.darshanudagire.employee.R;

public class AttendenceViewHolder extends RecyclerView.ViewHolder {

    public TextView date,startTime,endTime;

    public AttendenceViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date_id);
        startTime = itemView.findViewById(R.id.starttime_id);
        endTime = itemView.findViewById(R.id.endtime_id);
    }
}
