package net.sourceforge.fenixedu.applicationTier.Servico.student.senior;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;

public class ReadStudentSenior extends Service {

    public Senior run(final Registration registration) {
	if (registration.hasSenior()) {
	    return registration.getSenior();
	} else if (registration.isQualifiedForSeniority()) {
	    return new Senior(registration);
	} else {
	    return null;
	}
    }
    
}
