package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PhdGratuityFineExemption extends PhdGratuityFineExemption_Base {

    public PhdGratuityFineExemption() {
	super();
    }

    public PhdGratuityFineExemption(Employee who, Event event, String justification) {
	PhdEventExemptionJustification exemptionJustification = new PhdEventExemptionJustification(this,
		PhdEventExemptionJustificationType.FINE_EXEMPTION, event.getWhenOccured().toLocalDate(), justification);
	super.init(who, event, exemptionJustification);

	setRootDomainObject(RootDomainObject.getInstance());
	event.recalculateState(new DateTime());
    }

    @Service
    public static PhdGratuityFineExemption createPhdGratuityFineExemption(Employee who, PhdGratuityEvent event,
	    String justification) {
	if (event.hasExemptionsOfType(PhdGratuityFineExemption.class)) {
	    throw new DomainException("error.already.has.fine.exemption");
	}
	return new PhdGratuityFineExemption(who, event, justification);
    }
}
