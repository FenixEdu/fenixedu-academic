package net.sourceforge.fenixedu.applicationTier.Servico.accounting;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import pt.ist.fenixframework.Atomic;

public class CancelResidenceEvent {

    @Atomic
    public static void run(final ResidenceEvent residenceEvent, Person responsible) {
        residenceEvent.cancel(responsible);
    }

}