/*
 * InfoViewExamByDayAndShift.java
 *
 * Created on 2003/03/24
 */

package DataBeans;

import java.util.List;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class InfoViewExamByDayAndShift {
	
	protected InfoExam infoExam;
	protected List infoDegrees;

	public InfoViewExamByDayAndShift() {
	}

	public InfoViewExamByDayAndShift(InfoExam infoExam, List infoDegrees) {
		this.setInfoExam(infoExam);
		this.setInfoDegrees(infoDegrees);
	}

	public boolean equals(Object obj) {
		if (obj instanceof InfoViewExamByDayAndShift) {
			InfoViewExamByDayAndShift examObj = (InfoViewExamByDayAndShift) obj;
			return this.getInfoExam().equals(examObj.getInfoExam());
		}

		return false;
	}

	public String toString() {
		return "[INFOVIEWEXAMBYDAYANDSHIFT:"
			+ " exam= '" + this.getInfoExam() + "'"
			+ "]";
	}



	/**
	 * @return
	 */
	public InfoExam getInfoExam() {
		return infoExam;
	}

	/**
	 * @param exam
	 */
	public void setInfoExam(InfoExam exam) {
		infoExam = exam;
	}

	/**
	 * @return
	 */
	public List getInfoDegrees() {
		return infoDegrees;
	}

	/**
	 * @param list
	 */
	public void setInfoDegrees(List list) {
		infoDegrees = list;
	}

}