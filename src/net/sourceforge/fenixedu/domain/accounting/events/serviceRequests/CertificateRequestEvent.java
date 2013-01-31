package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
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

	protected void fillDescription(final LabelFormatter labelFormatter) {
		labelFormatter.appendLabel(" (");
		labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
		labelFormatter.appendLabel(")");
	}

	final public Integer getNumberOfUnits() {
		return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfUnits();
	}

	final public Integer getNumberOfPages() {
		return ((CertificateRequest) getAcademicServiceRequest()).getNumberOfPages();
	}

	@Override
	public boolean isExemptionAppliable() {
		return true;
	}

}
