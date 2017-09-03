package com.stonem.mssql;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.iodine.start.ArrayUtil;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by David Stoneham on 2017-02-25.
 */
public class Query extends AsyncTask<String, Void, Void> {
    private WritableArray sqlResponse;
    private String sqlError;
    private Promise sqlPromise;
    private Connection dbConnection;
    private final String eTag = "REACT-NATIVE-MSSQL";

    public Query(Connection db, Promise promise) {
        dbConnection = db;
        sqlPromise = promise;
    }

    protected Void doInBackground(String... params) {
        String driverClass = "net.sourceforge.jtds.jdbc.Driver";
        String query = params[0];
        try {
            Class.forName(driverClass);
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            JSONArray json = JSON.toJSON(rs);
            Object[] array = ArrayUtil.toArray(json);
            WritableArray writableArray = ArrayUtil.toWritableArray(array);
            sqlResponse = writableArray;
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
        if (null != sqlResponse) {
            sqlPromise.resolve(sqlResponse);
        } else {
            sqlPromise.reject(eTag, sqlError);
        }
    }

}