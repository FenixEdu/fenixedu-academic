package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(() -> Optional.ofNullable(persistentGroupMembers.getMembersLinkGroup()),
                () -> new PersistentMembersLinkGroup(persistentGroupMembers));
    }
}
