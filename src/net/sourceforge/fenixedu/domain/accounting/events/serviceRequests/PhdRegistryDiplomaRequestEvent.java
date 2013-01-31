package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdRegistryDiplomaRequestEvent extends PhdRegistryDiplomaRequestEvent_Base {

	protected PhdRegistryDiplomaRequestEvent() {
		super();
	}

	protected PhdRegistryDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
			final Person person, final AcademicServiceRequest academicServiceRequest) {
		this();

		if (!academicServiceRequest.isRegistryDiploma()) {
			throw new DomainException("error.PhdRegistryDiplomaRequestEvent.request.is.not.registry.diploma");
		}

		if (!EventType.BOLONHA_PHD_REGISTRY_DIPLOMA_REQUEST.equals(eventType)) {
			throw new DomainException("error.PhdRegistryDiplomaRequestEvent.eventType.is.not.phd.registry.diploma");
		}

		super.init(administrativeOffice, eventType, person, academicServiceRequest);
	}

	final static public PhdRegistryDiplomaRequestEvent create(final AdministrativeOffice administrativeOffice,
			final Person person, final PhdRegistryDiplomaRequest phdRegistryDiplomaRequest) {
		return new PhdRegistryDiplomaRequestEvent(administrativeOffice, phdRegistryDiplomaRequest.getEventType(), person,
				phdRegistryDiplomaRequest);
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
		final PhdRegistryDiplomaRequest request = (PhdRegistryDiplomaRequest) getAcademicServiceRequest();
		labelFormatter.appendLabel(request.getPhdIndividualProgramProcess().getPhdProgram().getName().getPreferedContent());
		labelFormatter.appendLabel(")");
	}
}
