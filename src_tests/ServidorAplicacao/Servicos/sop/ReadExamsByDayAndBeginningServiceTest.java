/*
 * ReadExamsByDayAndBeginningServiceTest.java
 * JUnit based test
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseReadServices;

public class ReadExamsByDayAndBeginningServiceTest
	extends TestCaseReadServices {
	public ReadExamsByDayAndBeginningServiceTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(ReadExamsByDayAndBeginningServiceTest.class);

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
		return "ReadExamsByDayAndBeginning";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2002);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 9);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		Date day = beginning.getTime();

		Object[] result = { day, beginning };
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Calendar beginning = Calendar.getInstance();
		beginning.set(Calendar.YEAR, 2003);
		beginning.set(Calendar.MONTH, Calendar.MARCH);
		beginning.set(Calendar.DAY_OF_MONTH, 19);
		beginning.set(Calendar.HOUR_OF_DAY, 9);
		beginning.set(Calendar.MINUTE, 0);
		beginning.set(Calendar.SECOND, 0);
		Date day = beginning.getTime();

		Object[] result = { day, beginning };
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
