/*
 * Created on 18/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoBibliographicReference;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentationTeacherPortal;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BibliographicReferenceManagerDispatchActionTest
	extends TestCasePresentationTeacherPortal {

	public static void main(java.lang.String[] args) {
		System.out.println("Começou a executar teste");
		junit.textui.TestRunner.run(suite());
	}

	public BibliographicReferenceManagerDispatchActionTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				BibliographicReferenceManagerDispatchActionTest.class);
		return suite;
	}

	public void setUp() {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-teacher.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/bibliographicReferenceManager";
	}

	public void testUnAuthorizedCreateBibliographicReference() {

		//set request path
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Inserir");

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setNome("Trabalho Final de Curso I");
		infoExecutionCourse.setSigla("TFCI");
		infoExecutionCourse.setPrograma("programa1");
		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
		infoExecutionCourse.setPraticalHours(new Double(2));
		infoExecutionCourse.setTheoPratHours(new Double(1.5));
		infoExecutionCourse.setLabHours(new Double(2));
		InfoExecutionPeriod iep =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		infoExecutionCourse.setInfoExecutionPeriod(iep);
		getSession().setAttribute("InfoExecutionCourse", infoExecutionCourse);
		setNotAuthorizedUser();

		//fills the form
		addRequestParameter("title", "matemática");
		addRequestParameter("authors", "jose");
		addRequestParameter("reference", "ref4");
		addRequestParameter("year", "2002");
		addRequestParameter("optional", "0");

		//action perform
		actionPerform();

		//verify that there are errors
		String[] errors = { "ServidorAplicacao.NotAuthorizedException" };
		verifyActionErrors(errors);
	}

	public void testAuthorizedCreateBibliographicReference() {

		//set request path
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Inserir");

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setNome("Trabalho Final de Curso I");
		infoExecutionCourse.setSigla("TFCI");
		infoExecutionCourse.setPrograma("programa1");
		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
		infoExecutionCourse.setPraticalHours(new Double(2));
		infoExecutionCourse.setTheoPratHours(new Double(1.5));
		infoExecutionCourse.setLabHours(new Double(2));
		InfoExecutionPeriod iep =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		infoExecutionCourse.setInfoExecutionPeriod(iep);
		getSession().setAttribute("InfoExecutionCourse", infoExecutionCourse);

		setAuthorizedUser();

		//fills the form
		addRequestParameter("title", "matemática");
		addRequestParameter("authors", "jose");
		addRequestParameter("reference", "ref4");
		addRequestParameter("year", "2002");
		addRequestParameter("optional", "0");

		//action perform
		actionPerform();

		//verify that there are errors

		verifyForward("bibliographyManagement");
	}

	public void testAuthorizedViewBibliographicReference() {

		//set request path
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Visualizar");

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setNome("Trabalho Final de Curso I");
		infoExecutionCourse.setSigla("TFCI");
		infoExecutionCourse.setPrograma("programa1");
		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
		infoExecutionCourse.setPraticalHours(new Double(2));
		infoExecutionCourse.setTheoPratHours(new Double(1.5));
		infoExecutionCourse.setLabHours(new Double(2));
		InfoExecutionPeriod iep =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		infoExecutionCourse.setInfoExecutionPeriod(iep);
		getSession().setAttribute("InfoExecutionCourse", infoExecutionCourse);

		setAuthorizedUser();

		//action perform
		actionPerform();

		assertNotNull(getSession().getAttribute("BibliographicReferences"));
		ArrayList biblioRefs =
			(ArrayList) getSession().getAttribute("BibliographicReferences");
		assertEquals(biblioRefs.size(), 2);
		InfoBibliographicReference infoBibRef = (InfoBibliographicReference)biblioRefs.get(0);
		assertEquals(infoBibRef.getTitle(),"xpto");
		verifyForward("bibliographyManagement");
	}

	public void testAuthorizedDeleteBibliographicReference() {

		//set request path		
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Apagar");
		addRequestParameter("infoBibliographicReferenceIndex", "1");
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setNome("Trabalho Final de Curso I");
		infoExecutionCourse.setSigla("TFCI");
		infoExecutionCourse.setPrograma("programa1");
		infoExecutionCourse.setTheoreticalHours(new Double(1.5));
		infoExecutionCourse.setPraticalHours(new Double(2));
		infoExecutionCourse.setTheoPratHours(new Double(1.5));
		infoExecutionCourse.setLabHours(new Double(2));
		
		InfoExecutionPeriod iep =
			new InfoExecutionPeriod(
				"2º Semestre",
				new InfoExecutionYear("2002/2003"));
		infoExecutionCourse.setInfoExecutionPeriod(iep);

		HttpSession session = getSession();

		session.setAttribute("InfoExecutionCourse", infoExecutionCourse);

		UserView userView = (UserView) session.getAttribute("UserView");
		Object args[] = { infoExecutionCourse, null };
		GestorServicos gestor = GestorServicos.manager();
		ArrayList references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args);
		} catch (Exception e) {			
		}
		
		session.setAttribute("BibliographicReferences", references);

		setAuthorizedUser();

		//action perform
		actionPerform();
		
		ArrayList biblioRefs =
			(ArrayList) session.getAttribute("BibliographicReferences");
		assertEquals(biblioRefs.size(), 1);		 				
		verifyForward("bibliographyManagement");
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// TODO Auto-generated method stub
		return null;
	}

}