package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnavailablePeriodByOID {

    @Service
    public static void run(Integer idInternal) {

        UnavailablePeriod unavailablePeriod =
                (UnavailablePeriod) RootDomainObject.readDomainObjectByOID(UnavailablePeriod.class, idInternal);

        unavailablePeriod.delete();

    }

}