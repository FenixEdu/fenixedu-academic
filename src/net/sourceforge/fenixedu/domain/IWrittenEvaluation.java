/*
 * Created on 8/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

/**
 * @author Ana e Ricardo
 */
public interface IWrittenEvaluation extends IEvaluation {
    public Calendar getBeginning();

    public Calendar getDay();

    public Calendar getEnd();

    public Calendar getEnrollmentBeginDay();

    public Calendar getEnrollmentEndDay();

    public Calendar getEnrollmentBeginTime();

    public Calendar getEnrollmentEndTime();

    public List getAssociatedCurricularCourseScope();

    public List getAssociatedRoomOccupation();

    public void setBeginning(Calendar beginning);

    public void setDay(Calendar day);

    public void setEnd(Calendar end);

    public void setEnrollmentBeginDay(Calendar calendar);

    public void setEnrollmentEndDay(Calendar calendar);

    public void setEnrollmentBeginTime(Calendar calendar);

    public void setEnrollmentEndTime(Calendar calendar);

    public void setAssociatedCurricularCourseScope(List list);

    public void setAssociatedRoomOccupation(List list);

}