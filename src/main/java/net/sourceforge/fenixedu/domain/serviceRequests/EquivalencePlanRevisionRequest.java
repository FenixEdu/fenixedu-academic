package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public class EquivalencePlanRevisionRequest extends EquivalencePlanRevisionRequest_Base {

    protected EquivalencePlanRevisionRequest() {
        super();
    }

    public EquivalencePlanRevisionRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setEquivalencePlanRequest(bean.getEquivalencePlanRequest());
    }

    @Override
    public void setEquivalencePlanRequest(final EquivalencePlanRequest equivalencePlanRequest) {
        throw new DomainException("error.EquivalencePlanRevisionRequest.cannot.modify.equivalencePlanRequest");
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        final EquivalencePlanRequest equivalencePlanRequest = bean.getEquivalencePlanRequest();
        final ExecutionYear executionYear = bean.getExecutionYear();

        if (equivalencePlanRequest == null) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.equivalencePlanRequest.cannot.be.null");
        }

        if (!equivalencePlanRequest.hasConcluded()) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.equivalencePlanRequest.is.not.concluded");
        }

        if (executionYear == null) {
            throw new DomainException("error.EquivalencePlanRevisionRequest.executionYear.cannot.be.null");
        }
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.REVISION_EQUIVALENCE_PLAN;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    protected void disconnect() {
        super.setEquivalencePlanRequest(null);
        super.disconnect();
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToProcess()) {
            academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
        }
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

}
