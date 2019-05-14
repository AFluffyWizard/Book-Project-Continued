package com.nhansen.bookproject.database;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

class SerializableDb<T extends Serializable> {

    private Context context;
    private File dbDir;
    private String fileExtension;

    SerializableDb(Context context, String dirName, String fileExtension) {
        this.context = context;
        this.fileExtension = fileExtension;
        dbDir = new File (context.getFilesDir(), dirName);
        dbDir.mkdir();
    }


    /** write a new serializable object
     *
     * if the object already exists, this returns false and does not modify anything
     * otherwise, it creates a file for and stores the object
     * NOTE: this method adds the file extension for you. you do not need to add it yourself. **/
    boolean write (String fileName, T obj) {
        try {
            File newFile = new File(dbDir, fileName + fileExtension);
            if (!newFile.createNewFile())
                return false;
            FileOutputStream fileOut = context.openFileOutput(newFile.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(obj);
            objOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** appends an existing file
     *
     * if the file does not already exists, returns false
     * otherwise, deletes the old file and replaces it with a new one containing newObj**/
    boolean append(String fileName, T newObj) {
        try {
            File appendedFile = new File(dbDir, fileName);
            if (!appendedFile.delete())
                return false;
            appendedFile.createNewFile();
            FileOutputStream fileOut = context.openFileOutput(appendedFile.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(newObj);
            objOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /** deletes a file
     *
     * returns true if the file existed and was deleted
     * returns false otherwise **/
    boolean delete(String fileName) {
        File deletedFile = new File (dbDir, fileName);
        return deletedFile.delete();
    }

    /** given a file name, reads that file
     *
     * if it does not exist, returns null
     * otherwise, returns an object of that file's type **/
    T read (String fileName) {
        T readObj = null;
        File readFile = new File(dbDir, fileName);
        if (!readFile.exists())
            return null;
        try {
            FileInputStream fileIn = context.openFileInput(readFile.getName());
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            readObj = (T) objIn.readObject();
            objIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return readObj;
    }

    /** returns an ArrayList of every file **/
    ArrayList<T> readAll() {
        ArrayList<T> allFiles = new ArrayList<>();
        for (String fileName : dbDir.list())
            allFiles.add(read(fileName));
        return allFiles;
    }


    String[] getFileNames() {
        return dbDir.list();
    }

    String[] getFriendlyFileNames() {
        ArrayList<String> friendlyFileNames = new ArrayList<>();
        for (String s : dbDir.list())
            friendlyFileNames.add(s.substring(0,s.indexOf(fileExtension)));
        return friendlyFileNames.toArray(new String[0]);
    }
}
