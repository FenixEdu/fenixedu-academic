package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdGratuityFineExemption extends PhdGratuityFineExemption_Base {

    public PhdGratuityFineExemption() {
        super();
    }

    public PhdGratuityFineExemption(Person responsible, Event event, String justification) {
        PhdEventExemptionJustification exemptionJustification =
                new PhdEventExemptionJustification(this, PhdEventExemptionJustificationType.FINE_EXEMPTION, event
                        .getWhenOccured().toLocalDate(), justification);
        super.init(responsible, event, exemptionJustification);

        setRootDomainObject(RootDomainObject.getInstance());
        event.recalculateState(new DateTime());
    }

    @Atomic
    public static PhdGratuityFineExemption createPhdGratuityFineExemption(Person responsible, PhdGratuityEvent event,
            String justification) {
        if (event.hasExemptionsOfType(PhdGratuityFineExemption.class)) {
            throw new DomainException("error.already.has.fine.exemption");
        }
        return new PhdGratuityFineExemption(responsible, event, justification);
    }
}
