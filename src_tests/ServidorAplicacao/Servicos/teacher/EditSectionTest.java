package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class EditSectionTest extends SectionBelongsExecutionCourseTest {

	/**
	 * @param testName
	 */
	public EditSectionTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "EditSection";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testEditSectionDataSet.xml";
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Object[] args =
			{ new Integer(27), new Integer(6), "novoNome", new Integer(0)};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected Object[] getTestSectionSuccessfullArguments() {
		Object[] args =
			{ new Integer(27), new Integer(6), "novoNome", new Integer(0)};
		return args;
	}

	protected Object[] getTestSectionUnsuccessfullArguments() {
		Object[] args =
			{ new Integer(27), new Integer(7), "novoNome", new Integer(0)};
		return args;
	}

	public void testEditExistingSection() {
		Object[] args = getTestSectionSuccessfullArguments();

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);

			compareDataSet("etc/datasets/testExpectedEditSectionDataSet.xml");
			System.out.println(
				"testEditExistingSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testEditExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditExistingSection");
		}
	}

	public void testEditNonExistingSection() {
		Object[] args =
			{ new Integer(27), new Integer(100), "novoNome", new Integer(0)};

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testEditNonExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditNonExistingSection");

		} catch (NotAuthorizedException e) {
			System.out.println(
				"testEditNonExistingSection was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testEditNonExistingSection was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testEditNonExistingSection");
		}
	}

}
