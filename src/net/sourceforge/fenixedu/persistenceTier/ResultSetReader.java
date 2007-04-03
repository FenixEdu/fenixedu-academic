package net.sourceforge.fenixedu.persistenceTier;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetReader {

    public static Long getFromBIGINT(ResultSet rs, String columnName) throws SQLException {
        Long result = rs.getLong(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Boolean getFromBIT(ResultSet rs, String columnName) throws SQLException {
        Boolean result = rs.getBoolean(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static byte[] getFromBLOB(ResultSet rs, String columnName) throws SQLException {
        Blob aBlob = rs.getBlob(columnName);
        return (rs.wasNull() ? null : aBlob.getBytes(1L, (int) aBlob.length()));
    }

    public static String getFromCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    public static Date getFromDATE(ResultSet rs, String columnName) throws SQLException {
        return rs.getDate(columnName);
    }

    public static Double getFromDOUBLE(ResultSet rs, String columnName) throws SQLException {
        Double result = rs.getDouble(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static Integer getFromINTEGER(ResultSet rs, String columnName) throws SQLException {
        Integer result = rs.getInt(columnName);
        return (rs.wasNull() ? null : result);
    }

    public static String getFromLONGVARCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    public static Time getFromTIME(ResultSet rs, String columnName) throws SQLException {
        return rs.getTime(columnName);
    }

    public static Timestamp getFromTIMESTAMP(ResultSet rs, String columnName) throws SQLException {
        return rs.getTimestamp(columnName);
    }

    public static String getFromVARCHAR(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
