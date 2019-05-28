package com.nhansen.bookproject.activity;

import android.os.Bundle;
import android.view.View;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.book.Book;
import com.nhansen.bookproject.search.SearchCriteria;

import java.util.ArrayList;

public class ListActivitySearch extends ListActivityBase {

    SearchCriteria searchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionButton.setVisibility(View.GONE);
        searchCriteria = getIntent().getParcelableExtra(Keys.INTENT_DATA_SEARCHCRITERIA);
        ArrayList<Book> searchResults = searchCriteria.getSearchResults();
        updateList(searchResults);
//        for (Book b : searchResults) {
//            System.out.println(b.getSimilarityIndex() + " " + b.toString());
//        }
    }
}
