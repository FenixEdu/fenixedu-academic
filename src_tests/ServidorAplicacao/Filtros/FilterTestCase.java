/*
 * Created on 06/Out/2003
 *  
 */
package ServidorAplicacao.Filtros;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public abstract class FilterTestCase extends TestCase {

    public FilterTestCase(String name) {
        super(name);
    }

    protected IDatabaseConnection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost/ciapl", "root",
                "");
        return new DatabaseConnection(jdbcConnection);
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(new File(getDataSetFilePath()));
    }

    protected void setUp() {

        try {
            super.setUp();
            IDatabaseConnection connection = getConnection();
            IDataSet dataSet = getDataSet();

            IDataSet fullDataSet = connection.createDataSet();
            DatabaseOperation.DELETE_ALL.execute(connection, fullDataSet);

            DatabaseOperation.INSERT.execute(connection, dataSet);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            sp.clearCache();
            sp.confirmarTransaccao();

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Setup failed loading database with test data set: " + ex);
        }
    }

    protected void tearDown() throws Exception {
    }

//    protected void compareDataSet(String expectedFileName) {
//
//        try {
//
//            FileReader fileReader = new FileReader(new File(expectedFileName));
//            IDataSet expectedDataSet = new FlatXmlDataSet(fileReader);
//
//            IDataSet currentDataSet = getConnection().createDataSet();
//
//            LinkedList tableNamesToFilter = readTableNamesToFilter();
//
//            int size = tableNamesToFilter.size();
//            String[] tableNames = new String[size];
//            for (int i = 0; i < size; i++) {
//                tableNames[i] = (String) tableNamesToFilter.get(i);
//            }
//
//            IDataSet filteredDateSet = new FilteredDataSet(tableNames, currentDataSet);
//            Assertion.assertEquals(expectedDataSet, filteredDateSet);
//        } catch (Exception ex) {
//            fail("compareDataSet failed to read data set files" + ex);
//        }
//    }
//
//    protected LinkedList readTableNamesToFilter() {
//
//        LinkedList listTableNamesToFilter = new LinkedList();
//        LinkedList defaultListTableNamesToFilter = new LinkedList();
//        String stringTableNamesToFilter = "";
//        String defaultStringTableNamesToFilter = "";
//
//        try {
//            ResourceBundle bundle = new PropertyResourceBundle(new FileInputStream(
//                    getTableNamesToFilterFilePath()));
//
//            stringTableNamesToFilter = bundle.getString(getNameOfServiceToBeTested());
//
//            defaultStringTableNamesToFilter = bundle.getString("Default");
//        } catch (MissingResourceException ex) {
//            fail("Resource " + getNameOfServiceToBeTested() + " not found in "
//                    + getTableNamesToFilterFilePath());
//        } catch (FileNotFoundException ex) {
//            fail("File " + getTableNamesToFilterFilePath() + " not found.");
//        } catch (IOException ex) {
//            fail("IOException reading file " + getTableNamesToFilterFilePath() + " " + ex);
//        }
//
//        StringTokenizer st = new StringTokenizer(stringTableNamesToFilter, ",");
//        while (st.hasMoreElements())
//            listTableNamesToFilter.add(st.nextElement());
//
//        st = new StringTokenizer(defaultStringTableNamesToFilter, ",");
//        while (st.hasMoreElements())
//            defaultListTableNamesToFilter.add(st.nextElement());
//
//        //listTableNamesToFilter.addAll(defaultListTableNamesToFilter);
//
//        return listTableNamesToFilter;
//    }

//    protected String getBackUpDataSetFilePath() {
//        return "etc/testBackup.xml";
//    }

//    protected String getTableNamesToFilterFilePath() {
//        return "etc/filterTables.properties";
//    }

//    /**
//     * Compares two datasets and uses expected dataset table columns. <br/><b>
//     * IMPORTANT: </b> DOES NOT USE filterTables.properties ANYMORE Expected
//     * dataset cannot refer to .dtd, otherwise the method will not use expected
//     * dataset table columns, but the initial dataset columns.
//     * 
//     * @param expectedFileName
//     */
//
//    protected void compareDataSetUsingExceptedDataSetTableColumns(String expectedFileName) {
//        compareDataSetUsingExceptedDataSetTablesAndColumns(expectedFileName);
//
//    }
//
//    /**
//     * Compares two datasets using expected dataset tables and columns
//     * 
//     * @param expectedFileName
//     */
//    protected void compareDataSetUsingExceptedDataSetTablesAndColumns(String expectedFileName) {
//        try {
//
//            FileReader fileReader = new FileReader(new File(expectedFileName));
//            IDataSet expectedDataSet = new FlatXmlDataSet(fileReader);
//
//            IDataSet currentDataSet = getConnection().createDataSet();
//
//            String[] tableNames = expectedDataSet.getTableNames();
//
//            IDataSet filteredDateSet = new FilteredDataSet(tableNames, currentDataSet);
//            int totalTables = tableNames.length;
//
//            for (int i = 0; i < totalTables; i++) {
//                ITable expectedTable = expectedDataSet.getTable(tableNames[i]);
//                ITable actualTable = filteredDateSet.getTable(tableNames[i]);
//                SortedTable sortedExpectedTable = new SortedTable(expectedTable);
//                SortedTable sortedActualTable = new SortedTable(actualTable, expectedTable
//                        .getTableMetaData());
//                Assertion.assertEquals(sortedExpectedTable, new CompositeTable(expectedTable
//                        .getTableMetaData(), sortedActualTable));
//            }
//
//        } catch (Exception ex) {
//            fail("compareDataSet failed to read data set files" + ex);
//        }
//    }

    protected abstract String getDataSetFilePath();

    protected abstract String getNameOfFilterToBeTested();

}