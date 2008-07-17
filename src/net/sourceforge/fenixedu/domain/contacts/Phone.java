package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.commons.lang.StringUtils;

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
	}
    };

    protected Phone() {
	super();
    }

    public Phone(final Party party, final PartyContactType type, final boolean defaultContact, final String number) {
	this();
	super.init(party, type, defaultContact);
	checkParameters(number);
	super.setNumber(number);
    }

    public Phone(final Party party, final PartyContactType type, final boolean visibleToPublic, final boolean visibleToStudents,
	    final boolean visibleToTeachers, final boolean visibleToEmployees, final boolean defaultContact, final String number) {
	this();
	super.init(party, type, visibleToPublic, visibleToStudents, visibleToTeachers, visibleToEmployees, defaultContact);
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

    @Override
    protected void checkRulesToDelete() {
	if (getParty().getPartyContacts(getClass()).size() == 1) {
	    throw new DomainException("error.domain.contacts.Phone.cannot.remove.last.phone");
	}
    }

    @Override
    public String getPresentationValue() {
	return getNumber();
    }
}
