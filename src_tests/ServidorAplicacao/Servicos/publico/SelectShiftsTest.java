package ServidorAplicacao.Servicos.publico;

import java.util.ArrayList;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoShift;
import ServidorAplicacao.Servicos.TestCaseServicos;

/**
 * @author João Mota
 *
 */
public class SelectShiftsTest extends TestCaseServicos {

	/**
	 * Constructor for SelectShiftsTest.
	 * @param testName
	 */
	public SelectShiftsTest(String testName) {
		super(testName);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SelectShiftsTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadAll() {
		Object argsSelecthifts[] = new Object[1];
		
		ArrayList result = null;
		InfoExecutionCourse iDE = 
		new InfoExecutionCourse("Trabalho Final de Curso I","TFCI",
					"programa1",
					new Double(0),
					new Double(0),
					new Double(0),
					new Double(0),
					new InfoExecutionPeriod("2º Semestre",new InfoExecutionYear("2002/2003")));
		InfoShift infoShift = new InfoShift();
		infoShift.setInfoDisciplinaExecucao(iDE);	
		argsSelecthifts[0] = infoShift;		

		try {
			result =
				(ArrayList) ServiceManagerServiceFactory.executeService(
					null,
					"SelectShifts",
			argsSelecthifts);
		} catch (Exception e) {
			fail("test read all shifts");
			e.printStackTrace();
		}
		assertNotNull("test real all shifts",result);
	
	}


}
