package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class PersonNameBean implements Serializable {

	private boolean external;

	private PersonName personName;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PersonNameBean() {
		super();
		setPersonName(null);
		external = false;
	}

	public PersonName getPersonName() {
		return personName;
	}

	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}

	public Person getPerson() {
		PersonName personName = getPersonName();
		return (personName != null) ? personName.getPerson() : null;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}
}
