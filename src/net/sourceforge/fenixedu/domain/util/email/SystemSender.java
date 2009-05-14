package net.sourceforge.fenixedu.domain.util.email;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
	super();
	setMembers(new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER)));
	setFromAddress("noreply@ist.utl.pt");
	setFromName("Sistema Fénix");
	setSystemRootDomainObject(getRootDomainObject());
    }

    @Service
    public Message newMessage(final Collection<Recipient> recipients, final String subject, final String body, final String bccs) {
	return new Message(this, this.getReplyTos(), recipients, subject, body, bccs);
    }
}
