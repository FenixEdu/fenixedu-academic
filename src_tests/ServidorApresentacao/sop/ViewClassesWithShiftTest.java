/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 27/Fev/2003
 */
public class ViewClassesWithShiftTest extends TestCasePresentationSopPortal{
	
	private List infoShifts = new ArrayList();

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(ViewClassesWithShiftTest.class);
		return suite;
	}
		
	public void setUp() {
		super.setUp();
	}
	
	public ViewClassesWithShiftTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/viewClassesWithShift";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/sop";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	private void readShifts() {

		ITurno shift = null;
		IDisciplinaExecucao executionCourse = null;
		SuportePersistenteOJB persistentSupport;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		ITurnoPersistente persistentShift = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			persistentShift = persistentSupport.getITurnoPersistente();

			persistentSupport.iniciarTransaccao();
			executionCourse = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
			assertNotNull(executionCourse);

			// Read Existing
			shift = persistentShift.readByNameAndExecutionCourse("turno1", executionCourse);
			assertNotNull(shift);
			assertEquals(shift.getNome(), "turno1");
			assertEquals(shift.getDisciplinaExecucao(), executionCourse);

			persistentSupport.confirmarTransaccao();
			
			this.infoShifts.add(Cloner.copyIShift2InfoShift(shift));
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		//create new user view with privileges for executing action sucessfully
		getSession().removeAttribute(SessionConstants.U_VIEW);
		setAuthorizedUser();

		InfoDegree infoDegree = new InfoDegree();
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		
		infoExecutionYear.setYear("2002/2003");
		
		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
		infoDegreeCurricularPlan.setName("plano1");
		infoDegree.setSigla("LEIC");
		infoDegree.setNome("Licenciatura de Engenharia Informatica e de Computadores");
		
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		HashMap hashMap = new HashMap();
		hashMap.put(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
		
		readShifts();
		hashMap.put(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY, this.infoShifts);
		hashMap.put(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		return hashMap;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap itemsInRequest = new HashMap();
		itemsInRequest.put("name", "turno1");
		return itemsInRequest;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		// 
		return null;
	}
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "sucess";
	}

}
