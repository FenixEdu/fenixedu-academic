package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

public class InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations extends
InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree {

    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);

        final List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
        setInfoEvaluations(new ArrayList(enrolmentEvaluations.size()));
        for (final EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
            getInfoEvaluations().add(InfoEnrolmentEvaluation.newInfoFromDomain(enrolmentEvaluation));
        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}
