package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class AcademicServiceRequestBean implements Serializable {
    
    private AcademicServiceRequestSituationType academicServiceRequestSituationType;
    private DateTime situationDate;
    private DomainReference<Unit> situationUnit;
    private Employee employee;
    private String justification;
    
    protected AcademicServiceRequestBean() {
	super();
    }
    
    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	this(academicServiceRequestSituationType, employee, null);
    }
    
    public AcademicServiceRequestBean(final Employee employee, final String justification) {
	this(null, employee, justification);
    }
    
    public AcademicServiceRequestBean(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee, final String justification) {
	this();
	setAcademicServiceRequestSituationType(academicServiceRequestSituationType);
	setEmployee(employee);
	setJustification(justification);
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
    
    public DateTime getSituationDate() {
        return situationDate;
    }

    public void setSituationDate(DateTime situationDate) {
        this.situationDate = situationDate;
    }
    
    public Unit getSituationUnit() {
	return (this.situationUnit != null) ? this.situationUnit.getObject() : null;
    }

    public void setSituationUnit(Unit situationUnit) {
	this.situationUnit = (situationUnit != null) ? new DomainReference<Unit>(situationUnit) : null;
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
    
    public boolean isToSendToUnit() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.SENT_TO_UNIT;
    }
    
    public boolean isToReceiveFromUnit() {
	return this.academicServiceRequestSituationType == AcademicServiceRequestSituationType.RECEIVED_FROM_UNIT;
    }
    
    public boolean isToCancelOrReject() {
	return isToCancel() || isToReject();
    }


}
