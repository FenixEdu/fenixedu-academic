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

	public InfoExamEnrollment(InfoExam infoExam,Calendar beginDate,Calendar endDate) {
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
					getInfoExam().equals(
						((InfoExamEnrollment) arg0).getInfoExam());
			}
			return result;
		}
}
