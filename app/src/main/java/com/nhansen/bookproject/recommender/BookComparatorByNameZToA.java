package com.nhansen.bookproject.recommender;

import java.util.Comparator;

import com.nhansen.bookproject.book.Book;

public class BookComparatorByNameZToA implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return b2.getTitle().compareTo(b1.getTitle());
    }
}
