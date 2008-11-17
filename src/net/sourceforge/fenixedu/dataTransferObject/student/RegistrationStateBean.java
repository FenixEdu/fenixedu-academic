/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStateBean extends StateBean implements Serializable {

    DomainReference<Registration> registration;

    String remarks;

    public RegistrationStateBean(Registration registration) {
	super();
	this.registration = new DomainReference<Registration>(registration);
	setStateDate(null);
    }

    public RegistrationStateBean(final RegistrationStateType type) {
	super(type.name());
    }

    public Registration getRegistration() {
	return registration == null ? null : registration.getObject();
    }

    public String getRemarks() {
	return remarks;
    }

    public RegistrationStateType getStateType() {
	return getNextState() == null ? null : RegistrationStateType.valueOf(getNextState());
    }

    public void setRegistration(DomainReference<Registration> registration) {
	this.registration = registration;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public void setStateType(final RegistrationStateType stateType) {
	setNextState(stateType == null ? null : stateType.name());
    }

}
