package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteUnavailablePeriodByOID {

    @Atomic
    public static void run(String externalId) {

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) FenixFramework.getDomainObject(externalId);

        unavailablePeriod.delete();

    }

}