package net.sourceforge.fenixedu.applicationTier.Servico.student.senior;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentSenior extends FenixService {

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static Senior run(final Registration registration) {
		if (registration.hasSenior()) {
			return registration.getSenior();
		} else if (registration.isQualifiedForSeniority()) {
			return new Senior(registration);
		} else {
			return null;
		}
	}

}