/*
 * ReadExamsByDayAndBeginningServiceTest.java
 *
 * Created on 2003/03/29
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TipoCurso;

public class ReadExamsByExecutionDegreeAndCurricularYearServiceTest
	extends TestCaseReadServices {
	public ReadExamsByExecutionDegreeAndCurricularYearServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadExamsByExecutionDegreeAndCurricularYearServiceTest.class);

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
		return "ReadExamsByExecutionDegreeAndCurricularYear";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoDegree infoDegree =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores",
				TipoCurso.LICENCIATURA_STRING);
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan("plano1", infoDegree);
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("3º Semestre",new InfoExecutionYear("2002/2003"));
		Integer curricularYear = new Integer(1);

		Object[] result = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoDegree infoDegree =
			new InfoDegree(
				"LEIC",
				"Licenciatura de Engenharia Informatica e de Computadores",
				TipoCurso.LICENCIATURA_STRING);
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan("plano1", infoDegree);
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree(infoDegreeCurricularPlan, infoExecutionYear);
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre",new InfoExecutionYear("2002/2003"));
		Integer curricularYear = new Integer(1);

		Object[] result = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
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
		return null;
	}

	protected boolean needsAuthorization() {
		return true;
	}

}
