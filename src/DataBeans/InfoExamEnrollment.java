/*
 * Created on 23/Mai/2003
 *
 */
package DataBeans;

import java.util.Calendar;

/**
 * @author Tânia Nunes
 *
 */
public class InfoExamEnrollment extends InfoObject {

	private Calendar beginDate;
	private Calendar endDate;
	private InfoExam infoExam;

	public InfoExamEnrollment() {
	}

	public InfoExamEnrollment(
		InfoExam infoExam,
		Calendar beginDate,
		Calendar endDate) {
		this.setInfoExam(infoExam);
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;

	}

	/**
	 * @param beginDate
	 */
	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;

	}

	/**
	 * @param infoExam
	 */
	public void setInfoExam(InfoExam exam) {
		this.infoExam = exam;
	}

	public String toString() {
		String result = "[ExamEnrollment";

		result += ", exam =" + getInfoExam();
		result += ", beginDate =" + getBeginDate();
		result += ", endDate =" + getEndDate();
		result += "]";
		return result;
	}

	/**
	 * @return
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @return
	 */
	public Calendar getBeginDate() {
		return beginDate;
	}

	/**
	 * @return
	 */
	public InfoExam getInfoExam() {
		return infoExam;

	}

	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof InfoExamEnrollment) {
			result =
				getInfoExam().equals(((InfoExamEnrollment) arg0).getInfoExam());
		}
		return result;
	}

	public String getBeginDay() {

		return new Integer(getBeginDate().getTime().getDate()).toString();
	}

	public String getBeginMonth() {
		return new Integer(getBeginDate().getTime().getMonth() + 1).toString();
	}
	public String getBeginYear() {
		return new Integer(getBeginDate().getTime().getYear() + 1900)
			.toString();
	}
	public String getBeginHour() {
		return new Integer(getBeginDate().getTime().getHours()).toString();
	}
	public String getBeginMinutes() {
		return new Integer(getBeginDate().getTime().getMinutes()).toString();
	}

	public String getEndDay() {

		return new Integer(getEndDate().getTime().getDate()).toString();
	}

	public String getEndMonth() {
		return new Integer(getEndDate().getTime().getMonth() + 1).toString();
	}
	public String getEndYear() {
		return new Integer(getEndDate().getTime().getYear() + 1900).toString();
	}
	public String getEndHour() {
		return new Integer(getEndDate().getTime().getHours()).toString();
	}
	public String getEndMinutes() {
		return new Integer(getEndDate().getTime().getMinutes()).toString();
	}



	public String getBeginHourString() {
		return formatTime(new Integer(getBeginDate().getTime().getHours()).toString());
	}
	public String getBeginMinutesString() {
		return formatTime(new Integer(getBeginDate().getTime().getMinutes()).toString());
	}
	public String getEndHourString() {
		return formatTime(new Integer(getEndDate().getTime().getHours()).toString());
	}
	public String getEndMinutesString() {
		return formatTime(new Integer(getEndDate().getTime().getMinutes()).toString());
	}
	
	private String formatTime(String time){
		String result= time;
		if (result.length()==1){
			result="0"+result;
		}
		return result;
	}
}
