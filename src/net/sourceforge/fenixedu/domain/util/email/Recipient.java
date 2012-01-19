package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import pt.ist.fenixWebFramework.services.Service;

public class Recipient extends Recipient_Base {

    public static final Comparator<Recipient> COMPARATOR_BY_NAME = new Comparator<Recipient>() {

	public int compare(Recipient r1, Recipient r2) {
	    final int c = r1.getToName().compareTo(r2.getToName());
	    return c == 0 ? COMPARATOR_BY_ID.compare(r1, r2) : c;
	}

    };

    public Recipient() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Recipient(final Group group) {
	this(group.getName(), group);
    }

    public Recipient(final Collection<Person> persons) {
	this(new FixedSetGroup(persons));
    }

    public Recipient(final String toName, final Group members) {
	this();
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
	    final EmailAddress emailAddress = person.getEmailAddressForSendingEmails();
	    if (emailAddress != null) {
		final String value = emailAddress.getValue();
		if (value != null && !value.isEmpty()) {
		    emailAddresses.add(value);
		}
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

    @Service
    public static Recipient newInstance(final String toName, final Group members) {
	return new Recipient(toName, members);
    }

    @Service
    public static Recipient newInstance(Group group) {
	return new Recipient(group);
    }

    @Service
    public static List<Recipient> newInstance(final List<? extends Group> groups) {
	List<Recipient> recipients = new ArrayList<Recipient>();
	for (Group group : groups) {
	    recipients.add(new Recipient(group));
	}
	return recipients;
    }

    public static Set<Recipient> newInstance(final Set<? extends Group> groups) {
	Set<Recipient> recipients = new HashSet<Recipient>();
	for (Group group : groups) {
	    recipients.add(new Recipient(group));
	}
	return recipients;
    }

    public Collection<Recipient> asCollection() {
	return Collections.singletonList(this);
    }

    public String getMembersEmailInText() {
	if (getMembers() == null) {
	    return "";
	}

	StringBuilder builder = new StringBuilder();
	Group membersGroup = getMembers();

	Set<Person> elements = membersGroup.getElements();

	for (Person person : elements) {
	    builder.append(person.getName()).append(" (").append(person.getEmailForSendingEmails()).append(")").append("\n");
	}

	return builder.toString();
    }

}
