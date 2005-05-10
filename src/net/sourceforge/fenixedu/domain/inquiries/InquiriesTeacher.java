/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesTeacher extends DomainObject implements IInquiriesTeacher {
    
    private Integer keyInquiriesCourse;
    private IInquiriesCourse inquiriesCourse;

    private Integer keyExecutionPeriod;
    private IExecutionPeriod executionPeriod;
    
    private Integer keyExecutionCourse;
    private IExecutionCourse executionCourse;
    
    private Integer keyExecutionDegreeCourse;
    private IExecutionDegree executionDegreeCourse;
    
    private Integer keyExecutionDegreeStudent;
    private IExecutionDegree executionDegreeStudent;
    
    private Integer keyTeacher;
    private ITeacher teacher;
    
    private Integer keyNonAffiliatedTeacher;
    private INonAffiliatedTeacher nonAffiliatedTeacher;
    
    private Integer classType;
    
    private Integer studentAssiduity;
    private Integer teacherAssiduity;
    private Double teacherPunctuality;
    private Double teacherClarity;
    private Double teacherAssurance;
    private Double teacherInterestStimulation;
    private Double teacherAvailability;
    private Double teacherReasoningStimulation;
    private Double globalAppreciation;
    
    
    
    /**
     * @return Returns the classType.
     */
    public Integer getClassType() {
        return classType;
    }
    /**
     * @param classType The classType to set.
     */
    public void setClassType(Integer classType) {
        this.classType = classType;
    }
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
     * @return Returns the globalAppreciation.
     */
    public Double getGlobalAppreciation() {
        return globalAppreciation;
    }
    /**
     * @param globalAppreciation The globalAppreciation to set.
     */
    public void setGlobalAppreciation(Double globalAppreciation) {
        this.globalAppreciation = globalAppreciation;
    }
    /**
     * @return Returns the inquiriesCourse.
     */
    public IInquiriesCourse getInquiriesCourse() {
        return inquiriesCourse;
    }
    /**
     * @param inquiriesCourse The inquiriesCourse to set.
     */
    public void setInquiriesCourse(IInquiriesCourse inquiriesCourse) {
        this.inquiriesCourse = inquiriesCourse;
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
     * @return Returns the keyInquiriesCourse.
     */
    public Integer getKeyInquiriesCourse() {
        return keyInquiriesCourse;
    }
    /**
     * @param keyInquiriesCourse The keyInquiriesCourse to set.
     */
    public void setKeyInquiriesCourse(Integer keyInquiriesCourse) {
        this.keyInquiriesCourse = keyInquiriesCourse;
    }
    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }
    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }
    /**
     * @return Returns the studentAssiduity.
     */
    public Integer getStudentAssiduity() {
        return studentAssiduity;
    }
    /**
     * @param studentAssiduity The studentAssiduity to set.
     */
    public void setStudentAssiduity(Integer studentAssiduity) {
        this.studentAssiduity = studentAssiduity;
    }
    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }
    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }
    /**
     * @return Returns the teacherAssiduity.
     */
    public Integer getTeacherAssiduity() {
        return teacherAssiduity;
    }
    /**
     * @param teacherAssiduity The teacherAssiduity to set.
     */
    public void setTeacherAssiduity(Integer teacherAssiduity) {
        this.teacherAssiduity = teacherAssiduity;
    }
    /**
     * @return Returns the teacherAssurance.
     */
    public Double getTeacherAssurance() {
        return teacherAssurance;
    }
    /**
     * @param teacherAssurance The teacherAssurance to set.
     */
    public void setTeacherAssurance(Double teacherAssurance) {
        this.teacherAssurance = teacherAssurance;
    }
    /**
     * @return Returns the teacherAvailability.
     */
    public Double getTeacherAvailability() {
        return teacherAvailability;
    }
    /**
     * @param teacherAvailability The teacherAvailability to set.
     */
    public void setTeacherAvailability(Double teacherAvailability) {
        this.teacherAvailability = teacherAvailability;
    }
    /**
     * @return Returns the teacherClarity.
     */
    public Double getTeacherClarity() {
        return teacherClarity;
    }
    /**
     * @param teacherClarity The teacherClarity to set.
     */
    public void setTeacherClarity(Double teacherClarity) {
        this.teacherClarity = teacherClarity;
    }
    /**
     * @return Returns the teacherInterestStimulation.
     */
    public Double getTeacherInterestStimulation() {
        return teacherInterestStimulation;
    }
    /**
     * @param teacherInterestStimulation The teacherInterestStimulation to set.
     */
    public void setTeacherInterestStimulation(Double teacherInterestStimulation) {
        this.teacherInterestStimulation = teacherInterestStimulation;
    }
    /**
     * @return Returns the teacherPunctuality.
     */
    public Double getTeacherPunctuality() {
        return teacherPunctuality;
    }
    /**
     * @param teacherPunctuality The teacherPunctuality to set.
     */
    public void setTeacherPunctuality(Double teacherPunctuality) {
        this.teacherPunctuality = teacherPunctuality;
    }
    /**
     * @return Returns the teacherReasoningStimulation.
     */
    public Double getTeacherReasoningStimulation() {
        return teacherReasoningStimulation;
    }
    /**
     * @param teacherReasoningStimulation The teacherReasoningStimulation to set.
     */
    public void setTeacherReasoningStimulation(Double teacherReasoningStimulation) {
        this.teacherReasoningStimulation = teacherReasoningStimulation;
    }
	/**
	 * @return Returns the keyNonAffiliatedTeacher.
	 */
	public Integer getKeyNonAffiliatedTeacher() {
		return keyNonAffiliatedTeacher;
	}
	
	/**
	 * @param keyNonAffiliatedTeacher The keyNonAffiliatedTeacher to set.
	 */
	public void setKeyNonAffiliatedTeacher(Integer keyNonAffiliatedTeacher) {
		this.keyNonAffiliatedTeacher = keyNonAffiliatedTeacher;
	}
	
	/**
	 * @return Returns the nonAffiliatedTeacher.
	 */
	public INonAffiliatedTeacher getNonAffiliatedTeacher() {
		return nonAffiliatedTeacher;
	}
	
	/**
	 * @param nonAffiliatedTeacher The nonAffiliatedTeacher to set.
	 */
	public void setNonAffiliatedTeacher(INonAffiliatedTeacher nonAffiliatedTeacher) {
		this.nonAffiliatedTeacher = nonAffiliatedTeacher;
	}
	
	
}
