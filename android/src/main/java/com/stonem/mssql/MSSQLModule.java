package com.stonem.mssql;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import android.util.Log;
import android.os.AsyncTask;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.iodine.start.ArrayUtil;

/**
 * Created by David Stoneham on 2017-02-25.
 */
public class MSSQLModule extends ReactContextBaseJavaModule {
    private WritableArray sqlResponse;
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
        if (config.hasKey("port")){
           int port = config.getInt("port");
           server = server + ":" + port;
        }
        String ConnURL = "jdbc:jtds:sqlserver://" + server + ";" +
                        "databaseName=" + database +
                        ";useLOBs=false" +
                        ";user=" + username +
                        ";password=" + password + ";";

        new AsyncTask < String, Void, Void > () {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String...params) {
                String classs = "net.sourceforge.jtds.jdbc.Driver";
                String ConnURL = params[0];
                try {
                    Class.forName(classs);
                    dbConnection = DriverManager.getConnection(ConnURL);
                } catch (SQLException e) {
                    Log.e(eTag, e.getMessage());
                    sqlError = e.getMessage();
                } catch (ClassNotFoundException e) {
                    Log.e(eTag, e.getMessage());
                    sqlError = e.getMessage();
                } catch (Exception e) {
                    Log.e(eTag, e.getMessage());
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
        sqlPromise = promise;

        new AsyncTask < String, Void, Void > () {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String...params) {
                String classs = "net.sourceforge.jtds.jdbc.Driver";
                String query = params[0];
                try {
                    Class.forName(classs);
                    Statement stmt = dbConnection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    JSONArray json = toJSON(rs);
                    Object[] array = ArrayUtil.toArray(json);
                    WritableArray writableArray = ArrayUtil.toWritableArray(array);
                    sqlResponse = writableArray;
                } catch (SQLException e) {
                    Log.e(eTag, e.getMessage());
                    sqlError = e.getMessage();
                } catch (ClassNotFoundException e) {
                    Log.e(eTag, e.getMessage());
                    sqlError = e.getMessage();
                } catch (Exception e) {
                    Log.e(eTag, e.getMessage());
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
        }.execute(query);
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
            Log.e(eTag, e.getMessage());
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        } catch (ClassNotFoundException e) {
            Log.e(eTag, e.getMessage());
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        } catch (Exception e) {
            Log.e(eTag, e.getMessage());
            sqlError = e.getMessage();
            sqlPromise.reject(eTag, sqlError);
        }
    }

    private static JSONArray toJSON(ResultSet rs) throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();
        String column_name;

        while (rs.next()) {
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                column_name = rsmd.getColumnName(i);

                if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rs.getArray(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rs.getBlob(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                    obj.put(column_name, rs.getString(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                    obj.put(column_name, rs.getDate(i));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rs.getTimestamp(i));
                } else {
                    obj.put(column_name, rs.getObject(i));
                }
            }
            json.put(obj);
        }
        return json;
    }

    @Override
    public String getName() {
        return "MSSQL";
    }
}