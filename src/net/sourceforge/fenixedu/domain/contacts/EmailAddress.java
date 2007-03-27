package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class EmailAddress extends EmailAddress_Base {
    
    protected EmailAddress() {
        super();
    }
    
    public EmailAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String value) {
	this();
	init(party, type, visible, defaultContact, value);
    }
    
    public EmailAddress(final Party party, final PartyContactType type, final boolean defaultContact, final String value) {
	this(party, type, true, defaultContact, value);
    }
    
    protected void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String value) {
	super.init(party, type, visible, defaultContact);
	checkParameters(value);
	super.setValue(value);
    }
    
    private void checkParameters(final String value) {
	if (!EmailSender.emailAddressFormatIsValid(value)) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.contacts.EmailAddress.invalid.format");
	}
    }
    
    public boolean hasValue() {
	return getValue() != null;
    }
    
    public boolean hasValue(final String emailAddressString) {
	return hasValue() && getValue().equalsIgnoreCase(emailAddressString);
    }
    
    @Override
    public boolean isEmailAddress() {
        return true;
    }

    static public EmailAddress find(final String emailAddressString) {
	for (final PartyContact contact : RootDomainObject.getInstance().getPartyContactsSet()) {
	    if (contact.isEmailAddress()) {
		final EmailAddress emailAddress = (EmailAddress) contact;
		if (emailAddress.hasValue(emailAddressString)) {
		    return emailAddress;
		}
	    }
	}
	return null;
    }

}
