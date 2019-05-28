package com.nhansen.bookproject.activity.viewpager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.google.android.flexbox.FlexboxLayout;
import com.nhansen.bookproject.ApplicationManager;
import com.nhansen.bookproject.FieldFocusTools;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.book.Genre;
import com.nhansen.bookproject.database.DbHelper;
import com.nhansen.bookproject.recommender.Recommender;
import com.nhansen.bookproject.user.Gender;
import com.nhansen.bookproject.user.ReadingHabits;

import java.util.ArrayList;

public class TabFragmentProfile extends TabFragmentBase {

    private Spinner genderSpinner;
    private EditText ageField;
    private Spinner readingHabitsSpinner;
    private FlexboxLayout likedGenresLayout;
    private FlexboxLayout dislikedGenresLayout;
    private Button updateButton;
    private Button deleteButton;
    private boolean confirmDeleteAccount = false;

    private ArrayList<Genre> likedGenres;
    private ArrayList<Genre> dislikedGenres;

    private DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DbHelper.getInstance(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        FieldFocusTools.setAllFieldsClearFocusOnFinish((ViewGroup)getView().findViewById(R.id.profile_layout_const));

        ageField = getView().findViewById(R.id.profile_field_age);
        ageField.setHint(Integer.toString(activeUser.getAge()));

        genderSpinner = getView().findViewById(R.id.profile_spinner_gender);
        genderSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Gender.values()));
        Util.setSpinnerSelection(genderSpinner,activeUser.getGender());

        readingHabitsSpinner = getView().findViewById(R.id.profile_spinner_readinghabits);
        readingHabitsSpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, ReadingHabits.values()));
        Util.setSpinnerSelection(readingHabitsSpinner,activeUser.getReadingHabits());

        likedGenres = new ArrayList<>();
        likedGenresLayout = getView().findViewById(R.id.profile_flexbox_likedgenres);
        Util.populateGenreSelector(likedGenres, likedGenresLayout, activeUser.getLikedGenres());

        dislikedGenres = new ArrayList<>();
        dislikedGenresLayout = getView().findViewById(R.id.profile_flexbox_dislikedgenres);
        Util.populateGenreSelector(dislikedGenres, dislikedGenresLayout, activeUser.getDislikedGenres());

        updateButton = getView().findViewById(R.id.profile_button_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender gender = (Gender)genderSpinner.getSelectedItem();
                String ageString = ageField.getText().toString();
                ReadingHabits readingHabits = (ReadingHabits) readingHabitsSpinner.getSelectedItem();

                if (ageString.equals("")) {
                    Util.shortToast(getContext(), "You must input a valid age");
                    return;
                }

                boolean updated =
                    activeUser.setGender(gender) &&
                    activeUser.setAge(Integer.parseInt(ageString)) &&
                    activeUser.setReadingHabits(readingHabits) &&
                    activeUser.setLikedGenres(likedGenres) &&
                    activeUser.setDislikedGenres(dislikedGenres);

                if (updated) {
                    Recommender r = new Recommender(activeUser, DbHelper.getInstance(getContext()).getAllBooks(), 10);
                    activeUser.setRecommendedList(r.produceRecommendedBooks());
                    Util.longToast(getContext(), "User information updated");
                }
                else {
                    Util.shortToast(getContext(), "No changes made - did not update user");
                }
            }
        });

        deleteButton = getView().findViewById(R.id.profile_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmDeleteAccount) {
                    AlertDialog deleteDialog = Util.styleFixedAlertDialogBuilder(getContext())
                            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.deleteUser(activeUser);
                                    Util.shortToast(getContext(),"Account deleted");
                                    ApplicationManager.logout();
                                }
                            })
                            .create();
                    deleteDialog.show();
                }
                else {
                    Util.longToast(getContext(),"Press again to delete your account");
                    confirmDeleteAccount = true;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        confirmDeleteAccount = false;
    }
}
