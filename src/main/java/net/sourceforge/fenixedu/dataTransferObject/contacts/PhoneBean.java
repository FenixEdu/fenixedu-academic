package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.PhoneUtil;

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
    @Atomic
    public Boolean edit() {
        boolean isValueChanged = super.edit();
        if (isValueChanged) {
            if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
                ((Phone) getContact()).edit(getValue());
            }
        }
        return isValueChanged;
    }

    @Override
    @Checked("RolePredicates.PARTY_CONTACT_BEAN_PREDICATE")
    public PartyContact createNewContact() {
        return Phone.createPhone(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }

    @Override
    public String getValidationMessageKey() {
        if (PhoneUtil.isMobileNumber(getValue())) {
            return "label.contact.validation.message.MobilePhone";
        }
        if (PhoneUtil.isFixedNumber(getValue())) {
            return "label.contact.validation.message.Phone";
        }
        return super.getValidationMessageKey();
    }

    @Override
    public boolean isValueChanged() {
        return !getValue().equals(((Phone) getContact()).getNumber());
    }
}
