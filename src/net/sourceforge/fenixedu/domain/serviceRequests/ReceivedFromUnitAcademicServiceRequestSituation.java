package net.sourceforge.fenixedu.domain.serviceRequests;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ReceivedFromUnitAcademicServiceRequestSituation extends ReceivedFromUnitAcademicServiceRequestSituation_Base {

    private ReceivedFromUnitAcademicServiceRequestSituation() {
	super();
    }

    ReceivedFromUnitAcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	this();
	checkParameters(academicServiceRequestBean);
	super.init(academicServiceRequest, academicServiceRequestBean);
	super.setReceivedDate(academicServiceRequestBean.getSituationDate());
    }

    @Override
    public void setReceivedDate(DateTime receivedDate) {
	throw new DomainException("error.ReceivedFromUnitAcademicServiceRequestSituation.cannot.modify.situation.date");
    }

    private void checkParameters(final AcademicServiceRequestBean academicServiceRequestBean) {
	if (!academicServiceRequestBean.isToReceiveFromUnit()) {
	    throw new DomainException("error.ReceivedFromUnitAcademicServiceRequestSituation.invalid.situation.type");
	}
    }
}
