package com.nhansen.bookproject.database;

import com.nhansen.bookproject.ApplicationManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

public class SaveActiveUserListener implements PropertyChangeListener {

    private static final long SAVE_DELAY = 1000;
    private DbHelper dbHelper = DbHelper.getInstance();
    private Timer saveDataTimer = new Timer();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO - implement some check that the file exists before trying to save to it
//        User sourceUser = (User)evt.getSource();
//        if (!dbHelper.userFileExists(sourceUser))

        saveDataTimer.cancel();
        saveDataTimer.purge();
        saveDataTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                dbHelper.appendUser(ApplicationManager.getActiveUser());
            }
        },SAVE_DELAY);
    }
}
