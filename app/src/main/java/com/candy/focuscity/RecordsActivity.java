package com.candy.focuscity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.candy.focuscity.Adapter.RecordAdapter;
import com.candy.focuscity.Model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private RecyclerView recordsRecyclerView;
    private RecordAdapter recordAdapter;
    private DatabaseHandler db;

    private List<RecordModel> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recordsList = new ArrayList<>();

        db = new DatabaseHandler(getApplicationContext());
        db.openDatabase();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recordsRecyclerView = (RecyclerView) findViewById(R.id.recordsRecyclerView);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter(this);
        recordsRecyclerView.setAdapter(recordAdapter);

        //RecordModel record = new RecordModel();
        recordsList = db.getAllRecords();

//        record.setDateTimeFormatted();
//        record.setTotalMinutes(60);
//        record.setBuildingImageId(R.drawable.jett120);
//        record.setId(1);
//
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);
//        recordList.add(record);

        recordAdapter.setRecords(recordsList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}