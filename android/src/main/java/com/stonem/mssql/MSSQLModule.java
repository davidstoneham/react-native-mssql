package com.stonem.mssql;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import android.util.Log;
import android.os.AsyncTask;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by David Stoneham on 2017-02-25.
 */
public class MSSQLModule extends ReactContextBaseJavaModule {
    private String sqlError;
    private Promise sqlPromise;

    private Connection dbConnection;
    private final String eTag = "REACT-NATIVE-MSSQL";

    public MSSQLModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void connect(ReadableMap config, Promise promise) {
        sqlPromise = promise;
        dbConnection = null;
        String server = config.getString("server");
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");
        if (config.hasKey("port")) {
            int port = config.getInt("port");
            server = server + ":" + port;
        }
        String ConnURL = "jdbc:jtds:sqlserver://" + server + ";" + "databaseName=" + database + ";useLOBs=false"
                + ";user=" + username + ";password=" + password + ";loginTimeout=5;";

        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... params) {
                String classs = "net.sourceforge.jtds.jdbc.Driver";
                String ConnURL = params[0];
                try {
                    Class.forName(classs);
                    dbConnection = DriverManager.getConnection(ConnURL);
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
                if (null != dbConnection) {
                    sqlPromise.resolve("Connection Successful!");
                } else {
                    sqlPromise.reject(eTag, sqlError);
                }
            }
        }.execute(ConnURL);
    }

    @ReactMethod
    public void executeQuery(String query, Promise promise) {
        new Query(dbConnection, promise).execute(query);
    }

    @ReactMethod
    public void executeUpdate(String query, Promise promise) {
        new Update(dbConnection, promise).execute(query);
    }

    @ReactMethod
    public void close(Promise promise) {
        sqlPromise = promise;
        String classs = "net.sourceforge.jtds.jdbc.Driver";
        try {
            if (null == dbConnection || dbConnection.isClosed() == true) {
                sqlError = "There is no open database connection";
                sqlPromise.reject(eTag, sqlError);
            } else {
                Class.forName(classs);
                dbConnection.close();
                dbConnection = null;
                sqlPromise.resolve("Connection Closed");
            }
        } catch (SQLException e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        } catch (ClassNotFoundException e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        } catch (Exception e) {
            Log.e(eTag, "exception", e);
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        }
    }

    @Override
    public String getName() {
        return "MSSQL";
    }
}