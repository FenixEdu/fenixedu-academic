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
		final String emailAddressTrimmed = emailAddress.trim();
		if (emailAddressTrimmed.length() > 0) {
		    emailAddresses.append(", ");
		}
		emailAddresses.append(emailAddressTrimmed);
	    }
	}
	this.emailAddresses = emailAddresses.length() == 0 ? null : emailAddresses.toString();
    }

    @Override
    public String toString() {
	return emailAddresses;
    }

    public String[] toArray() {
	return emailAddresses == null ? null : emailAddresses.split(", ");
    }

    public Collection<String> toCollection() {
	if (emailAddresses == null) {
	    return null;
	}
	final Collection<String> collection = new ArrayList<String>();
	for (final String emailAddress : toArray()) {
	    collection.add(emailAddress);
	}
	return collection;
    }

}
