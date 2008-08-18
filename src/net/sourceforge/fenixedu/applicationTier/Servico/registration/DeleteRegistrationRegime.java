package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.student.RegistrationRegime;

public class DeleteRegistrationRegime extends Service {

    public void run(final RegistrationRegime regime) {
	regime.delete();
    }
}
