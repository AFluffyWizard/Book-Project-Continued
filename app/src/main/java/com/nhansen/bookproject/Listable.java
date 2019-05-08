package com.nhansen.bookproject;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;

public interface Listable {
    @LayoutRes int getListViewLayoutRes();
    void populateListView(View listView);
    @Nullable Intent getDisplayIntent(Context context);
}
