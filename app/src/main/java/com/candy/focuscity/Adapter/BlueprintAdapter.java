package com.candy.focuscity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.candy.focuscity.BlueprintActivity;
import com.candy.focuscity.Model.BlueprintModel;
import com.candy.focuscity.Model.RecordModel;
import com.candy.focuscity.R;
import com.candy.focuscity.RecordsActivity;

import java.util.List;

public class BlueprintAdapter extends RecyclerView.Adapter<BlueprintAdapter.ViewHolder> {

    private List<BlueprintModel> blueprintList;
    private BlueprintActivity activity;

    public BlueprintAdapter(BlueprintActivity activity) {
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blueprint_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        BlueprintModel item = blueprintList.get(position);
        holder.blueprintBuildingImage.setImageResource(item.getBuildingImageId());
        holder.blueprintBuildingName.setText(item.getBuildingName());
        holder.blueprintTotalMinutes.setText(String.format("%d\n"+"Mins", item.getTotalMinutes()));
    }

    public int getItemCount() {
        return blueprintList.size();
    }

    public void setBlueprints(List<BlueprintModel> blueprintList) {
        this.blueprintList = blueprintList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView blueprintBuildingImage;
        TextView blueprintBuildingName;
        TextView blueprintTotalMinutes;

        ViewHolder(View view) {
            super(view);
            blueprintBuildingImage = view.findViewById(R.id.blueprintBuildingImage);
            blueprintBuildingName = view.findViewById(R.id.blueprintBuildingName);
            blueprintTotalMinutes = view.findViewById(R.id.blueprintTotalMinutes);
        }

    }


}
