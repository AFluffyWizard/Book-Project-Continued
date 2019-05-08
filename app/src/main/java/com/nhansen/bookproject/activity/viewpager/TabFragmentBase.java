package com.nhansen.bookproject.activity.viewpager;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhansen.bookproject.ApplicationManager;
import com.nhansen.bookproject.user.User;

public abstract class TabFragmentBase extends Fragment {

    private static final String KEY_LAYOUT_RES = "KEY_LAYOUT_RES";

    User activeUser;
    @LayoutRes int layoutRes;


    // newInstance constructor for creating fragment with arguments
    public static TabFragmentBase newInstance(Class<? extends TabFragmentBase> tabFragmentClass, @LayoutRes int layoutRes) {
        TabFragmentBase tabFragment = null;
        try {
            tabFragment = tabFragmentClass.newInstance();
            Bundle args = new Bundle();
            args.putInt(KEY_LAYOUT_RES, layoutRes);
            tabFragment.setArguments(args);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutRes = getArguments().getInt(KEY_LAYOUT_RES);
        activeUser = ApplicationManager.getActiveUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        activeUser = ApplicationManager.getActiveUser();
    }
}
