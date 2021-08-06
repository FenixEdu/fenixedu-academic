package org.fenixedu.academic.domain.serviceRequests;

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class ExtraordinarySeasonRequest extends ExtraordinarySeasonRequest_Base implements IDeferableRequest {
    
    public ExtraordinarySeasonRequest() {
        super();
    }

    public ExtraordinarySeasonRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);

        super.setBeginExecutionPeriod(bean.getExecutionYear().getFirstExecutionPeriod());
        super.setEndExecutionPeriod(bean.getExecutionYear().getLastExecutionPeriod());
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
        super.createAcademicServiceRequestSituations(academicServiceRequestBean);

        if (academicServiceRequestBean.isNew()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.PROCESSING, academicServiceRequestBean.getResponsible()));
        }

        if (academicServiceRequestBean.isToConclude()) {
            AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
                    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
        }

    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {

            Student student = getRegistration().getStudent();

            if (getDeferred() != null && getDeferred()) {
                new StudentStatute(student, StatuteType.findExtraordinarySeasonGrantedStatuteType().orElse(null),
                        getBeginExecutionPeriod(), getEndExecutionPeriod());
            }
        }

    }

    public String getDefermentDescription() {
        if (getDeferred() == null) {
            return "-";
        }
        final String key = getDeferred() ? "request.granted" : "request.declined";
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }

    @Atomic
    public void setDeferment(Boolean deferred) {
        this.setDeferred(deferred);
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.EXTRAORDINARY_SEASON_REQUEST;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
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
    public boolean isToPrint() {
        return false;
    }


}
