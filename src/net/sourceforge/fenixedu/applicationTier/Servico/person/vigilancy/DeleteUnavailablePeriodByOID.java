package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteUnavailablePeriodByOID extends Service {

    public void run(Integer idInternal) throws ExcepcaoPersistencia {

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) RootDomainObject
                .readDomainObjectByOID(UnavailablePeriod.class, idInternal);

        unavailablePeriod.delete();

    }

}
