/*
 * Created on 3:59:51 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 *         Created at 3:59:51 PM, Mar 11, 2005
 */
public class InfoExternalEnrollmentInfo {
    private Collection evaluations;
    private InfoExternalCurricularCourseInfo course;
    private String finalGrade;
    private String gradeScale;

    /**
     * @return Returns the finalGrade.
     */
    public String getFinalGrade() {
	return this.finalGrade;
    }

    /**
     * @return Returns the grade.
     */

    public String getGradeScale() {
	return this.gradeScale;
    }

    public void setGrade(Grade grade) {
	this.finalGrade = grade.getValue();
	this.gradeScale = grade.getGradeScale().toString();
    }

    /**
     * @return Returns the course.
     */
    public InfoExternalCurricularCourseInfo getCourse() {
	return this.course;
    }

    /**
     * @param course
     *            The course to set.
     */
    public void setCourse(InfoExternalCurricularCourseInfo course) {
	this.course = course;
    }

    public static InfoExternalEnrollmentInfo newFromEnrollment(Enrolment enrollment) {
	InfoExternalEnrollmentInfo info = new InfoExternalEnrollmentInfo();
	info.setCourse(InfoExternalCurricularCourseInfo.newFromDomain(enrollment.getCurricularCourse()));
	info.setEvaluations(InfoExternalEnrollmentInfo.buildExternalEvaluationsInfo(enrollment.getEvaluations()));
	return info;
    }

    public static InfoExternalEnrollmentInfo newFromOptionalEnrollment(OptionalEnrolment enrollment) {
	InfoExternalEnrollmentInfo info = new InfoExternalEnrollmentInfo();
	info.setCourse(InfoExternalCurricularCourseInfo.newFromDomain(enrollment.getOptionalCurricularCourse()));
	info.setEvaluations(InfoExternalEnrollmentInfo.buildExternalEvaluationsInfo(enrollment.getEvaluations()));
	return info;
    }

    public static InfoExternalEnrollmentInfo newFromCurricularCourse(CurricularCourse curricularCourse, Grade finalGrade) {
	InfoExternalEnrollmentInfo info = new InfoExternalEnrollmentInfo();
	info.setCourse(InfoExternalCurricularCourseInfo.newFromDomain(curricularCourse));
	info.setGrade(finalGrade);
	return info;
    }

    /**
     * @param evaluations2
     * @return
     */
    private static Collection buildExternalEvaluationsInfo(List evaluations) {
	Collection result = new ArrayList();
	for (Iterator iter = evaluations.iterator(); iter.hasNext();) {
	    EnrolmentEvaluation evaluation = (EnrolmentEvaluation) iter.next();
	    InfoExternalEvaluationInfo info = InfoExternalEvaluationInfo.newFromEvaluation(evaluation);
	    result.add(info);

	}

	return result;
    }

    /**
     * @return Returns the evaluations.
     */
    public Collection getEvaluations() {
	return this.evaluations;
    }

    /**
     * @param evaluations
     *            The evaluations to set.
     */
    public void setEvaluations(Collection evaluations) {
	this.evaluations = evaluations;
    }
}
