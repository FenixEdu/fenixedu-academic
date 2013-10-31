package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.BundleUtil;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
        super();
        setMembers(new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER)));
        setFromAddress(Sender.getNoreplyMail());
        setSystemRootDomainObject(getRootDomainObject());
    }

    @Override
    public String getFromName() {
        String fenixSystem = BundleUtil.getMessageFromModuleOrApplication("Application", "message.email.sender.system");
        return BundleUtil.getMessageFromModuleOrApplication("Application", "message.email.sender.template",
                Unit.getInstitutionAcronym(), fenixSystem);
    }

    @Deprecated
    public boolean hasSystemRootDomainObject() {
        return getSystemRootDomainObject() != null;
    }

}
