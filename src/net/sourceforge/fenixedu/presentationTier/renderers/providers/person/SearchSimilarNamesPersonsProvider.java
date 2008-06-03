/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SearchSimilarNamesPersonsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	ChoosePersonBean choosePersonBean = (ChoosePersonBean) source;
	Set<Person> result = new HashSet<Person>(Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber()));
	result.addAll(Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(), Person
		.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())));
	return result;
    }

}
