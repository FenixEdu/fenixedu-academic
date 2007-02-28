package net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
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

    public String getLatestEquivalenceEnrolmentEvaluationInformation() {
	return getLatestEnrolmentEvaluationInformation(EnrolmentEvaluationType.EQUIVALENCE);
    }
    
    private String getLatestEnrolmentEvaluationInformation(final EnrolmentEvaluationType enrolmentEvaluationType) {
	final EnrolmentEvaluation latestEnrolmentEvaluation = getEnrolment().getLatestEnrolmentEvaluationBy(enrolmentEvaluationType);
	if (latestEnrolmentEvaluation == null) {
	    return "--";
	}
	
	final String grade = latestEnrolmentEvaluation.getGrade();
	if (!latestEnrolmentEvaluation.isFinal()) {
	    return bundle.getString("msg.enrolled");
	} else if ((grade == null && latestEnrolmentEvaluation.isFinal()) ||grade.equals(GradeScale.NA)) {
	    return bundle.getString("msg.notEvaluated");
	} else if (grade.equals(GradeScale.RE)) {
	    return bundle.getString("msg.notApproved");
	} else if (grade.equals(GradeScale.AP)) {
	    return bundle.getString("msg.approved");
	} else {
	    return grade;
	}
    }

    public String getLatestEnrolmentEvaluationInformation() {
	if (getEnrolment().isEnrolmentStateApproved()) {
	    final String grade = getEnrolment().getGrade();
	    
	    if (grade.equals(GradeScale.AP)) {
		return bundle.getString("msg.approved");
	    } else {
		return grade;
	    }
	} else {
	    return bundle.getString(getEnrolment().getEnrollmentState().name());
	}
    }

}
