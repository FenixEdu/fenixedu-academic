package net.sourceforge.fenixedu.dataTransferObject.contacts;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public abstract class PartyContactBean implements Serializable {

    private Party party;

    private PartyContact contact;

    private String value;

    private PartyContactType type;

    private Boolean defaultContact = Boolean.FALSE;

    private Boolean visibleToPublic = Boolean.FALSE;

    private Boolean visibleToStudents = Boolean.FALSE;

    private Boolean visibleToTeachers = Boolean.FALSE;

    private Boolean visibleToEmployees = Boolean.FALSE;

    private Boolean visibleToAlumni = Boolean.FALSE;

    private Boolean visibleToManagement = Boolean.TRUE;

    public PartyContactBean(Party party) {
	setParty(party);
    }

    public PartyContactBean(PartyContact partyContact) {
	init(partyContact);
    }

    private void init(PartyContact partyContact) {
	setParty(partyContact.getParty());
	setContact(partyContact);
	setType(partyContact.getType());
	setDefaultContact(partyContact.getDefaultContact());
	setVisibleToPublic(partyContact.getVisibleToPublic());
	setVisibleToStudents(partyContact.getVisibleToStudents());
	setVisibleToTeachers(partyContact.getVisibleToTeachers());
	setVisibleToEmployees(partyContact.getVisibleToEmployees());
	setVisibleToAlumni(partyContact.getVisibleToAlumni());
    }

    public Party getParty() {
	return this.party;
    }

    public void setParty(Party party) {
	this.party = party;
    }

    public PartyContact getContact() {
	return this.contact;
    }

    public void setContact(PartyContact contact) {
	this.contact = contact;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public PartyContactType getType() {
	return type;
    }

    public void setType(PartyContactType type) {
	this.type = type;
    }

    public Boolean getDefaultContact() {
	return defaultContact;
    }

    public void setDefaultContact(Boolean defaultContact) {
	this.defaultContact = defaultContact;
    }

    public Boolean getVisibleToPublic() {
	return visibleToPublic;
    }

    public void setVisibleToPublic(Boolean visibleToPublic) {
	this.visibleToPublic = visibleToPublic;
    }

    public Boolean getVisibleToStudents() {
	return visibleToStudents;
    }

    public void setVisibleToStudents(Boolean visibleToStudents) {
	this.visibleToStudents = visibleToStudents;
    }

    public Boolean getVisibleToTeachers() {
	return visibleToTeachers;
    }

    public void setVisibleToTeachers(Boolean visibleToTeachers) {
	this.visibleToTeachers = visibleToTeachers;
    }

    public Boolean getVisibleToEmployees() {
	return visibleToEmployees;
    }

    public void setVisibleToEmployees(Boolean visibleToEmployees) {
	this.visibleToEmployees = visibleToEmployees;
    }

    public Boolean getVisibleToAlumni() {
	return visibleToAlumni;
    }

    public void setVisibleToAlumni(Boolean visibleToAlumni) {
	this.visibleToAlumni = visibleToAlumni;
    }

    public Boolean getVisibleToManagement() {
	return visibleToManagement;
    }

    public void setVisibleToManagement(Boolean visibleToManagement) {
	this.visibleToManagement = visibleToManagement;
    }

    public abstract String getContactName();

    public abstract PartyContact createNewContact();

    public boolean isInstitutional() {
	return getType().equals(PartyContactType.INSTITUTIONAL);
    }

    public Boolean edit() {
	boolean isValueChanged = isValueChanged();
	if (isValueChanged) {
	    PartyContact newPartyContact = createNewContact();
	    newPartyContact.setPrevPartyContact(getContact());
	    setContact(newPartyContact);
	}
	if (!isInstitutional()) {
	    getContact().setType(getType());
	}
	final boolean isDefault = getDefaultContact().booleanValue();

	if (isValueChanged) {
	    getContact().getPartyContactValidation().setToBeDefault(isDefault);
	} else {
	    getContact().setDefaultContact(isDefault);
	}
	getContact().setVisibleToPublic(getVisibleToPublic());
	getContact().setVisibleToStudents(getVisibleToStudents());
	getContact().setVisibleToTeachers(getVisibleToTeachers());
	getContact().setVisibleToEmployees(getVisibleToEmployees());
	getContact().setVisibleToAlumni(getVisibleToAlumni());
	return isValueChanged;
    }

    public boolean hasPartyContact() {
	/*
	 * if (getContact() != null && isValueChanged()) { return
	 * getParty().hasPartyContact(getContact().getClass(), getType(),
	 * getValue()); } return false;
	 */
	return false;
    }

    public static PartyContactBean createFromDomain(PartyContact partyContact) {
	if (partyContact instanceof Phone)
	    return new PhoneBean((Phone) partyContact);
	if (partyContact instanceof MobilePhone)
	    return new MobilePhoneBean((MobilePhone) partyContact);
	if (partyContact instanceof EmailAddress)
	    return new EmailAddressBean((EmailAddress) partyContact);
	if (partyContact instanceof WebAddress)
	    return new WebAddressBean((WebAddress) partyContact);
	if (partyContact instanceof PhysicalAddress)
	    return new PhysicalAddressBean((PhysicalAddress) partyContact);
	return null;
    }

    public abstract boolean isValueChanged();

    public String getValidationMessageKey() {
	return "label.contact.validation.message." + getContact().getClass().getSimpleName();
    }
}
