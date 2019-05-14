package com.nhansen.bookproject.database;

import android.content.Context;

import com.nhansen.bookproject.ApplicationManager;
import com.nhansen.bookproject.book.Book;
import com.nhansen.bookproject.book.Genre;
import com.nhansen.bookproject.user.User;

import java.util.ArrayList;
import java.util.Collections;

public class DbHelper {

    private SerializableDb<User> userDb;
    private CsvDb bookDb;
    private ArrayList<Book> allBooks;

    private static DbHelper singletonInstance;
    public static synchronized DbHelper getInstance(Context context) {
        if (singletonInstance == null)
            singletonInstance = new DbHelper(context);
        return singletonInstance;
    }
    public static synchronized DbHelper getInstance() {
        return getInstance(ApplicationManager.getContext());
    }

    private DbHelper(Context context) {
        userDb = new SerializableDb<>(context, "UserDb", ".ser");
        bookDb = new CsvDb(context, "bookDB.csv");
    }



    private Book parseBook (String[] bookData) {
        return new Book(bookData[0],
                bookData[1],
                bookData[2],
                Float.parseFloat(bookData[3]),
                bookData[4],
                Integer.parseInt(bookData[5]),
                Integer.parseInt(bookData[6]),
                Genre.getEnum(bookData[7]));
    }

    public ArrayList<Book> getAllBooks() {
        if (allBooks != null)
            return allBooks;
        else
            allBooks = new ArrayList<>();

        ArrayList<String[]> bookData = bookDb.readFile();
        for (int i = 1; i < bookData.size(); i++) {
            allBooks.add(parseBook(bookData.get(i)));
        }

        return allBooks;
    }

    public User getUser (String username, String password) {
        User user = getUser(username);
        if (user == null)
            return null;
        else if (user.getPasswordHash() == password.hashCode())
            return user;
        else
            return null;
    }

    // TODO - FIX THIS
    // in theory, this class shouldn't know - or have to know - the file extension of the user files
    // however, userDb.read() NEEDS the string to have the file extension
    private User getUser (String username) {
        return userDb.read(username + ".ser");
    }

    public boolean addUser(User user) {
        return userDb.write(user.getName(), user);
    }

    public boolean appendUser (String oldUserName, User newUserData) {
        return userDb.append(oldUserName, newUserData);
    }

    public boolean appendUser (User newUserData) {
        return userDb.append(newUserData.getName(), newUserData);
    }

    public boolean deleteUser (User user) {
        return userDb.delete(user.getName());
    }

    public boolean usernameTaken(String name) {
        ArrayList<String> userNames = new ArrayList<>();
        Collections.addAll(userNames, userDb.getFriendlyFileNames());
        return (userNames.contains(name));
    }

}
