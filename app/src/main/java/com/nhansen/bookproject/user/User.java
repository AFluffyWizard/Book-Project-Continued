package com.nhansen.bookproject.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.nhansen.bookproject.Keys;
import com.nhansen.bookproject.Util;
import com.nhansen.bookproject.book.Book;
import com.nhansen.bookproject.book.BookList;
import com.nhansen.bookproject.book.Genre;
import com.nhansen.bookproject.database.SaveActiveUserListener;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess","UnusedReturnValue","UnusedDeclaration"})
public class User implements Serializable, Parcelable {
    private static final long serialVersionUID = Util.generateSerialUID("user_v2");

    private transient PropertyChangeSupport pcs;
    {
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(new SaveActiveUserListener());
    }

    private String name;
    private int passwordHash;
    private Gender gender;
    private int age;
    private ReadingHabits readingHabits;
    private ArrayList<Genre> likedGenres;
    private ArrayList<Genre> dislikedGenres;
    private ArrayList<Book> ratedBooks;
    private BookList recommendedList;
    private ArrayList<BookList> customLists;

    public User(String name, int passwordHash, Gender gender, int age, ReadingHabits readingHabits, ArrayList<Genre> likedGenres, ArrayList<Genre> dislikedGenres, ArrayList<Book> ratedBooks, BookList recommendedList, ArrayList<BookList> customLists) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.age = age;
        this.readingHabits = readingHabits;
        this.likedGenres = likedGenres;
        this.dislikedGenres = dislikedGenres;
        this.ratedBooks = ratedBooks;
        this.recommendedList = recommendedList;
        this.customLists = customLists;
    }

    public User(String name, String password, Gender gender, int age, ReadingHabits readingHabits, ArrayList<Genre> likedGenres, ArrayList<Genre> dislikedGenres, ArrayList<Book> ratedBooks, BookList recommendedList, ArrayList<BookList> customLists) {
        this.name = name;
        this.passwordHash = password.hashCode();
        this.gender = gender;
        this.age = age;
        this.readingHabits = readingHabits;
        this.likedGenres = likedGenres;
        this.dislikedGenres = dislikedGenres;
        this.ratedBooks = ratedBooks;
        this.recommendedList = recommendedList;
        this.customLists = customLists;
    }

    public User (User u) {
        this.name = u.name;
        this.passwordHash = u.passwordHash;
        this.gender = u.gender;
        this.age = u.age;
        this.readingHabits = u.readingHabits;
        this.likedGenres = u.likedGenres;
        this.dislikedGenres = u.dislikedGenres;
        this.ratedBooks = u.ratedBooks;
        this.recommendedList = u.recommendedList;
        this.customLists = u.customLists;
    }

    public String getName() { return name; }
    public int getPasswordHash() { return passwordHash; }
    public Gender getGender() { return gender; }
    public int getAge() { return age; }
    public ReadingHabits getReadingHabits() { return readingHabits; }
    public ArrayList<Genre> getLikedGenres() { return likedGenres; }
    public ArrayList<Genre> getDislikedGenres() { return dislikedGenres; }
    public ArrayList<Book> getRatedBooks() { return ratedBooks; }
    public BookList getRecommendedList() {return recommendedList;}
    public ArrayList<BookList> getCustomLists() {return customLists;}

//    DEPRECATED - no longer allow changing of user names
//
//    public void setName(String name) {
//        String oldValue = this.name;
//        this.name = name;
//        pcs.firePropertyChange(Keys.USER_SAVEEVENT_NAME, oldValue, name);
//    }

    public boolean setPassword(String password) { return setPasswordHash(password.hashCode()); }
    public boolean setPasswordHash(int passwordHash) {
        if (this.passwordHash == passwordHash)
            return false;

        int oldValue = this.passwordHash;
        this.passwordHash = passwordHash;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_PASS, oldValue, passwordHash);
        return true;
    }

    public boolean setGender(Gender gender) {
        if (this.gender == gender)
            return false;

        Gender oldValue = this.gender;
        this.gender = gender;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_GENDER, oldValue, gender);
        return true;
    }

    public boolean setAge(int age) {
        if (this.age == age)
            return false;

        int oldValue = this.age;
        this.age = age;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_AGE, oldValue, age);
        return true;
    }

    public boolean setReadingHabits(ReadingHabits readingHabits) {
        if (this.readingHabits == readingHabits)
            return false;

        ReadingHabits oldValue = this.readingHabits;
        this.readingHabits = readingHabits;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_READINGHABITS, oldValue, readingHabits);
        return true;
    }

    public boolean setLikedGenres(ArrayList<Genre> likedGenres) {
        if (this.likedGenres == likedGenres)
            return false;

        ArrayList<Genre> oldValue = new ArrayList<>(this.likedGenres);
        this.likedGenres = likedGenres;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_LIKEDGENRE, oldValue, likedGenres);
        return true;
    }

    public boolean setDislikedGenres(ArrayList<Genre> dislikedGenres) {
        if (this.dislikedGenres == dislikedGenres)
            return false;

        ArrayList<Genre> oldValue = new ArrayList<>(this.dislikedGenres);
        this.dislikedGenres = dislikedGenres;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_DISLIKEDGENRE, oldValue, dislikedGenres);
        return true;
    }

    public boolean setRatedBooks(ArrayList<Book> ratedBooks) {
        if (this.ratedBooks == ratedBooks)
            return false;

        ArrayList<Book> oldValue = new ArrayList<>(this.ratedBooks);
        this.ratedBooks = ratedBooks;
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_RATEDBOOKS, oldValue, ratedBooks);
        return true;
    }

    public boolean setRecommendedList(ArrayList<Book> recommendedList) { return setRecommendedList(new BookList("Recommended Books", recommendedList)); }
    public boolean setRecommendedList(BookList recommendedList) {
        if (this.recommendedList == recommendedList)
            return false;

        //BookList oldValue = new BookList("Recommended Books", this.recommendedList);
        this.recommendedList = recommendedList;
        //pcs.firePropertyChange(Keys.USER_SAVEEVENT_RECOMMENDEDLIST, oldValue, recommendedList);
        return true;
    }

    public boolean addCustomList(BookList newList) {
        // currently no invalid values for newList

        ArrayList<BookList> oldValue = new ArrayList<>(this.customLists);
        this.customLists.add(newList);
        pcs.firePropertyChange(Keys.USER_SAVEEVENT_CUSTOMLIST_CREATE, oldValue, this.customLists);
        return true;
    }
    public boolean appendCustomList(Book book, int listIndex) {
        BookList bookList = customLists.get(listIndex);
        if (bookList.containsBook(book))
            return false;
        else {
            BookList oldValue = new BookList(bookList);
            bookList.addBook(book);
            pcs.firePropertyChange(Keys.USER_SAVEEVENT_CUSTOMLIST_APPEND, oldValue, bookList);
            return true;
        }
    }
    public boolean deleteCustomList(BookList list) {
        for (BookList bl : customLists) {
            if (bl.equals(list)) {
                ArrayList<BookList> oldValue = new ArrayList<>(customLists);
                boolean deleted = customLists.remove(bl);
                pcs.firePropertyChange(Keys.USER_SAVEEVENT_CUSTOMLIST_DELETE, oldValue, customLists);
                return deleted;
            }
        }
        return false;
    }

    @Override
    @NonNull
    public String toString() {
        return name + ", " + age + ", " + gender.toString();
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof User))
            return false;
        User other = (User)obj;

        boolean sameName = this.name.equals(other.name);
        boolean samePass = this.passwordHash == other.passwordHash;
        boolean sameGender = this.gender.equals(other.gender);
        boolean sameAge = this.age == other.age;
        boolean sameReadingHabits = this.readingHabits.equals(other.readingHabits);
        boolean sameLikedGenres = this.likedGenres.equals(other.likedGenres);
        boolean sameDislikedGenres = this.dislikedGenres.equals(other.dislikedGenres);
        boolean sameRatedBooks = this.ratedBooks.equals(other.ratedBooks);
        boolean sameRecommendedList = this.recommendedList.equals(other.recommendedList);
        boolean sameCustomLists = this.customLists.equals(other.customLists);

        return (sameName && samePass && sameGender && sameAge && sameReadingHabits && sameLikedGenres && sameDislikedGenres && sameRatedBooks && sameRecommendedList && sameCustomLists);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(passwordHash);
        dest.writeString(gender.toString());
        dest.writeInt(age);
        dest.writeString(readingHabits.toString());
        dest.writeTypedList(likedGenres);
        dest.writeTypedList(dislikedGenres);
        dest.writeTypedList(ratedBooks);
        dest.writeString(recommendedList.getListName());
        dest.writeParcelable(recommendedList,0);
        dest.writeTypedList(customLists);
    }

    protected User(Parcel in) {
        name = in.readString();
        passwordHash = in.readInt();
        gender = Gender.getEnum(in.readString());
        age = in.readInt();
        readingHabits = ReadingHabits.getEnum(in.readString());
        likedGenres = in.createTypedArrayList(Genre.CREATOR);
        dislikedGenres = in.createTypedArrayList(Genre.CREATOR);
        ratedBooks = in.createTypedArrayList(Book.CREATOR);
        recommendedList = new BookList(in.readString(), in.createTypedArrayList(Book.CREATOR));
        customLists = in.createTypedArrayList(BookList.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
