/*
 * Created on 21/Mai/2003
 *
 */
package Dominio;

import java.util.Calendar;

/**
 * @author Tânia Nunes
 *
 */
public class ExamEnrollment extends DomainObject implements IExamEnrollment {
	private IExam exam;
	private Integer keyExam;
	private Calendar beginDate;
	private Calendar endDate;
	
	public boolean equals(Object obj) {
			if (obj instanceof ExamEnrollment) {
				ExamEnrollment examEnrollmentObj = (ExamEnrollment) obj;
				return getExam().equals(examEnrollmentObj.getExam());
			}
			return false;
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
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @return
	 */
	public IExam getExam() {
		return exam;
	}

	/**
	 * @return
	 */
	public Integer getKeyExam() {
		return keyExam;
	}

	/**
	 * @param calendar
	 */
	public void setBeginDate(Calendar date) {
		beginDate = date;
	}

	/**
	 * @param calendar
	 */
	public void setEndDate(Calendar date) {
		endDate = date;
	}

	/**
	 * @param exam
	 */
	public void setExam(IExam exam) {
		this.exam = exam;
	}

	/**
	 * @param integer
	 */
	public void setKeyExam(Integer integer) {
		keyExam = integer;
	}
	
}
