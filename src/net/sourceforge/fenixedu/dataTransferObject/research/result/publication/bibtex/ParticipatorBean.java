package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

public class ParticipatorBean implements Serializable {

    private String personName;

    private DomainReference<Person> person;

    // private boolean other = false;

    public ParticipatorBean(Person person) {
	setPerson(person);
	setPersonName(person.getName());
    }

    public ParticipatorBean(String name) {
	setPerson(null);
	setPersonName(name);
	// setOther(true);
    }

    public Person getPerson() {
	return (this.person == null) ? null : this.person.getObject();
    }

    public void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public String getPersonName() {
	return personName;
    }

    public void setPersonName(String personName) {
	this.personName = personName;
    }

    // public boolean isOther() {
    // return other;
    // }
    //
    // public void setOther(boolean other) {
    // this.other = other;
    // }

    @Override
    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof ParticipatorBean))
	    return false;

	ParticipatorBean bean = (ParticipatorBean) obj;
	return ((bean.getPerson() == null && this.getPerson() == null) || (bean.getPerson() != null
		&& this.getPerson() != null && bean.getPerson().equals(this.getPerson())));
    }
}
