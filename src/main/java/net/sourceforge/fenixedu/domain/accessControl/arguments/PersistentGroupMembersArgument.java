package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class PersistentGroupMembersArgument extends DomainObjectArgumentParser<PersistentGroupMembers> {
    @Override
    public Class<PersistentGroupMembers> type() {
        return PersistentGroupMembers.class;
    }
}
