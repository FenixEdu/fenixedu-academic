/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import DataBeans.InfoCurricularYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.commons.ReadCurricularYearByOID.UnexistingCurricularYearException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadCurricularYearByOIDTest extends ServiceTestCase {

	/**
	 * @param name
	 */
	public ReadCurricularYearByOIDTest(java.lang.String testName) {
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
		return "ReadCurricularYearByOID";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getSuccessfullArguments() {
		Integer id = new Integer(1);
		Object[] args = { id };
		return args;
	}

	protected Object[] getUnsuccessfullArguments() {
		Integer id = new Integer(6);
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

	public void testSuccessfullReadCurricularYearByOID() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			InfoCurricularYear infoCurricularYear =
				(InfoCurricularYear) ServiceUtils.executeService(
					userView,
					getNameOfServiceToBeTested(),
					getSuccessfullArguments());

			compareDataSet(getExpectedDataSetFilePath());
			assertEquals(infoCurricularYear.getIdInternal(), new Integer(1));

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadCurricularYearByOID" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullReadCurricularYearByOID error on compareDataSet"
					+ ex);
		}
	}

	public void testUnsuccessfullReadCurricularYearByOID() {
		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ServiceUtils.executeService(
				userView,
				getNameOfServiceToBeTested(),
				getUnsuccessfullArguments());

			fail("testUnsuccessfullReadCurricularYearByOID reading of an unexisting Curricular Year");

		} catch (UnexistingCurricularYearException ex) {
			try {
				compareDataSet(getExpectedDataSetFilePath());
			} catch (Exception exc) {
				fail(
					"testUnsuccessfullReadCurricularYearByOID error on compareDataSet"
						+ exc);
			}
			System.out.println(
				"testUnsuccessfullReadCurricularYearByOID "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail(
				"testUnsuccessfullReadCurricularYearByOID error on test"
					+ e);
			
		}
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try {
			return (IUserView)ServiceUtils.executeService(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;

		}
	}

}
