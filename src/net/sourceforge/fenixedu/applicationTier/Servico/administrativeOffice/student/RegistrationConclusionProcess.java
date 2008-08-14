package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationConclusionProcess extends Service {

    public void run(final RegistrationConclusionBean conclusionBean) {
	final Registration registration = conclusionBean.getRegistration();
	if (registration.isBolonha()) {
	    registration.conclude(conclusionBean.getCycleCurriculumGroup());
	} else {
	    registration.conclude();
	}
    }

}
