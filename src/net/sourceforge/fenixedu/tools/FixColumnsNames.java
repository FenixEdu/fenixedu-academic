/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class FixColumnsNames {

    /**
     * @param args
     */
    public static void main(String[] args) {

	Map ojbMetadata = MetadataManager.getInstance().getGlobalRepository().getDescriptorTable();

	checkColumnsNames(ojbMetadata);

    }

    private static void checkColumnsNames(Map ojbMetadata) {

	try {
	    final Connection connection = getConnection("localhost", "ciapl", "root", "");

	    Set<String> alterCommands = new HashSet<String>();
	    int invalidColumnNames = 0;

	    for (ClassDescriptor classDescriptor : (Collection<ClassDescriptor>) ojbMetadata.values()) {

		if (!classDescriptor.getClassNameOfObject().startsWith("org.apache.ojb")
			&& classDescriptor.getFieldDescriptions() != null && classDescriptor.getFullTableName() != null) {

		    final Statement statement = connection.createStatement();
		    final ResultSet resultSet;
		    try {
			resultSet = statement.executeQuery("show create table " + classDescriptor.getFullTableName());
		    } catch (SQLException e) {
			continue;
		    }

		    resultSet.next();

		    final Map<String, String> tableColumns = new HashMap<String, String>();
		    final String[] tableInfo = resultSet.getString(2).split("\n");
		    for (String tableInfoLine : tableInfo) {
			tableInfoLine = tableInfoLine.trim();
			if (!tableInfoLine.startsWith("`")) {
			    continue;
			}
			String[] tableInfoLineSplitted = tableInfoLine.split(" ");
			if (tableInfoLineSplitted.length > 1) {
			    tableInfoLine = tableInfoLine.substring(0, tableInfoLine.length() - 1);
			    tableColumns.put(tableInfoLineSplitted[0].toUpperCase(), tableInfoLine.replaceFirst(
				    tableInfoLineSplitted[0], ""));
			}
		    }

		    for (FieldDescriptor descriptor : classDescriptor.getFieldDescriptions()) {
			String name = descriptor.getAttributeName();
			String convertedName = StringFormatter.convertToDBStyle(name);
			String columnName = descriptor.getColumnName();

			if (!columnName.equals(convertedName)) {

			    String columnMetadata = tableColumns.get("`" + columnName.toUpperCase() + "`");

			    alterCommands.add("alter table " + classDescriptor.getFullTableName() + " change column "
				    + columnName + " " + convertedName + " " + columnMetadata + ";");

			    // System.out.println("CLASS: " +
			    // classDescriptor.getClassNameOfObject()
			    // + " - " + columnName + " -> " + convertedName + "
			    // :: "
			    // + columnMetadata);
			    invalidColumnNames++;
			}
		    }

		}

	    }

	    System.out.println("\nInvalid Names: " + invalidColumnNames);

	    Formatter resultFile = new Formatter("renameColumns.sql");
	    for (String command : alterCommands) {
		resultFile.format("%s\n", command);
	    }
	    resultFile.flush();
	    resultFile.close();

	} catch (Exception e) {
	    e.printStackTrace();
	}

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
