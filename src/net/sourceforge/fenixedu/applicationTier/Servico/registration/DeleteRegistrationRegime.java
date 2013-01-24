package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import net.sourceforge.fenixedu.domain.student.RegistrationRegime;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteRegistrationRegime {

    @Service
    public static void run(final RegistrationRegime regime) {
	regime.delete();
    }
}
