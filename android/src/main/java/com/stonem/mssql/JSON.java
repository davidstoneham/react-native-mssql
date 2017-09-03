package com.stonem.mssql;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by David Stoneham on 2017-02-25.
 */
public class JSON {
    public static JSONArray toJSON(ResultSet rs) throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();
        String column_name;

        while (rs.next()) {
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                column_name = rsmd.getColumnName(i);
                String myValue = rs.getString(i);
                if (!rs.wasNull()) {
                    if (rsmd.getColumnType(i) == java.sql.Types.CHAR) {
                        obj.put(column_name, rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARCHAR) {
                        obj.put(column_name, rs.getString(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BINARY) {
                        obj.put(column_name, rs.getBytes(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARBINARY) {
                        obj.put(column_name, rs.getBytes(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARBINARY) {
                        obj.put(column_name, rs.getBinaryStream(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIT) {
                        obj.put(column_name, rs.getBoolean(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.REAL) {
                        obj.put(column_name, rs.getFloat(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rs.getDouble(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rs.getFloat(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DECIMAL) {
                        obj.put(column_name, rs.getBigDecimal(i).doubleValue());
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
                        obj.put(column_name, rs.getBigDecimal(i).doubleValue());
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rs.getDate(i).toString());
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIME) {
                        obj.put(column_name, rs.getDate(i).toString());
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rs.getTimestamp(i).toString());
                    } else if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                        obj.put(column_name, rs.getArray(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rs.getBoolean(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        obj.put(column_name, rs.getBlob(i));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rs.getNString(i));
                    } else {
                        obj.put(column_name, rs.getObject(i));
                    }
                } else {
                    obj.put(column_name, JSONObject.NULL);
                }
            }
            json.put(obj);
        }
        return json;
    }
}