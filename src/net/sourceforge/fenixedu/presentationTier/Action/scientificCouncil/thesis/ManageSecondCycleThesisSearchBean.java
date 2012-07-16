package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.CollectionPager;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public class ManageSecondCycleThesisSearchBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear;

    private String searchString;

    public ManageSecondCycleThesisSearchBean() {
	this(null);
    }

    public ManageSecondCycleThesisSearchBean(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	} else {
	    setExecutionYear(executionYear);
	}
    }

    public ExecutionYear getExecutionYear() {
	return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public String getSearchString() {
	return searchString;
    }

    public void setSearchString(String searchString) {
	this.searchString = searchString;
    }


    public SortedSet<Person> findPersonBySearchString() {
	final SortedSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
	if (searchString != null && !searchString.isEmpty()) {
	    result.addAll(searchName(searchString));
	    result.addAll(searchUsername(searchString));
	    result.addAll(searchStudentNumber(searchString));
	}
	return result;
    }

    private Collection<Person> searchName(final String name) {
	final SearchParameters searchParameters = new SearchParameters();
	searchParameters.setName(name);
	return search(searchParameters);
    }

    private Collection<Person> searchUsername(final String username) {
	final SearchParameters searchParameters = new SearchParameters();
	searchParameters.setUsername(username);
	return search(searchParameters);
    }

    private Collection<Person> searchStudentNumber(final String number) {
	if (StringUtils.isNumeric(number)) {
	    final SearchParameters searchParameters = new SearchParameters();
	    searchParameters.setStudentNumber(new Integer(number));
	    return search(searchParameters);
	}
	return Collections.emptySet();
    }

    private Collection<Person> search(final SearchParameters searchParameters) {
	final SearchPersonPredicate searchPersonPredicate = new SearchPerson.SearchPersonPredicate(searchParameters);
	SearchPerson searchPerson = new SearchPerson();
	final CollectionPager<Person> people = searchPerson.run(searchParameters, searchPersonPredicate);
	return people.getCollection();
    }

}
