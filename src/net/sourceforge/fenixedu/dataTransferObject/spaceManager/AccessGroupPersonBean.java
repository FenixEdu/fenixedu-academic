package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class AccessGroupPersonBean implements Serializable {

    private DomainReference<PersistentGroupMembers> persistentGroupMembersReference;
    private SpaceAccessGroupType accessGroupType;
    private DomainReference<Person> personReference;
    private Boolean maintainElements; 
    private RoleType roleType;    
    
    public AccessGroupPersonBean() {}
    
    public Boolean getMaintainElements() {
        return maintainElements;
    }

    public void setMaintainElements(Boolean maintainElements) {
        this.maintainElements = maintainElements;
    }

    public Person getPerson() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setPerson(Person Person) {
        this.personReference = (Person != null) ? new DomainReference<Person>(Person) : null;
    }

    public PersistentGroupMembers getPersistentGroupMembers() {
        return (this.persistentGroupMembersReference != null) ? this.persistentGroupMembersReference.getObject() : null;
    }

    public void setPersistentGroupMembers (PersistentGroupMembers persistentGroupMembers) {
        this.persistentGroupMembersReference = (persistentGroupMembers != null) ? new DomainReference<PersistentGroupMembers>(persistentGroupMembers) : null;
    }
    
    public SpaceAccessGroupType getAccessGroupType() {
        return accessGroupType;
    }

    public void setAccessGroupType(SpaceAccessGroupType accessGroupType) {
        this.accessGroupType = accessGroupType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
