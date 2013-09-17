package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SentToExternalEntityAcademicServiceRequestSituation extends SentToExternalEntityAcademicServiceRequestSituation_Base {

    private SentToExternalEntityAcademicServiceRequestSituation() {
        super();
    }

    SentToExternalEntityAcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        this();
        checkOwnParameters(academicServiceRequest, academicServiceRequestBean);
        super.init(academicServiceRequest, academicServiceRequestBean);
        super.setSentDate(academicServiceRequestBean.getFinalSituationDate());
    }

    @Override
    protected void checkParameters(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        super.checkParameters(academicServiceRequest, academicServiceRequestBean);
        if (!academicServiceRequestBean.hasJustification()) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequestSituation.justification.cannot.be.null.for.when.send");
        }
    }

    @Override
    public void setSentDate(DateTime sentDate) {
        throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.cannot.modify.situation.date");
    }

    private void checkOwnParameters(final AcademicServiceRequest academicServiceRequest,
            final AcademicServiceRequestBean academicServiceRequestBean) {
        if (!academicServiceRequestBean.isToSendToExternalEntity()) {
            throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.invalid.situation.type");
        }

        if (academicServiceRequestBean.getFinalSituationDate().isBefore(
                academicServiceRequest.getActiveSituation().getSituationDate())) {
            throw new DomainException("error.serviceRequests.SentToUnitAcademicServiceRequestSituation.invalid.situation.date");
        }
    }

    @Override
    public DateTime getFinalSituationDate() {
        return getSentDate();
    }
    @Deprecated
    public boolean hasSentDate() {
        return getSentDate() != null;
    }

}
