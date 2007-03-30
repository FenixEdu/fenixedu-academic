package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class EmailAddress extends EmailAddress_Base {
    
    public static Comparator<EmailAddress> COMPARATOR_BY_EMAIL = new Comparator<EmailAddress>() {
	public int compare(EmailAddress contact, EmailAddress otherContact) {
	    final String value = contact.getValue();
	    final String otherValue = otherContact.getValue();
	    int result = 0;
	    if (value != null && otherValue != null) {
		result = value.compareTo(otherValue);
	    } else if (value != null) {
		result = 1;
	    } else if (otherValue != null) {
		result = -1;
	    }
	    return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
	}};
    
    protected EmailAddress() {
        super();
    }
    
    public EmailAddress(final Party party, final PartyContactType type, final Boolean visible, final String value) {
        this(party, type, visible, false, value);
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
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
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.contacts.EmailAddress.invalid.format", value);
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
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public void edit(final String value) {
	super.setValue(value);
    }

    @Override
    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.contacts.EmailAddress.cannot.delete.emailAddress", getValue());
	}
        super.delete();
    }

    private boolean canBeDeleted() {
	return !isInstitutionalType();
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
