package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class Phone extends Phone_Base {
    
    public static Comparator<Phone> COMPARATOR_BY_NUMBER = new Comparator<Phone>() {
	public int compare(Phone contact, Phone otherContact) {
	    final String number = contact.getNumber();
	    final String otherNumber = otherContact.getNumber();
	    int result = 0;
	    if (number != null && otherNumber != null) {
		result = number.compareTo(otherNumber);
	    } else if (number != null) {
		result = 1;
	    } else if (otherNumber != null) {
		result = -1;
	    }
	    return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
	}};
    
    protected Phone() {
        super();
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    protected Phone(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
        this();
        super.init(party, type, visible, defaultContact);
    }
    
    public Phone(final Party party, final PartyContactType type, final Boolean defaultContact, final String number) {
        this(party, type, true, defaultContact, number);
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public Phone(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String number) {
	this();
	super.init(party, type, visible, defaultContact);
	checkParameters(number);
	super.setNumber(number);
    }

    private void checkParameters(final String number) {
	if (StringUtils.isEmpty(number)) {
	    throw new DomainException("error.contacts.Phone.invalid.number");
	}
    }
    
    @Override
    public boolean isPhone() {
        return true;
    }
    
    @Checked("PartyContactPredicates.checkPermissionsToManage")
    public void edit(final String number) {
	super.setNumber(number);
    }
    
    public void edit(final PartyContactType type, final Boolean defaultContact, final String number) {
	super.edit(type, true, defaultContact);
	edit(number);
    }
    
    @Override
    protected void checkRulesToDelete() {
	if (getParty().getPartyContacts(getClass()).size() == 1) {
	    throw new DomainException("error.domain.contacts.Phone.cannot.remove.last.phone");
	}
    }
}
