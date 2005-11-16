/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Ana e Ricardo
 * 
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest(Date testDate, Date testStartTime, Date testEndTime,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period, String description) {

        checkEvaluationDate(testDate, executionCoursesToAssociate);
        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);

        this.setOjbConcreteClass(WrittenTest.class.getName());
        this.setDescription(description);
    }

    private void checkEvaluationDate(final Date writtenEvaluationDate,
            final List<IExecutionCourse> executionCoursesToAssociate) {

        for (final IExecutionCourse executionCourse : executionCoursesToAssociate) {
            if (executionCourse.getExecutionPeriod().getBeginDate().after(writtenEvaluationDate)
                    || executionCourse.getExecutionPeriod().getEndDate().before(writtenEvaluationDate)) {
                throw new DomainException("error.invalidWrittenTestDate");
            }
        }
    }

    public String toString() {
        return "[WRITTEN_TEST:" + " id= '" + this.getIdInternal() + "'\n" + " day= '" + this.getDay()
                + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '" + this.getEnd()
                + "'\n" + "";
    }

    public void edit(Date testDate, Date testStartTime, Date testEndTime,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period, String description) {
        
        checkEvaluationDate(testDate, executionCoursesToAssociate);

        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();

        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);
        this.setDescription(description);
    }
}
