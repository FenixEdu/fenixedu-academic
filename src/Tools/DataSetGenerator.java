package Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

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
 *         (naat@mega.ist.utl.pt)
 */
public class DataSetGenerator
{

    private static final String DB_PASS_PROPERTY = "db.pass";

    private static final String DB_USER_PROPERTY = "db.user";

    private static final String DB_URL_PROPERTY = "db.url";

    private static final String DB_DRIVER_PROPERTY = "db.driver";

    private static final String DATASET_CONFIG = "/DataSetGenerator.properties";

    private static final String DESTINATION_DATASET_PROPERTY = "destination.dataset";

    private Properties props;

    private ArrayList propertiesToIgnore;

    /**
     * 
     */
    public DataSetGenerator(String configFilename) throws NullPointerException,
            FileNotFoundException, IOException
    {
        this.props = new Properties();
        this.props.load(new FileInputStream(configFilename));
        addPropertiesToIgnore();
    }

    public DataSetGenerator(String configFilename, String filePath)
            throws NullPointerException, FileNotFoundException, IOException
    {
        this.props = new Properties();
        this.props.load(new FileInputStream(configFilename));
        this.props.put("destination.dataset", filePath);
        addPropertiesToIgnore();
    }

    /**
     * 
     * */
    public DataSetGenerator() throws IOException, NullPointerException
    {
        this.props = new Properties();
        this.props.load(DataSetGenerator.class
                .getResourceAsStream(DATASET_CONFIG));
        addPropertiesToIgnore();
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

    private IDatabaseConnection getConnection() throws Exception
    {
        String driver = this.props.getProperty(DB_DRIVER_PROPERTY);
        String url = this.props.getProperty(DB_URL_PROPERTY);
        String user = this.props.getProperty(DB_USER_PROPERTY);
        String pass = this.props.getProperty(DB_PASS_PROPERTY);

        Class.forName(driver);
        Connection jdbcConnection = DriverManager
                .getConnection(url, user, pass);

        return new DatabaseConnection(jdbcConnection);
    }

    public void writeDataSet()
    {
        IDataSet dataset = null;
        IDatabaseConnection con = null;

        try
        {
            con = getConnection();
            QueryDataSet queryDataset = new QueryDataSet(con);

            Enumeration enum = this.props.propertyNames();
            while (enum.hasMoreElements())
            {
                String propertyName = (String) enum.nextElement();
                if (propertyName.endsWith(".columns")
                        || propertyName.endsWith(".whereCondition")
                        || propertiesToIgnore.contains(propertyName)) continue; //we
                // have
                // already
                // processed
                // this
                // properties

                String tableName = this.props.getProperty(propertyName);
                String tableColumns = this.props.getProperty(propertyName
                        + ".columns");
                String tableWhereCondition = this.props
                        .getProperty(propertyName + ".whereCondition");
                String sqlInst = "SELECT " + tableColumns + " FROM "
                        + tableName + " WHERE " + tableWhereCondition;
                queryDataset.addTable(tableName, sqlInst);
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
