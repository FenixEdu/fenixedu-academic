package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;
import java.util.List;

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
    
    protected EmailAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
        this();
        super.init(party, type, visible, defaultContact);
    }
    
    public EmailAddress(final Party party, final PartyContactType type, final Boolean defaultContact, final String value) {
	this(party, type, true, defaultContact.booleanValue(), value);
    }
    
    public EmailAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String value) {
	this();
	init(party, type, visible, defaultContact, value);
    }
    
    protected void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String value) {
	super.init(party, type, visible, defaultContact);
	checkParameters(value);
	super.setValue(value);
    }
    
    private void checkParameters(final String value) {
	if (!EmailSender.emailAddressFormatIsValid(value)) {
	    throw new DomainException("error.domain.contacts.EmailAddress.invalid.format", value);
	}
    }
    
    @Override
    public void setType(PartyContactType type) {
        checkEmailType(type);
        super.setType(type);
    }
    
    private void checkEmailType(PartyContactType type) {
	if (type == PartyContactType.INSTITUTIONAL) {
	    final List<PartyContact> contacts = (List<PartyContact>) getParty().getPartyContacts(getClass(), type);
	    contacts.remove(this);
	    if (!contacts.isEmpty()) {
		throw new DomainException("error.domain.contacts.EmailAddress.can.only.have.one.institutional.emailAddress");
	    }
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
    
    public void edit(final PartyContactType type, final Boolean defaultContact, final String value) {
	super.edit(type, true, defaultContact);
	edit(value);
    }

    @Override
    protected void checkRulesToDelete() {
	if (isInstitutionalType()) {
	    throw new DomainException("error.domain.contacts.EmailAddress.cannot.delete.institution.emailAddress", getValue());
	}
	if (getParty().getPartyContacts(getClass()).size() == 1) {
	    throw new DomainException("error.domain.contacts.EmailAddress.cannot.remove.last.emailAddress");
	}
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
