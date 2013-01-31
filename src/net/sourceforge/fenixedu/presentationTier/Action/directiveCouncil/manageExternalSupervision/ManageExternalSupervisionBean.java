package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;

public class ManageExternalSupervisionBean implements Serializable {

	private RegistrationProtocol registrationProtocol;
	private RegistrationAgreement registrationAgreement;
	private Person newSupervisor;

	public ManageExternalSupervisionBean() {
		super();
		newSupervisor = null;
	}

	public ManageExternalSupervisionBean(RegistrationProtocol registrationProtocol) {
		this();
		this.registrationProtocol = registrationProtocol;
		this.registrationAgreement = registrationProtocol.getRegistrationAgreement();
	}

	public RegistrationProtocol getRegistrationProtocol() {
		return registrationProtocol;
	}

	public void setRegistrationProtocol(RegistrationProtocol registrationProtocol) {
		this.registrationProtocol = registrationProtocol;
	}

	public RegistrationAgreement getRegistrationAgreement() {
		return registrationAgreement;
	}

	public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
		this.registrationAgreement = registrationAgreement;
	}

	public Person getNewSupervisor() {
		return newSupervisor;
	}

	public void setNewSupervisor(Person newSupervisor) {
		this.newSupervisor = newSupervisor;
	}

	public Set<Person> getSupervisors() {
		return registrationProtocol.getSupervisorsSet();
	}

	public void addSupervisor() {
		registrationProtocol.addSupervisor(newSupervisor);
	}

	public void removeSupervisor(Person supervisor) {
		registrationProtocol.removeSupervisor(supervisor);
	}

}
