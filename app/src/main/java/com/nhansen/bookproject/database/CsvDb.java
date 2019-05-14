package com.nhansen.bookproject.database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

class CsvDb {

    private Context context;
    private String csvFileName;

    CsvDb(Context context, String csvFileName) {
        this.context = context;
        this.csvFileName = csvFileName;
    }

    /** returns an ArrayList of book data strings **/
    ArrayList<String[]> readFile() {
        ArrayList<String[]> file = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open(csvFileName));
            BufferedReader reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null)
                file.add(splitCsvString(line));

            reader.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // because some data values have commas in them, so they can't easily be retrieved.
    // those that do are surrounded by quotes
    private String[] splitCsvString(String csvData) {
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
}
