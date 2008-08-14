package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.EditExternalEnrolmentBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class EditExternalEnrolment extends Service {

    public void run(final EditExternalEnrolmentBean bean, final Registration registration) {
	final ExternalEnrolment externalEnrolment = bean.getExternalEnrolment();
	externalEnrolment.edit(registration, bean.getGrade(), bean.getExecutionPeriod(), bean.getEvaluationDate(), bean
		.getEctsCredits());
    }
}
