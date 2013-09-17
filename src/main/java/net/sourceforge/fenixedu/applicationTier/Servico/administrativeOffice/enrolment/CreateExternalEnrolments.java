package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import pt.ist.fenixframework.Atomic;

public class CreateExternalEnrolments {

    @Atomic
    public static void run(final Registration registration, final List<ExternalCurricularCourseEnrolmentBean> beans) {
        for (final ExternalCurricularCourseEnrolmentBean bean : beans) {
            new ExternalEnrolment(registration, bean.getExternalCurricularCourse(), bean.getGrade(), bean.getExecutionPeriod(),
                    bean.getEvaluationDate(), bean.getEctsCredits());
        }
    }
}