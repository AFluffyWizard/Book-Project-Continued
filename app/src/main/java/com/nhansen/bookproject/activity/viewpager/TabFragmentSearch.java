package com.nhansen.bookproject.activity.viewpager;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.flexbox.FlexboxLayout;
import com.nhansen.bookproject.FieldFocusTools;
import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.activity.ListActivitySearch;
import com.nhansen.bookproject.book.Genre;
import com.nhansen.bookproject.search.InvalidSearchCriteriaException;
import com.nhansen.bookproject.search.SearchCriteria;

import java.util.ArrayList;

public class TabFragmentSearch extends TabFragmentBase {

    EditText title;
    EditText author;
    EditText publishYearFloor;
    EditText publishYearCeiling;
    EditText pcountFloor;
    EditText pcountCeiling;
    FlexboxLayout preferredGenresLayout;
    FlexboxLayout excludedGenresLayout;
    ArrayList<Genre> preferredGenres;
    ArrayList<Genre> excludedGenres;
    Button searchButton;

    @Override
    public void onStart() {
        super.onStart();
        FieldFocusTools.setAllFieldsPassFocusOnFinish((ViewGroup)getView().findViewById(R.id.search_layout_const));

        title = getView().findViewById(R.id.search_field_title);
        author = getView().findViewById(R.id.search_field_author);
        publishYearFloor = getView().findViewById(R.id.search_field_publishfloor);
        publishYearCeiling = getView().findViewById(R.id.search_field_publishceiling);
        pcountFloor = getView().findViewById(R.id.search_field_pagefloor);
        pcountCeiling = getView().findViewById(R.id.search_field_pageceiling);

        preferredGenres = new ArrayList<>();
        preferredGenresLayout = getView().findViewById(R.id.search_flexbox_preferredgenres);
        Util.populateGenreSelector(preferredGenres, preferredGenresLayout, null);

        excludedGenres = new ArrayList<>();
        excludedGenresLayout = getView().findViewById(R.id.search_flexbox_excludedgenres);
        Util.populateGenreSelector(excludedGenres, excludedGenresLayout, null);

        searchButton = getView().findViewById(R.id.search_button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCriteria searchCriteria;
                try {
                    searchCriteria = new SearchCriteria(
                            title.getText().toString(),
                            author.getText().toString(),
                            publishYearFloor.getText().toString(),
                            publishYearCeiling.getText().toString(),
                            pcountFloor.getText().toString(),
                            pcountCeiling.getText().toString(),
                            preferredGenres,
                            excludedGenres);
                } catch (InvalidSearchCriteriaException e) {
                    Util.shortToast(getContext(),e.getSearchError().toString());
                    return;
                }

                Intent searchIntent = new Intent(getContext(), ListActivitySearch.class);
                searchIntent.putExtra(Keys.INTENT_DATA_LIST_LISTTITLE, "Search Results");
                searchIntent.putExtra(Keys.INTENT_DATA_LIST_LAYOUTRES, R.layout.listitem_book);
                searchIntent.putExtra(Keys.INTENT_DATA_LIST_LABELIFEMPTY, "No books match your search");
                searchIntent.putExtra(Keys.INTENT_DATA_SEARCHCRITERIA, searchCriteria);
                startActivity(searchIntent);
            }
        });
    }
}
