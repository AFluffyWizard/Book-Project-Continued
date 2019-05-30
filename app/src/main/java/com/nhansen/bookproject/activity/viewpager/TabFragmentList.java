package com.nhansen.bookproject.activity.viewpager;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.RecyclerViewListableAdapter;
import com.nhansen.bookproject.activity.ListActivityUserLists;

public class TabFragmentList extends TabFragmentBase {

    @Override
    public void onStart() {
        super.onStart();

        RecyclerView recommendedList = getView().findViewById(R.id.tab_list_layout_listrecyclerview);
        recommendedList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        RecyclerViewListableAdapter recyclerViewAdapter = new RecyclerViewListableAdapter(activeUser.getRecommendedList().getIterator(), R.layout.listitem_book/*recommendedBooks.get(0).getListViewLayoutRes()*/, true);
        recommendedList.setAdapter(recyclerViewAdapter);

        Button customListsButton = getView().findViewById(R.id.tab_list_button_customlists);
        customListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userListsIntent = new Intent(getContext(), ListActivityUserLists.class);
                userListsIntent.putExtra(Keys.INTENT_DATA_LIST_LISTTITLE, "Custom Lists");
                userListsIntent.putExtra(Keys.INTENT_DATA_LIST_LAYOUTRES, R.layout.listitem_booklist);
                userListsIntent.putExtra(Keys.INTENT_DATA_LIST_DATASET, activeUser.getCustomLists());
                userListsIntent.putExtra(Keys.INTENT_DATA_LIST_LABELIFEMPTY, "You don't have any lists yet");
                startActivity(userListsIntent);
            }
        });
    }
}
