package ServidorAplicacao.Servicos.teacher;

import java.io.FileInputStream;
import java.sql.DriverManager;
import ServidorAplicacao.Servicos.ServiceTestCase;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.sql.Connection;

public class InsertItemDbUnitTest extends ServiceTestCase {

	public InsertItemDbUnitTest(String name) {
		super(name);
	}

	protected String getDataSetFilePath() {
		return "etc/sfsf.xml";		
	}
	
	public void testSuccessfullInsertItem() {
		
	}

}
