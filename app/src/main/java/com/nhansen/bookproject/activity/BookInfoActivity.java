package com.nhansen.bookproject.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.UserListAdapter;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.book.Book;
import com.nhansen.bookproject.database.DbHelper;

public class BookInfoActivity extends UserActivityBase {

    Book activeBook;

    TextView title;
    TextView author;
    TextView pubAndYear;
    TextView isbn;
    TextView genreAndPcount;
    TextView rating;
    Button addToListButton;

    AlertDialog addToListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);

        activeBook = getIntent().getParcelableExtra(Keys.INTENT_DATA_BOOK);

        title = findViewById(R.id.bookinfo_label_title);
        author = findViewById(R.id.bookinfo_label_author);
        pubAndYear = findViewById(R.id.bookinfo_label_publisherANDyear);
        isbn = findViewById(R.id.bookinfo_label_isbn);
        genreAndPcount = findViewById(R.id.bookinfo_label_genreANDpagecount);
        rating = findViewById(R.id.bookinfo_label_rating);
        addToListButton = findViewById(R.id.bookinfo_button_addtolist);

        title.setText(activeBook.getTitle());
        author.setText(activeBook.getAuthor());
        pubAndYear.setText("Published in " + activeBook.getYearPublished() + " by " + activeBook.getPublisher());
        isbn.setText("ISBN#: " + activeBook.getIsbn());
        genreAndPcount.setText(activeBook.getGenre().toString() + " - " + activeBook.getNumPages() + " pages");
        rating.setText("Average rating: " + activeBook.getRating() + "/5");


        DialogInterface.OnClickListener addToList = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (activeUser.appendCustomList(activeBook,which)) {
                    System.out.println("Added " + activeBook + " to list " + activeUser.getCustomLists().get(which));
                    Util.shortToast(getApplicationContext(), "Added!");
                    DbHelper.getInstance(getApplicationContext()).appendUser(activeUser);
                }
                else
                    Util.shortToast(getApplicationContext(), "That list already containsBook this book");
            }
        };
        addToListDialog = new AlertDialog.Builder(this)
                .setTitle(addToListButton.getText())
                .setNegativeButton("Cancel", null)
                .setAdapter(new UserListAdapter(this,R.layout.support_simple_spinner_dropdown_item,activeUser.getCustomLists(), activeBook),addToList)
                .create();
        addToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeUser.getCustomLists().size() == 0)
                    Util.shortToast(getApplicationContext(), "You don't have any lists to addBook this to");
                else
                    addToListDialog.show();
            }
        });

    }
}
