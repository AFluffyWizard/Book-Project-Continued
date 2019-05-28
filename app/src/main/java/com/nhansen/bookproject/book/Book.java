package com.nhansen.bookproject.book;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.Listable;
import com.nhansen.bookproject.R;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.activity.BookInfoActivity;

import java.io.Serializable;

public class Book implements Parcelable, Listable, Serializable {

    private static final long serialVersionUID = Util.generateSerialUID("book_v1");
    private String title;
    private String author;
    private String isbn;
    private float rating;
    private String publisher;
    private int numPages;
    private int yearPublished;
    private Genre genre;
    private transient float similarityIndex = 0f; // used for searches. transient means it won't be saved during serialization

    public Book(String title, String author, String isbn, float rating, String publisher,  int numPages, int yearPublished, Genre genre) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.rating = rating;
        this.publisher = publisher;
        this.numPages = numPages;
        this.yearPublished = yearPublished;
        this.genre = genre;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getPublisher() {return publisher;}
    public int getYearPublished() {return yearPublished;}
    public String getIsbn() {return isbn;}
    public Genre getGenre() {return genre;}
    public int getNumPages() {return numPages;}
    public float getRating() {return rating;}
    public void setSimilarityIndex(float similarityIndex) {this.similarityIndex = similarityIndex;}
    public float getSimilarityIndex() {return similarityIndex;}

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book))
            return false;
        Book other = (Book)obj;

        boolean sameTitle = this.title.equals(other.title);
        boolean sameAuthor = this.author.equals(other.author);
        boolean samePublisher = this.publisher.equals(other.publisher);
        boolean sameYearPublished = this.yearPublished == other.yearPublished;
        boolean sameIsbn = this.isbn.equals(other.isbn);
        boolean sameGenre = this.genre.equals(other.genre);
        boolean sameNumPages = this.numPages == other.numPages;
        boolean sameRating = this.rating == other.rating;
        return sameTitle && sameAuthor && samePublisher && sameYearPublished && sameIsbn && sameGenre && sameNumPages && sameRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(isbn);
        dest.writeFloat(rating);
        dest.writeString(publisher);
        dest.writeInt(numPages);
        dest.writeInt(yearPublished);
        dest.writeString(genre.toString());
    }

    public Book(Parcel in)
        {
            title = in.readString();
            author = in.readString();
            isbn = in.readString();
            rating = in.readFloat();
            publisher = in.readString();
            numPages = in.readInt();
            yearPublished = in.readInt();
            genre = Genre.getEnum(in.readString());
        }

    public static final Parcelable.Creator<Book> CREATOR
                = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
            }

        public Book[] newArray(int size) {
                return new Book[size];
            }
        };



    @Override
    public void populateListView(View listView) {
        TextView title = listView.findViewById(R.id.listitem_book_label_title);
        title.setText(this.title);
        TextView author = listView.findViewById(R.id.listitem_book_label_author);
        author.setText(this.author);
        TextView yearGenre = listView.findViewById(R.id.listitem_book_label_yearANDgenre);
        yearGenre.setText(this.yearPublished + " - " + this.genre);
        TextView pageCount = listView.findViewById(R.id.listitem_book_label_pcount);
        pageCount.setText(this.numPages + " pages");
        TextView rating = listView.findViewById(R.id.listitem_book_label_rating);
        rating.setText(this.rating + "/5");
    }

    @Nullable
    @Override
    public Intent getDisplayIntent(Context context) {
        Intent displayIntent = new Intent(context, BookInfoActivity.class);
        displayIntent.putExtra(Keys.INTENT_DATA_BOOK, (Parcelable)this);
        return displayIntent;
    }

    @Override
    public int getListViewLayoutRes() {
        return R.layout.listitem_book;
    }
}


