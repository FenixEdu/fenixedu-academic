package net.sourceforge.fenixedu.domain.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class EmailAddressList implements Serializable {

    private final String emailAddresses;

    public EmailAddressList(final String emailAddresses) {
	super();
	this.emailAddresses = emailAddresses;
    }

    public EmailAddressList(final Collection<String> emailAddressCollection) {
	super();
	final StringBuilder emailAddresses = new StringBuilder();
	if (emailAddressCollection != null) {
	    for (final String emailAddress : emailAddressCollection) {
		if (emailAddresses.length() > 0) {
		    emailAddresses.append(", ");
		}
		emailAddresses.append(emailAddress);
	    }
	}
	this.emailAddresses = emailAddresses.toString();
    }

    @Override
    public String toString() {
	return emailAddresses;
    }

    public String[] toArray() {
	return emailAddresses.split(", ");
    }

    public Collection<String> toCollection() {
	final Collection<String> collection = new ArrayList<String>();
	for (final String emailAddress : toArray()) {
	    collection.add(emailAddress);
	}
	return collection;
    }

}
