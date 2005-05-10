/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesCourse extends DomainObject implements IInquiriesCourse {

    private Integer keyExecutionPeriod;
    private IExecutionPeriod executionPeriod;
    
    private Integer keyExecutionCourse;
    private IExecutionCourse executionCourse;
    
    private Integer keyExecutionDegreeCourse;
    private IExecutionDegree executionDegreeCourse;
    
    private Integer keyExecutionDegreeStudent;
    private IExecutionDegree executionDegreeStudent;
    
    private Integer keySchoolClass;
    private ISchoolClass studentSchoolClass;

    private Integer studentCurricularYear;
    private Integer studentFirstEnrollment;
    
    private Double classCoordination;
    private Double studyElementsContribution;
    private Double previousKnowledgeArticulation;
    private Double contributionForGraduation;
    private Double evaluationMethodAdequation;
    private Integer weeklySpentHours;
    private Double globalAppreciation;
    
    
    /**
     * @return Returns the classCoordination.
     */
    public Double getClassCoordination() {
        return classCoordination;
    }
    /**
     * @param classCoordination The classCoordination to set.
     */
    public void setClassCoordination(Double classCoordination) {
        this.classCoordination = classCoordination;
    }
    /**
     * @return Returns the contributionForGraduation.
     */
    public Double getContributionForGraduation() {
        return contributionForGraduation;
    }
    /**
     * @param contributionForGraduation The contributionForGraduation to set.
     */
    public void setContributionForGraduation(Double contributionForGraduation) {
        this.contributionForGraduation = contributionForGraduation;
    }
    /**
     * @return Returns the evaluationMethodAdequation.
     */
    public Double getEvaluationMethodAdequation() {
        return evaluationMethodAdequation;
    }
    /**
     * @param evaluationMethodAdequation The evaluationMethodAdequation to set.
     */
    public void setEvaluationMethodAdequation(Double evaluationMethodAdequation) {
        this.evaluationMethodAdequation = evaluationMethodAdequation;
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
     * @return Returns the keySchoolClass.
     */
    public Integer getKeySchoolClass() {
        return keySchoolClass;
    }
    /**
     * @param keySchoolClass The keySchoolClass to set.
     */
    public void setKeySchoolClass(Integer keySchoolClass) {
        this.keySchoolClass = keySchoolClass;
    }
    /**
     * @return Returns the previousKnowledgeArticulation.
     */
    public Double getPreviousKnowledgeArticulation() {
        return previousKnowledgeArticulation;
    }
    /**
     * @param previousKnowledgeArticulation The previousKnowledgeArticulation to set.
     */
    public void setPreviousKnowledgeArticulation(
            Double previousKnowledgeArticulation) {
        this.previousKnowledgeArticulation = previousKnowledgeArticulation;
    }
    /**
     * @return Returns the studentCurricularYear.
     */
    public Integer getStudentCurricularYear() {
        return studentCurricularYear;
    }
    /**
     * @param studentCurricularYear The studentCurricularYear to set.
     */
    public void setStudentCurricularYear(Integer studentCurricularYear) {
        this.studentCurricularYear = studentCurricularYear;
    }
    /**
     * @return Returns the studentFirstEnrollment.
     */
    public Integer getStudentFirstEnrollment() {
        return studentFirstEnrollment;
    }
    /**
     * @param studentFirstEnrollment The studentFirstEnrollment to set.
     */
    public void setStudentFirstEnrollment(Integer studentFirstEnrollment) {
        this.studentFirstEnrollment = studentFirstEnrollment;
    }
    /**
     * @return Returns the studentSchoolClass.
     */
    public ISchoolClass getStudentSchoolClass() {
        return studentSchoolClass;
    }
    /**
     * @param studentSchoolClass The studentSchoolClass to set.
     */
    public void setStudentSchoolClass(ISchoolClass studentSchoolClass) {
        this.studentSchoolClass = studentSchoolClass;
    }
    /**
     * @return Returns the studyElementsContribution.
     */
    public Double getStudyElementsContribution() {
        return studyElementsContribution;
    }
    /**
     * @param studyElementsContribution The studyElementsContribution to set.
     */
    public void setStudyElementsContribution(Double studyElementsContribution) {
        this.studyElementsContribution = studyElementsContribution;
    }
    /**
     * @return Returns the weeklySpentHours.
     */
    public Integer getWeeklySpentHours() {
        return weeklySpentHours;
    }
    /**
     * @param weeklySpentHours The weeklySpentHours to set.
     */
    public void setWeeklySpentHours(Integer weeklySpentHours) {
        this.weeklySpentHours = weeklySpentHours;
    }
	
}
