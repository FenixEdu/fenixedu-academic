/*
 * Created on 20/Ago/2003
 *
 */
package DataBeans;

import java.util.Calendar;

import Util.CorrectionAvailability;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class InfoDistributedTest extends InfoObject {
	private String Title;
	private String TestInformation;
	private Calendar beginDate;
	private Calendar endDate;
	private Calendar beginHour;
	private Calendar endHour;
	private TestType testType;
	private CorrectionAvailability correctionAvailability;
	private Boolean studentFeedback;
	private Integer numberOfQuestions;
	private InfoExecutionCourse infoExecutionCourse;

	public InfoDistributedTest() {
	}

	public String getTitle() {
		return Title;
	}

	public String getTestInformation() {
		return TestInformation;
	}

	public Calendar getBeginDate() {
		return beginDate;
	}

	public Calendar getBeginHour() {
		return beginHour;
	}
	public CorrectionAvailability getCorrectionAvailability() {
		return correctionAvailability;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public Calendar getEndHour() {
		return endHour;
	}

	public Boolean getStudentFeedback() {
		return studentFeedback;
	}

	public TestType getTestType() {
		return testType;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	public void setTitle(String string) {
		Title = string;
	}
	public void setTestInformation(String string) {
		TestInformation = string;
	}

	public void setBeginDate(Calendar calendar) {
		beginDate = calendar;
	}

	public void setBeginHour(Calendar calendar) {
		beginHour = calendar;
	}

	public void setCorrectionAvailability(CorrectionAvailability availability) {
		correctionAvailability = availability;
	}

	public void setEndDate(Calendar calendar) {
		endDate = calendar;
	}

	public void setEndHour(Calendar calendar) {
		endHour = calendar;
	}

	public void setStudentFeedback(Boolean boolean1) {
		studentFeedback = boolean1;
	}

	public void setTestType(TestType type) {
		testType = type;
	}

	public void setNumberOfQuestions(Integer integer) {
		numberOfQuestions = integer;
	}

	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	public String getBeginDateFormatted() {
		String result = new String();
		Calendar date = getBeginDate();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}

	public String getBeginHourFormatted() {
		String result = "";
		Calendar date = getBeginHour();
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10)
			result += "0";
		result += date.get(Calendar.MINUTE);
		return result;
	}

	public String getEndDateFormatted() {
		String result = new String();
		Calendar date = getEndDate();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}

	public String getEndHourFormatted() {
		String result = "";
		Calendar date = getEndHour();
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10)
			result += "0";
		result += date.get(Calendar.MINUTE);
		return result;
	}

	public String getBeginDateTimeFormatted() {
		String result = new String();
		Calendar date = getBeginDate();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		result += " ";
		date = getBeginHour();
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10)
			result += "0";
		result += date.get(Calendar.MINUTE);
		return result;
	}

	public String getEndDateTimeFormatted() {
		String result = new String();
		Calendar date = getEndDate();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		result += " ";
		date = getEndHour();
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10)
			result += "0";
		result += date.get(Calendar.MINUTE);
		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDistributedTest) {
			InfoDistributedTest infoDistributedTest = (InfoDistributedTest) obj;
			result =
				(getIdInternal().equals(infoDistributedTest.getIdInternal()))
					&& (getTitle().equals(infoDistributedTest.getTitle()))
					&& (getBeginDate().equals(infoDistributedTest.getBeginDate()))
					&& (getBeginHour().equals(infoDistributedTest.getBeginHour()))
					&& (getEndDate().equals(infoDistributedTest.getEndDate()))
					&& (getEndHour().equals(infoDistributedTest.getEndHour()))
					&& (getTestType().equals(infoDistributedTest.getTestType()))
					&& (getCorrectionAvailability()
						.equals(
							infoDistributedTest.getCorrectionAvailability()))
					&& (getStudentFeedback()
						.equals(infoDistributedTest.getStudentFeedback()))
					&& (getNumberOfQuestions()
						.equals(infoDistributedTest.getNumberOfQuestions()))
					&& (getInfoExecutionCourse()
						.equals(infoDistributedTest.getInfoExecutionCourse()));
		}
		return result;
	}

}
