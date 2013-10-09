package net.sourceforge.fenixedu.dataTransferObject.contacts;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

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
    @Atomic
    public Boolean edit() {
        boolean isValueChanged = super.edit();
        if (isValueChanged) {
            if (!getType().equals(PartyContactType.INSTITUTIONAL)) {
                ((MobilePhone) getContact()).edit(getValue());
            }
        }
        return isValueChanged;
    }

    @Override
    public PartyContact createNewContact() {
        check(this, RolePredicates.PARTY_CONTACT_BEAN_PREDICATE);
        return MobilePhone.createMobilePhone(getParty(), getValue(), getType(), getDefaultContact(), getVisibleToPublic(),
                getVisibleToStudents(), getVisibleToTeachers(), getVisibleToEmployees(), getVisibleToAlumni());
    }

    @Override
    public boolean isValueChanged() {
        return !((MobilePhone) getContact()).getNumber().equals(getValue());
    }
}
