package ServidorAplicacao.Servicos.student;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class ReadShiftsByTypeFromExecutionCourseServicesTest extends TestCaseReadServices {
	public ReadShiftsByTypeFromExecutionCourseServicesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadShiftsByTypeFromExecutionCourseServicesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}



	protected String getNameOfServiceToBeTested() {
		return "ReadShiftsByTypeFromExecutionCourse";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		IExecutionCourse executionCourse = null; 
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			executionCourse = SuportePersistenteOJB.getInstance().getIPersistentExecutionCourse().readBySiglaAndAnoLectivoAndSiglaLicenciatura("APR", "2002/2003", "LEIC");
			assertNotNull(executionCourse);
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		Object[] result = { Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse), new TipoAula(TipoAula.RESERVA) };
		return result;
	  }

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
	  IExecutionCourse executionCourse = null; 
	  try {
		  SuportePersistenteOJB.getInstance().iniciarTransaccao();
		  executionCourse = SuportePersistenteOJB.getInstance().getIPersistentExecutionCourse().readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
		  assertNotNull(executionCourse);
		  SuportePersistenteOJB.getInstance().confirmarTransaccao();
	  } catch (ExcepcaoPersistencia ex) {
		  ex.printStackTrace();
	  }

	  Object[] result = { Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse), new TipoAula(TipoAula.TEORICA) };
	  return result;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 5;
	}

	protected Object getObjectToCompare() {
	  return null;
	}

}