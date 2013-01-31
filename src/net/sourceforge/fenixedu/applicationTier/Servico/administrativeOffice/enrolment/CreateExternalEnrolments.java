package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import pt.ist.fenixWebFramework.services.Service;

public class CreateExternalEnrolments {

	@Service
	public static void run(final Registration registration, final List<ExternalCurricularCourseEnrolmentBean> beans) {
		for (final ExternalCurricularCourseEnrolmentBean bean : beans) {
			new ExternalEnrolment(registration, bean.getExternalCurricularCourse(), bean.getGrade(), bean.getExecutionPeriod(),
					bean.getEvaluationDate(), bean.getEctsCredits());
		}
	}
}