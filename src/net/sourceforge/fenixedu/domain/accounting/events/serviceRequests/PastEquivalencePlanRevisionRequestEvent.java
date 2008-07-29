package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRevisionRequest;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastEquivalencePlanRevisionRequestEvent extends PastEquivalencePlanRevisionRequestEvent_Base implements
	IPastRequestEvent {

    protected PastEquivalencePlanRevisionRequestEvent() {
	super();
    }

    public PastEquivalencePlanRevisionRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final EquivalencePlanRevisionRequest request) {
	this();
	super.init(administrativeOffice, EventType.PAST_REVISION_EQUIVALENCE_PLAN_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money pastAmount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter result = super.getDescription();
	fillDescription(result);
	return result;
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);

	fillDescription(labelFormatter);

	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
	}

	return labelFormatter;
    }

    private void fillDescription(final LabelFormatter labelFormatter) {
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
	labelFormatter.appendLabel(")");
    }

}
