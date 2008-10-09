package net.sourceforge.fenixedu.domain.util.email;

import java.util.Set;

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

    public void delete() {
	for (final Sender sender : getSendersSet()) {
	    removeSenders(sender);
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void addDestinationEmailAddresses(final Set<String> emailAddresses) {
	final EmailAddressThread thread = new EmailAddressThread(getMembers(), emailAddresses);
	thread.start();
	try {
	    thread.join();
	} catch (InterruptedException e) {
	    throw new Error(e);
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
