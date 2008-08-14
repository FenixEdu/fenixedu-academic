/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DBColumnInfo {

    public static String getInfoWithType(String tableName, String columnName, boolean withType) {

	try {

	    final Connection connection = getConnection("localhost", "ciapl", "root", "");
	    final Statement statement = connection.createStatement();
	    final ResultSet resultSet = statement.executeQuery("show create table " + tableName);

	    resultSet.next();

	    final String[] tableInfo = resultSet.getString(2).split("\n");
	    for (String tableInfoLine : tableInfo) {
		tableInfoLine = tableInfoLine.trim();
		if (!tableInfoLine.startsWith("`")) {
		    continue;
		}

		String[] tableInfoLineSplitted = tableInfoLine.split(" ");
		if (tableInfoLineSplitted.length > 1 && tableInfoLineSplitted[0].substring(1).startsWith(columnName)) {

		    if (withType) {
			tableInfoLine = tableInfoLine.substring(0, tableInfoLine.length() - 1);
			return tableInfoLine.replaceFirst(tableInfoLineSplitted[0], "");
		    } else {
			StringBuilder result = new StringBuilder();
			for (int i = 2; i < tableInfoLineSplitted.length; i++) {
			    result.append(tableInfoLineSplitted[i]);
			    result.append(" ");
			}
			return result.toString().substring(0, result.toString().length() - 2);
		    }
		}
	    }

	} catch (Exception e) {
	    System.err.println("ERROR: " + tableName + " - " + columnName);
	    e.printStackTrace();
	}

	return null;

    }

    private static Connection getConnection(final String host, final String db, final String user, final String pass)
	    throws ClassNotFoundException, SQLException {
	Class.forName("com.mysql.jdbc.Driver");

	final String connectionString = getConnectionString(host, db);

	return DriverManager.getConnection(connectionString, user, pass);
    }

    private static String getConnectionString(final String host, final String db) {
	final StringBuilder url = new StringBuilder();
	url.append("jdbc:mysql://");
	url.append(host);
	url.append("/");
	url.append(db);
	return url.toString();
    }

}
