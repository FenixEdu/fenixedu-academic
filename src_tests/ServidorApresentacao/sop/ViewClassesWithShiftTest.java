/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCaseActionExecution;
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
public class ViewClassesWithShiftTest extends TestCaseActionExecution {
	
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
		return "/WEB-INF/tests/web-sop.xml";
	}

	public void testSuccessfulExecution() {
		//create new user view with privileges for executing action sucessfully
		getSession().removeAttribute(SessionConstants.U_VIEW);
		this.userView = null;
		HashSet privileges = new HashSet();
		privileges.add("ReadClassesWithShiftService");
		this.userView = new UserView("user", privileges);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
		
		//items in request
		HashMap itemsInRequest = new HashMap();
		String forward = "sucess";
		itemsInRequest.put("name", "turno1");
		
		//items in session
		HashMap itemsInSession = new HashMap();		
		readShifts();
		itemsInSession.put(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY, this.infoShifts);
		
		doTest(itemsInRequest, itemsInSession, forward, null, null, null, null);
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
}
