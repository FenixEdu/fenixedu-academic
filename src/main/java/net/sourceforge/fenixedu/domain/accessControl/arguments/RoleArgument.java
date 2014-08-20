package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class RoleArgument implements ArgumentParser<Role> {

    @Override
    public Role parse(String argument) {
        return Role.getRoleByRoleType(RoleType.valueOf(argument));
    }

    @Override
    public String serialize(Role argument) {
        return argument.getRoleType().name();
    }

    @Override
    public Class<Role> type() {
        return Role.class;
    }

}
