package com.candy.focuscity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.candy.focuscity.Model.RecordModel;
import com.candy.focuscity.R;
import com.candy.focuscity.RecordsActivity;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<RecordModel> recordList;
    private RecordsActivity activity;

    public RecordAdapter(RecordsActivity activity) {
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        RecordModel item = recordList.get(position);
        holder.recordsBuildingImage.setImageResource(item.getBuildingImageId());
        holder.recordsBuildingName.setText(item.getBuildingName());
        holder.recordsDateTime.setText(item.getDateTimeFormatted());
        holder.recordsTotalMinutes.setText(String.format("%d\n"+"Mins", item.getTotalMinutes()));
    }

    public int getItemCount() {
        return recordList.size();
    }

    public void setRecords(List<RecordModel> recordList) {
        this.recordList = recordList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recordsBuildingImage;
        TextView recordsBuildingName;
        TextView recordsDateTime;
        TextView recordsTotalMinutes;

        ViewHolder(View view) {
            super(view);
            recordsBuildingImage = view.findViewById(R.id.recordsBuildingImage);
            recordsBuildingName = view.findViewById(R.id.recordsBuildingName);
            recordsDateTime = view.findViewById(R.id.recordsDateTime);
            recordsTotalMinutes = view.findViewById(R.id.recordsTotalMinutes);
        }

    }


}
