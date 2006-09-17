package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

public class PasswordBean implements Serializable {

    private DomainReference<Person> person;

    private String password;

    public PasswordBean(Person person, String password) {
	super();
	setPerson(person);
	setPassword(password);
    }

    public Person getPerson() {
	return (this.person != null) ? this.person.getObject() : null;
    }

    private void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String generatedPassword) {
	this.password = generatedPassword;
    }

}
