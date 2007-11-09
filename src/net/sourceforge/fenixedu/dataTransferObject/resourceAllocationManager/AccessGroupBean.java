package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;

public class AccessGroupBean implements Serializable {
    
    private DomainReference<Person> personReference;
    
    private ResourceAllocationAccessGroupType accessGroupType;
        
    public AccessGroupBean() {}
    
    public Person getPerson() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setPerson(Person Person) {
        this.personReference = (Person != null) ? new DomainReference<Person>(Person) : null;
    }

    public ResourceAllocationAccessGroupType getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(ResourceAllocationAccessGroupType accessGroupType) {
        this.accessGroupType = accessGroupType;
    }
}
