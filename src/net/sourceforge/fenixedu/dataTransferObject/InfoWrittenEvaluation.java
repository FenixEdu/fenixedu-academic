/*
 * Created on 29/Out/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.space.IRoomOccupation;

/**
 * @author Ana e Ricardo
 *  
 */
public class InfoWrittenEvaluation extends InfoEvaluation {

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
    protected Integer enrolledStudents;
    
    public List getAssociatedCurricularCourseScope() {
        return associatedCurricularCourseScope;
    }

    public List getAssociatedRoomOccupation() {
        return associatedRoomOccupation;
    }

    public Calendar getBeginning() {
        return beginning;
    }

    public Calendar getDay() {
        return day;
    }

    public Calendar getEnd() {
        return end;
    }

    public Calendar getEnrollmentBeginDay() {
        return enrollmentBeginDay;
    }

    public Calendar getEnrollmentBeginTime() {
        return enrollmentBeginTime;
    }

    public Calendar getEnrollmentEndDay() {
        return enrollmentEndDay;
    }

    public Calendar getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    public void setAssociatedCurricularCourseScope(List list) {
        associatedCurricularCourseScope = list;
    }

    public void setAssociatedRoomOccupation(List list) {
        associatedRoomOccupation = list;
    }

    public void setBeginning(Calendar calendar) {
        beginning = calendar;
    }

    public void setDay(Calendar calendar) {
        day = calendar;
    }

    public void setEnd(Calendar calendar) {
        end = calendar;
    }

    public void setEnrollmentBeginDay(Calendar calendar) {
        enrollmentBeginDay = calendar;
    }

    public void setEnrollmentBeginTime(Calendar calendar) {
        enrollmentBeginTime = calendar;
    }

    public void setEnrollmentEndDay(Calendar calendar) {
        enrollmentEndDay = calendar;
    }

    public void setEnrollmentEndTime(Calendar calendar) {
        enrollmentEndTime = calendar;
    }

    public List getAssociatedExecutionCourse() {
        return associatedExecutionCourse;
    }

    public void setAssociatedExecutionCourse(List list) {
        associatedExecutionCourse = list;
    }
    
    public Integer getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Integer enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Calendar getInicio() {
        return getBeginning();
    }

    public Calendar getFim() {
        return getEnd();
    }

    public void copyFromDomain(IWrittenEvaluation writtenEvaluation) {
        super.copyFromDomain(writtenEvaluation);
        if (writtenEvaluation != null) {
            associatedExecutionCourse = new ArrayList();
            associatedRoomOccupation = new ArrayList();
            associatedCurricularCourseScope = new ArrayList();
            for(IExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()){
                associatedExecutionCourse.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
            for(IRoomOccupation roomOccupation : writtenEvaluation.getAssociatedRoomOccupation()){
                associatedRoomOccupation.add(InfoRoomOccupationWithInfoRoom.newInfoFromDomain(roomOccupation));
            }
            for(ICurricularCourseScope curricularCourseScope : writtenEvaluation.getAssociatedCurricularCourseScope()){
                associatedCurricularCourseScope.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
            setBeginning(writtenEvaluation.getBeginning());
            setDay(writtenEvaluation.getDay());
            setEnd(writtenEvaluation.getEnd());
            setEnrollmentBeginDay(writtenEvaluation.getEnrollmentBeginDay());
            setEnrollmentBeginTime(writtenEvaluation.getEnrollmentBeginTime());
            setEnrollmentEndDay(writtenEvaluation.getEnrollmentEndDay());
            setEnrollmentEndTime(writtenEvaluation.getEnrollmentEndTime());
        }
    }  

    public static InfoWrittenEvaluation newInfoFromDomain(IWrittenEvaluation writtenEvaluation) {
        InfoWrittenEvaluation infoWrittenEvaluation = null;
        if (writtenEvaluation != null) {
            if (writtenEvaluation instanceof IExam) {
                infoWrittenEvaluation = InfoExam.newInfoFromDomain((IExam) writtenEvaluation);
            } else if (writtenEvaluation instanceof IWrittenTest) {
                infoWrittenEvaluation = InfoWrittenTest.newInfoFromDomain((IWrittenTest)writtenEvaluation);
            } else {
                infoWrittenEvaluation = new InfoWrittenEvaluation();
                infoWrittenEvaluation.copyFromDomain(writtenEvaluation);
            }
        }
        return infoWrittenEvaluation;
    }
}