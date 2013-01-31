package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.person.PersonName;

public class ResidenceRoleManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private PersonName personName;
	private String name;

	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}

	public PersonName getPersonName() {
		return personName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
