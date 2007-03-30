package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public abstract class PartyContact extends PartyContact_Base {
    
    public static Comparator<PartyContact> COMPARATOR_BY_TYPE = new Comparator<PartyContact>() {
	public int compare(PartyContact contact, PartyContact otherContact) {
	    int result = contact.getType().compareTo(otherContact.getType());
	    return (result == 0) ? contact.getIdInternal().compareTo(otherContact.getIdInternal()) : result;
	}};
    
    protected PartyContact() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setOjbConcreteClass(getClass().getName());
    }
    
    protected void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
	
	checkParameters(party, type);
	
	super.setParty(party);
	super.setType(type);
	super.setVisible(Boolean.valueOf(visible));
	
	setDefaultContactInformation(defaultContact);
    }
    
    private void setDefaultContactInformation(final boolean defaultContact) {
	if (defaultContact) {
	    changeToDefault();
	} else {
	    if (getParty().hasDefaultPartyContact(getClass())) {
		super.setDefaultContact(Boolean.FALSE);
	    } else {
		super.setDefaultContact(Boolean.TRUE);
	    }
	}
    }

    private void checkParameters(final Party party, final PartyContactType type) {
	if (party == null) {
	    throw new DomainException("error.contacts.PartyContact.party.cannot.be.null");
	}
	if (type == null) {
	    throw new DomainException("error.contacts.PartyContact.contactType.cannot.be.null");
	}
    }
    
    public void edit(final PartyContactType type, final boolean visible, final boolean defaultContact) {
	
	checkParameters(getParty(), type);
	
	super.setType(type);
	super.setVisible(Boolean.valueOf(visible));
	
	setDefaultContactInformation(defaultContact);
    }
    
    public void changeToDefault() {
	final PartyContact defaultPartyContact = getParty().getDefaultPartyContact(getClass());
	if (defaultPartyContact != null) {
	    defaultPartyContact.setDefaultContact(Boolean.FALSE);
	}
	super.setDefaultContact(Boolean.TRUE);
    }
    
    public boolean isDefault() {
	return hasDefaultContactValue() && getDefaultContact().booleanValue();
    }
    
    private boolean hasDefaultContactValue() {
	return getDefaultContact() != null;
    }
    
    public boolean isContactVisible() {
	return getVisible().booleanValue();
    }
    
    public boolean isWebAddress() {
	return false;
    }
    
    public boolean isPhysicalAddress() {
	return false;
    }
    
    public boolean isEmailAddress() {
	return false;
    }
    
    public boolean isPhone() {
	return false;
    }
    
    public boolean isMobile() {
	return false;
    }
    
    public void delete() {
	setAnotherContactAsDefault();
	removeParty();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
    private void setAnotherContactAsDefault() {
	if (isDefault()) {
	    final List<PartyContact> contacts = (List<PartyContact>) getParty().getPartyContacts(getClass());
	    if (!contacts.isEmpty()) {
		contacts.get(0).setDefaultContact(Boolean.TRUE);
	    }
	}
    }

    static public WebAddress createDefaultPersonalWebAddress(final Party party) {
	return new WebAddress(party, PartyContactType.PERSONAL, true, true);
    }
    
    static public WebAddress createWebAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String url) {
	return new WebAddress(party, type, visible, defaultContact, url);
    }
    
    static public PhysicalAddress createDefaultPersonalPhysicalAddress(final Party party) {
	return new PhysicalAddress(party, PartyContactType.PERSONAL, true, true);
    }
    
    static public PhysicalAddress createPhysicalAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final PhysicalAddressData data) {
	return new PhysicalAddress(party, type, visible, defaultContact, data);
    }
    
    static public Phone createDefaultPersonalPhone(final Party party) {
	return new Phone(party, PartyContactType.PERSONAL, true, true);
    }
    
    static public Phone createPhone(final Party party, final PartyContactType type, boolean visible, boolean defaultContact, final String number) {
	return new Phone(party, type, visible, defaultContact, number);
    }

    static public MobilePhone createDefaultPersonalMobilePhone(final Party party) {
	return new MobilePhone(party, PartyContactType.PERSONAL, true, true);
    }
    
    static public MobilePhone createMobilePhone(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String number) {
	return new MobilePhone(party, type, visible, defaultContact, number);
    }
    
    static public EmailAddress createEmailAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String value) {
	return new EmailAddress(party, type, visible, defaultContact, value);
    }
    
}
