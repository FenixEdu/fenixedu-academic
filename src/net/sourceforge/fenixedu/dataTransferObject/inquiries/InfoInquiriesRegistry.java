/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.inquiries.IInquiriesRegistry;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesRegistry extends InfoObject implements Comparable {

	private Integer keyExecutionPeriod;
	private InfoExecutionPeriod executionPeriod;
    
	private Integer keyExecutionCourse;
	private InfoExecutionCourse executionCourse;
    
	private Integer keyExecutionDegreeCourse;
	private InfoExecutionDegree executionDegreeCourse;
    
	private Integer keyExecutionDegreeStudent;
	private InfoExecutionDegree executionDegreeStudent;
    
	private Integer keyStudent;
	private InfoStudent student;
    
    /**
     * @return Returns the executionCourse.
     */
    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }
    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(InfoExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }
    /**
     * @return Returns the executionDegreeCourse.
     */
    public InfoExecutionDegree getExecutionDegreeCourse() {
        return executionDegreeCourse;
    }
    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(
            InfoExecutionDegree executionDegreeCourse) {
        this.executionDegreeCourse = executionDegreeCourse;
    }
    /**
     * @return Returns the executionDegreeStudent.
     */
    public InfoExecutionDegree getExecutionDegreeStudent() {
        return executionDegreeStudent;
    }
    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            InfoExecutionDegree executionDegreeStudent) {
        this.executionDegreeStudent = executionDegreeStudent;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public InfoExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(InfoExecutionPeriod executionPeriod) {
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
    public InfoStudent getStudent() {
        return student;
    }
    /**
     * @param student The student to set.
     */
    public void setStudent(InfoStudent student) {
        this.student = student;
    }
    public int compareTo(Object arg0) {
        return 0;
    }

    public static InfoInquiriesRegistry newInfoFromDomain(IInquiriesRegistry inquiriesRegistry) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoInquiriesRegistry newInfo = null;
        if(inquiriesRegistry != null) {
            newInfo = new InfoInquiriesRegistry();
            newInfo.copyFromDomain(inquiriesRegistry);
        }
        return newInfo;
    }
       
    public void copyFromDomain(IInquiriesRegistry inquiriesRegistry) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (inquiriesRegistry != null) {
            super.copyFromDomain(inquiriesRegistry);
        }
        
        CopyUtils.copyPropertiesNullConvertion(this, inquiriesRegistry);
        this.setExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(inquiriesRegistry.getExecutionPeriod()));
        this.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(inquiriesRegistry.getExecutionCourse()));
        this.setExecutionDegreeCourse(InfoExecutionDegree.newInfoFromDomain(inquiriesRegistry.getExecutionDegreeCourse()));
        this.setExecutionDegreeStudent(InfoExecutionDegree.newInfoFromDomain(inquiriesRegistry.getExecutionDegreeStudent()));
        this.setStudent(InfoStudent.newInfoFromDomain(inquiriesRegistry.getStudent()));        
    }
        
}
