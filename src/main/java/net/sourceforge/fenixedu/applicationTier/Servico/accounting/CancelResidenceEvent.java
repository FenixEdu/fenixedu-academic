package net.sourceforge.fenixedu.applicationTier.Servico.accounting;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import pt.ist.fenixWebFramework.services.Service;

public class CancelResidenceEvent {

    @Service
    public static void run(final ResidenceEvent residenceEvent, Person responsible) {
        residenceEvent.cancel(responsible);
    }

}