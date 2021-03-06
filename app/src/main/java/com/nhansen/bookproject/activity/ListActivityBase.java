package com.nhansen.bookproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.Listable;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.RecyclerViewListableAdapter;

import java.util.ArrayList;

public abstract class ListActivityBase extends UserActivityBase {

    protected ArrayList<? extends Listable> dataset;
    protected int viewLayoutRes;
    protected String textIfEmpty;

    protected TextView listTitle;
    protected RecyclerView listRecyclerView;
    protected TextView emptyLabel;
    protected Button actionButton;
    private RecyclerViewListableAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listTitle = findViewById(R.id.list_label_title);
        listRecyclerView = findViewById(R.id.list_layout_listrecyclerview);
        emptyLabel = findViewById(R.id.list_label_listempty);
        actionButton = findViewById(R.id.list_button_actionbutton);

        listTitle.setText(getIntent().getStringExtra(Keys.INTENT_DATA_LIST_LISTTITLE));
        dataset = getIntent().getParcelableArrayListExtra(Keys.INTENT_DATA_LIST_DATASET);
        viewLayoutRes = getIntent().getIntExtra(Keys.INTENT_DATA_LIST_LAYOUTRES, 0);
        textIfEmpty = getIntent().getStringExtra(Keys.INTENT_DATA_LIST_LABELIFEMPTY);
        if (textIfEmpty == null)
            textIfEmpty = "empty";

        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerViewAdapter = new RecyclerViewListableAdapter(dataset, viewLayoutRes, true);
        listRecyclerView.setAdapter(recyclerViewAdapter);

        checkListEmpty();
    }

    private void checkListEmpty() {
        emptyLabel.setText(textIfEmpty);
        if (dataset == null)
            //TODO - SHOW LOADING ICON
            emptyLabel.setVisibility(View.VISIBLE);
        else if (dataset.size() == 0)
            emptyLabel.setVisibility(View.VISIBLE);
        else
            emptyLabel.setVisibility(View.GONE);
    }

    protected void updateList(ArrayList<? extends Listable> dataset) {
        this.dataset = dataset;
        recyclerViewAdapter.updateList(dataset);
        checkListEmpty();
    }

}