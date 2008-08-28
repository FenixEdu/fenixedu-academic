package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class AcademicServiceRequestSituation extends AcademicServiceRequestSituation_Base {

    public static Comparator<AcademicServiceRequestSituation> COMPARATOR_BY_MOST_RECENT_CREATION_DATE_AND_ID = new Comparator<AcademicServiceRequestSituation>() {
	public int compare(AcademicServiceRequestSituation leftAcademicServiceRequestSituation,
		AcademicServiceRequestSituation rightAcademicServiceRequestSituation) {
	    int comparationResult = leftAcademicServiceRequestSituation.getCreationDate().compareTo(
		    rightAcademicServiceRequestSituation.getCreationDate());
	    return -((comparationResult == 0) ? COMPARATOR_BY_ID.compare(leftAcademicServiceRequestSituation,
		    rightAcademicServiceRequestSituation) : comparationResult);
	}
    };

    protected AcademicServiceRequestSituation() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setCreationDate(new DateTime());
    }

    private AcademicServiceRequestSituation(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	this();
	init(academicServiceRequest, academicServiceRequestBean);
    }

    protected void init(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	checkParameters(academicServiceRequest, academicServiceRequestBean);

	super.setAcademicServiceRequest(academicServiceRequest);
	super.setAcademicServiceRequestSituationType(academicServiceRequestBean.getAcademicServiceRequestSituationType());
	super.setEmployee(academicServiceRequestBean.getEmployee());
	super.setJustification(academicServiceRequestBean.hasJustification() ? academicServiceRequestBean.getJustification()
		: null);
	super.setSituationDate(academicServiceRequestBean.getFinalSituationDate());
    }

    protected void checkParameters(final AcademicServiceRequest academicServiceRequest, final AcademicServiceRequestBean bean) {
	if (academicServiceRequest == null) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequest.cannot.be.null");
	}

	if (!bean.hasAcademicServiceRequestSituationType()) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequestSituationType.cannot.be.null");
	}

	final AcademicServiceRequestSituation activeSituation = academicServiceRequest.getActiveSituation();
	if (activeSituation != null) {
	    final DateTime activeSituationDate = activeSituation.getSituationDate();
	    final DateTime finalSituationDate = bean.getFinalSituationDate();
	    if (finalSituationDate.toLocalDate().isBefore(activeSituationDate.toLocalDate())) {
		throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.situation.date.is.before");
	    }

	    if (finalSituationDate.toLocalDate().isEqual(activeSituationDate.toLocalDate())
		    && finalSituationDate.isBefore(activeSituationDate)) {
		bean.setFinalSituationDate(activeSituationDate.plusMinutes(1));
	    }
	}

	if (bean.getFinalSituationDate().isAfterNow()) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.situation.date.is.after");
	}

	if (bean.isToCancelOrReject()) {
	    if (!bean.hasJustification()) {
		throw new DomainException(
			"error.serviceRequests.AcademicServiceRequestSituation.justification.cannot.be.null.for.cancelled.and.rejected.situations");
	    }
	}
    }

    @Override
    public void setAcademicServiceRequest(AcademicServiceRequest academicServiceRequest) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequest");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.employee");
    }

    @Override
    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequestSituationType");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.creationDate");
    }

    @Override
    public void setJustification(String justification) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.justification");
    }

    public boolean isDelivered() {
	return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED;
    }

    void edit(final AcademicServiceRequestBean academicServiceRequestBean) {
	super.setEmployee(academicServiceRequestBean.getEmployee());
	super.setJustification(academicServiceRequestBean.getJustification());
    }

    public void delete() {
	checkRulesToDelete();

	super.setRootDomainObject(null);
	super.setEmployee(null);
	super.setAcademicServiceRequest(null);

	super.deleteDomainObject();
    }

    protected void checkRulesToDelete() {
	if (isDelivered()) {
	    throw new DomainException("AcademicServiceRequestSituation.already.delivered");
	}
    }

    static AcademicServiceRequestSituation create(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {

	switch (academicServiceRequestBean.getAcademicServiceRequestSituationType()) {
	case SENT_TO_EXTERNAL_ENTITY:
	    return new SentToExternalEntityAcademicServiceRequestSituation(academicServiceRequest, academicServiceRequestBean);
	case RECEIVED_FROM_EXTERNAL_ENTITY:
	    return new ReceivedFromExternalEntityAcademicServiceRequestSituation(academicServiceRequest,
		    academicServiceRequestBean);
	default:
	    return new AcademicServiceRequestSituation(academicServiceRequest, academicServiceRequestBean);
	}
    }
}
