package net.sourceforge.fenixedu.persistenceTier.statementInterceptors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ist.fenixframework.pstm.MetadataManager;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.ExecutionPath;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;

/**
 * 
 * SQL Interceptor that is used to record the modifications in the content
 * structure in order to generate another SQL which will have the SQL
 * instructions for the modifications using only the CONTENT_IDs instead of
 * idInternals
 * 
 * @author pcma
 * 
 */

public class FenixStatementInterceptor implements StatementInterceptor {

    private static PrintWriter logFile = null;

    private static Class[] loggingClasses = { Node.class, Content.class, AvailabilityPolicy.class, ExecutionPath.class };

    private static OjbHelper objHelper = new OjbHelper();

    private static Pattern findPattern = Pattern.compile("(UPDATE|INSERT|DELETE).*?(" + objHelper.getTableNamesForClasses("|")
	    + ")(\\s|\\().*");

    private static Pattern getContentID = Pattern.compile(".*CONTENT_ID='(.*?)'.*");

    private static Pattern deletePattern = Pattern.compile("DELETE FROM (.*) WHERE ID_INTERNAL = ([0-9]+)");

    private static Pattern insertPatternFindContents = Pattern.compile(".*\\((.*?)\\) VALUES \\((.*)\\)");

    private static Pattern tokenPattern = Pattern.compile("TOKEN\\(([A-Z]+,[0-9]+)\\)");

    private static java.sql.Connection connection;

    private List<String> sqlCommands = new ArrayList<String>();

    private List<String> uuidTableCommands = new ArrayList<String>();

    private int counter = 0;

    public void destroy() {
	if (isLogging()) {
	    stopLogging();
	}
    }

    /*
     * No inserts into DB are generated so it doesn't need to be at top level
     * only
     */
    public boolean executeTopLevelOnly() {
	return false;
    }

    public void init(Connection arg0, Properties arg1) throws SQLException {

    }

    public ResultSetInternalMethods postProcess(String sql, Statement statment, ResultSetInternalMethods resultSet,
	    Connection connection) throws SQLException {
	return resultSet;
    }

    public ResultSetInternalMethods preProcess(String sql, Statement statment, Connection conn) throws SQLException {
	if (logFile != null && !sql.startsWith("SELECT")) {
	    process(sql.replaceAll("_binary", ""));
	}
	return null;
    }

    private void process(String sql) {
	Matcher matcher = findPattern.matcher(sql);
	if (matcher.find()) {
	    process(sql, matcher);
	} else if (sql.equals("commit") && !sqlCommands.isEmpty()) {
	    for (String sqlCommand : sqlCommands) {
		logFile.write(resolve(sqlCommand) + " ;\n\n");
	    }
	    sqlCommands.clear();

	    logFile
		    .write("CREATE TEMPORARY TABLE UUID_TEMP_TABLE(COUNTER integer, UUID varchar(255), FROM_UUID varchar(255));\n\n");

	    for (String sqlCommand : uuidTableCommands) {
		logFile.write(resolve(sqlCommand) + " ;\n");
	    }

	    logFile.write("ALTER TABLE UUID_TEMP_TABLE ADD INDEX (COUNTER), ADD INDEX (UUID), ADD INDEX (FROM_UUID); \n\n");

	    if (!uuidTableCommands.isEmpty()) {
		for (Class clazz : loggingClasses) {
		    Set<String> columns = objHelper.getInterestingColumnsForClass(clazz);

		    for (String column : columns) {

			logFile.write("\nUPDATE " + objHelper.getTableNameForClass(clazz) + " T, UUID_TEMP_TABLE UIT, "
				+ objHelper.getTableFromKey(clazz, column) + " CT set T." + column + "=CT.ID_INTERNAL WHERE T."
				+ column + "=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;");
		    }
		}
	    }
	    uuidTableCommands.clear();
	    logFile.write("\nDROP TABLE UUID_TEMP_TABLE;\n\n");
	}

    }

    private String resolve(String sqlCommandWithPossibleTokens) {
	Matcher matcher = tokenPattern.matcher(sqlCommandWithPossibleTokens);
	String realSql = sqlCommandWithPossibleTokens;
	while (matcher.find()) {
	    String identifiers = matcher.group(1);
	    String[] fields = identifiers.split(",");
	    String contentId = getContentFromTable(fields[0], fields[1]);
	    realSql = realSql.replaceFirst("TOKEN\\(" + identifiers + "\\)", contentId);
	}
	return realSql;
    }

    private void process(String sql, Matcher matcher) {
	String type = matcher.group(1);
	if (type.equals("UPDATE")) {
	    sqlCommands.add(generateSQLUpdate(sql));
	} else if (type.equals("INSERT")) {
	    sqlCommands.add(generateSQLInsert(sql));
	} else if (type.equals("DELETE")) {
	    sqlCommands.add(generateSQLDelete(sql));
	}
    }

    private String generateSQLUpdate(String sql) {
	String tableName = sql.replaceAll("UPDATE (.*?) SET .*", "$1");
	String ojbConcreteClass = sql.replaceAll("UPDATE .*OJB_CONCRETE_CLASS='(.*?)'.*", "$1");
	if (ojbConcreteClass.length() == sql.length()) {
	    ojbConcreteClass = objHelper.getClassFromTableName(tableName).getName();
	}

	Matcher contentMatcher = getContentID.matcher(sql);
	if (contentMatcher.find()) {
	    String mainContentId = contentMatcher.group(1);
	    String modified = sql.replaceFirst("ID_INTERNAL = [0-9]+", "CONTENT_ID = '" + mainContentId + "'");
	    Pattern updatePattern = Pattern
		    .compile(".*?((" + objHelper.getInterestingColumnsForClass(objHelper.getClassFromTableName(tableName), "|")
			    + ")=)([0-9]+).*?");

	    Matcher replaceIdsMatcher = updatePattern.matcher(modified);
	    while (replaceIdsMatcher.find()) {
		String idInternal = replaceIdsMatcher.group(3);
		String parameter = replaceIdsMatcher.group(2);

		modified = modified.replaceFirst(replaceIdsMatcher.group(1) + idInternal, replaceIdsMatcher.group(1)
			+ registerKey(objHelper.getTableFromKey(ojbConcreteClass, parameter), idInternal, mainContentId));
	    }
	    return modified;
	}
	return null;
    }

    private String generateSQLInsert(String sql) {
	String tableName = sql.replaceAll("INSERT INTO (.*?) \\(.*", "$1");
	String sqlModified = sql.replace("ID_INTERNAL,", "").replaceFirst("VALUES \\([0-9]+,", "VALUES (");
	Matcher findValues = insertPatternFindContents.matcher(sqlModified);
	findValues.find();
	String[] arguments = findValues.group(1).split(",");
	String[] values = findValues.group(2).split(",");
	String mainUUID = null;
	String ojbConcreteClass = null;
	for (int i = 0; i < arguments.length; i++) {
	    if (arguments[i].equals("CONTENT_ID")) {
		mainUUID = values[i].substring(1, values[i].length() - 1);
	    } else if (arguments[i].equals("OJB_CONCRETE_CLASS")) {
		ojbConcreteClass = values[i].substring(1, values[i].length() - 1);
	    }
	}
	if (ojbConcreteClass == null) {
	    ojbConcreteClass = objHelper.getClassFromTableName(tableName).getName();
	}

	for (int i = 0; i < arguments.length; i++) {
	    if (arguments[i].matches(objHelper.getInterestingColumnsForClass(objHelper.getClassFromTableName(tableName), "|"))
		    && StringUtils.isNumeric(values[i])) {

		sqlModified = sqlModified.replaceFirst(values[i], String.valueOf(registerKey(objHelper.getTableFromKey(
			ojbConcreteClass, arguments[i]), values[i], mainUUID)));

	    }
	}
	return sqlModified;

    }

    private String generateSQLDelete(String sql) {
	Matcher idInternalMatcher = deletePattern.matcher(sql);
	if (idInternalMatcher.find()) {
	    String idInternal = idInternalMatcher.group(2);
	    String table = idInternalMatcher.group(1);
	    return "DELETE FROM " + table + " WHERE CONTENT_ID = '" + getContentFromTable(table, idInternal) + "'";
	}
	return null;
    }

    private int registerKey(String table, String idInternal, String fromUUID) {
	String contentId = findContentId(table, idInternal);
	counter++;
	uuidTableCommands.add("INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(" + counter + ",'" + contentId + "','"
		+ fromUUID + "')");
	return counter;
    }

    private String findContentId(String table, String idInternal) {
	String contentId = getContentFromTable(table, idInternal);
	return (contentId == null) ? "TOKEN(" + table + "," + idInternal + ")" : contentId;
    }

    private String getContentFromTable(String table, String idInternalAsString) {
	try {
	    if (connection == null) {
		connection = PersistenceBrokerFactory.defaultPersistenceBroker().serviceConnectionManager().getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	    }
	    java.sql.PreparedStatement stmt = connection.prepareStatement("SELECT CONTENT_ID FROM " + table
		    + " WHERE ID_INTERNAL= ?");
	    stmt.setString(1, idInternalAsString);
	    java.sql.ResultSet set = stmt.executeQuery();
	    if (set.next()) {
		String contentId = set.getString(1);
		return contentId;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static void startLogging() {
	if (!isLogging()) {
	    Properties properties = null;
	    try {
		properties = PropertiesManager.loadProperties("/build.properties");
	    } catch (IOException e1) {
		// do nothing
	    }
	    String filename = null;
	    filename = properties != null ? properties.getProperty("sql.interceptor.log") : null;
	    if (StringUtils.isEmpty(filename)) {
		filename = "/tmp/intercepted.sql";
	    }
	    String enconding = properties != null ? properties.getProperty("sql.interceptor.encoding") : null;
	    if (StringUtils.isEmpty(enconding)) {
		enconding = "iso-8859-1";
	    }
	    try {
		logFile = new PrintWriter(new File(filename), enconding);
		logFile.write("SET AUTOCOMMIT = 0;\n\nSTART TRANSACTION;\n\n");
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void stopLogging() {
	if (isLogging()) {
	    logFile.write("\n\nCOMMIT;\n");
	    logFile.flush();
	    logFile.close();
	    logFile = null;
	    if (connection != null) {
		try {
		    connection.close();
		    connection = null;
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public static boolean isLogging() {
	return logFile != null;
    }

    public static class OjbHelper {

	private DescriptorRepository globalRepository = MetadataManager.getOjbMetadataManager().getGlobalRepository();

	public String getTableFromKey(Class clazz, String key) {
	    String keyName = extractNameFromKey(key);
	    ObjectReferenceDescriptor descriptor = getOjbDescriptorFor(clazz).getObjectReferenceDescriptorByName(keyName);
	    if (descriptor == null) {
		Vector<Class> classes = getMappedExtendedClasses(clazz);
		for (Class concreteClass : classes) {
		    descriptor = getOjbDescriptorFor(concreteClass).getObjectReferenceDescriptorByName(keyName);
		    if (descriptor != null) {
			break;
		    }
		}
	    }
	    return getTableNameForClass(descriptor.getItemClass());
	}

	public String getTableFromKey(String concreteClassName, String key) {
	    ObjectReferenceDescriptor descriptor = getOjbDescriptor(concreteClassName).getObjectReferenceDescriptorByName(
		    extractNameFromKey(key));
	    return getTableNameForClass(descriptor.getItemClass());
	}

	private String extractNameFromKey(String key) {
	    String name = key.replace("KEY_", "");
	    name = name.toLowerCase();
	    StringBuffer buffer = new StringBuffer("");
	    for (String part : name.split("_")) {
		if (buffer.length() == 0) {
		    buffer.append(part);
		} else {
		    buffer.append(part.substring(0, 1).toUpperCase());
		    buffer.append(part.substring(1));
		}
	    }
	    return buffer.toString();
	}

	public String getTableNameForClass(Class clazz) {
	    return getOjbDescriptorFor(clazz).getFullTableName();
	}

	public String getTableNamesForClasses(String separator) {
	    StringBuffer tableNames = new StringBuffer("");
	    int i = 1;
	    for (Class clazz : loggingClasses) {
		tableNames.append(getTableNameForClass(clazz));
		if (i < loggingClasses.length) {
		    tableNames.append(separator);
		}
		i++;
	    }
	    return tableNames.toString();
	}

	public Class getClassFromTableName(String tableName) {
	    for (Class clazz : loggingClasses) {
		if (getOjbDescriptorFor(clazz).getFullTableName().equals(tableName)) {
		    return clazz;
		}
	    }
	    return null;
	}

	public String getInterestingColumnsForClass(Class clazz, String separator) {
	    Set<String> interestingKeys = getInterestingColumnsForClass(clazz);
	    StringBuffer buffer = new StringBuffer("");
	    for (String interestingKey : interestingKeys) {
		if (buffer.length() > 0) {
		    buffer.append(separator);
		}
		buffer.append(interestingKey);
	    }

	    return buffer.toString();
	}

	public Set<String> getInterestingColumnsForClass(Class clazz) {
	    Vector<Class> classes = getMappedExtendedClasses(clazz);
	    Set<String> interestingKeys = new HashSet<String>();
	    for (Class concreteClass : classes) {
		Vector<ObjectReferenceDescriptor> referenceDescriptors = getOjbDescriptorFor(concreteClass)
			.getObjectReferenceDescriptors();
		for (ObjectReferenceDescriptor referenceDescriptor : referenceDescriptors) {
		    if (shouldBeLogged(referenceDescriptor.getItemClass())) {
			interestingKeys.add(toSqlName(referenceDescriptor.getForeignKeyFields().get(0).toString()));
		    }
		}
	    }

	    return interestingKeys;
	}

	public String toSqlName(String name) {
	    return name.replaceAll("([a-z]*)([A-Z])(.*?)", "$1_$2$3").toUpperCase();
	}

	private boolean shouldBeLogged(Class itemClass) {
	    for (Class clazz : loggingClasses) {
		if (clazz.isAssignableFrom(itemClass)) {
		    return true;
		}
	    }
	    return false;
	}

	public Vector<Class> getMappedExtendedClasses(Class clazz) {
	    Vector<Class> classes = getOjbDescriptorFor(clazz).getExtentClasses();
	    if (!Modifier.isAbstract(clazz.getModifiers())) {
		classes.add(clazz);
	    }
	    return classes;
	}

	public ClassDescriptor getOjbDescriptor(String className) {

	    return globalRepository.getDescriptorFor(className);
	}

	public ClassDescriptor getOjbDescriptorFor(Class clazz) {
	    return globalRepository.getDescriptorFor(clazz);
	}
    }
}
