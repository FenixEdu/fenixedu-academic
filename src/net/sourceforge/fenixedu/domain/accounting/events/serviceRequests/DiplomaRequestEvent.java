package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class DiplomaRequestEvent extends DiplomaRequestEvent_Base {

    protected DiplomaRequestEvent() {
	super();
    }

    public DiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final DiplomaRequest diplomaRequest) {
	this();
	super.init(administrativeOffice, EventType.DIPLOMA_REQUEST, person, diplomaRequest);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getName());
	labelFormatter.appendLabel(")");
	
	return labelFormatter;
    }

}
