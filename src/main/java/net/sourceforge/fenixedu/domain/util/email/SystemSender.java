package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
        super();
        setMembers(RoleGroup.get(RoleType.MANAGER));
        setFromAddress(Sender.getNoreplyMail());
        setSystemRootDomainObject(getRootDomainObject());
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), "Sistema Fénix");
    }

    public Recipient getRoleRecipient(RoleType roleType) {
        final Group roleGroup = RoleGroup.get(roleType);
        for (Recipient recipient : getRecipientsSet()) {
            final Group members = recipient.getMembers();
            if (roleGroup.equals(members)) {
                return recipient;
            }
        }
        return createRoleRecipient(roleGroup);
    }

    @Atomic(mode = TxMode.WRITE)
    private Recipient createRoleRecipient(Group roleGroup) {
        final Recipient recipient = new Recipient(roleGroup);
        addRecipients(recipient);
        return recipient;
    }

}
