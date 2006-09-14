package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class EditUnavailablePeriod extends Service {

    public void run(Integer idInternal, DateTime begin, DateTime end, String justification)
            throws ExcepcaoPersistencia {

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) RootDomainObject
                .readDomainObjectByOID(UnavailablePeriod.class, idInternal);
        unavailablePeriod.edit(begin, end, justification);

    }

}
