/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends DomainObject implements IInquiriesRegistry {
    
    private Integer keyExecutionPeriod;
    private IExecutionPeriod executionPeriod;
    
    private Integer keyExecutionCourse;
    private IExecutionCourse executionCourse;
    
    private Integer keyExecutionDegreeCourse;
    private IExecutionDegree executionDegreeCourse;
    
    private Integer keyExecutionDegreeStudent;
    private IExecutionDegree executionDegreeStudent;
    
    private Integer keyStudent;
    private IStudent student;
       
    /**
     * @return Returns the executionCourse.
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }
    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }
    /**
     * @return Returns the executionDegreeCourse.
     */
    public IExecutionDegree getExecutionDegreeCourse() {
        return executionDegreeCourse;
    }
    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(IExecutionDegree executionDegreeCourse) {
        this.executionDegreeCourse = executionDegreeCourse;
    }
    /**
     * @return Returns the executionDegreeStudent.
     */
    public IExecutionDegree getExecutionDegreeStudent() {
        return executionDegreeStudent;
    }
    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            IExecutionDegree executionDegreeStudent) {
        this.executionDegreeStudent = executionDegreeStudent;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }
    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }
    /**
     * @param keyExecutionCourse The keyExecutionCourse to set.
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }
    /**
     * @return Returns the keyExecutionDegreeCourse.
     */
    public Integer getKeyExecutionDegreeCourse() {
        return keyExecutionDegreeCourse;
    }
    /**
     * @param keyExecutionDegreeCourse The keyExecutionDegreeCourse to set.
     */
    public void setKeyExecutionDegreeCourse(Integer keyExecutionDegreeCourse) {
        this.keyExecutionDegreeCourse = keyExecutionDegreeCourse;
    }
    /**
     * @return Returns the keyExecutionDegreeStudent.
     */
    public Integer getKeyExecutionDegreeStudent() {
        return keyExecutionDegreeStudent;
    }
    /**
     * @param keyExecutionDegreeStudent The keyExecutionDegreeStudent to set.
     */
    public void setKeyExecutionDegreeStudent(Integer keyExecutionDegreeStudent) {
        this.keyExecutionDegreeStudent = keyExecutionDegreeStudent;
    }
    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }
    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }
    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }
    /**
     * @param keyStudent The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }
    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }
    /**
     * @param student The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }
}
