package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalCurricularCourseBean;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class CreateExternalCurricularCourse extends Service {

    public ExternalCurricularCourse run(final CreateExternalCurricularCourseBean bean) throws FenixServiceException {

	final ExternalCurricularCourse externalCurricularCourse = new ExternalCurricularCourse(bean
		.getParentUnit(), bean.getName(), bean.getCode());

	if (bean.isEnrolStudent()) {

	    final Student student = Student.readStudentByNumber(bean.getExternalEnrolmentBean().getStudentNumber());
	    if (student == null) {
		throw new FenixServiceException("error.CreateExternalEnrolment.student.cannot.be.null");
	    }
	    new ExternalEnrolment(student, externalCurricularCourse, bean.getExternalEnrolmentBean()
		    .getGrade(), bean.getExternalEnrolmentBean().getExecutionPeriod());
	}

	return externalCurricularCourse;
    }
}
