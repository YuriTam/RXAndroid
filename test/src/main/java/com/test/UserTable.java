package com.test;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by tanzhongyang on 2016/7/29.
 */
public class UserTable {

    public Callable<List<User>> getUsers(final SQLiteOpenHelper mhelp, int userId){
        return new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return null;
            }
        };
    }
}
