package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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

    public Recipient getRoleRecipient(RoleType roleType) {
        final RoleGroup roleGroup = new RoleGroup(roleType);
        for (Recipient recipient : getRecipientsSet()) {
            final Group members = recipient.getMembers();
            if (members != null && members instanceof RoleGroup) {
                if (members.equals(roleGroup)) {
                    return recipient;
                }
            }
        }
        return createRoleRecipient(roleGroup);
    }

    @Atomic(mode = TxMode.WRITE)
    private Recipient createRoleRecipient(RoleGroup roleGroup) {
        final Recipient recipient = new Recipient(roleGroup);
        addRecipients(recipient);
        return recipient;
    }

}
