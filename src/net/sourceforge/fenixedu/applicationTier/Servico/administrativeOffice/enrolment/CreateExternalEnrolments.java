package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.ExternalCurricularCourseEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class CreateExternalEnrolments extends Service {
    
    public void run(final Student student, final List<ExternalCurricularCourseEnrolmentBean> beans) throws FenixServiceException {
	if (student == null) {
	    throw new FenixServiceException("error.CreateExternalEnrolment.student.cannot.be.null");
	}
	for (final ExternalCurricularCourseEnrolmentBean bean : beans) {
	    new ExternalEnrolment(student, bean.getExternalCurricularCourse(), bean.getGrade(), bean.getExecutionPeriod());
	}
    }
}
