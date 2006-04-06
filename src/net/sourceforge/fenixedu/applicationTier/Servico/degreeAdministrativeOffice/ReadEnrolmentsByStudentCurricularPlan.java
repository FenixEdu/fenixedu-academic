package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadEnrolmentsByStudentCurricularPlan extends Service {

    public List<InfoEnrolment> run(Integer studentCurricularPlanId) throws ExcepcaoPersistencia {
        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanId);
        final List<InfoEnrolment> infoEnrolments;
        if (studentCurricularPlan != null) {
            final List<Enrolment> enrolments = studentCurricularPlan.getEnrolments();
            infoEnrolments = new ArrayList<InfoEnrolment>(enrolments.size());
            for (final Enrolment enrolment : enrolments) {
                final InfoEnrolment infoEnrolment = InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegreeAndEvaluations.newInfoFromDomain(enrolment);

                final EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) Collections.max(enrolment.getEvaluations());
                infoEnrolment.setInfoEnrolmentEvaluation(InfoEnrolmentEvaluation.newInfoFromDomain(enrolmentEvaluation));

                infoEnrolments.add(infoEnrolment);
            }
        } else {
            infoEnrolments = new ArrayList<InfoEnrolment>(0);
        }

        return infoEnrolments;
    }

}