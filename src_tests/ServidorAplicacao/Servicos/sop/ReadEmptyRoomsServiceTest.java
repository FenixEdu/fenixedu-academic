/*
 * ReadEmptyRoomsServiceTest.java
 * JUnit based test
 *
 * Created on 2003/03/16
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.DiaSemana;
import Util.TipoAula;
import Util.TipoSala;

public class ReadEmptyRoomsServiceTest extends TestCaseReadServices {
	public ReadEmptyRoomsServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadEmptyRoomsServiceTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadEmptyRoomsService";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		InfoRoom infoRoom = new InfoRoom();
		InfoLesson infoLesson = new InfoLesson();

		infoLesson.setDiaSemana(new DiaSemana(DiaSemana.TERCA_FEIRA));
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 8);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		infoLesson.setInicio(start);
		Calendar end = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 9);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		infoLesson.setFim(end);
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º semestre", new InfoExecutionYear("2002/2003"));
		

		Object[] result = { infoRoom, infoLesson, infoExecutionPeriod };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {		
		return 1;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 12);
		inicio.set(Calendar.MINUTE, 30);
		inicio.set(Calendar.SECOND, 0);
		Calendar fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 13);
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);
		InfoLesson infoLesson =
			new InfoLesson(
				new DiaSemana(2),
				inicio,
				fim,
				new TipoAula(1),
				new InfoRoom(
					"Ga1",
					"Pavilhao Central",
					new Integer(0),
					new TipoSala(1),
					new Integer(100),
					new Integer(50)),
				new InfoExecutionCourse(
					"Trabalho Final de Curso I",
					"TFCI",
					"programa1",
					new Double(0),
					new Double(0),
					new Double(0),
					new Double(0),
					new InfoExecutionPeriod(
						"2º Semestre",
						new InfoExecutionYear("2002/2003"))));
		return infoLesson;
	}
	
	protected boolean needsAuthorization() {
				return true;
			}

}
