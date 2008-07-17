package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class WebAddressBean extends PartyContactBean {

    private static final String CONTACT_NAME = "WebAddress";

    private static final long serialVersionUID = 232589021187587867L;

    public WebAddressBean(Party party) {
	super(party);
    }

    public WebAddressBean(WebAddress partyContact) {
	super(partyContact);
	setValue(partyContact.getUrl());
    }

    @Override
    public String getContactName() {
	return CONTACT_NAME;
    }

    @Override
    public void edit() {
	super.edit();
	((WebAddress) getContact()).setUrl(getValue());
    }

    @Override
    public void createNewContact() {
	new WebAddress(getParty(), getType(), getVisibleToPublic(), getVisibleToStudents(), getVisibleToTeachers(),
		getVisibleToEmployees(), getDefaultContact(), getValue());
    }
}
