package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class CreateBibliographicReferenceTest
	extends BibliographicReferenceBelongsExecutionCourse {

	String author = "Shari Pfleeger";
	String title = "Software Engineering: Theory and Practice";
	String reference = "Recomended bibliography";
	String year = "2002";
	Boolean optional = new Boolean(false);

	/**
	 * @param testName
	 */
	public CreateBibliographicReferenceTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "CreateBibliographicReference";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testCreateBibliographicReferenceDataSet.xml";
	}

	protected String getRecomendedExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedCreateRecomendedBibliographicReferenceDataSet.xml";
	}

	protected String getOptionalExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedCreateOptionalBibliographicReferenceDataSet.xml";
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

		Object[] args =
			{ executionCourseCode, title, author, reference, year, optional };

		return args;
	}

	protected Object[] getTestBibliographicReferenceSuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);

		Object[] args =
			{ executionCourseCode, title, author, reference, year, optional };

		return args;
	}

	protected Object[] getTestBibliographicReferenceUnsuccessfullArguments() {

		Integer executionCourseCode = new Integer(242);

		Object[] args =
			{ executionCourseCode, title, author, reference, year, optional };

		return args;
	}

	protected Object[] getTestExistingRecomendedBibliographicReferenceArguments() {

		Integer executionCourseCode = new Integer(24);
		author = "pedro";
		title = "xpto";
		reference = "ref";
		year = "2002";
		optional = new Boolean(false);

		Object[] args =
			{ executionCourseCode, title, author, reference, year, optional };

		return args;
	}

	protected Object[] getTestExistingOptionalBibliographicReferenceArguments() {

		Integer executionCourseCode = new Integer(24);
		author = "rs";
		title = "ep";
		reference = "ref5";
		year = "2040";
		optional = new Boolean(true);

		Object[] args =
			{ executionCourseCode, title, author, reference, year, optional };

		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfullCreateRecomendedBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getRecomendedExpectedDataSetFilePath());
			System.out.println(
				"testSuccessfullCreateRecomendedBibliographicReference "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullCreateRecomendedBibliographicReference" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullCreateRecomendedBibliographicReference error on compareDataSet"
					+ ex);
		}
	}

	public void testUnuccessfullCreateExistingRecomendedBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getTestExistingRecomendedBibliographicReferenceArguments());

			// verificar as alteracoes da bd
			//compareDataSet(getRecomendedExpectedDataSetFilePath());
			fail(
				"testUnuccessfullCreateExistingRecomendedBibliographicReference: "
					+ "insertion of an existing bibliography");

		} catch (FenixServiceException ex) {
			System.out.println(
				"testUnuccessfullCreateExistingRecomendedBibliographicReference "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			fail(
				"testUnuccessfullCreateExistingRecomendedBibliographicReference error on compareDataSet"
					+ ex);
		}
	}

	public void testSuccessfullCreateOptionalBibliographicReference() {

		author = "Shari Pfleeger";
		title = "Software Engineering: Theory and Practice";
		reference = "Optional bibliography";
		year = "2002";
		optional = new Boolean(true);

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getOptionalExpectedDataSetFilePath());
			System.out.println(
				"testSuccessfullCreateOptionalBibliographicReference "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullCreateOptionalBibliographicReference" + ex);
		} catch (Exception ex) {
			fail(
				"testSuccessfullCreateOptionalBibliographicReference error on compareDataSet"
					+ ex);
		}
	}

	public void testUnsuccessfullCreateExistingOptionalBibliographicReference() {

		try {

			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getTestExistingOptionalBibliographicReferenceArguments());

			// verificar as alteracoes da bd
			compareDataSet(getOptionalExpectedDataSetFilePath());
			fail(
				"testUnsuccessfullCreateExistingOptionalBibliographicReference: "
					+ "insertion of an existing bibliography");

		} catch (FenixServiceException ex) {
			System.out.println(
				"testUnsuccessfullCreateExistingOptionalBibliographicReference "
					+ "was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			fail(
				"testUnsuccessfullCreateExistingOptionalBibliographicReference error on compareDataSet"
					+ ex);
		}
	}

}