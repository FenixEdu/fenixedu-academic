/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

/**
 * @author Ana e Ricardo
 * 
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest(Date testDate, Date testStartTime, Date testEndTime,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period, String description) {

        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);

        //TODO: check some constrains if needed
        
        this.setOjbConcreteClass(WrittenTest.class.getName());
        this.setDescription(description);
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

        //TODO: check some constrains if needed

        setAttributesAndAssociateRooms(testDate, testStartTime, testEndTime,
                executionCoursesToAssociate, curricularCourseScopesToAssociate, rooms, period);        
        this.setDescription(description);
    }
}
