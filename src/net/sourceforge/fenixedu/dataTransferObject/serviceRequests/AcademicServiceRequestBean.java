package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AcademicServiceRequestBean implements Serializable {

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private DomainReference<Employee> employeeDomainReference;

    private String justification;

    private YearMonthDay situationDate;

    private Integer serviceRequestYear;

    public AcademicServiceRequestBean() {
	super();
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee) {
	this();
	setAcademicServiceRequestSituationType(academicServiceRequestSituationType);
	setEmployee(employee);
	setSituationDate(new YearMonthDay());
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee, final Integer serviceRequestYear) {
	this(academicServiceRequestSituationType, employee);
	setServiceRequestYear(serviceRequestYear);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee, final String justification) {
	this(academicServiceRequestSituationType, employee);
	setJustification(justification);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee, final YearMonthDay situationDate, final String justification) {
	this(academicServiceRequestSituationType, employee, justification);
	setSituationDate(situationDate);
    }

    public AcademicServiceRequestBean(final Employee employee, final String justification) {
	this((AcademicServiceRequestSituationType) null, employee, justification);
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public boolean hasAcademicServiceRequestSituationType() {
	return this.academicServiceRequestSituationType != null;
    }

    public DateTime getFinalSituationDate() {
	return getSituationDate().toDateTimeAtMidnight().plusDays(1).minusMinutes(1);
    }

    public YearMonthDay getSituationDate() {
	return situationDate;
    }

    public void setSituationDate(YearMonthDay situationDate) {
	this.situationDate = situationDate;
    }

    public Employee getEmployee() {
	return employeeDomainReference == null ? null : employeeDomainReference.getObject();
    }

    public void setEmployee(Employee employee) {
	this.employeeDomainReference = new DomainReference<Employee>(employee);
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }

    public boolean hasJustification() {
	return !StringUtils.isEmpty(this.justification);
    }

    public Integer getServiceRequestYear() {
	return serviceRequestYear;
    }

    public void setServiceRequestYear(Integer serviceRequestYear) {
	this.serviceRequestYear = serviceRequestYear;
    }

    public boolean isNew() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.NEW;
    }

    public boolean isToProcess() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING;
    }

    public boolean isToDeliver() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.DELIVERED;
    }

    public boolean isToCancel() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.CANCELLED;
    }

    public boolean isToReject() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.REJECTED;
    }

    public boolean isToConclude() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED;
    }

    public boolean isToSendToExternalEntity() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY;
    }

    public boolean isToReceiveFromExternalUnit() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY;
    }

    public boolean isToCancelOrReject() {
	return isToCancel() || isToReject();
    }

    private AdministrativeOffice getAdministrativeOffice() {
	return getEmployee() == null ? null : getEmployee().getAdministrativeOffice();
    }

    private Campus getCampus() {
	return getEmployee() == null ? null : getEmployee().getCurrentCampus();
    }

    public Collection<AcademicServiceRequest> searchAcademicServiceRequests() {
	return getAdministrativeOffice().searchRegistrationAcademicServiceRequests(serviceRequestYear,
		academicServiceRequestSituationType, getCampus());
    }

}
