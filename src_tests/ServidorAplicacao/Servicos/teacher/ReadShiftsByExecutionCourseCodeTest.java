/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByExecutionCourseCodeTest extends TestCaseReadServices {

	public ReadShiftsByExecutionCourseCodeTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadShiftsByExecutionCourseCode";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(26)};
		return args;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 3;
	}

	protected Object getObjectToCompare() {
		List shiftList = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			ITurnoPersistente turnoPersistente = sp.getITurnoPersistente();
			ITurno turno19 = new Turno(new Integer(19));
			turno19 = (ITurno) turnoPersistente.readByOId(turno19, false);
			assertNotNull("turno19 null", turno19);

			ITurno turno20 = new Turno(new Integer(20));
			turno20 = (ITurno) turnoPersistente.readByOId(turno20, false);
			assertNotNull("turno20 null", turno20);

			ITurno turno21 = new Turno(new Integer(21));
			turno21 = (ITurno) turnoPersistente.readByOId(turno21, false);
			assertNotNull("turno21 null", turno21);

			sp.confirmarTransaccao();

			InfoShift infoTurno19 = (InfoShift) Cloner.get(turno19);
			InfoShift infoTurno20 = (InfoShift) Cloner.get(turno20);
			InfoShift infoTurno21 = (InfoShift) Cloner.get(turno21);

			List lessons = turno19.getAssociatedLessons();
			Iterator itLessons = lessons.iterator();
			List infoLessons = new ArrayList();
			InfoLesson infoLesson;
			while (itLessons.hasNext()) {
				infoLesson =
					Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
				infoLessons.add(infoLesson);
			}
			infoTurno19.setInfoLessons(infoLessons);

			lessons = turno20.getAssociatedLessons();
			itLessons = lessons.iterator();
			infoLessons = new ArrayList();
			while (itLessons.hasNext()) {
				infoLesson =
					Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
				infoLessons.add(infoLesson);
			}
			infoTurno20.setInfoLessons(infoLessons);
			
			lessons = turno21.getAssociatedLessons();
			itLessons = lessons.iterator();
			infoLessons = new ArrayList();
			while (itLessons.hasNext()) {
				infoLesson =
					Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
				infoLessons.add(infoLesson);
			}
			infoTurno21.setInfoLessons(infoLessons);
			
			shiftList.add(infoTurno19);
			shiftList.add(infoTurno20);
			shiftList.add(infoTurno21);
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia ");
		}
		return shiftList;
	}

}
