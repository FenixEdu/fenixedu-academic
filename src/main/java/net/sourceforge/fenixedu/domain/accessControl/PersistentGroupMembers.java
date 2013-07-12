package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PersistentGroupMembers extends PersistentGroupMembers_Base {

    @Checked("PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups")
    public PersistentGroupMembers(String name, PersistentGroupMembersType type) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setName(name);
        setType(type);
        checkIfPersistenGroupAlreadyExists(name, type);
    }

    @Checked("PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups")
    public void edit(String name, PersistentGroupMembersType type) {
        setName(name);
        setType(type);
        checkIfPersistenGroupAlreadyExists(name, type);
    }

    @Checked("PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups")
    public void delete() {
        getPersons().clear();
        if (hasUnit()) {
            getUnit().removeGroupFromUnitFiles(this);
        }
        setUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Checked("PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups")
    public void setNewPersonToMembersList(Person person) {
        if (person == null) {
            throw new DomainException("error.PersistentGroupMembers.empty.person");
        }
        addPersons(person);
    }

    @Override
    @Checked("PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups")
    public void removePersons(Person person) {
        super.removePersons(person);
    }

    // This method is only used for Renderers
    public Person getNewPersonToMembersList() {
        return null;
    }

    @Override
    public void setName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.PersistentGroupMembers.empty.name");
        }
        super.setName(name);
    }

    @Override
    public void setType(PersistentGroupMembersType type) {
        if (type == null) {
            throw new DomainException("error.PersistentGroupMembers.empty.type");
        }
        super.setType(type);
    }

    private void checkIfPersistenGroupAlreadyExists(String name, PersistentGroupMembersType type) {
        Collection<PersistentGroupMembers> persistentGroupMembers = RootDomainObject.getInstance().getPersistentGroupMembers();
        for (PersistentGroupMembers persistentGroup : persistentGroupMembers) {
            if (!persistentGroup.equals(this) && persistentGroup.getName().equalsIgnoreCase(name)
                    && persistentGroup.getType().equals(type)) {
                throw new DomainException("error.PersistentGroupMembers.group.already.exists");
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getPersons() {
        return getPersonsSet();
    }

    @Deprecated
    public boolean hasAnyPersons() {
        return !getPersonsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
