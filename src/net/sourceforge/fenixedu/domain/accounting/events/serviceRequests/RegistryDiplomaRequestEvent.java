package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class RegistryDiplomaRequestEvent extends RegistryDiplomaRequestEvent_Base {

	protected RegistryDiplomaRequestEvent() {
		super();
	}

	protected RegistryDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
			final Person person, final RegistryDiplomaRequest registryDiplomaRequest) {
		this();
		super.init(administrativeOffice, eventType, person, registryDiplomaRequest);
	}

	final static public RegistryDiplomaRequestEvent create(final AdministrativeOffice administrativeOffice, final Person person,
			final RegistryDiplomaRequest registryDiplomaRequest) {
		switch (registryDiplomaRequest.getEventType()) {
		case BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST:
		case BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST:
		case BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST:
			return new RegistryDiplomaRequestEvent(administrativeOffice, registryDiplomaRequest.getEventType(), person,
					registryDiplomaRequest);
		default:
			throw new DomainException("error.registryDiplomaRequestEvent.invalid.event.type.in.creation");
		}
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

		return labelFormatter;
	}

	private void fillDescription(final LabelFormatter labelFormatter) {
		labelFormatter.appendLabel(" (");
		final RegistryDiplomaRequest request = (RegistryDiplomaRequest) getAcademicServiceRequest();
		if (request.getRequestedCycle() != null) {
			labelFormatter.appendLabel(request.getRequestedCycle().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES)
					.appendLabel(" ").appendLabel("label.of", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ");
		}

		labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
		labelFormatter.appendLabel(" ");
		labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
		labelFormatter.appendLabel(")");
	}
}
