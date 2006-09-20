package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class AccessGroupPersonBean implements Serializable {

    private SpaceAccessGroupType accessGroupType;
    private DomainReference<Person> personReference;
    
    public Person getPerson() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setPerson(Person Person) {
        this.personReference = (Person != null) ? new DomainReference<Person>(
                Person) : null;
    }

    public SpaceAccessGroupType getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(SpaceAccessGroupType accessGroupType) {
        this.accessGroupType = accessGroupType;
    }
}
