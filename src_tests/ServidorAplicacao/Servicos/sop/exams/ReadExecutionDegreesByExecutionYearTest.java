/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import DataBeans.InfoExecutionYear;
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
public class ReadExecutionDegreesByExecutionYearTest extends ServiceTestCase {

	/**
	 * @param name
	 */
	public ReadExecutionDegreesByExecutionYearTest(java.lang.String testName) {
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
		return "ReadExecutionDegreesByExecutionYear";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getSuccessfullArguments() {
		Integer id = new Integer(1);
		InfoExecutionYear info = new InfoExecutionYear();
		info.setYear("2002/2003");
		info.setIdInternal(id);
		Object[] args = { info };
		return args;
	}

	protected Object[] getUnsuccessfullArguments() {
		Integer id = new Integer(6);
		InfoExecutionYear info = new InfoExecutionYear();
		info.setYear("2007/2008");
		info.setIdInternal(id);
		Object[] args = { info };
		return args;
	}
	
	protected Object[] getSuccessfullArgumentsForEmptyList() {
		Integer id = new Integer(5);
		InfoExecutionYear info = new InfoExecutionYear();
		info.setYear("2004/2005");
		info.setIdInternal(id);
		Object[] args = { info };
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

	public void testSuccessfullReadExecutionDegreesByExecutionYear() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			List listExecutionDegrees =
				(List) ServiceUtils.executeService(
					userView,
					getNameOfServiceToBeTested(),
					getSuccessfullArguments());
			
			compareDataSet(getExpectedDataSetFilePath());
			assertEquals(listExecutionDegrees.size(),4);

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadExecutionDegreesByExecutionYear" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullReadExecutionDegreesByExecutionYear error on compareDataSet"
					+ ex);
		}
	}
	
	public void testSuccessfullReadExecutionDegreeByExecutionYearEmpty() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			List listExecutionDegrees =
				(List) ServiceUtils.executeService(
					userView,
					getNameOfServiceToBeTested(),
					getSuccessfullArgumentsForEmptyList());

			compareDataSet(getExpectedDataSetFilePath());
			assertEquals(listExecutionDegrees.size(),0);

		} catch (FenixServiceException ex) {
			fail("testSuccessfullReadExecutionDegreeByExecutionYearEmpty" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullReadExecutionDegreeByExecutionYearEmpty error on compareDataSet"
					+ ex);
		}
	}

	public void testUnsuccessfullReadExecutionDegreeByExecutionYear() {
		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			List list = (List) ServiceUtils.executeService(userView,getNameOfServiceToBeTested(),getUnsuccessfullArguments());
			if (list == null)
				fail("testUnsuccessfullReadExecutionDegreeByExecutionYear reading of an unexisting Execution Degree");
			else {
				compareDataSet(getExpectedDataSetFilePath());
				System.out.println(
					"testUnsuccessfullReadExecutionDegreeByExecutionYear "
						+ "was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			}
			
		} catch (FenixServiceException ex) {
			fail(
				"testUnsuccessfullReadCurricularYearByOID error on test"
					+ ex);		
		}
		catch (Exception ex){
			fail(
				"testUnsuccessfullReadExecutionDegreeByExecutionYear error on compareDataSet"
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
