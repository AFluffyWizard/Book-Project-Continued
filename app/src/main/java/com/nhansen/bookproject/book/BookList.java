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
import com.nhansen.bookproject.activity.ListActivityBookList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("UnusedReturnValue")
public class BookList implements Parcelable, Serializable, Listable {

    private static final long serialVersionUID = Util.generateSerialUID("blist_v2");
    private String listName;
    private ArrayList<Book> list;


    public BookList(String listName) {
        this(listName,new ArrayList<Book>());
    }
    public BookList(BookList bookList) {
        this(bookList.listName, bookList.list);
    }
    public BookList(String listName, ArrayList<Book> list) {
        this.listName = listName;
        this.list = list;
    }


    public boolean addBook(Book b) { return list.add(b); }
    public boolean containsBook(Book b) { return list.contains(b); }
    public String getListName() {return listName;}
    public void setListName(String listName) {this.listName = listName;}
    public Iterator<Book> getIterator() { return list.iterator(); }

    private String getNumBooksString() {
        if (list.size() == 1)
            return "1 book";
        else
            return list.size() + " books";
    }

    @Override
    public String toString() {
        return listName + ": " + getNumBooksString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BookList))
            return false;
        BookList otherList = (BookList)obj;
        boolean sameName = listName.equals(otherList.getListName());
        boolean sameList = list.equals(otherList.list);
        return sameName && sameList;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listName);
        dest.writeTypedList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        @Override
        public BookList createFromParcel(Parcel in) {
            return new BookList(in.readString(), in.createTypedArrayList(Book.CREATOR));
        }

        @Override
        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };


    @Override
    public int getListViewLayoutRes() {
        return R.layout.listitem_booklist;
    }

    @Override
    public void populateListView(View listView) {
        TextView listTitle = listView.findViewById(R.id.listitem_booklist_label_listtitle);
        listTitle.setText(listName);
        TextView numBooks = listView.findViewById(R.id.listitem_booklist_label_numbooks);
        numBooks.setText(getNumBooksString());
    }

    @Nullable @Override
    public Intent getDisplayIntent(Context context) {
        Intent displayIntent = new Intent(context, ListActivityBookList.class);
        displayIntent.putExtra(Keys.INTENT_DATA_LIST_DATASET, (Parcelable)this);
        displayIntent.putExtra(Keys.INTENT_DATA_LIST_LISTTITLE, this.listName);
        displayIntent.putExtra(Keys.INTENT_DATA_LIST_LAYOUTRES, R.layout.listitem_book);
        return displayIntent;
    }

}
