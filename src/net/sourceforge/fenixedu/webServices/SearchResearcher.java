package net.sourceforge.fenixedu.webServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.ResearcherDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.Researcher;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchResearcher implements ISearchResearcher {

    @Override
    public ResearcherDTO[] seacherByName(String name) {
	SearchParameters parameters = new SearchPerson.SearchParameters(name, null, null, null, null, RoleType.RESEARCHER
		.toString(), null, null, null, Boolean.TRUE, null, Boolean.FALSE, Boolean.TRUE);

	final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(parameters);

	CollectionPager<Person> result = null;
	try {
	    result = (CollectionPager<Person>) ServiceUtils
		    .executeService("SearchPerson", new Object[] { parameters, predicate });
	} catch (FenixFilterException e) {
	    e.printStackTrace();
	} catch (FenixServiceException e) {
	    e.printStackTrace();
	}

	List<Researcher> results = new ArrayList<Researcher>();
	for (Person person : result.getCollection()) {
	    results.add(person.getResearcher());
	}
	Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);

	return getArrayFromResearchersList(results);
    }

    @Override
    public ResearcherDTO[] searchByKeyword(String keywords) {
	if (keywords != null) {
	    List<Researcher> results = new ArrayList<Researcher>();
	    for (Researcher researcher : RootDomainObject.getInstance().getResearchers()) {
		if (researcher.getAllowsToBeSearched() && researcher.hasAtLeastOneKeyword(keywords.split(" "))) {
		    results.add(researcher);
		}
	    }
	    Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
	    return getArrayFromResearchersList(results);
	}

	return new ResearcherDTO[0];
    }

    private ResearcherDTO[] getArrayFromResearchersList(List<Researcher> results) {
	ResearcherDTO[] returnResults = new ResearcherDTO[results.size()];
	int i = 0;
	for (Researcher researcher : results)
	    returnResults[i++] = new ResearcherDTO(researcher);

	return returnResults;
    }

}
