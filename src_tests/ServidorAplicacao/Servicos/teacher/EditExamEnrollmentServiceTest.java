/*
 * Created on 28/Mai/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;

import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

/**
 * @author Tânia Nunes
 *
 */
public class EditExamEnrollmentServiceTest
	extends TestCaseDeleteAndEditServices {

	/**
	 * @param testName
	 */
	public EditExamEnrollmentServiceTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditExamEnrollment";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(Calendar.YEAR, 2003);
		beginDate.set(Calendar.MONTH, Calendar.MAY);
		beginDate.set(Calendar.DATE, 01);
		beginDate.set(Calendar.HOUR_OF_DAY, 0);
		beginDate.set(Calendar.MINUTE, 0);
		beginDate.set(Calendar.SECOND, 0);
		beginDate.set(Calendar.MILLISECOND, 0);

		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR, 2003);
		endDate.set(Calendar.MONTH, Calendar.MAY);
		endDate.set(Calendar.DATE, 31);
		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MILLISECOND, 0);

		Object[] result =
			{ new Integer("27"), new Integer("3"), beginDate, endDate };
		return result;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		
		Calendar beginDate = Calendar.getInstance();
		beginDate.set(Calendar.YEAR, 2003);
		beginDate.set(Calendar.MONTH, Calendar.MAY);
		beginDate.set(Calendar.DATE, 01);
		beginDate.set(Calendar.HOUR_OF_DAY, 0);
		beginDate.set(Calendar.MINUTE, 0);
		beginDate.set(Calendar.SECOND, 0);
		beginDate.set(Calendar.MILLISECOND, 0);

		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR, 2003);
		endDate.set(Calendar.MONTH, Calendar.MAY);
		endDate.set(Calendar.DATE, 31);
		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MILLISECOND, 0);

		Object[] result =
			{ new Integer("24"), new Integer("1"), beginDate, endDate };
		return result;
	}

}
