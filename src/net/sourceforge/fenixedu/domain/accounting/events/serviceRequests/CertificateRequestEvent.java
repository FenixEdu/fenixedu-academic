package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class CertificateRequestEvent extends CertificateRequestEvent_Base {

    protected CertificateRequestEvent() {
	super();
    }

    public CertificateRequestEvent(final AdministrativeOffice administrativeOffice,
	    final EventType eventType, final Person person, final CertificateRequest certificateRequest) {
	this();
	super.init(administrativeOffice, eventType, person, certificateRequest);
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
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

    final public Integer getNumberOfUnits() {
	return ((CertificateRequest)getAcademicServiceRequest()).getNumberOfUnits();
    }

    final public Integer getNumberOfPages() {
	return ((CertificateRequest)getAcademicServiceRequest()).getNumberOfPages();
    }

}
