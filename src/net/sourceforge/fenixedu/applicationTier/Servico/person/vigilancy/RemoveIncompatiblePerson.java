package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveIncompatiblePerson extends Service {

    public void run(Vigilant vigilant) throws ExcepcaoPersistencia {
        vigilant.removeIncompatiblePerson();
    }

}