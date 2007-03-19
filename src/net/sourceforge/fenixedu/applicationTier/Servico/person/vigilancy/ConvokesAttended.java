package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ConvokesAttended extends Service {

    public void run(Vigilancy convoke, Boolean bool) throws ExcepcaoPersistencia {
        convoke.setAttendedToConvoke(bool);
    }

}