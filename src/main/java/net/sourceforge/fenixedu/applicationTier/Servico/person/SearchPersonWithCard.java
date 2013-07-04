package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchPersonWithCard extends SearchPerson {

    @Override
    public CollectionPager<Person> run(SearchParameters searchParameters, Predicate predicate) {
        CollectionPager<Person> acquiredPersons = super.run(searchParameters, predicate);

        if (searchParameters.emptyParameters()) {
            return acquiredPersons;
        }

        final Collection<Person> persons;

        if (searchParameters.getName() != null) {
            persons = new ArrayList<Person>();
            persons.addAll(findPersons(searchParameters.getName()));
        } else {
            persons = new ArrayList<Person>(0);
        }

        acquiredPersons.getCollection().addAll(persons);
        TreeSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
        result.addAll(acquiredPersons.getCollection());
        return new CollectionPager<Person>(result, acquiredPersons.getMaxElementsPerPage());
    }

    private Collection<Person> findPersons(String searchName) {
        Collection<Person> persons = new ArrayList<Person>();

        for (CardGenerationEntry cardGenerationEntry : rootDomainObject.getCardGenerationEntriesSet()) {
            if (cardGenerationEntry.getPerson() != null) {
                if (matchableSearchNameAndCardName(searchName, cardGenerationEntry.getLine().substring(178).trim().toLowerCase())) {
                    persons.add(cardGenerationEntry.getPerson());
                }
            }
        }
        return persons;
    }

    private boolean matchableSearchNameAndCardName(String searchName, String cardName) {
        final String[] nameParts = PersonNamePart.getNameParts(searchName);
        if (nameParts.length > 0) {
            for (final String namePart : nameParts) {
                if (cardName.indexOf(namePart) == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}