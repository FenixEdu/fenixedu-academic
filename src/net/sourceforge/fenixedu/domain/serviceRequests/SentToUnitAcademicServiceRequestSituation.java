package net.sourceforge.fenixedu.domain.serviceRequests;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SentToUnitAcademicServiceRequestSituation extends SentToUnitAcademicServiceRequestSituation_Base {

    private SentToUnitAcademicServiceRequestSituation() {
	super();
    }

    SentToUnitAcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	this();
	checkParameters(academicServiceRequestBean);
	super.init(academicServiceRequest, academicServiceRequestBean);
	super.setSentDate(academicServiceRequestBean.getSituationDate());
    }

    @Override
    public void setSentDate(DateTime sentDate) {
	throw new DomainException("error.SentToUnitAcademicServiceRequestSituation.cannot.modify.situation.date");
    }

    private void checkParameters(final AcademicServiceRequestBean academicServiceRequestBean) {
	if (!academicServiceRequestBean.isToSendToUnit()) {
	    throw new DomainException("error.SentToUnitAcademicServiceRequestSituation.invalid.situation.type");
	}
    }
}
