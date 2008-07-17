package net.sourceforge.fenixedu.dataTransferObject.contacts;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public abstract class PartyContactBean implements Serializable {

    private DomainReference<Party> party;

    private DomainReference<PartyContact> contact;

    private String value;

    private PartyContactType type;

    private Boolean defaultContact = Boolean.FALSE;

    private Boolean visibleToPublic = Boolean.FALSE;

    private Boolean visibleToStudents = Boolean.FALSE;

    private Boolean visibleToTeachers = Boolean.FALSE;

    private Boolean visibleToEmployees = Boolean.FALSE;

    private Boolean visibleToManagement = Boolean.TRUE;

    public PartyContactBean(Party party) {
	setParty(party);
    }

    public PartyContactBean(PartyContact partyContact) {
	setParty(partyContact.getParty());
	setContact(partyContact);
	setType(partyContact.getType());
	setDefaultContact(partyContact.getDefaultContact());
	setVisibleToPublic(partyContact.getVisibleToPublic());
	setVisibleToStudents(partyContact.getVisibleToStudents());
	setVisibleToTeachers(partyContact.getVisibleToTeachers());
	setVisibleToEmployees(partyContact.getVisibleToEmployees());
    }

    public Party getParty() {
	return (this.party != null) ? this.party.getObject() : null;
    }

    public void setParty(Party party) {
	this.party = (party != null) ? new DomainReference<Party>(party) : null;
    }

    public PartyContact getContact() {
	return (this.contact != null) ? this.contact.getObject() : null;
    }

    public void setContact(PartyContact contact) {
	this.contact = (contact != null) ? new DomainReference<PartyContact>(contact) : null;
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

    public Boolean getVisibleToManagement() {
	return visibleToManagement;
    }

    public void setVisibleToManagement(Boolean visibleToManagement) {
	this.visibleToManagement = visibleToManagement;
    }

    public abstract String getContactName();

    public abstract void createNewContact();

    public void edit() {
	PartyContact domainContact = getContact();
	domainContact.edit(getType(), getDefaultContact().booleanValue());
	domainContact.setVisibleToPublic(getVisibleToPublic());
	domainContact.setVisibleToStudents(getVisibleToStudents());
	domainContact.setVisibleToTeachers(getVisibleToTeachers());
	domainContact.setVisibleToEmployees(getVisibleToEmployees());
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
	return null;
    }
}
