package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveIncompatiblePerson extends FenixService {

    public void run(Vigilant vigilant) {
	vigilant.removeIncompatiblePerson();
    }

}