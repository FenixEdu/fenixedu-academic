package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class CreateUnavailablePeriod extends Service {

    public void run(Vigilant vigilant, DateTime begin, DateTime end, String justification)
            throws ExcepcaoPersistencia {

        new UnavailablePeriod(begin, end, justification,vigilant);
    }

}
