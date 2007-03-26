package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public abstract class ExemptionJustification extends ExemptionJustification_Base {

    protected ExemptionJustification() {
	super();
    }

    protected void init(final Exemption exemption, final String reason) {

	checkParameters(exemption);

	super.setExemption(exemption);
	super.setReason(reason);

    }

    private void checkParameters(Exemption exemption) {
	if (exemption == null) {
	    throw new DomainException(
		    "error.accounting.events.ExemptionJustification.exemption.cannot.be.null");
	}
    }

    @Override
    public void setExemption(Exemption exemption) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.exemption");
    }

    @Override
    public void setReason(String reason) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.reason");
    }

    public void delete() {
	removeRootDomainObject();
	removeExemption();

	super.deleteDomainObject();

    }

    @Override
    public void removeExemption() {
	super.setExemption(null);
    }

    abstract public LabelFormatter getDescription();
}
