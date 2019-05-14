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



    private Book parseBook (String bookData) {
        String[] bookparam = parseCsvString(bookData);
        return new Book(bookparam[0],
                bookparam[1],
                bookparam[2],
                Float.parseFloat(bookparam[3]),
                bookparam[4],
                Integer.parseInt(bookparam[5]),
                Integer.parseInt(bookparam[6]),
                Genre.getEnum(bookparam[7]));
    }

    // because some data values have commas in them, so they can't easily be retrieved.
    // those that do are surrounded by quotes
    private String[] parseCsvString(String csvData) {
        ArrayList<String> data = new ArrayList<>();

        // find each value and cut it out
        // if there are quotes, use them as reference
        // if not, use the comma as reference
        while(!csvData.equals("")) {
            if (csvData.indexOf('"') < 0) {
                Collections.addAll(data,csvData.split(","));
                break;
            }
            else if (csvData.indexOf('"') < csvData.indexOf(',')) {
                data.add(csvData.substring(1,csvData.indexOf('"',1)));
                csvData = csvData.substring(csvData.indexOf('"',1)+2);
            }
            else {
                data.add(csvData.substring(0,csvData.indexOf(',')));
                csvData = csvData.substring(csvData.indexOf(',')+1);
            }
        }

        String[] dataArray = new String[data.size()];
        return data.toArray(data.toArray(dataArray));
    }

    public ArrayList<Book> getAllBooks() {
        if (allBooks != null)
            return allBooks;
        else
            allBooks = new ArrayList<>();

        ArrayList<String> bookData = bookDb.readEntireFile();
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
