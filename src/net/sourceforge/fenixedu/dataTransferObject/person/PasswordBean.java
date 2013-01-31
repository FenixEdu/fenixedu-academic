package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class PasswordBean implements Serializable {

	private Person person;

	private String password;

	public PasswordBean(Person person, String password) {
		super();
		setPerson(person);
		setPassword(password);
	}

	public Person getPerson() {
		return this.person;
	}

	private void setPerson(Person person) {
		this.person = person;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String generatedPassword) {
		this.password = generatedPassword;
	}

}
