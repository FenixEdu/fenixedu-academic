package net.sourceforge.fenixedu.domain.accessControl;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.PersistentGroupMembersPredicates;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public class PersistentGroupMembers extends PersistentGroupMembers_Base {

    public PersistentGroupMembers(String name, PersistentGroupMembersType type) {
//        check(this, PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups);
        super();
        setRootDomainObject(Bennu.getInstance());
        setName(name);
        setType(type);
        checkIfPersistenGroupAlreadyExists(name, type);
    }

    public void edit(String name, PersistentGroupMembersType type) {
        check(this, PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups);
        setName(name);
        setType(type);
        checkIfPersistenGroupAlreadyExists(name, type);
    }

    public void delete() {
        check(this, PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups);
        if (getMembersLinkGroup() != null) {
            throw new DomainException("error.persistentGroupMembers.cannotDeletePersistentGroupMembersUsedInAccessControl");
        }
        getPersons().clear();
        if (hasUnit()) {
            getUnit().removeGroupFromUnitFiles(this);
        }
        setUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void setNewPersonToMembersList(Person person) {
        check(this, PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups);
        if (person == null) {
            throw new DomainException("error.PersistentGroupMembers.empty.person");
        }
        addPersons(person);
    }

    @Override
    public void removePersons(Person person) {
        check(this, PersistentGroupMembersPredicates.checkPermissionsToManagePersistentGroups);
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
        Collection<PersistentGroupMembers> persistentGroupMembers = Bennu.getInstance().getPersistentGroupMembersSet();
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
    public boolean hasBennu() {
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
