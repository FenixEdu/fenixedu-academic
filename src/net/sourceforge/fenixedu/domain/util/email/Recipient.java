package net.sourceforge.fenixedu.domain.util.email;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.ist.fenixWebFramework.services.Service;

public class Recipient extends Recipient_Base {

    public Recipient() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Recipient(final String toName, final Group members) {
	this();
	setRootDomainObject(RootDomainObject.getInstance());
	setToName(toName);
	setMembers(members);
    }

    @Service
    public void delete() {
	for (final Sender sender : getSendersSet()) {
	    removeSenders(sender);
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void addDestinationEmailAddresses(final Set<String> emailAddresses) {
	for (final Person person : getMembers().getElements()) {
	    final String emailAddress = person.getEmail();
	    if (emailAddress != null && !emailAddress.isEmpty()) {
		emailAddresses.add(emailAddress);
	    }
	}
    }

    @Override
    public String getToName() {
	if (super.getToName() == null) {
	    initToName();
	}
	return super.getToName();
    }

    @Service
    private void initToName() {
	setToName(getMembers().getName());
    }

    @Override
    public void setToName(final String toName) {
	super.setToName(toName);
    }

}
