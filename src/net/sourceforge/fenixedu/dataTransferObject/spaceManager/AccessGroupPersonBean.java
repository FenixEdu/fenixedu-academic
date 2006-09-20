package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

public class AccessGroupPersonBean implements Serializable {

    private DomainReference<Person> personReference;
    
    public Person getPerson() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setPerson(Person Person) {
        this.personReference = (Person != null) ? new DomainReference<Person>(
                Person) : null;
    }
}
