package net.sourceforge.fenixedu.domain.serviceRequests;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ReceivedFromExternalEntityAcademicServiceRequestSituation extends
	ReceivedFromExternalEntityAcademicServiceRequestSituation_Base {

    private ReceivedFromExternalEntityAcademicServiceRequestSituation() {
	super();
    }

    ReceivedFromExternalEntityAcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	this();
	checkOwnParameters(academicServiceRequest, academicServiceRequestBean);
	super.init(academicServiceRequest, academicServiceRequestBean);
	super.setReceivedDate(academicServiceRequestBean.getFinalSituationDate());
    }

    @Override
    public void setReceivedDate(DateTime receivedDate) {
	throw new DomainException("error.serviceRequests.ReceivedFromUnitAcademicServiceRequestSituation.cannot.modify.situation.date");
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

    private void checkOwnParameters(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	if (!academicServiceRequestBean.isToReceiveFromExternalUnit()) {
	    throw new DomainException("error.serviceRequests.ReceivedFromUnitAcademicServiceRequestSituation.invalid.situation.type");
	}

	if (academicServiceRequestBean.getFinalSituationDate().isBefore(
		academicServiceRequest.getActiveSituation().getCreationDate())) {
	    throw new DomainException("error.serviceRequests.ReceivedFromUnitAcademicServiceRequestSituation.invalid.situation.date");
	}
    }
}
