package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ConfirmStudentsFinalEvaluation {

    @Service
    public static Boolean run(Integer curricularCourseCode, String yearString, IUserView userView) {

        final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);

        final List<Enrolment> enrolments =
                (yearString != null) ? curricularCourse.getEnrolmentsByYear(yearString) : curricularCourse.getEnrolments();

        final List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>();
        for (final Enrolment enrolment : enrolments) {
            final List<EnrolmentEvaluation> allEnrolmentEvaluations = enrolment.getEvaluations();
            enrolmentEvaluations.add(allEnrolmentEvaluations.get(allEnrolmentEvaluations.size() - 1));
        }

        if (!enrolmentEvaluations.isEmpty()) {
            for (final EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
                if (enrolmentEvaluation.hasGrade() && enrolmentEvaluation.isTemporary()
                        && enrolmentEvaluation.hasExamDateYearMonthDay()) {
                    enrolmentEvaluation.confirmSubmission(userView.getPerson(), "Lançamento de Notas na Secretaria");
                }
            }
        }

        return Boolean.TRUE;
    }

}