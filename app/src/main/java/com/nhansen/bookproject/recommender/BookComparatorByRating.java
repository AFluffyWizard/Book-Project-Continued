package com.nhansen.bookproject.recommender;

import java.util.Comparator;

import com.nhansen.bookproject.book.Book;

public class BookComparatorByRating implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        return (int)(b1.getRating() - b2.getRating());
    }
}
