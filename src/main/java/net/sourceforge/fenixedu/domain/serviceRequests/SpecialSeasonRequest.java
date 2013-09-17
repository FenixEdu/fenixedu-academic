package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SpecialSeasonRequest extends SpecialSeasonRequest_Base {

    public SpecialSeasonRequest() {
        super();
    }

    public SpecialSeasonRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
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

            if (getDeferred() != null && getDeferred() == true) {
                new StudentStatute(student, StudentStatuteType.SPECIAL_SEASON_GRANTED_BY_REQUEST, getBeginExecutionPeriod(),
                        getEndExecutionPeriod());
            }
        }

    }

    public String getDefermentDescription() {
        if (getDeferred() == null) {
            return "-";
        }
        if (getDeferred() == true) {
            return ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale()).getString("request.granted");
        } else {
            return ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale()).getString("request.declined");
        }
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
        return AcademicServiceRequestType.SPECIAL_SEASON_REQUEST;
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

    @Deprecated
    public boolean hasBeginExecutionPeriod() {
        return getBeginExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasEndExecutionPeriod() {
        return getEndExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasDeferred() {
        return getDeferred() != null;
    }

}
