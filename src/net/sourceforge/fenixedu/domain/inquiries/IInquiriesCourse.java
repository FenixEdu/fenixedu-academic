/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IInquiriesCourse extends IDomainObject {
    /**
     * @return Returns the classCoordination.
     */
    public Double getClassCoordination();

    /**
     * @param classCoordination The classCoordination to set.
     */
    public void setClassCoordination(Double classCoordination);

    /**
     * @return Returns the contributionForGraduation.
     */
    public Double getContributionForGraduation();

    /**
     * @param contributionForGraduation The contributionForGraduation to set.
     */
    public void setContributionForGraduation(Double contributionForGraduation);

    /**
     * @return Returns the evaluationMethodAdequation.
     */
    public Double getEvaluationMethodAdequation();

    /**
     * @param evaluationMethodAdequation The evaluationMethodAdequation to set.
     */
    public void setEvaluationMethodAdequation(Double evaluationMethodAdequation);

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
     * @return Returns the keySchoolClass.
     */
    public Integer getKeySchoolClass();

    /**
     * @param keySchoolClass The keySchoolClass to set.
     */
    public void setKeySchoolClass(Integer keySchoolClass);

    /**
     * @return Returns the previousKnowledgeArticulation.
     */
    public Double getPreviousKnowledgeArticulation();

    /**
     * @param previousKnowledgeArticulation The previousKnowledgeArticulation to set.
     */
    public void setPreviousKnowledgeArticulation(
            Double previousKnowledgeArticulation);

    /**
     * @return Returns the studentCurricularYear.
     */
    public Integer getStudentCurricularYear();

    /**
     * @param studentCurricularYear The studentCurricularYear to set.
     */
    public void setStudentCurricularYear(Integer studentCurricularYear);

    /**
     * @return Returns the studentFirstEnrollment.
     */
    public Integer getStudentFirstEnrollment();

    /**
     * @param studentFirstEnrollment The studentFirstEnrollment to set.
     */
    public void setStudentFirstEnrollment(Integer studentFirstEnrollment);

    /**
     * @return Returns the studentSchoolClass.
     */
    public ISchoolClass getStudentSchoolClass();

    /**
     * @param studentSchoolClass The studentSchoolClass to set.
     */
    public void setStudentSchoolClass(ISchoolClass studentSchoolClass);

    /**
     * @return Returns the studyElementsContribution.
     */
    public Double getStudyElementsContribution();

    /**
     * @param studyElementsContribution The studyElementsContribution to set.
     */
    public void setStudyElementsContribution(Double studyElementsContribution);

    /**
     * @return Returns the weeklySpentHours.
     */
    public Integer getWeeklySpentHours();

    /**
     * @param weeklySpentHours The weeklySpentHours to set.
     */
    public void setWeeklySpentHours(Integer weeklySpentHours);
}