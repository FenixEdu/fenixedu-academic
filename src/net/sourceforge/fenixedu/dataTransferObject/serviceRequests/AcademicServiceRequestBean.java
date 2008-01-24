package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AcademicServiceRequestBean implements Serializable {

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private Employee employee;

    private String justification;

    private YearMonthDay situationDate;

    public AcademicServiceRequestBean() {
	super();
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee) {
	this(academicServiceRequestSituationType, employee, null);
    }

    public AcademicServiceRequestBean(final Employee employee, final String justification) {
	this(null, employee, justification);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee, final String justification) {
	this();
	setAcademicServiceRequestSituationType(academicServiceRequestSituationType);
	setEmployee(employee);
	setJustification(justification);
    }

    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    final Employee employee, final YearMonthDay situationDate, final String justification) {
	this(academicServiceRequestSituationType, employee, justification);
	setSituationDate(situationDate);
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

    public YearMonthDay getSituationDate() {
	return situationDate;
    }

    public void setSituationDate(YearMonthDay situationDate) {
	this.situationDate = situationDate;
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
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

    public DateTime getFinalSituationDate() {
	return getSituationDate().toDateTimeAtCurrentTime();
    }
}
