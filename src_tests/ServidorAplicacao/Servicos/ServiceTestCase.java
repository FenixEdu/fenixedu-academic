/*
 * Created on 06/Out/2003
 *
 */
package ServidorAplicacao.Servicos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public abstract class ServiceTestCase extends TestCase {

	public ServiceTestCase(String name) {
		super(name);
	}

	protected IDatabaseConnection getConnection() throws Exception {
		Class driverClass = Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection =
			DriverManager.getConnection(
				"jdbc:mysql://localhost/ciapl",
				"root",
				"");
		return new DatabaseConnection(jdbcConnection);
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(new File(getDataSetFilePath()));
	}

	public void backUpDataBaseContents() throws Exception {
		IDataSet fullDataSet = getConnection().createDataSet();
		FileWriter fileWriter =
			new FileWriter(new File(getBackUpDataSetFilePath()));
		FlatXmlDataSet.write(fullDataSet, fileWriter, "ISO-8859-1");
	}

	public void loadDataBase() throws Exception {
		FileReader fileReader =
			new FileReader(new File(getBackUpDataSetFilePath()));
		IDataSet dataSet = new FlatXmlDataSet(fileReader);
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), dataSet);
	}

	protected void setUp() {

		try {
			super.setUp();

			IDatabaseConnection connection = getConnection();
			IDataSet dataSet = getDataSet();

			backUpDataBaseContents();

			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

			connection.close();
		} catch (Exception ex) {
			fail("Setup failed loading database with test data set: " + ex);
		}
	}

	protected void tearDown() throws Exception {

//		try {
//			super.tearDown();
//			loadDataBase();
//		} catch (Exception ex) {
//			fail("Tear down failed: " + ex);
//		}
	}

	protected String getBackUpDataSetFilePath() {
		return "etc/testBackup.xml";
	}

	protected abstract String getDataSetFilePath();
}