package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DegreeFinalizationCertificateRequestEvent extends DegreeFinalizationCertificateRequestEvent_Base {

	private DegreeFinalizationCertificateRequestEvent() {
		super();
	}

	public DegreeFinalizationCertificateRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
			final DegreeFinalizationCertificateRequest certificateRequest) {
		this();
		super.init(administrativeOffice, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, person, certificateRequest);
	}

	@Override
	public DegreeFinalizationCertificateRequest getAcademicServiceRequest() {
		return (DegreeFinalizationCertificateRequest) super.getAcademicServiceRequest();
	}

	@Override
	protected void fillDescription(final LabelFormatter labelFormatter) {
		labelFormatter.appendLabel(" (");
		addCycleDescriptionIfRequired(labelFormatter);
		labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
		labelFormatter.appendLabel(")");
		labelFormatter.appendLabel(" - ").appendLabel(getAcademicServiceRequest().getServiceRequestNumberYear());
	}

	private void addCycleDescriptionIfRequired(LabelFormatter labelFormatter) {
		final DegreeFinalizationCertificateRequest request = getAcademicServiceRequest();
		if (request.getRequestedCycle() != null) {
			labelFormatter.appendLabel(request.getRequestedCycle().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES)
					.appendLabel(" ").appendLabel("label.of", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ");
		}
	}

}
