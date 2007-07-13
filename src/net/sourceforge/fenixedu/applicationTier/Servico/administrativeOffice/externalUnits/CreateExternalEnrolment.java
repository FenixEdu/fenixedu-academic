package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class CreateExternalEnrolment extends Service {

    public ExternalEnrolment run(final CreateExternalEnrolmentBean bean, final Registration registration)
	    throws FenixServiceException {
	return new ExternalEnrolment(registration, bean.getExternalCurricularCourse(), bean.getGrade(),
		bean.getExecutionPeriod(), bean.getEvaluationDate(), bean.getEctsCredits());
    }
}
