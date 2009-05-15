package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class PhoneBean extends PartyContactBean {

    private static final String CONTACT_NAME = "Phone";

    private static final long serialVersionUID = 7318803302492544891L;

    public PhoneBean(Party party) {
	super(party);
    }

    public PhoneBean(Phone partyContact) {
	super(partyContact);
	setValue(partyContact.getNumber());
    }

    @Override
    public String getContactName() {
	return CONTACT_NAME;
    }

    @Override
    public void edit() {
	super.edit();
	if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
	    ((Phone) getContact()).setNumber(getValue());
	}
    }

    @Override
    public void createNewContact() {
	Phone.createPhone(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
		getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }
}
