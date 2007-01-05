package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public class InfoEnrolment extends InfoObject {
    
    private Enrolment enrolment;
    
    protected InfoEnrolment() {}
    
    protected InfoEnrolment(Enrolment enrolment) {
	this.enrolment = enrolment;
    }
    
    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
	return (enrolment != null) ? new InfoEnrolment(enrolment) : null;
    }
    
    public Enrolment getEnrolment() {
	return enrolment;
    }
    
    @Override
    public Integer getIdInternal() {
        return enrolment.getIdInternal();
    }
    
    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }
    
    public Integer getAccumulatedWeight() {
        return enrolment.getAccumulatedWeight();
    }

    public String getEnrollmentTypeResourceKey() {
	if (enrolment.isExtraCurricular()) {
            return "option.curricularCourse.extra";
        } else if (enrolment instanceof EnrolmentInOptionalCurricularCourse) {
            return "option.curricularCourse.optional";
        } else {
            return enrolment.getCurricularCourse().getType().getKeyName();
        }
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return InfoCurricularCourse.newInfoFromDomain(enrolment.getCurricularCourse());
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(enrolment.getExecutionPeriod());
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return InfoStudentCurricularPlan.newInfoFromDomain(enrolment.getStudentCurricularPlan());
    }

    public EnrollmentState getEnrollmentState() {
        return enrolment.getEnrollmentState();
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolment.getEnrolmentEvaluationType();
    }

    public List<InfoEnrolmentEvaluation> getInfoEvaluations() {
	final List<InfoEnrolmentEvaluation> result = new ArrayList<InfoEnrolmentEvaluation>(enrolment.getEvaluationsCount());
	for (final EnrolmentEvaluation enrolmentEvaluation : enrolment.getEvaluationsSet()) {
	    result.add(InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolmentEvaluation));
	}
        return result;
    }

    public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation() {
        return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getLatestEnrolmentEvaluation());
    }

    public Date getCreationDate() {
        return enrolment.getCreationDateDateTime().toDate();
    }

    public EnrollmentCondition getCondition() {
        return enrolment.getEnrolmentCondition();
    }

    public InfoEnrolmentEvaluation getInfoNormalEnrolmentEvaluation() {
	return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.NORMAL));
    }
    
    public InfoEnrolmentEvaluation getInfoImprovmentEnrolmentEvaluation() {
	return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.IMPROVEMENT));
    }

    public InfoEnrolmentEvaluation getInfoSpecialSeasonEnrolmentEvaluation() {
	return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.SPECIAL_SEASON));
    }

    public InfoEnrolmentEvaluation getInfoEquivalenceEnrolmentEvaluation() {
	return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.EQUIVALENCE));
    }

    public String getGrade() {
        final Enrolment enrolment = (Enrolment) RootDomainObject.getInstance().readCurriculumModuleByOID(getIdInternal());
        EnrolmentEvaluation enrolmentEvaluation = null;
        if(!enrolment.getEvaluationsSet().isEmpty()) {
            enrolmentEvaluation = (EnrolmentEvaluation) Collections.max(enrolment.getEvaluationsSet());
        }
        return enrolmentEvaluation == null ? null : enrolmentEvaluation.getGrade();
    }
    
    public boolean equals(Object obj) {
	return (obj instanceof InfoEnrolment) ? this.enrolment == ((InfoEnrolment) obj).getEnrolment() : false;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoStudentCurricularPlan = " + getInfoStudentCurricularPlan() + "; ";
        result += "infoExecutionPeriod = " + getInfoExecutionPeriod() + "; ";
        result += "state = " + getEnrollmentState() + "; ";
        result += "infoCurricularCourse = " + getInfoCurricularCourse() + "; ";
        result += "enrolmentEvaluationType = " + getEnrolmentEvaluationType() + "; ";
        result += "infoEvaluations = " + getInfoEvaluations() + "]\n";
        return result;
    }
}
