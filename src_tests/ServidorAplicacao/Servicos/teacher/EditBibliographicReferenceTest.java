package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class EditBibliographicReferenceTest extends BibliographicReferenceBelongsExecutionCourse {

	String author = "Shari Pfleeger";
	String title = "Software Engineering: Theory and Practice";
	String reference = "Recomended bibliografy";
	String year = "2002";
	Boolean optional = new Boolean(false);

	/**
	 * @param testName
	 */
	public EditBibliographicReferenceTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditBibliographicReference";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testEditBibliographicReferenceDataSet.xml";
	}

	protected String getRecomendedExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedEditRecomendedBibliographicReferenceDataSet.xml";
	}

	protected String getOptionalExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedEditOptionalBibliographicReferenceDataSet.xml";
	}


	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(1);

		Object[] args =
			{
				executionCourseCode,
				bibliographicReferenceCode,
				title,
				author,
				reference,
				year,
				optional };

		return args;
	}

	protected Object[] getTestBibliographicReferenceSuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(1);

		Object[] args =
			{
				executionCourseCode,
				bibliographicReferenceCode,
				title,
				author,
				reference,
				year,
				optional };

		return args;
	}

	protected Object[] getTestBibliographicReferenceUnsuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer bibliographicReferenceCode = new Integer(123);

		Object[] args =
			{
				executionCourseCode,
				bibliographicReferenceCode,
				title,
				author,
				reference,
				year,
				optional };

		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfullEditRecomendedBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getRecomendedExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullEditRecomendedBibliographicReference" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullEditRecomendedBibliographicReference error on compareDataSet" + ex);
		}
	}

	//	public void testSuccessfullEditOptionalBibliographicReference() {
	//
	//		try {
	//			author = "Shari Pfleeger";
	//			title = "Software Engineering: Theory and Practice";
	//			reference = "Optional bibliografy";
	//			year = "2002";
	//			optional = new Boolean(true);
	//			String[] args = getAuthorizedUser();
	//			IUserView userView = authenticateUser(args);
	//
	//			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());
	//
	//			// verificar as alteracoes da bd
	//			compareDataSet(getOptionalExpectedDataSetFilePath());
	//
	//		} catch (FenixServiceException ex) {
	//			fail("testSuccessfullEditOptionalBibliographicReference" + ex);
	//		} catch (Exception ex) {
	//			fail("testSuccessfullEditOptionalBibliographicReference error on compareDataSet" + ex);
	//		}
	//	}

}