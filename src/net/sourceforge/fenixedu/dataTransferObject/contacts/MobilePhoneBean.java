package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class MobilePhoneBean extends PartyContactBean {

    private static final String CONTACT_NAME = "MobilePhone";

    private static final long serialVersionUID = -17019887608353092L;

    public MobilePhoneBean(Party party) {
	super(party);
    }

    public MobilePhoneBean(MobilePhone partyContact) {
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
	((MobilePhone) getContact()).setNumber(getValue());
    }

    @Override
    public void createNewContact() {
	new MobilePhone(getParty(), getType(), getVisibleToPublic(), getVisibleToStudents(), getVisibleToTeachers(),
		getVisibleToEmployees(), getDefaultContact(), getValue());
    }
}
