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
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BibliographicReferenceManagerDispatchActionTest
	extends TestCasePresentationTeacherPortal {

	public static void main(java.lang.String[] args) {
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

	/*public void testUnAuthorizedCreateBibliographicReference() {
	
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
	}*/

	public void testAuthorizedCreateBibliographicReference() {

		//set request path
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Confirmar Inserir");

		
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
		
		InfoSite infoSite = new InfoSite(infoExecutionCourse);		
		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);

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

		InfoSite infoSite = new InfoSite(infoExecutionCourse);		
		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);				

		setAuthorizedUser();

		//action perform
		actionPerform();

		assertNotNull(getSession().getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST));
		ArrayList biblioRefs =
			(ArrayList) getSession().getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);
		assertEquals(biblioRefs.size(), 3);
		InfoBibliographicReference infoBibRef =
			(InfoBibliographicReference) biblioRefs.get(0);
		assertEquals(infoBibRef.getTitle(), "xpto");
		verifyForward("bibliographyManagement");
	}

	public void testAuthorizedDeleteBibliographicReference() {

		//set request path		
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Apagar");
		addRequestParameter("infoBibliographicReferenceIndex", "0");
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
		InfoSite infoSite = new InfoSite(infoExecutionCourse);		
		session.setAttribute(SessionConstants.INFO_SITE, infoSite);
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

		session.setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST, references);
		setAuthorizedUser();

		//action perform
		actionPerform();

		ArrayList biblioRefs =
			(ArrayList) session.getAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE_LIST);
		assertEquals(biblioRefs.size(), 2);
		InfoBibliographicReference infoBibRef =
			(InfoBibliographicReference) biblioRefs.get(0);
		assertEquals(infoBibRef.getTitle(), "as");
		verifyForward("bibliographyManagement");
	}

	public void testAuthorizedEditBibliographicReference() {

		//set request path
		setRequestPathInfo("/teacher", "/bibliographicReferenceManager");
		//sets needed objects to session/request
		addRequestParameter("method", "Confirmar Editar");

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
		InfoSite infoSite = new InfoSite(infoExecutionCourse);		
		getSession().setAttribute(SessionConstants.INFO_SITE, infoSite);
		Object args1[] = { infoExecutionCourse, null };
		GestorServicos gestor = GestorServicos.manager();
		UserView userView = (UserView) getSession().getAttribute("UserView");
		ArrayList references = null;
		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		InfoBibliographicReference infoBibRef =
			(InfoBibliographicReference) references.get(0);

		getSession().setAttribute(SessionConstants.INFO_BIBLIOGRAPHIC_REFERENCE, infoBibRef);
		setAuthorizedUser();
		//fills the form
		addRequestParameter("title", "matemática");
		addRequestParameter("authors", "jose");
		addRequestParameter("reference", "ref4");
		addRequestParameter("year", "2002");
		addRequestParameter("optional", "0");

		//action perform
		actionPerform();		

		try {
			references =
				(ArrayList) gestor.executar(
					userView,
					"ReadBibliographicReference",
					args1);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		InfoBibliographicReference infoBiblioRefVerify = (InfoBibliographicReference)references.get(0);		
		assertEquals(infoBiblioRefVerify.getTitle(),"matemática");
		assertEquals(infoBiblioRefVerify.getAuthors(),"jose");
		assertEquals(infoBiblioRefVerify.getReference(),"ref4");
		assertEquals(infoBiblioRefVerify.getYear(),"2002");		
		verifyForward("bibliographyManagement");

	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}
}