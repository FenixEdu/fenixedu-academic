package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;

import org.apache.commons.lang.RandomStringUtils;

import pt.utl.ist.fenix.tools.util.PhoneUtil;

public class PhoneValidation extends PhoneValidation_Base {

    public PhoneValidation(PartyContact contact) {
	super();
	super.init(contact);
	assert (contact instanceof Phone || contact instanceof MobilePhone);
	setToken(RandomStringUtils.random(4, false, true));
    }

    public String getNumber() {
	final PartyContact partyContact = getPartyContact();
	if (partyContact instanceof Phone) {
	    return ((Phone) partyContact).getNumber();
	}
	if (partyContact instanceof MobilePhone) {
	    return ((MobilePhone) partyContact).getNumber();
	}
	return null;
    }

    @Override
    public void triggerValidationProcess() {
	if (!isValid()) {
	    final String number = getNumber();
	    final String token = getToken();
	    final Person person = (Person) getPartyContact().getParty();
	    final Country country = person.getCountry();
	    final String language = Country.isCPLPCountry(country) ? "pt" : "en";

	    if (PhoneUtil.isFixedNumber(number) || !PhoneUtil.isPortugueseNumber(number)) {
		PhoneValidationUtils.getInstance().makeCall(PhoneUtil.getInternacionalFormatNumber(number), token, language);
	    } else if (PhoneUtil.isMobileNumber(number)) {
		PhoneValidationUtils.getInstance().sendSMS(PhoneUtil.getInternacionalFormatNumber(number), token);
	    }

	    person.incValidationRequest();
	}
    }
}
