package UtilTests.enrollment;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class dbaccess {
    private IDatabaseConnection _connection = null;

    private String dbName;

    private String username;

    private String password;

    public dbaccess() {
    }

    public void openConnection() throws Exception {
        if (_connection == null) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection jdbcConnection = DriverManager.getConnection(this
                    .getDbName(), this.getUsername(), this.getPassword());
            _connection = new DatabaseConnection(jdbcConnection);
        }
    }

    public void closeConnection() throws Exception {
        _connection.close();
        _connection = null;
    }

    public void backUpDataBaseContents(String filename) throws Exception {
        IDataSet fullDataSet = _connection.createDataSet();
        FileWriter fileWriter = new FileWriter(new File(filename));
        FlatXmlDataSet.write(fullDataSet, fileWriter, "ISO-8859-1");
    }

    public void loadDataBase(String filename) throws Exception {
        FileReader fileReader = new FileReader(new File(filename));
        IDataSet dataSet = new FlatXmlDataSet(fileReader);
        DatabaseOperation.INSERT.execute(_connection, dataSet);
    }

    public IDatabaseConnection getConnection() {
        return _connection;
    }

    public String getDbName() {
        if (this.dbName == null) {
            return "jdbc:mysql://localhost/ciapl";
        }
        return "jdbc:mysql://localhost/" + this.dbName;

    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPassword() {
        if (this.password == null) {
            return "";
        }
        return this.password;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        if (this.username == null) {
            return "root";
        }
        return this.username;

    }

    public void setUsername(String username) {
        this.username = username;
    }

}