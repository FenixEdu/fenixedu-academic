package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.research.Researcher;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PartyContactsForResearcher implements DataProvider {

    public Converter getConverter() {
	return null;
    }

    public Object provide(Object source, Object currentValue) {
	Person person = ((Researcher) source).getPerson();
	List<PartyContact> contacts = new ArrayList<PartyContact>();
	contacts.addAll(person.getPartyContacts(Phone.class));
	contacts.addAll(person.getPartyContacts(EmailAddress.class));
	contacts.addAll(person.getPartyContacts(MobilePhone.class));
	return contacts;
    }

}
