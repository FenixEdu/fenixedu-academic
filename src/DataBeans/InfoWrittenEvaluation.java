/*
 * Created on 29/Out/2003
 *
  */
package DataBeans;

import java.util.Calendar;
import java.util.List;

/**
 * @author Ana e Ricardo
 *
 */
public class InfoWrittenEvaluation extends InfoEvaluation{
	
	protected Calendar day;
	protected Calendar beginning;
	protected Calendar end;
	protected Calendar enrollmentBeginDay;
	protected Calendar enrollmentEndDay;
	protected Calendar enrollmentBeginTime;
	protected Calendar enrollmentEndTime;
	protected List associatedCurricularCourseScope;
	protected List associatedRoomOccupation;
	protected List associatedExecutionCourse;
	
	/**
	 * @return
	 */
	public List getAssociatedCurricularCourseScope() {
		return associatedCurricularCourseScope;
	}

	/**
	 * @return
	 */
	public List getAssociatedRoomOccupation() {
		return associatedRoomOccupation;
	}

	/**
	 * @return
	 */
	public Calendar getBeginning() {
		return beginning;
	}

	/**
	 * @return
	 */
	public Calendar getDay() {
		return day;
	}

	/**
	 * @return
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginDay() {
		return enrollmentBeginDay;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginTime() {
		return enrollmentBeginTime;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndDay() {
		return enrollmentEndDay;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndTime() {
		return enrollmentEndTime;
	}

	/**
	 * @param list
	 */
	public void setAssociatedCurricularCourseScope(List list) {
		associatedCurricularCourseScope = list;
	}

	/**
	 * @param list
	 */
	public void setAssociatedRoomOccupation(List list) {
		associatedRoomOccupation = list;
	}

	/**
	 * @param calendar
	 */
	public void setBeginning(Calendar calendar) {
		beginning = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setDay(Calendar calendar) {
		day = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnd(Calendar calendar) {
		end = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginDay(Calendar calendar) {
		enrollmentBeginDay = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginTime(Calendar calendar) {
		enrollmentBeginTime = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndDay(Calendar calendar) {
		enrollmentEndDay = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndTime(Calendar calendar) {
		enrollmentEndTime = calendar;
	}

    /**
     * @return
     */
    public List getAssociatedExecutionCourse()
    {
        return associatedExecutionCourse;
    }

    /**
     * @param list
     */
    public void setAssociatedExecutionCourse(List list)
    {
        associatedExecutionCourse = list;
    }

}
