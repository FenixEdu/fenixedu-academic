/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IInquiriesTeacher extends IDomainObject {
    public ShiftType getShiftType();
    
    public void setShiftType(ShiftType shiftType);
    
    /**
     * @return Returns the executionCourse.
     */
    public IExecutionCourse getExecutionCourse();

    /**
     * @param executionCourse The executionCourse to set.
     */
    public void setExecutionCourse(IExecutionCourse executionCourse);

    /**
     * @return Returns the executionDegreeCourse.
     */
    public IExecutionDegree getExecutionDegreeCourse();

    /**
     * @param executionDegreeCourse The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(IExecutionDegree executionDegreeCourse);

    /**
     * @return Returns the executionDegreeStudent.
     */
    public IExecutionDegree getExecutionDegreeStudent();

    /**
     * @param executionDegreeStudent The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(
            IExecutionDegree executionDegreeStudent);

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod();

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    /**
     * @return Returns the globalAppreciation.
     */
    public Double getGlobalAppreciation();

    /**
     * @param globalAppreciation The globalAppreciation to set.
     */
    public void setGlobalAppreciation(Double globalAppreciation);

    /**
     * @return Returns the inquiriesCourse.
     */
    public IInquiriesCourse getInquiriesCourse();

    /**
     * @param inquiriesCourse The inquiriesCourse to set.
     */
    public void setInquiriesCourse(IInquiriesCourse inquiriesCourse);

    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse();

    /**
     * @param keyExecutionCourse The keyExecutionCourse to set.
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse);

    /**
     * @return Returns the keyExecutionDegreeCourse.
     */
    public Integer getKeyExecutionDegreeCourse();

    /**
     * @param keyExecutionDegreeCourse The keyExecutionDegreeCourse to set.
     */
    public void setKeyExecutionDegreeCourse(Integer keyExecutionDegreeCourse);

    /**
     * @return Returns the keyExecutionDegreeStudent.
     */
    public Integer getKeyExecutionDegreeStudent();

    /**
     * @param keyExecutionDegreeStudent The keyExecutionDegreeStudent to set.
     */
    public void setKeyExecutionDegreeStudent(Integer keyExecutionDegreeStudent);

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod();

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod);

    /**
     * @return Returns the keyInquiriesCourse.
     */
    public Integer getKeyInquiriesCourse();

    /**
     * @param keyInquiriesCourse The keyInquiriesCourse to set.
     */
    public void setKeyInquiriesCourse(Integer keyInquiriesCourse);

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher();

    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher);

    /**
     * @return Returns the studentAssiduity.
     */
    public Integer getStudentAssiduity();

    /**
     * @param studentAssiduity The studentAssiduity to set.
     */
    public void setStudentAssiduity(Integer studentAssiduity);

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher();

    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher);

    /**
     * @return Returns the teacherAssiduity.
     */
    public Integer getTeacherAssiduity();

    /**
     * @param teacherAssiduity The teacherAssiduity to set.
     */
    public void setTeacherAssiduity(Integer teacherAssiduity);

    /**
     * @return Returns the teacherAssurance.
     */
    public Double getTeacherAssurance();

    /**
     * @param teacherAssurance The teacherAssurance to set.
     */
    public void setTeacherAssurance(Double teacherAssurance);

    /**
     * @return Returns the teacherAvailability.
     */
    public Double getTeacherAvailability();

    /**
     * @param teacherAvailability The teacherAvailability to set.
     */
    public void setTeacherAvailability(Double teacherAvailability);

    /**
     * @return Returns the teacherClarity.
     */
    public Double getTeacherClarity();

    /**
     * @param teacherClarity The teacherClarity to set.
     */
    public void setTeacherClarity(Double teacherClarity);

    /**
     * @return Returns the teacherInterestStimulation.
     */
    public Double getTeacherInterestStimulation();

    /**
     * @param teacherInterestStimulation The teacherInterestStimulation to set.
     */
    public void setTeacherInterestStimulation(Double teacherInterestStimulation);

    /**
     * @return Returns the teacherPunctuality.
     */
    public Double getTeacherPunctuality();

    /**
     * @param teacherPunctuality The teacherPunctuality to set.
     */
    public void setTeacherPunctuality(Double teacherPunctuality);

    /**
     * @return Returns the teacherReasoningStimulation.
     */
    public Double getTeacherReasoningStimulation();

    /**
     * @param teacherReasoningStimulation The teacherReasoningStimulation to set.
     */
    public void setTeacherReasoningStimulation(Double teacherReasoningStimulation);

	/**
	 * @return Returns the keyNonAffiliatedTeacher.
	 */
	public Integer getKeyNonAffiliatedTeacher();
	
	/**
	 * @param keyNonAffiliatedTeacher The keyNonAffiliatedTeacher to set.
	 */
	public void setKeyNonAffiliatedTeacher(Integer keyNonAffiliatedTeacher);
	
	/**
	 * @return Returns the nonAffiliatedTeacher.
	 */
	public INonAffiliatedTeacher getNonAffiliatedTeacher();
	
	/**
	 * @param nonAffiliatedTeacher The nonAffiliatedTeacher to set.
	 */
	public void setNonAffiliatedTeacher(INonAffiliatedTeacher nonAffiliatedTeacher);

}