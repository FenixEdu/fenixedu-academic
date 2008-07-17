package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.commons.lang.StringUtils;

public class MobilePhone extends MobilePhone_Base {

    public static Comparator<MobilePhone> COMPARATOR_BY_NUMBER = new Comparator<MobilePhone>() {
	public int compare(MobilePhone contact, MobilePhone otherContact) {
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

    protected MobilePhone() {
	super();
    }

    public MobilePhone(final Party party, final PartyContactType type, final boolean defaultContact, final String number) {
	this();
	super.init(party, type, defaultContact);
	checkParameters(number);
	super.setNumber(number);
    }

    public MobilePhone(final Party party, final PartyContactType type, final boolean visibleToPublic,
	    final boolean visibleToStudents, final boolean visibleToTeachers, final boolean visibleToEmployees,
	    final boolean defaultContact, final String number) {
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
    public boolean isMobile() {
	return true;
    }

    public void edit(final String number) {
	super.setNumber(number);
    }

    @Override
    public String getPresentationValue() {
	return getNumber();
    }
}
