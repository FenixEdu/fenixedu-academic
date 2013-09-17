package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import net.sourceforge.fenixedu.domain.student.RegistrationRegime;
import pt.ist.fenixframework.Atomic;

public class DeleteRegistrationRegime {

    @Atomic
    public static void run(final RegistrationRegime regime) {
        regime.delete();
    }
}
