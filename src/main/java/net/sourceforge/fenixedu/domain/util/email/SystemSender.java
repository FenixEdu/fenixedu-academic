package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
        super();
        setMembers(new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER)));
        setFromAddress(Sender.getNoreplyMail());
        setSystemRootDomainObject(getRootDomainObject());
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), "Sistema Fénix");
    }

    @Deprecated
    public boolean hasSystemRootDomainObject() {
        return getSystemRootDomainObject() != null;
    }

}
