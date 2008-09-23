package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.RegistrationRegime;

public class DeleteRegistrationRegime extends FenixService {

    public void run(final RegistrationRegime regime) {
	regime.delete();
    }
}
