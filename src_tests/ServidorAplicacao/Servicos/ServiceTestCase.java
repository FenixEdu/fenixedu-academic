/*
 * Created on 06/Out/2003
 *
 */
package ServidorAplicacao.Servicos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import ServidorAplicacao.GestorServicos;

public abstract class ServiceTestCase extends TestCase {

	protected GestorServicos gestor = null;

	public ServiceTestCase(String name) {
		super(name);
	}

	protected IDatabaseConnection getConnection() throws Exception {

		Class driverClass = Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection =
			DriverManager.getConnection("jdbc:mysql://localhost/ciapl", "root", "");
		return new DatabaseConnection(jdbcConnection);
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(new File(getDataSetFilePath()));
	}

	public void backUpDataBaseContents() throws Exception {

		IDataSet fullDataSet = getConnection().createDataSet();
		FileWriter fileWriter = new FileWriter(new File(getBackUpDataSetFilePath()));
		FlatXmlDataSet.write(fullDataSet, fileWriter, "ISO-8859-1");
	}

	public void loadDataBase() throws Exception {

		FileReader fileReader = new FileReader(new File(getBackUpDataSetFilePath()));
		IDataSet dataSet = new FlatXmlDataSet(fileReader);
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), dataSet);
	}

	protected void setUp() {

		try {
			super.setUp();

			//IDatabaseConnection connection = getConnection();
			//			IDataSet dataSet = getDataSet();
			//
			//			backUpDataBaseContents();
			//
			//			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

			gestor = GestorServicos.manager();

			//connection.close();
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

	protected void compareDataSet(String expectedFileName) {

		try {

			ITable expectedDataSetTable;
			ITable currentDataSetTable;

			FileReader fileReader = new FileReader(new File(expectedFileName));
			IDataSet expectedDataSet = new FlatXmlDataSet(fileReader);

			IDataSet currentDataSet = getConnection().createDataSet();

			String[] tableNames = currentDataSet.getTableNames();
			Object[] filteredTables = filterTablesToBeComparedByDataSet(tableNames);


			for (int iter = 0; iter < filteredTables.length; iter++) {

				currentDataSetTable = currentDataSet.getTable((String) filteredTables[iter]);
				expectedDataSetTable = expectedDataSet.getTable((String) filteredTables[iter]);
				Assertion.assertEquals(expectedDataSetTable, currentDataSetTable);
			}

		} catch (Exception ex) {
			fail("compareDataSet failed to read data set files" + ex);
		}
	}

	protected Object[] filterTablesToBeComparedByDataSet(String[] tableNames) {

		LinkedList filteredTablesList = new LinkedList();
		LinkedList tableNamesToFilter = readTableNamesToFilter();

		for (int iter = 0; iter < tableNames.length; iter++) {
			if (!tableNamesToFilter.contains(tableNames[iter]))
				filteredTablesList.add(tableNames[iter]);
		}
		Object[] filteredTables = filteredTablesList.toArray();

		return filteredTables;
	}

	protected LinkedList readTableNamesToFilter() {

		LinkedList listTableNamesToFilter = new LinkedList();
		LinkedList defaultListTableNamesToFilter = new LinkedList();
		String stringTableNamesToFilter = "";
		String defaultStringTableNamesToFilter = "";

		try {
			ResourceBundle bundle =
				new PropertyResourceBundle(new FileInputStream(getTableNamesToFilterFilePath()));
			stringTableNamesToFilter = bundle.getString(getNameOfServiceToBeTested());
			defaultStringTableNamesToFilter = bundle.getString("Default");
		} catch (FileNotFoundException fex) {
			fail("File " + getTableNamesToFilterFilePath() + " not found.");
		} catch (IOException ioex) {
			fail("IOException reading file " + getTableNamesToFilterFilePath() + " " + ioex);
		}

		StringTokenizer st = new StringTokenizer(stringTableNamesToFilter, ",");
		while (st.hasMoreElements())
			listTableNamesToFilter.add(st.nextElement());

		st = new StringTokenizer(defaultStringTableNamesToFilter, ",");
		while (st.hasMoreElements())
			defaultListTableNamesToFilter.add(st.nextElement());

		listTableNamesToFilter.addAll(defaultListTableNamesToFilter);

		return listTableNamesToFilter;
	}

	protected String getBackUpDataSetFilePath() {
		return "etc/testBackup.xml";
	}

	protected String getTableNamesToFilterFilePath() {
		return "etc/filterTables.properties";
	}

	protected abstract String getDataSetFilePath();
	protected abstract String getNameOfServiceToBeTested();

}