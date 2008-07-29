package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class CertificateRequestEvent extends CertificateRequestEvent_Base {

    protected CertificateRequestEvent() {
	super();
    }

    public CertificateRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
	    final Person person, final CertificateRequest certificateRequest) {
	this();
	super.init(administrativeOffice, eventType, person, certificateRequest);
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter result = super.getDescription();
	fillDescription(result);
	return result;
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
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
	addCycleDescriptionIfRequired(labelFormatter);
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
	labelFormatter.appendLabel(")");
    }

    private void addCycleDescriptionIfRequired(LabelFormatter labelFormatter) {
	// FIXME: Hack because there are no subclasses for certificate request
	// events
	if (getAcademicServiceRequest() instanceof DegreeFinalizationCertificateRequest) {
	    final DegreeFinalizationCertificateRequest request = (DegreeFinalizationCertificateRequest) getAcademicServiceRequest();
	    if (request.getRequestedCycle() != null) {
		labelFormatter.appendLabel(request.getRequestedCycle().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES)
			.appendLabel(" ").appendLabel("label.of", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ");
	    }
	}

    }

    final public Integer getNumberOfUnits() {
	return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfUnits();
    }

    final public Integer getNumberOfPages() {
	return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfPages();
    }

}
