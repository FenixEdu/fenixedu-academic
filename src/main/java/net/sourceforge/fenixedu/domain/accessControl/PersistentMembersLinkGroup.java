package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentMembersLinkGroup extends PersistentMembersLinkGroup_Base {
    protected PersistentMembersLinkGroup(PersistentGroupMembers persistentGroupMembers) {
        super();
        setPersistentGroupMembers(persistentGroupMembers);
    }

    @Override
    public Group toGroup() {
        return MembersLinkGroup.get(getPersistentGroupMembers());
    }

    @Override
    protected void gc() {
        setPersistentGroupMembers(null);
        super.gc();
    }

    public static PersistentMembersLinkGroup getInstance(PersistentGroupMembers persistentGroupMembers) {
        PersistentMembersLinkGroup instance = persistentGroupMembers.getMembersLinkGroup();
        return instance != null ? instance : create(persistentGroupMembers);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentMembersLinkGroup create(PersistentGroupMembers persistentGroupMembers) {
        PersistentMembersLinkGroup instance = persistentGroupMembers.getMembersLinkGroup();
        return instance != null ? instance : new PersistentMembersLinkGroup(persistentGroupMembers);
    }
}
