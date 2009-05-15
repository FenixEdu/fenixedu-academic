package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

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
    public void edit() {
	super.edit();
	((EmailAddress) getContact()).edit(getValue());
    }

    @Override
    public void createNewContact() {
	EmailAddress.createEmailAddress(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
		getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }
}
