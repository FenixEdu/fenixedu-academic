package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class SearchSimilarPersonsForOver23CandidacyProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) source;
	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
	Set<Person> result = new HashSet<Person>(Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber()));
	result.addAll(Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(), Person
		.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())));
	filterPersonsWithStudent(result);
	return result;
    }

    private void filterPersonsWithStudent(final Collection<Person> persons) {
	final Iterator<Person> personsIter = persons.iterator();
	while (personsIter.hasNext()) {
	    if (personsIter.next().hasStudent()) {
		personsIter.remove();
	    }
	}
    }
}
