package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Person;

public class ResultAuthorshipSimpleCreationBean implements Serializable {
    private DomainReference<Person> person;
    private String personName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String name) {
        this.personName = name;
    }
    
    public Person getPerson() {
        return (this.person == null) ? null : this.person.getObject();
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }
    
    public ExternalPerson getExternalPerson() {
        return (this.person == null) ? null : this.person.getObject().getExternalPerson();
    }

    public void setExternalPerson(ExternalPerson externalPerson) {
        if (externalPerson == null) {
            this.person = null;
        }
        else {
            setPerson(externalPerson.getPerson());
        }
    }
}
