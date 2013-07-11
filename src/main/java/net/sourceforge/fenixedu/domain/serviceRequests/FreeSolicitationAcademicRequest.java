package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

import org.apache.commons.lang.StringUtils;

public class FreeSolicitationAcademicRequest extends FreeSolicitationAcademicRequest_Base {

    protected FreeSolicitationAcademicRequest() {
        super();
    }

    public FreeSolicitationAcademicRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setSubject(bean.getSubject());
        super.setPurpose(bean.getPurpose());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
        if (StringUtils.isEmpty(bean.getSubject())) {
            throw new DomainException("error.FreeSolicitationAcademicRequest.invalid.subject");
        }
    }

    @Override
    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

    }

    @Override
    public void setSubject(String subject) {
        throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.subject");
    }

    @Override
    public void setPurpose(String purpose) {
        throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.purpose");
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.FREE_SOLICITATION_ACADEMIC_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
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

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasPurpose() {
        return getPurpose() != null;
    }

}
