package ServidorAplicacao.Servicos.enrolment.degree;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ShowAvailableDegreesForOptionTest extends TestCaseReadServices {
	
	public ShowAvailableDegreesForOptionTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ShowAvailableDegreesForOptionTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "ShowAvailableDegreesForOption";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		return null;
	}

	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	protected Object getObjectToCompare() {
		return null;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testShowAvailableDegreesForOptionServiceRun() {

		Object args[] = {_userView};
		InfoEnrolmentContext result = null;
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, "ShowAvailableCurricularCourses", args);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}

		Object args2[] = {result};
		try {
			result = (InfoEnrolmentContext) ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args2);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Execution of service!");
		}
		
		List optionalSpan = result.getInfoDegreesForOptionalCurricularCourses();

		assertEquals("Optional Span size:", optionalSpan.size(), 1);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoPersistente cursoPersistente = sp.getICursoPersistente();
			sp.iniciarTransaccao();
			ICurso degree = cursoPersistente.readBySigla("LERCI");
			sp.confirmarTransaccao();
			InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(degree);
			assertEquals(true, optionalSpan.contains(infoDegree));
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from data base");
		}

	}
}