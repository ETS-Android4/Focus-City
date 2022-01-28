package com.candy.focuscity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.candy.focuscity.BlueprintActivity;
import com.candy.focuscity.DatabaseHandlers.BlueprintsDatabaseHandler;
import com.candy.focuscity.MainActivity;
import com.candy.focuscity.Model.BlueprintModel;
import com.candy.focuscity.Model.RecordModel;
import com.candy.focuscity.R;
import com.candy.focuscity.RecordsActivity;

import java.util.List;

public class BlueprintAdapter extends RecyclerView.Adapter<BlueprintAdapter.ViewHolder> {

    private List<BlueprintModel> blueprintList;
    private Context context;
    private BlueprintActivity activity;
    private BlueprintsDatabaseHandler db;

    public BlueprintAdapter(BlueprintsDatabaseHandler db, Context context, BlueprintActivity activity) {
        this.context = context;
        this.activity = activity;
        this.db = db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blueprint_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        BlueprintModel item = blueprintList.get(position);
        holder.blueprintBuildingImage.setImageResource(item.getBuildingImageId());
        holder.blueprintBuildingName.setText(item.getBuildingName());
        holder.blueprintTotalMinutes.setText(String.format("%d\n"+"Mins", item.getTotalMinutes()));
        holder.blueprintBuildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("buildingId", item.getBuildingImageId());
                i.putExtra("taskName", item.getBuildingName());
                i.putExtra("totalMinutes", item.getTotalMinutes());
                context.startActivity(i);
            }
        });
        holder.blueprintCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(holder.getAdapterPosition());
            }
        });
    }

    public int getItemCount() {
        return blueprintList.size();
    }

    public void deleteItem(int position) {
        BlueprintModel item = blueprintList.get(position);
        db.deleteBlueprint(item.getId());
        blueprintList.remove(position);
        notifyItemRemoved(position);
        if (blueprintList.isEmpty()) {
            activity.textViewWhenEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void setBlueprints(List<BlueprintModel> blueprintList) {
        this.blueprintList = blueprintList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView blueprintBuildingImage;
        TextView blueprintBuildingName;
        TextView blueprintTotalMinutes;
        Button blueprintBuildButton;
        ImageButton blueprintCloseButton;

        ViewHolder(View view) {
            super(view);
            blueprintBuildingImage = view.findViewById(R.id.blueprintBuildingImage);
            blueprintBuildingName = view.findViewById(R.id.blueprintBuildingName);
            blueprintTotalMinutes = view.findViewById(R.id.blueprintTotalMinutes);
            blueprintBuildButton = view.findViewById(R.id.blueprintBuildButton);
            blueprintCloseButton = view.findViewById(R.id.blueprintCloseButton);
        }

    }


}
