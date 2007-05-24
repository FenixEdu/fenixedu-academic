package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

abstract public class DiplomaRequestEvent extends DiplomaRequestEvent_Base {

    protected DiplomaRequestEvent() {
	super();
    }

    protected DiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person, final DiplomaRequest diplomaRequest) {
	this();
	super.init(administrativeOffice, eventType, person, diplomaRequest);
    }

    final static public DiplomaRequestEvent create(final AdministrativeOffice administrativeOffice, final Person person, final DiplomaRequest diplomaRequest) {
	switch (diplomaRequest.getEventType()) {
	case BOLONHA_DEGREE_DIPLOMA_REQUEST:
	    return new BolonhaDegreeDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person, diplomaRequest);
	case BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST:
	    return new BolonhaMasterDegreeDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person, diplomaRequest);
	case BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST:
	    return new BolonhaPhdProgramDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person, diplomaRequest);
	default:
	    throw new DomainException("DiplomaRequestEvent.invalid.event.type.in.creation");
	}
    }
    
    @Override
    final public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
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
