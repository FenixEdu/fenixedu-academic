package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EmailAddressBean extends PartyContactBean {

    private static final String CONTACT_NAME = "EmailAddress";

    private static final long serialVersionUID = 8521361651957831331L;

    public EmailAddressBean(Party party) {
	super(party);
    }

    public EmailAddressBean(EmailAddress email) {
	super(email);
	setValue(email.getValue());
    }

    @Override
    public String getContactName() {
	return CONTACT_NAME;
    }

    @Override
    public EmailAddress getContact() {
	// TODO Auto-generated method stub
	return (EmailAddress) super.getContact();
    }

    @Override
    @Service
    public Boolean edit() {
	boolean isValueChanged = super.edit();
	if (isValueChanged) {
	    getContact().edit(getValue());
	}
	return isValueChanged;
    }

    @Override
    public boolean isValueChanged() {
	return !getValue().equals(getContact().getValue());
    }

    @Override
    @Checked("RolePredicates.PARTY_CONTACT_BEAN_PREDICATE")
    public PartyContact createNewContact() {
	return EmailAddress.createEmailAddress(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
		getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }

}
