package net.sourceforge.fenixedu.domain.contacts;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class Phone extends Phone_Base {
    
    protected Phone() {
        super();
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
    
}
