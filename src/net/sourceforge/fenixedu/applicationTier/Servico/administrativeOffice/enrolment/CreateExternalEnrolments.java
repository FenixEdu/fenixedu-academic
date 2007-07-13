package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class CreateExternalEnrolments extends Service {

    public void run(final Registration registration,
	    final List<ExternalCurricularCourseEnrolmentBean> beans) throws FenixServiceException {

	for (final ExternalCurricularCourseEnrolmentBean bean : beans) {
	    new ExternalEnrolment(registration, bean.getExternalCurricularCourse(), bean.getGrade(),
		    bean.getExecutionPeriod(), bean.getEvaluationDate(), bean.getEctsCredits());
	}
    }
}
