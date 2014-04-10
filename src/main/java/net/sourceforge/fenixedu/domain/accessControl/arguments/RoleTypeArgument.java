package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class RoleTypeArgument extends EnumArgument<RoleType> {
    @Override
    public Class<RoleType> type() {
        return RoleType.class;
    }
}
