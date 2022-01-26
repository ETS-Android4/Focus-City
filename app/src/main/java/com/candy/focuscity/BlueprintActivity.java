package com.candy.focuscity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.candy.focuscity.Adapter.BlueprintAdapter;
import com.candy.focuscity.Adapter.RecordAdapter;
import com.candy.focuscity.DatabaseHandlers.BlueprintsDatabaseHandler;
import com.candy.focuscity.DatabaseHandlers.RecordsDatabaseHandler;
import com.candy.focuscity.Model.BlueprintModel;
import com.candy.focuscity.Model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class BlueprintActivity extends AppCompatActivity {


    private RecyclerView blueprintRecyclerView;
    private BlueprintAdapter blueprintAdapter;
    private BlueprintsDatabaseHandler db;
    private TextView textViewWhenEmpty;

    private List<BlueprintModel> blueprintsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueprint);

        blueprintsList = new ArrayList<>();
        textViewWhenEmpty = (TextView) findViewById(R.id.blueprintTextViewWhenEmpty);

        db = new BlueprintsDatabaseHandler(getApplicationContext());
        db.openDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        blueprintRecyclerView = (RecyclerView) findViewById(R.id.blueprintsRecyclerView);
        blueprintRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        blueprintAdapter = new BlueprintAdapter(this);
        blueprintRecyclerView.setAdapter(blueprintAdapter);

        blueprintsList = db.getAllBlueprints();
        blueprintAdapter.setBlueprints(blueprintsList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        blueprintRecyclerView.setLayoutManager(gridLayoutManager);
        blueprintAdapter.setBlueprints(blueprintsList);

        // Show Empty Records Hint when Records is Empty
        if (!blueprintsList.isEmpty()) {
            textViewWhenEmpty.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}