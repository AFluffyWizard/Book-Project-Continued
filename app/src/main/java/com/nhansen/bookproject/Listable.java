package com.nhansen.bookproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public interface Listable {
    @LayoutRes int getListViewLayoutRes();
    void populateListView(View listView);
    @Nullable Intent getDisplayIntent(Context context);
}
