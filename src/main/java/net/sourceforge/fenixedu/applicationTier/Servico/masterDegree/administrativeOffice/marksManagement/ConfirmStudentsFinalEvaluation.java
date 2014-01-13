package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ConfirmStudentsFinalEvaluation {

    @Atomic
    public static Boolean run(String curricularCourseCode, String yearString, User userView) {

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);

        final List<Enrolment> enrolments =
                (yearString != null) ? curricularCourse.getEnrolmentsByYear(yearString) : curricularCourse.getEnrolments();

        final List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>();
        for (final Enrolment enrolment : enrolments) {
            final List<EnrolmentEvaluation> allEnrolmentEvaluations = new ArrayList<>(enrolment.getEvaluations());
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