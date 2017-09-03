package com.stonem.mssql;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.react.bridge.Promise;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by David Stoneham on 2017-02-25.
 */
public class Update extends AsyncTask<String, Void, Void> {
    private int sqlResponse = -1;
    private String sqlError;
    private Promise sqlPromise;
    private Connection dbConnection;
    private final String eTag = "REACT-NATIVE-MSSQL";

    public Update(Connection db, Promise promise) {
        dbConnection = db;
        sqlPromise = promise;
    }

    protected Void doInBackground(String... params) {
        String driverClass = "net.sourceforge.jtds.jdbc.Driver";
        String query = params[0];
        try {
            Class.forName(driverClass);
            Statement stmt = dbConnection.createStatement();
            sqlResponse = stmt.executeUpdate(query);
        } catch (SQLException e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
        } catch (ClassNotFoundException e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
        } catch (Exception e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
        }
        return null;
    }

    protected void onPostExecute(Void dummy) {
        if (sqlResponse > -1) {
            sqlPromise.resolve(sqlResponse);
        } else {
            sqlPromise.reject(eTag, sqlError);
        }
    }
}