package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class CardGenerationSearchDA extends FenixDispatchAction {

    public ActionForward search(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	SearchParameters searchParameters = (SearchParameters) getRenderedObject();
	if (searchParameters == null) {
	    searchParameters = new SearchPerson.SearchParameters();
	    final String name = request.getParameter("name");
	    if (name != null && name.length() > 0) {
		searchParameters.setName(name);
	    }
	    final String email = request.getParameter("email");
	    if (email != null && email.length() > 0) {
		searchParameters.setEmail(email);
	    }
	    final String username = request.getParameter("username");
	    if (username != null && username.length() > 0) {
		searchParameters.setUsername(username);
	    }
	    final String documentIdNumber = request.getParameter("documentIdNumber");
	    if (documentIdNumber != null && documentIdNumber.length() > 0) {
		searchParameters.setDocumentIdNumber(documentIdNumber);
	    }
	}
	request.setAttribute("searchParameters", searchParameters);

	if (!searchParameters.emptyParameters()) {
	    final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);
	    final Object[] args = { searchParameters, predicate };
	    final CollectionPager<Person> searchPersonCollectionPager = (CollectionPager<Person>) executeService(request, "SearchPerson", args);
	    request.setAttribute("searchPersonCollectionPager", searchPersonCollectionPager);
	    request.setAttribute("numberOfPages", searchPersonCollectionPager.getNumberOfPages());
	    final String pageNumberString = request.getParameter("pageNumber");
	    final Integer pageNumber = pageNumberString != null && pageNumberString.length() > 0 ? 
		    Integer.valueOf(pageNumberString) : Integer.valueOf(1);
	    request.setAttribute("pageNumber", pageNumber);
	    final Collection<Person> people = searchPersonCollectionPager.getPage(pageNumber.intValue());
	    request.setAttribute("people", people);
	}

	return mapping.findForward("showSearchPage");
    }

    public ActionForward viewPersonCards(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final String personIdString = request.getParameter("personId");
	final Integer personId = personIdString != null && personIdString.length() > 0 ? Integer.valueOf(personIdString) : null;
	if (personId != null) {
	    final Person person = (Person) rootDomainObject.readPartyByOID(personId);
	    request.setAttribute("person", person);
	}
	return mapping.findForward("viewPersonCards");
    }

    public ActionForward viewPersonCard(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final String cardGenerationEntryIdString = request.getParameter("cardGenerationEntryId");
	final Integer cardGenerationEntryId = cardGenerationEntryIdString != null && cardGenerationEntryIdString.length() > 0 ? Integer.valueOf(cardGenerationEntryIdString) : null;
	if (cardGenerationEntryId != null) {
	    final CardGenerationEntry cardGenerationEntry = rootDomainObject.readCardGenerationEntryByOID(cardGenerationEntryId);
	    request.setAttribute("cardGenerationEntry", cardGenerationEntry);
	    final Person person = cardGenerationEntry.getPerson();
	    request.setAttribute("person", person);
	}
	return mapping.findForward("viewPersonCards");
    }

}
