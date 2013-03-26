package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;

public class AccessGroupBean implements Serializable {

    private Person personReference;

    private ResourceAllocationAccessGroupType accessGroupType;

    public AccessGroupBean() {
    }

    public Person getPerson() {
        return this.personReference;
    }

    public void setPerson(Person Person) {
        this.personReference = Person;
    }

    public ResourceAllocationAccessGroupType getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(ResourceAllocationAccessGroupType accessGroupType) {
        this.accessGroupType = accessGroupType;
    }
}
