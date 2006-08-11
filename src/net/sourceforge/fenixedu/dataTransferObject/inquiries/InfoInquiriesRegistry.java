/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesRegistry extends InfoObject implements Comparable {

	private Integer keyExecutionPeriod;
	private InfoExecutionPeriod executionPeriod;
    
	private Integer keyExecutionCourse;
	private InfoExecutionCourse executionCourse;
        
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

    public static InfoInquiriesRegistry newInfoFromDomain(InquiriesRegistry inquiriesRegistry) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoInquiriesRegistry newInfo = null;
        if(inquiriesRegistry != null) {
            newInfo = new InfoInquiriesRegistry();
            newInfo.copyFromDomain(inquiriesRegistry);
        }
        return newInfo;
    }
       
    public void copyFromDomain(InquiriesRegistry inquiriesRegistry) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (inquiriesRegistry != null) {
            super.copyFromDomain(inquiriesRegistry);
        }
        this.setIdInternal(inquiriesRegistry.getIdInternal());
        this.setExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(inquiriesRegistry.getExecutionPeriod()));
        this.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(inquiriesRegistry.getExecutionCourse()));
        this.setStudent(InfoStudent.newInfoFromDomain(inquiriesRegistry.getStudent()));        
    }
        
}
