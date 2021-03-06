package com.nhansen.bookproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;
import com.nhansen.bookproject.FieldFocusTools;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.book.Book;
import com.nhansen.bookproject.book.BookList;
import com.nhansen.bookproject.book.Genre;
import com.nhansen.bookproject.database.DbHelper;
import com.nhansen.bookproject.recommender.Recommender;
import com.nhansen.bookproject.user.Gender;
import com.nhansen.bookproject.user.ReadingHabits;
import com.nhansen.bookproject.user.User;

import java.util.ArrayList;

import static com.nhansen.bookproject.Keys.INTENT_DATA_NEWUSER_USERNAME;

public class CreateAccActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    Spinner genderSpinner;
    EditText ageField;
    Spinner readingHabitsSpinner;
    FlexboxLayout likedGenresLayout;
    FlexboxLayout dislikedGenresLayout;
    Button createButton;

    ArrayList<Genre> likedGenres;
    ArrayList<Genre> dislikedGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FieldFocusTools.setAllFieldsPassFocusOnFinish((ViewGroup)findViewById(R.id.createacc_layout_const));

        usernameField = findViewById(R.id.createacc_field_username);
        passwordField = findViewById(R.id.createacc_field_pass);
        ageField = findViewById(R.id.createacc_field_age);
        genderSpinner = findViewById(R.id.createacc_spinner_gender);
        genderSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, Gender.values()));
        readingHabitsSpinner = findViewById(R.id.createacc_spinner_readinghabits);
        readingHabitsSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, ReadingHabits.values()));

        likedGenres = new ArrayList<>();
        likedGenresLayout = findViewById(R.id.createAcc_flexbox_likedgenres);
        Util.populateGenreSelector(likedGenres, likedGenresLayout, null);

        dislikedGenres = new ArrayList<>();
        dislikedGenresLayout = findViewById(R.id.createAcc_flexbox_dislikedgenres);
        Util.populateGenreSelector(dislikedGenres, dislikedGenresLayout, null);

        createButton = findViewById(R.id.createacc_button_create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data
                String name = usernameField.getText().toString();
                String pass = passwordField.getText().toString();
                Gender gender = (Gender) genderSpinner.getSelectedItem();
                String ageString = ageField.getText().toString();
                ReadingHabits readingHabits = (ReadingHabits) readingHabitsSpinner.getSelectedItem();

                // final checks
                if (name.equals("")) {
                    Util.shortToast("No username chosen");
                    return;
                }
                else if (DbHelper.getInstance(getApplicationContext()).usernameTaken(name)) {
                    Util.shortToast("A user with that name already exists!");
                    return;
                }
                else if (pass.equals("")) {
                    Util.shortToast("No password chosen");
                    return;
                }
                else if (ageString.equals("")) {
                    Util.shortToast("No age specified");
                    return;
                }
                else if (likedGenres.size() == 0) {
                    Util.shortToast("You must specify at least one liked genre");
                    return;
                }
                else if (Util.listsCollide(likedGenres, dislikedGenres)) {
                    Util.shortToast("You cannot like and dislike the same genre");
                    return;
                }

                // create user, save, and return
                User newUser = new User(name, pass, gender, Integer.parseInt(ageString), readingHabits, likedGenres, dislikedGenres, new ArrayList<Book>(), new BookList("Recommended"), new ArrayList<BookList>());
                Recommender r = new Recommender(newUser, DbHelper.getInstance(getApplicationContext()).getAllBooks(), 10);
                //r.makeRecommendedBookList();
                newUser.setRecommendedList(new BookList("Recommended Books", r.produceRecommendedBooks()));

                DbHelper.getInstance(getApplicationContext()).addUser(newUser);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(INTENT_DATA_NEWUSER_USERNAME, name);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
