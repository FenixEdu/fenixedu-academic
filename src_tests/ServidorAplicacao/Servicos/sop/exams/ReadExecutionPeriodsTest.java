/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.io.File;
import java.util.List;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadExecutionPeriodsTest extends ServiceTestCase{

	/**
	 * @param name
	 */
	public ReadExecutionPeriodsTest(java.lang.String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
	 */
	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}


	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNotSOPEmployeeUser()
	 */
	protected String[] getNotSOPEmployeeUser() {
		String[] args = { "45498", "pass", getApplication()};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadExecutionPeriods";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getSuccessfullArguments() {
		Object[] args = {};
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/sop/testCommonDataSet.xml";
	}
	
	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/sop/testExpectedCommonDataSet.xml";
	}
	protected String getDataSetWithoutExecutionPeriodFilePath() {
		return "etc/datasets/servicos/sop/testNoExecutionPeriodsDataSet.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.INTRANET;
	}
	
	public void testSuccessfullReadExecutionPeriods() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			List executionPeriodsList = (List) ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), getSuccessfullArguments());

			compareDataSet(getExpectedDataSetFilePath());
			assertEquals(executionPeriodsList.size(), 8);

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadExecutionPeriods" + ex);
		} catch (Exception ex) {
			fail("testSuccessfullReadExecutionPeriods error on compareDataSet" + ex);
		}
	}
	
	public void testSuccessfullReadExecutionPeriodsEmptyList() {
		setUpAux();
		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			List executionPeriodsList = (List) ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), getSuccessfullArguments());

			assertEquals(executionPeriodsList.size(), 0);

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadExecutionPeriodsEmptyList" + ex);
		} catch (Exception ex) {
			fail("testSuccessfullReadExecutionPeriodsEmptyList error on compareDataSet" + ex);
		}
	}
	
	protected void setUpAux() {

		try {
			super.setUp();
			backUpDataBaseContents();

			IDatabaseConnection connection = getConnection();
			IDataSet dataSet = new FlatXmlDataSet(new File(getDataSetWithoutExecutionPeriodFilePath()));

			//String[] tableNames = { "OJB_HL_SEQ" };
			IDataSet fullDataSet = connection.createDataSet();
			DatabaseOperation.DELETE_ALL.execute(connection, fullDataSet);

			DatabaseOperation.INSERT.execute(connection, dataSet);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.clearCache();

			connection.close();
		} catch (Exception ex) {
			fail("Setup failed loading database with test data set: " + ex);
		}
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try {
			return (IUserView) ServiceUtils.executeService(
				null,
				"Autenticacao",
				args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;

		}
	}

}
