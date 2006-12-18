package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class AcademicServiceRequestSituation extends AcademicServiceRequestSituation_Base {

    public static Comparator<AcademicServiceRequestSituation> COMPARATOR_BY_MOST_RECENT_CREATION_DATE = new Comparator<AcademicServiceRequestSituation>() {
	public int compare(AcademicServiceRequestSituation leftAcademicServiceRequestSituation,
		AcademicServiceRequestSituation rightAcademicServiceRequestSituation) {
	    int comparationResult = -(leftAcademicServiceRequestSituation.getCreationDate()
		    .compareTo(rightAcademicServiceRequestSituation.getCreationDate()));
	    return (comparationResult == 0) ? leftAcademicServiceRequestSituation.getIdInternal()
		    .compareTo(rightAcademicServiceRequestSituation.getIdInternal()) : comparationResult;
	}
    };

    private AcademicServiceRequestSituation() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setCreationDate(new DateTime());
    }

    AcademicServiceRequestSituation(AcademicServiceRequest academicServiceRequest,
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	this(academicServiceRequest, academicServiceRequestSituationType, employee, null);

    }

    AcademicServiceRequestSituation(AcademicServiceRequest academicServiceRequest,
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee,
	    String justification) {
	this();

	init(academicServiceRequest, academicServiceRequestSituationType, employee, justification);

    }

    private void checkParameters(AcademicServiceRequest academicServiceRequest,
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee,
	    String justification) {
	if (academicServiceRequest == null) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequest.cannot.be.null");
	}
	if (academicServiceRequestSituationType == null) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequestSituation.academicServiceRequestSituationType.cannot.be.null");
	}

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CANCELLED
		|| academicServiceRequestSituationType == AcademicServiceRequestSituationType.REJECTED) {
	    if (StringUtils.isEmpty(justification)) {
		throw new DomainException(
			"error.serviceRequests.AcademicServiceRequestSituation.justification.cannot.be.null.for.cancelled.and.rejected.situations");
	    }
	}
    }

    protected void init(AcademicServiceRequest academicServiceRequest,
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee,
	    String justification) {
	checkParameters(academicServiceRequest, academicServiceRequestSituationType, employee,
		justification);
	super.setAcademicServiceRequest(academicServiceRequest);
	super.setAcademicServiceRequestSituationType(academicServiceRequestSituationType);
	super.setEmployee(employee);
	super.setJustification(StringUtils.isEmpty(justification) ? null : justification);
    }

    public void delete() {
	if (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED) {
	    throw new DomainException("AcademicServiceRequestSituation.already.delivered");
	}
	
	super.removeRootDomainObject();
	super.removeEmployee();
	super.removeAcademicServiceRequest();
	super.deleteDomainObject();
    }
    
    @Override
    public void setAcademicServiceRequest(AcademicServiceRequest academicServiceRequest) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequest");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.employee");
    }

    @Override
    public void setAcademicServiceRequestSituationType(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.academicServiceRequestSituationType");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.creationDate");
    }

    @Override
    public void setJustification(String justification) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequestSituation.cannot.modify.justification");
    }

    void edit(Employee employee, String justification) {
	super.setEmployee(employee);
	super.setJustification(justification);
    }

}
