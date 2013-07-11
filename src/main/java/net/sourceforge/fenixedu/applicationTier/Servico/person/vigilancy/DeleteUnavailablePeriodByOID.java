package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteUnavailablePeriodByOID {

    @Service
    public static void run(String externalId) {

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) FenixFramework.getDomainObject(externalId);

        unavailablePeriod.delete();

    }

}