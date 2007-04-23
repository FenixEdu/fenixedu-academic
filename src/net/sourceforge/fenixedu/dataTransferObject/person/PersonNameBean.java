package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class PersonNameBean implements Serializable {

	private DomainReference<PersonName> personName;
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
	}

	public PersonName getPersonName() {
		return personName.getObject();
	}

	public void setPersonName(PersonName personName) {
		this.personName = new DomainReference<PersonName>(personName);
	}
	
	public Person getPerson() {
		PersonName personName =  getPersonName();
		return (personName!=null) ? personName.getPerson() : null;
	}
}
