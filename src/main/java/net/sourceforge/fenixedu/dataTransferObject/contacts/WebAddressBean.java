package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.services.Service;

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
    @Service
    public Boolean edit() {
        final boolean isValueChanged = super.edit();
        if (isValueChanged) {
            if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
                ((WebAddress) getContact()).edit(getValue());
            }
        }
        return isValueChanged;
    }

    @Override
    public PartyContact createNewContact() {
        return WebAddress.createWebAddress(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }

    @Override
    public boolean isValueChanged() {
        return !((WebAddress) getContact()).getUrl().equals(getValue());
    }
}
