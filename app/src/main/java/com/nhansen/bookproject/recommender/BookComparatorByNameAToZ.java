package com.nhansen.bookproject.recommender;

import java.util.Comparator;

import com.nhansen.bookproject.book.Book;

public class BookComparatorByNameAToZ implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return b1.getTitle().compareTo(b2.getTitle());
    }
}
