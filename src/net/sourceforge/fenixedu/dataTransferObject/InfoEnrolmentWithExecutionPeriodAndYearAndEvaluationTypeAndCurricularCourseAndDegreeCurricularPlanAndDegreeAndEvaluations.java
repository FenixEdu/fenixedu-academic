package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;

public class InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations extends
InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree {

    public void copyFromDomain(IEnrolment enrolment) {
        super.copyFromDomain(enrolment);

        final List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
        setInfoEvaluations(new ArrayList(enrolmentEvaluations.size()));
        for (final IEnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
            getInfoEvaluations().add(InfoEnrolmentEvaluation.newInfoFromDomain(enrolmentEvaluation));
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}
