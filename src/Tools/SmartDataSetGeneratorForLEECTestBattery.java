package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/*
 * Created on Oct 25, 2003
 */

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt) Description: Generates datasets using
 *         reachability graphs
 */
public class SmartDataSetGeneratorForLEECTestBattery
{

    //constants
    private static final String DB_PASS_PROPERTY = "db.pass";

    private static final String DB_USER_PROPERTY = "db.user";

    private static final String DB_URL_PROPERTY = "db.url";

    private static final String DB_DRIVER_PROPERTY = "db.driver";

    private static final String DESTINATION_DATASET_PROPERTY = "destination.dataset";

    private static final String MAIN_TABLE_NAME_PROPERTY = "mainTable.name";

    private static final String MAIN_TABLE_COLUMNS_PROPERTY = "mainTable.columns";

    private static final String MAIN_TABLE_WHERE_CONDITION_PROPERTY = "mainTable.whereCondition";

    private static final String CONVENTIONS_PROPERTY = "conventions";

    private static final String MANY_TO_MANY_TABLES_PROPERTY = "manyToManyTables";

    private static final String DATASET_CONFIG = "/SmartDataSetGenerator.properties";

    private static final String FOREIGN_KEY_CONVENTIONS_CONFIG = "/ForeignKeyConventions.properties";

    private static String STUDENT_NUMBER = "";

    //attributes
    private Properties props;

    private ArrayList propertiesToIgnore;

    private Connection connection;

    private DatabaseMetaData databaseMetaData;

    private HashMap processedTables; /* TableInfo */

    private Properties fkConventionsProps;

    private ArrayList conventions;

    private ArrayList manyToManyTables;

    private HashMap manyToManyTablesValidReferences;

    /**
     * @param configFilename
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public SmartDataSetGeneratorForLEECTestBattery(String configFilename)
            throws NullPointerException, FileNotFoundException, IOException
    {
        this.props = new Properties();
        this.props.load(new FileInputStream(configFilename));
        addPropertiesToIgnore();
        this.processedTables = new HashMap();
        this.fkConventionsProps = new Properties();
        this.fkConventionsProps.load(SmartDataSetGenerator.class
                .getResourceAsStream(FOREIGN_KEY_CONVENTIONS_CONFIG));
        readConventions();
        readManyToManyTablesNamesAndValidReferences();

    }

    /**
     * @param configFilename
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public SmartDataSetGeneratorForLEECTestBattery(String configFilename,
            String filePath) throws NullPointerException,
            FileNotFoundException, IOException
    {
        this.props = new Properties();
        this.props.load(new FileInputStream(configFilename));
        this.props.put("destination.dataset", filePath);
        addPropertiesToIgnore();
        this.processedTables = new HashMap();
        this.fkConventionsProps = new Properties();
        this.fkConventionsProps.load(SmartDataSetGenerator.class
                .getResourceAsStream(FOREIGN_KEY_CONVENTIONS_CONFIG));
        readConventions();
        readManyToManyTablesNamesAndValidReferences();

    }

    /**
     * @param configFilename
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public SmartDataSetGeneratorForLEECTestBattery(String configFilename,
            String filePath, String studentNumber) throws NullPointerException,
            FileNotFoundException, IOException
    {
        STUDENT_NUMBER = studentNumber;
        this.props = new Properties();
        this.props.load(new FileInputStream(configFilename));
        this.props.put("destination.dataset", filePath);
        addPropertiesToIgnore();
        this.processedTables = new HashMap();
        this.fkConventionsProps = new Properties();
        this.fkConventionsProps.load(SmartDataSetGenerator.class
                .getResourceAsStream(FOREIGN_KEY_CONVENTIONS_CONFIG));
        readConventions();
        readManyToManyTablesNamesAndValidReferences();

    }

    /**
     * @throws IOException
     * @throws NullPointerException
     */
    public SmartDataSetGeneratorForLEECTestBattery() throws IOException,
            NullPointerException
    {
        this.props = new Properties();
        this.props.load(SmartDataSetGenerator.class
                .getResourceAsStream(DATASET_CONFIG));
        addPropertiesToIgnore();
        this.processedTables = new HashMap();
        this.fkConventionsProps = new Properties();
        this.fkConventionsProps.load(SmartDataSetGenerator.class
                .getResourceAsStream(FOREIGN_KEY_CONVENTIONS_CONFIG));
        readConventions();
        readManyToManyTablesNamesAndValidReferences();

    }

    private void addPropertiesToIgnore()
    {
        propertiesToIgnore = new ArrayList();
        propertiesToIgnore.add(DB_DRIVER_PROPERTY);
        propertiesToIgnore.add(DB_URL_PROPERTY);
        propertiesToIgnore.add(DB_USER_PROPERTY);
        propertiesToIgnore.add(DB_PASS_PROPERTY);
        propertiesToIgnore.add(DESTINATION_DATASET_PROPERTY);
    }

    private void connectAndPrepareMetadata() throws ClassNotFoundException,
            SQLException
    {
        String driver = this.props.getProperty(DB_DRIVER_PROPERTY);
        String url = this.props.getProperty(DB_URL_PROPERTY);
        String user = this.props.getProperty(DB_USER_PROPERTY);
        String pass = this.props.getProperty(DB_PASS_PROPERTY);

        Class.forName(driver);
        this.connection = DriverManager.getConnection(url, user, pass);
        this.databaseMetaData = this.connection.getMetaData();

    }

    public void prepareDataSetInfo()
    {
        String tableNameInProcess = "";
        try
        {
            String mainTableName = this.props
                    .getProperty(MAIN_TABLE_NAME_PROPERTY);
            String mainTableColumns = this.props
                    .getProperty(MAIN_TABLE_COLUMNS_PROPERTY);
            String mainTableWhereCondition = this.props
                    .getProperty(MAIN_TABLE_WHERE_CONDITION_PROPERTY);
            String sqlInst = "SELECT " + mainTableColumns + " FROM "
                    + mainTableName + " WHERE " + mainTableWhereCondition
                    + " AND NUMBER=" + STUDENT_NUMBER;

            Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlInst);

            ResultSetMetaData resultMetadata = resultSet.getMetaData();

            String mainTablePrimaryKey = readTablePrimaryKey(mainTableName);
            TableInfo mainTableInfo = new TableInfo(mainTableName,
                    mainTablePrimaryKey, mainTableColumns);

            while (resultSet.next())
            {
                mainTableInfo.addIdToSelect(new Integer(resultSet
                        .getInt(mainTablePrimaryKey)));
            }

            ArrayList tablesToProcess = new ArrayList();
            tablesToProcess.add(mainTableInfo);

            int totalTables = 1;
            for (int tableCounter = 0; tableCounter < totalTables; tableCounter++)
            {

                TableInfo tableInfo = (TableInfo) tablesToProcess
                        .get(tableCounter);
                tableNameInProcess = tableInfo.getTableName();
               
                TableInfo processedTableInfo = (TableInfo) this.processedTables
                        .get(tableInfo.getTableName());
                if (processedTableInfo == null)
                {
                    processedTableInfo = tableInfo;
                }

                resultSet = stmt.executeQuery(tableInfo.toSql());
                resultMetadata = resultSet.getMetaData();
                HashMap foreignKeyTableNameMap = preprocessForeingKeys(
                        tableInfo.getTableName(), resultMetadata);

                boolean hasRows = false;
                while (resultSet.next())
                {
                    hasRows = true;
                    int primaryKeyValue = resultSet.getInt(tableInfo
                            .getPrimaryKey());
                    processedTableInfo.addIdToSelect(new Integer(
                            primaryKeyValue));

                    for (int i = 1; i <= resultMetadata.getColumnCount(); i++)
                    {
                        //process foreign keys
                        String columnName = resultMetadata.getColumnName(i);

                        if (foreignKeyTableNameMap.containsKey(columnName))
                        {
                            String fkTableName = (String) foreignKeyTableNameMap
                                    .get(columnName);
                            String fkTablePrimaryKey = readTablePrimaryKey(fkTableName);

                            ArrayList idsToSelect = new ArrayList();
                            idsToSelect.add(new Integer(resultSet
                                    .getInt(columnName)));
                            TableInfo tableInfoToProcess = new TableInfo(
                                    (String) foreignKeyTableNameMap
                                            .get(columnName),
                                    fkTablePrimaryKey, "*", idsToSelect);

                            if (tablesToProcess.contains(tableInfoToProcess) == false)
                            {
                                tablesToProcess.add(tableInfoToProcess);
                                totalTables++;
                            }
                        }

                    }

                    for (Iterator iter = this.manyToManyTables.iterator(); iter
                            .hasNext(); )
                    {
                        //process many-to-many associations
                        String manyToManyTableName = (String) iter.next();
                        ArrayList validReferences = (ArrayList) this.manyToManyTablesValidReferences
                                .get(manyToManyTableName);
                        if (validReferences.contains(tableInfo.getTableName()) == false)
                        {
                            //this table has already been processed by other
                            // table reference
                            //and the other table is the responsible for this
                            // relation
                            continue;
                        }

                        String manyToManyTablePrimaryKey = readTablePrimaryKey(manyToManyTableName);
                        Statement stmtManyToMany = this.connection
                                .createStatement();
                        String sqlQuery = "SELECT * FROM "
                                + manyToManyTableName + " WHERE "
                                + manyToManyTablePrimaryKey + " IS NULL";
                        ResultSet manyToManyResultSet = stmtManyToMany
                                .executeQuery(sqlQuery);
                        ResultSetMetaData manyToManyTableMetadata = manyToManyResultSet
                                .getMetaData();
                        HashMap manyToManyTableFkMap = preprocessForeingKeys(
                                manyToManyTableName, manyToManyTableMetadata);
                        Collection fkMapTableNames = manyToManyTableFkMap
                                .values();
                        if (fkMapTableNames.contains(tableInfo.getTableName()))
                        {
                            Set keys = manyToManyTableFkMap.keySet();
                            Iterator fkNamesInterator = keys.iterator();
                            String fkName = null;
                            while (fkNamesInterator.hasNext())
                            {
                                fkName = (String) fkNamesInterator.next();
                                String fkTableName = (String) manyToManyTableFkMap
                                        .get(fkName);
                                if (fkTableName
                                        .equals(tableInfo.getTableName()))
                                {
                                    //we found the fk name thats references in
                                    // process table
                                    break;
                                }

                            }
                            if (manyToManyTableName == "DEGREE_CURRICULAR_PLAN")
                            {
                                sqlQuery = "SELECT * FROM "
                                        + manyToManyTableName + " WHERE "
                                        + fkName + " = " + primaryKeyValue
                                        + " AND ID_INTERNAL=48";
                            }
                            else
                            {
                                sqlQuery = "SELECT * FROM "
                                        + manyToManyTableName + " WHERE "
                                        + fkName + " = " + primaryKeyValue;
                            }

                            manyToManyResultSet = stmtManyToMany
                                    .executeQuery(sqlQuery);

                            ArrayList idsToSelectInManyToMany = new ArrayList();
                            while (manyToManyResultSet.next())
                            {
                                int manyToManyTablePkValue = manyToManyResultSet
                                        .getInt(manyToManyTablePrimaryKey);
                                idsToSelectInManyToMany.add(new Integer(
                                        manyToManyTablePkValue));
                            }

                            if (idsToSelectInManyToMany.isEmpty() == false)
                            {
                                //we have rows in many to many table that must
                                // be processed
                                TableInfo manyToManyTableInfo = new TableInfo(
                                        manyToManyTableName,
                                        manyToManyTablePrimaryKey, "*");
                                manyToManyTableInfo
                                        .setIdsToSelect(idsToSelectInManyToMany);
                                tablesToProcess.add(manyToManyTableInfo);
                                totalTables++;

                            }

                        }

                    }
                }
                if (hasRows)
                {
                    this.processedTables.put(tableInfo.getTableName(),
                            processedTableInfo);
                }

            }
        }
        catch (SQLException e)
        {
            System.out.println("Tip: Correct table " + tableNameInProcess
                    + " foreign key conventions");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        /*
         * finally { if (this.connection != null) try {
         * this.connection.close(); } catch (SQLException e2) {
         * e2.printStackTrace(); }
         */
    }

    private String readTablePrimaryKey(String tableName) throws SQLException
    {
        ResultSet primaryKeyResultSet = this.databaseMetaData.getPrimaryKeys(
                "", "", tableName);
        primaryKeyResultSet.next(); //move to information row
        String mainTablePrimaryKey = primaryKeyResultSet
                .getString("COLUMN_NAME");
        return mainTablePrimaryKey;
    }

    /**
     * @param tableName
     * @param resultMetadata
     * @return HashMap contaning pairs <KEY_NAME>=<TABLE_NAME>
     * @throws SQLException
     * @throws IOException
     */

    private HashMap preprocessForeingKeys(String nameOfTable,
            ResultSetMetaData resultMetadata) throws SQLException, IOException
    {
        HashMap tableFkCorrections = readTableFkCorrections(nameOfTable);
        HashMap foreignKeyTableMap = new HashMap();
        //check fields to see if they match the fk conventions
        for (int i = 1; i <= resultMetadata.getColumnCount(); i++)
        {
            String columnName = resultMetadata.getColumnName(i);
            if (tableFkCorrections.containsKey(columnName))
            {
                foreignKeyTableMap.put(columnName, tableFkCorrections
                        .get(columnName));
            }
            else
            {
                Iterator conventionsIterator = this.conventions.iterator();
                while (conventionsIterator.hasNext())
                {
                    String convention = (String) conventionsIterator.next();
                    int conventionStartIndex = columnName.indexOf(convention);
                    if (conventionStartIndex != -1)
                    { //we have a FK
                        if (conventionStartIndex == 0)
                        {
                            //prefix convention
                            String tableName = columnName.substring(convention
                                    .length(), columnName.length());
                            foreignKeyTableMap.put(columnName, tableName);
                        }
                        else
                        {
                            //suffix convention
                            String tableName = columnName.substring(0,
                                    columnName.length() - convention.length());
                            foreignKeyTableMap.put(columnName, tableName);
                        }
                    }
                }
            }

        }
        return foreignKeyTableMap;
    }

    private void readManyToManyTablesNamesAndValidReferences()
    {
        this.manyToManyTables = new ArrayList();
        this.manyToManyTablesValidReferences = new HashMap();
        String manyToManyTablesPropertyValue = this.props
                .getProperty(MANY_TO_MANY_TABLES_PROPERTY);
        StringTokenizer tokenizer = new StringTokenizer(
                manyToManyTablesPropertyValue, ",");
        while (tokenizer.hasMoreElements())
        {
            String tableName = tokenizer.nextToken().trim();
            this.manyToManyTables.add(tableName);
            String strValidReferences = this.props.getProperty(tableName
                    + ".validReferences");
            StringTokenizer st = new StringTokenizer(strValidReferences, ",");
            ArrayList validReferences = new ArrayList();
            while (st.hasMoreElements())
            {
                validReferences.add(st.nextToken().trim());
            }
            this.manyToManyTablesValidReferences
                    .put(tableName, validReferences);
        }
    }

    private void readConventions()
    {
        //read conventions
        String conventionsCommaSeparated = this.fkConventionsProps
                .getProperty(CONVENTIONS_PROPERTY);
        StringTokenizer conventionsTokenizer = new StringTokenizer(
                conventionsCommaSeparated, ",");
        this.conventions = new ArrayList();
        while (conventionsTokenizer.hasMoreElements())
        {
            this.conventions.add(conventionsTokenizer.nextToken().trim());
        }

    }

    /**
     * Reads table foreign key corrections (names that do not match standard
     * convention)
     * 
     * @param nameOfTable
     * @return
     */
    private HashMap readTableFkCorrections(String nameOfTable)
    {
        //read table FK corrections
        HashMap tableFkCorrectionsMap = new HashMap();
        String tableCorrections = fkConventionsProps.getProperty(nameOfTable
                + ".corrections");
        if (tableCorrections != null)
        {
            StringTokenizer tokenizerTableCorrections = new StringTokenizer(
                    tableCorrections, ",");
            while (tokenizerTableCorrections.hasMoreElements())
            {
                StringTokenizer tokenizerEqual = new StringTokenizer(
                        tokenizerTableCorrections.nextToken().trim(), "=");
                String fkName = tokenizerEqual.nextToken().trim();
                String fkTableName = tokenizerEqual.nextToken().trim();
                tableFkCorrectionsMap.put(fkName, fkTableName);
            }
        }

        return tableFkCorrectionsMap;
    }

    private IDatabaseConnection getConnectionForDataSet() throws Exception
    {
        return new DatabaseConnection(this.connection);
    }

    public void writeDataSet() throws Exception, SQLException
    {
        connectAndPrepareMetadata();
        prepareDataSetInfo();
        IDataSet dataset = null;
        IDatabaseConnection con = null;

        try
        {
            con = getConnectionForDataSet();
            QueryDataSet queryDataset = new QueryDataSet(con);
            Vector tablesNames = new Vector(this.processedTables.keySet());
            Collections.sort(tablesNames);

            Iterator iter = tablesNames.iterator();
            while (iter.hasNext())
            {
                String tableName = (String) iter.next();
                TableInfo tableInfo = (TableInfo) this.processedTables
                        .get(tableName);
                queryDataset.addTable(tableInfo.getTableName(), tableInfo
                        .toSql());
            }
            dataset = queryDataset;
            String destinationDataSetPath = this.props
                    .getProperty(DESTINATION_DATASET_PROPERTY);
            destinationDataSetPath.replaceAll("\\\\", "/");
            FileWriter fileWriter = new FileWriter(new File(
                    destinationDataSetPath));
            FlatXmlDataSet.write(dataset, fileWriter, "ISO-8859-1");
            fileWriter.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (con != null) try
            {
                con.close();
            }
            catch (SQLException e2)
            {
                e2.printStackTrace();
            }

        }
    }
}