package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

	private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

	protected SchoolRegistrationDeclarationRequest() {
		super();
	}

	public SchoolRegistrationDeclarationRequest(final DocumentRequestCreateBean bean) {
		this();
		checkRulesToCreate(bean);
		bean.setExecutionYear(getRequestedExecutionYear(bean));
		super.init(bean);
	}

	private ExecutionYear getRequestedExecutionYear(DocumentRequestCreateBean bean) {
		return bean.getRegistration().hasIndividualCandidacyFor(bean.getExecutionYear().getNextExecutionYear()) ? bean
				.getExecutionYear().getNextExecutionYear() : bean.getExecutionYear();
	}

	protected void checkRulesToCreate(final DocumentRequestCreateBean bean) {
		final ExecutionYear executionYear = bean.getExecutionYear();

		if (!bean.getRegistration().isRegistered(executionYear)
				&& !bean.getRegistration().hasIndividualCandidacyFor(executionYear.getNextExecutionYear())) {
			throw new DomainException(
					"SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
		}
	}

	@Override
	final public DocumentRequestType getDocumentRequestType() {
		return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
	}

	@Override
	final public String getDocumentTemplateKey() {
		return getClass().getName();
	}

	@Override
	final public EventType getEventType() {
		return EventType.SCHOOL_REGISTRATION_DECLARATION_REQUEST;
	}

	@Override
	public boolean hasPersonalInfo() {
		return true;
	}

	@Override
	protected boolean hasFreeDeclarationRequests() {
		final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

		final Set<DocumentRequest> schoolRegistrationDeclarations =
				getRegistration().getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
						DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION, false);

		final Set<DocumentRequest> enrolmentDeclarations =
				getRegistration().getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
						DocumentRequestType.ENROLMENT_DECLARATION, false);

		return ((schoolRegistrationDeclarations.size() + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR);
	}

}
