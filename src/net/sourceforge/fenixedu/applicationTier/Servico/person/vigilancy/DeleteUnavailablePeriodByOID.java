package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteUnavailablePeriodByOID extends FenixService {

    public void run(Integer idInternal) {

	UnavailablePeriod unavailablePeriod = (UnavailablePeriod) RootDomainObject.readDomainObjectByOID(UnavailablePeriod.class,
		idInternal);

	unavailablePeriod.delete();

    }

}
