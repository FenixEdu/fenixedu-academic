/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadExecutionDegreeByOIDTest extends ServiceTestCase {

	/**
	 * @param name
	 */
	public ReadExecutionDegreeByOIDTest(java.lang.String testName) {
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
		return "ReadExecutionDegreeByOID";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getSuccessfullArguments() {
		Integer id = new Integer(10);
		Object[] args = { id };
		return args;
	}

	protected Object[] getUnsuccessfullArguments() {
		Integer id = new Integer(1);
		Object[] args = { id };
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

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.INTRANET;
	}

	public void testSuccessfullReadExecutionDegreeByOID() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) ServiceUtils.executeService(
					userView,
					getNameOfServiceToBeTested(),
					getSuccessfullArguments());

			compareDataSet(getExpectedDataSetFilePath());
			assertEquals(infoExecutionDegree.getIdInternal(), new Integer(10));

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadExecutionDegreeByOID" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullReadExecutionDegreeByOID error on compareDataSet"
					+ ex);
		}
	}

	public void testUnsuccessfullReadExecutionDegreeByOID() {
		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
				userView,
				getNameOfServiceToBeTested(),
				getUnsuccessfullArguments());
			if (infoExecutionDegree == null) {
				compareDataSet(getExpectedDataSetFilePath());
				System.out.println(
					"testUnsuccessfullReadExecutionDegreeByOID "
						+ "was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
				
			}
			else {
				fail("testUnsuccessfullReadExecutionDegreeByOID reading of an unexisting Execution Degree");
			}
			
		} catch (FenixServiceException ex) {
			fail(
				"testUnsuccessfullReadCurricularYearByOID error on compareDataSet"
					+ ex);
			
		}
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try {
			return (IUserView) ServiceUtils.executeService(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;

		}
	}

}
