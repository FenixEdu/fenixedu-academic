package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class DeclarationRequestEvent extends DeclarationRequestEvent_Base {

    protected DeclarationRequestEvent() {
	super();
    }

    public DeclarationRequestEvent(final AdministrativeOffice administrativeOffice,
	    final EventType eventType, final Person person, final DeclarationRequest declarationRequest) {
	this();
	super.init(administrativeOffice, eventType, person, declarationRequest);
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(),
		LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getName());
	labelFormatter.appendLabel(")");
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());    
	}
	

	return labelFormatter;
    }

    final public Integer getNumberOfPages() {
	return ((DeclarationRequest) getAcademicServiceRequest()).getNumberOfPages();
    }

}
