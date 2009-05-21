package net.sourceforge.fenixedu.domain.util.email;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

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
	return new Message(this, this.getConcreteReplyTos(), recipients, subject, body, bccs);
    }

    @Service
    public Message newMessage(final Collection<Recipient> recipients, final String subject, final String body,
	    final Set<String> bccs) {
	return new Message(this, this.getConcreteReplyTos(), recipients, subject, body, bccs);
    }

    @Service
    public Message newMessage(final Recipient recipient, final String subject, final String body, final String bccs) {
	return new Message(this, this.getConcreteReplyTos(), Collections.singletonList(recipient), subject, body, bccs);
    }
}
