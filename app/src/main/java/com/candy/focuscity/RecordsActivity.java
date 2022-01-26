package com.candy.focuscity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.candy.focuscity.Adapter.RecordAdapter;
import com.candy.focuscity.DatabaseHandlers.RecordsDatabaseHandler;
import com.candy.focuscity.Model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private RecyclerView recordsRecyclerView;
    private RecordAdapter recordAdapter;
    private RecordsDatabaseHandler db;
    private TextView textViewWhenEmpty;

    private List<RecordModel> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordsList = new ArrayList<>();
        textViewWhenEmpty = (TextView) findViewById(R.id.textViewWhenEmpty);

        db = new RecordsDatabaseHandler(getApplicationContext());
        db.openDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recordsRecyclerView = (RecyclerView) findViewById(R.id.recordsRecyclerView);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter(this);
        recordsRecyclerView.setAdapter(recordAdapter);

        recordsList = db.getAllRecords();
        recordAdapter.setRecords(recordsList);

        // Show Empty Records Hint when Records is Empty
        if (!recordsList.isEmpty()) {
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