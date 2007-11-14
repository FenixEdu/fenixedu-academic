package net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class InfoEnrolmentHistoricReport implements Serializable {

    final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
    
    private DomainReference<Enrolment> enrolment;

    public Enrolment getEnrolment() {
	return this.enrolment == null ?  null : this.enrolment.getObject();
    }

    private void setEnrolment(final Enrolment enrolment) {
	this.enrolment = (enrolment == null) ? null : new DomainReference<Enrolment>(enrolment);

    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return getEnrolment().getStudentCurricularPlan();
    }
    
    public InfoEnrolmentHistoricReport(final Enrolment enrolment) {
	setEnrolment(enrolment);
    }
    
    public String getLatestNormalEnrolmentEvaluationInformation() {
	return getLatestEnrolmentEvaluationInformation(EnrolmentEvaluationType.NORMAL);
    }

    public String getLatestSpecialSeasonEnrolmentEvaluationInformation() {
	return getLatestEnrolmentEvaluationInformation(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    public String getLatestImprovementEnrolmentEvaluationInformation() {
	return getLatestEnrolmentEvaluationInformation(EnrolmentEvaluationType.IMPROVEMENT);
    }

    private String getLatestEnrolmentEvaluationInformation(final EnrolmentEvaluationType enrolmentEvaluationType) {
	final EnrolmentEvaluation latestEnrolmentEvaluation = getEnrolment().getLatestEnrolmentEvaluationBy(enrolmentEvaluationType);
	if (latestEnrolmentEvaluation == null) {
	    return "--";
	}
	
	final Grade grade = latestEnrolmentEvaluation.getGrade();
	if (!latestEnrolmentEvaluation.isFinal()) {
	    return bundle.getString("msg.enrolled");
	} else if (grade.isEmpty() || grade.isNotEvaluated()) {
	    return bundle.getString("msg.notEvaluated");
	} else if (grade.isNotApproved()) {
	    return bundle.getString("msg.notApproved");
	} else if (!grade.isNumeric() && grade.isApproved()) {
	    return bundle.getString("msg.approved");
	} else {
	    return grade.getValue();
	}
    }

    public String getLatestEnrolmentEvaluationInformation() {
	if (getEnrolment().isApproved()) {
	    final Grade grade = getEnrolment().getGrade();
	    
	    if (grade.getGradeScale() == GradeScale.TYPEAP) {
		return bundle.getString("msg.approved");
	    } else {
		return grade.getValue();
	    }
	} else {
	    return bundle.getString(getEnrolment().getEnrollmentState().name());
	}
    }

}
