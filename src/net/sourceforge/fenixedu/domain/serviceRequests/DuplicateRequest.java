package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DuplicateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;

public class DuplicateRequest extends DuplicateRequest_Base {

	protected DuplicateRequest() {
		super();
	}

	public DuplicateRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
		this();
		super.init(bean);

		checkParameters(bean);
		super.setDescription(bean.getDescription());
		super.setAmountToPay(bean.getAmountToPay());

		new DuplicateRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}

	private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
		if (StringUtils.isEmpty(bean.getDescription())) {
			throw new DomainException("error.DuplicateRequest.invalid.description");
		}

		if (bean.getAmountToPay() == null || Money.ZERO.equals(bean.getAmountToPay())) {
			throw new DomainException("error.DuplicateRequest.invalid.amountToPay");
		}
	}

	@Override
	public boolean isAvailableForTransitedRegistrations() {
		return false;
	}

	@Override
	public boolean isPayedUponCreation() {
		return true;
	}

	@Override
	public boolean isToPrint() {
		return false;
	}

	@Override
	public boolean isPossibleToSendToOtherEntity() {
		return false;
	}

	@Override
	public boolean isManagedWithRectorateSubmissionBatch() {
		return false;
	}

	@Override
	public EventType getEventType() {
		return EventType.DUPLICATE_REQUEST;

	}

	@Override
	public AcademicServiceRequestType getAcademicServiceRequestType() {
		return AcademicServiceRequestType.DUPLICATE_REQUEST;
	}

	@Override
	public boolean hasPersonalInfo() {
		return false;
	}

	@Override
	public String getDescription() {
		return getDescription(getAcademicServiceRequestType()) + " - " + super.getDescription();
	}

}
