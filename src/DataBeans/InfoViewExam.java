/*
 * InfoExam.java
 *
 * Created on 2003/03/19
 */

package DataBeans;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.List;

public class InfoViewExam {
	protected List infoViewExamsByDayAndShift;
	protected Integer availableRoomOccupation;
	
	public InfoViewExam() {
	}

	public InfoViewExam(List infoViewExamsByDayAndShift, Integer availableRoomOccupation) {
		this.setAvailableRoomOccupation(availableRoomOccupation);
		this.setInfoViewExamsByDayAndShift(infoViewExamsByDayAndShift);
	}

	/**
	 * @return
	 */
	public Integer getAvailableRoomOccupation() {
		return availableRoomOccupation;
	}

	/**
	 * @return
	 */
	public List getInfoViewExamsByDayAndShift() {
		return infoViewExamsByDayAndShift;
	}

	/**
	 * @param integer
	 */
	public void setAvailableRoomOccupation(Integer integer) {
		availableRoomOccupation = integer;
	}

	/**
	 * @param list
	 */
	public void setInfoViewExamsByDayAndShift(List list) {
		infoViewExamsByDayAndShift = list;
	}

}