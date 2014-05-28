/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse;
import net.sourceforge.fenixedu.util.InquiriesUtil;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesCourse extends InfoObject implements Comparable {

    private Integer keyExecutionPeriod;

    private InfoExecutionPeriod executionPeriod;

    private Integer keyExecutionCourse;

    private InfoExecutionCourse executionCourse;

    private Integer keyExecutionDegreeCourse;

    private InfoExecutionDegree executionDegreeCourse;

    private Integer keyExecutionDegreeStudent;

    private InfoExecutionDegree executionDegreeStudent;

    private Integer keySchoolClass;

    private InfoClass studentSchoolClass;

    private Integer studentCurricularYear;

    private Integer studentFirstEnrollment;

    private Double onlineInfo;

    private Double classCoordination;

    private Double studyElementsContribution;

    private Double previousKnowledgeArticulation;

    private Double contributionForGraduation;

    private Double evaluationMethodAdequation;

    private Integer weeklySpentHours;

    private Double globalAppreciation;

    public Double getOnlineInfo() {
        return onlineInfo;
    }

    public void setOnlineInfo(Double onlineInfo) {
        if (InquiriesUtil.isValidAnswer(onlineInfo)) {
            this.onlineInfo = onlineInfo;
        } else {
            this.onlineInfo = null;
        }
    }

    /**
     * @return Returns the classCoordination.
     */
    public Double getClassCoordination() {
        return classCoordination;
    }

    /**
     * @param classCoordination
     *            The classCoordination to set.
     */
    public void setClassCoordination(Double classCoordination) {
        if (InquiriesUtil.isValidAnswer(classCoordination)) {
            this.classCoordination = classCoordination;
        } else {
            this.classCoordination = null;
        }
    }

    /**
     * @return Returns the contributionForGraduation.
     */
    public Double getContributionForGraduation() {
        return contributionForGraduation;
    }

    /**
     * @param contributionForGraduation
     *            The contributionForGraduation to set.
     */
    public void setContributionForGraduation(Double contributionForGraduation) {
        if (InquiriesUtil.isValidAnswerWithExtraOption(contributionForGraduation)) {
            this.contributionForGraduation = contributionForGraduation;
        } else {
            this.contributionForGraduation = null;
        }
    }

    /**
     * @return Returns the evaluationMethodAdequation.
     */
    public Double getEvaluationMethodAdequation() {
        return evaluationMethodAdequation;
    }

    /**
     * @param evaluationMethodAdequation
     *            The evaluationMethodAdequation to set.
     */
    public void setEvaluationMethodAdequation(Double evaluationMethodAdequation) {
        if (InquiriesUtil.isValidAnswer(evaluationMethodAdequation)) {
            this.evaluationMethodAdequation = evaluationMethodAdequation;
        } else {
            this.evaluationMethodAdequation = null;
        }
    }

    /**
     * @return Returns the executionCourse.
     */
    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @param executionCourse
     *            The executionCourse to set.
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
     * @param executionDegreeCourse
     *            The executionDegreeCourse to set.
     */
    public void setExecutionDegreeCourse(InfoExecutionDegree executionDegreeCourse) {
        this.executionDegreeCourse = executionDegreeCourse;
    }

    /**
     * @return Returns the executionDegreeStudent.
     */
    public InfoExecutionDegree getExecutionDegreeStudent() {
        return executionDegreeStudent;
    }

    /**
     * @param executionDegreeStudent
     *            The executionDegreeStudent to set.
     */
    public void setExecutionDegreeStudent(InfoExecutionDegree executionDegreeStudent) {
        this.executionDegreeStudent = executionDegreeStudent;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public InfoExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(InfoExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @return Returns the globalAppreciation.
     */
    public Double getGlobalAppreciation() {
        return globalAppreciation;
    }

    /**
     * @param globalAppreciation
     *            The globalAppreciation to set.
     */
    public void setGlobalAppreciation(Double globalAppreciation) {
        if (InquiriesUtil.isValidAnswer(globalAppreciation)) {
            this.globalAppreciation = globalAppreciation;
        } else {
            this.globalAppreciation = null;
        }
    }

    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @param keyExecutionCourse
     *            The keyExecutionCourse to set.
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
     * @param keyExecutionDegreeCourse
     *            The keyExecutionDegreeCourse to set.
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
     * @param keyExecutionDegreeStudent
     *            The keyExecutionDegreeStudent to set.
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
     * @param keyExecutionPeriod
     *            The keyExecutionPeriod to set.
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
     * @param keySchoolClass
     *            The keySchoolClass to set.
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
     * @param previousKnowledgeArticulation
     *            The previousKnowledgeArticulation to set.
     */
    public void setPreviousKnowledgeArticulation(Double previousKnowledgeArticulation) {
        if (InquiriesUtil.isValidAnswer(previousKnowledgeArticulation)) {
            this.previousKnowledgeArticulation = previousKnowledgeArticulation;
        } else {
            this.previousKnowledgeArticulation = null;
        }
    }

    /**
     * @return Returns the studentCurricularYear.
     */
    public Integer getStudentCurricularYear() {
        return studentCurricularYear;
    }

    /**
     * @param studentCurricularYear
     *            The studentCurricularYear to set.
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
     * @param studentFirstEnrollment
     *            The studentFirstEnrollment to set.
     */
    public void setStudentFirstEnrollment(Integer studentFirstEnrollment) {
        this.studentFirstEnrollment = studentFirstEnrollment;
    }

    /**
     * @return Returns the studentSchoolClass.
     */
    public InfoClass getStudentSchoolClass() {
        return studentSchoolClass;
    }

    /**
     * @param studentSchoolClass
     *            The studentSchoolClass to set.
     */
    public void setStudentSchoolClass(InfoClass studentSchoolClass) {
        this.studentSchoolClass = studentSchoolClass;
    }

    /**
     * @return Returns the studyElementsContribution.
     */
    public Double getStudyElementsContribution() {
        return studyElementsContribution;
    }

    /**
     * @param studyElementsContribution
     *            The studyElementsContribution to set.
     */
    public void setStudyElementsContribution(Double studyElementsContribution) {
        if (InquiriesUtil.isValidAnswer(studyElementsContribution)) {
            this.studyElementsContribution = studyElementsContribution;
        } else {
            this.studyElementsContribution = null;
        }
    }

    /**
     * @return Returns the weeklySpentHours.
     */
    public Integer getWeeklySpentHours() {
        return weeklySpentHours;
    }

    /**
     * @param weeklySpentHours
     *            The weeklySpentHours to set.
     */
    public void setWeeklySpentHours(Integer weeklySpentHours) {
        if (InquiriesUtil.isValidAnswer(weeklySpentHours)) {
            this.weeklySpentHours = weeklySpentHours;
        } else {
            this.weeklySpentHours = null;
        }
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    public static InfoInquiriesCourse newInfoFromDomain(InquiriesCourse inquiriesCourse) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        InfoInquiriesCourse newInfo = null;
        if (inquiriesCourse != null) {
            newInfo = new InfoInquiriesCourse();
            newInfo.copyFromDomain(inquiriesCourse);
        }
        return newInfo;
    }

    public void copyFromDomain(InquiriesCourse inquiriesCourse) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        if (inquiriesCourse != null) {
            super.copyFromDomain(inquiriesCourse);
            this.setOnlineInfo(inquiriesCourse.getOnlineInfo());
            this.setClassCoordination(inquiriesCourse.getClassCoordination());
            this.setContributionForGraduation(inquiriesCourse.getContributionForGraduation());
            this.setEvaluationMethodAdequation(inquiriesCourse.getEvaluationMethodAdequation());
            this.setGlobalAppreciation(inquiriesCourse.getGlobalAppreciation());
            this.setPreviousKnowledgeArticulation(inquiriesCourse.getPreviousKnowledgeArticulation());
            this.setStudentCurricularYear(inquiriesCourse.getStudentCurricularYear());
            this.setStudentFirstEnrollment(inquiriesCourse.getStudentFirstEnrollment());
            this.setStudyElementsContribution(inquiriesCourse.getStudyElementsContribution());
            this.setWeeklySpentHours(inquiriesCourse.getWeeklySpentHours());
            this.setExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(inquiriesCourse.getExecutionPeriod()));
            this.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(inquiriesCourse.getExecutionCourse()));
            this.setExecutionDegreeCourse(InfoExecutionDegree.newInfoFromDomain(inquiriesCourse.getExecutionDegreeCourse()));
            this.setExecutionDegreeStudent(InfoExecutionDegree.newInfoFromDomain(inquiriesCourse.getExecutionDegreeStudent()));
            this.setStudentSchoolClass(InfoClass.newInfoFromDomain(inquiriesCourse.getStudentSchoolClass()));
        }

    }

    @Override
    public String toString() {
        String result = "[INFOINQUIRIESCOURSE";
        result += ", id=" + getExternalId();
        if (executionPeriod != null) {
            result += ", executionPeriod=" + executionPeriod.toString();
        }
        if (executionCourse != null) {
            result += ", executionCourse=" + executionCourse.toString();
        }
        if (executionDegreeCourse != null) {
            result += ", executionDegreeCourse=" + executionDegreeCourse.toString();
        }
        if (executionDegreeStudent != null) {
            result += ", executionDegreeStudent=" + executionDegreeStudent.toString();
        }
        if (studentSchoolClass != null) {
            result += ", studentSchoolClass=" + studentSchoolClass.toString();
        }
        result += ", studentCurricularYear=" + studentCurricularYear;
        result += ", studentFirstEnrollment=" + studentFirstEnrollment;
        result += ", onlineInfo=" + onlineInfo;
        result += ", classCoordination=" + classCoordination;
        result += ", studyElementsContribution=" + studyElementsContribution;
        result += ", previousKnowledgeArticulation=" + previousKnowledgeArticulation;
        result += ", contributionForGraduation=" + contributionForGraduation;
        result += ", evaluationMethodAdequation=" + evaluationMethodAdequation;
        result += ", weeklySpentHours=" + weeklySpentHours;
        result += ", globalAppreciation=" + globalAppreciation;
        result += "]\n";
        return result;
    }

}
