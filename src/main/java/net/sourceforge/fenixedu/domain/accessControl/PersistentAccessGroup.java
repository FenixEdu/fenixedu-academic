package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class PersistentAccessGroup extends PersistentAccessGroup_Base {
    protected PersistentAccessGroup() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
    }

    {
        super.setCreated(new DateTime());
        super.setCreator(AccessControl.getPerson());
    }

    protected abstract PersistentAccessGroup instantiate();

    public boolean isActive() {
        return super.getRootDomainObject() != null;
    }

    protected void copy(PersistentAccessGroup old) {
        super.setOldGroup(old);
        old.setDeletedRootDomainObject(old.getRootDomainObject());
        old.setRootDomainObject(null);
        getMemberSet().addAll(old.getMemberSet());
    }

    public PersistentAccessGroup grant(Party member) {
        PersistentAccessGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.addMember(member);
        return newGroup;
    }

    public PersistentAccessGroup revoke(Party member) {
        PersistentAccessGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.removeMember(member);
        return newGroup;
    }

    public PersistentAccessGroup changeMembers(Set<Party> members) {
        PersistentAccessGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.getMemberSet().clear();
        newGroup.getMemberSet().addAll(members);
        return newGroup;
    }

    @Atomic
    public PersistentAccessGroup delete() {
        PersistentAccessGroup newGroup = instantiate();
        newGroup.copy(this);
        newGroup.setDeletedRootDomainObject(newGroup.getRootDomainObject());
        newGroup.setRootDomainObject(null);
        return newGroup;
    }

    public Set<Person> getElements() {
        Set<Person> members = new HashSet<Person>();
        for (Party party : getMemberSet()) {
            if (party instanceof Person) {
                members.add((Person) party);
            } else {
                fillElements(members, (Unit) party);
            }
        }
        return members;
    }

    private void fillElements(Set<Person> members, Unit unit) {
        members.addAll(unit.getPossibleGroupMembers());
        for (Party child : unit.getActiveChildParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class)) {
            fillElements(members, (Unit) child);
        }
    }

    @Override
    public void setCreated(DateTime created) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreator(Person creator) {
        throw new UnsupportedOperationException();
    }

    public RootDomainObject getRootDomainObject() {
        if (super.getRootDomainObject() != null) {
            return super.getRootDomainObject();
        }
        return super.getDeletedRootDomainObject();
    }

    @ConsistencyPredicate
    public final boolean hasRootLink() {
        return super.getRootDomainObject() != null || super.getDeletedRootDomainObject() != null || super.getNewGroup() != null;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getMember() {
        return getMemberSet();
    }

    @Deprecated
    public boolean hasAnyMember() {
        return !getMemberSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreated() {
        return getCreated() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasOldGroup() {
        return getOldGroup() != null;
    }

    @Deprecated
    public boolean hasNewGroup() {
        return getNewGroup() != null;
    }

    @Deprecated
    public boolean hasDeletedRootDomainObject() {
        return getDeletedRootDomainObject() != null;
    }

}
