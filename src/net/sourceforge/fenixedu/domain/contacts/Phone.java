package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

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
    
    public Phone(final Party party, final PartyContactType type, final Boolean visible, final String number) {
        this(party, type, visible, false, number);
    }
    
    public Phone(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
        this();
        super.init(party, type, visible, defaultContact);
    }
    
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
    
    public void edit(final String number) {
	super.setNumber(number);
    }
}
