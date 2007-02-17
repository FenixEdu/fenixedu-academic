package net.sourceforge.fenixedu.persistenceTier;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetReader {

    public static Object getFromBIGINT(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getLong(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Object getFromBIT(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getBoolean(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Object getFromBLOB(ResultSet rs, String columnName) throws SQLException {
        Blob aBlob = rs.getBlob(columnName);
        return (rs.wasNull() ? null : aBlob.getBytes(1L, (int) aBlob.length()));
    }

    public static Object getFromCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    public static Object getFromDATE(ResultSet rs, String columnName) throws SQLException {
        return rs.getDate(columnName);
    }

    public static Object getFromDOUBLE(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getDouble(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Object getFromINTEGER(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getInt(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Object getFromLONGVARCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    public static Object getFromTIME(ResultSet rs, String columnName) throws SQLException {
        return rs.getTime(columnName);
    }

    public static Object getFromTIMESTAMP(ResultSet rs, String columnName) throws SQLException {
        return rs.getTimestamp(columnName);
    }

    public static Object getFromVARCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
