/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStateBean implements Serializable {

    YearMonthDay stateDate;

    DomainReference<Registration> registration;

    String remarks;

    RegistrationStateType stateType;

    public RegistrationStateBean(Registration registration) {
	super();
	this.registration = new DomainReference<Registration>(registration);
	this.stateDate = new YearMonthDay();
    }

    public Registration getRegistration() {
	return registration == null ? null : registration.getObject();
    }

    public String getRemarks() {
	return remarks;
    }

    public YearMonthDay getStateDate() {
	return stateDate;
    }

    public RegistrationStateType getStateType() {
	return stateType;
    }

    public void setRegistration(DomainReference<Registration> registration) {
	this.registration = registration;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public void setStateDate(YearMonthDay stateDate) {
	this.stateDate = stateDate;
    }

    public void setStateType(RegistrationStateType stateType) {
	this.stateType = stateType;
    }

}
