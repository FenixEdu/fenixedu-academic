package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPersonWithCard;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "identificationCardManager", path = "/searchPeople", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "viewPersonCards", path = "/identificationCardManager/viewPersonCards.jsp", tileProperties = @Tile(
                title = "private.identificationcards.search")),
        @Forward(name = "showSearchPage", path = "/identificationCardManager/searchPeople.jsp", tileProperties = @Tile(
                title = "private.identificationcards.search")) })
public class CardGenerationSearchDA extends FenixDispatchAction {

    public ActionForward search(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        SearchParameters searchParameters = getRenderedObject();
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
            final String mechanoGraphicalNumber = request.getParameter("mechanoGraphicalNumber");
            if (mechanoGraphicalNumber != null && mechanoGraphicalNumber.length() > 0 && mechanoGraphicalNumber.matches("[0-9]+")) {
                searchParameters.setMechanoGraphicalNumber(Integer.parseInt(mechanoGraphicalNumber));
            }
        }
        request.setAttribute("searchParameters", searchParameters);

        if (!searchParameters.emptyParameters()) {
            final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);
            final CollectionPager<Person> searchPersonCollectionPager =
                    SearchPersonWithCard.runSearchPersonWithCard(searchParameters, predicate);
            request.setAttribute("searchPersonCollectionPager", searchPersonCollectionPager);
            request.setAttribute("numberOfPages", searchPersonCollectionPager.getNumberOfPages());
            final String pageNumberString = request.getParameter("pageNumber");
            final Integer pageNumber =
                    pageNumberString != null && pageNumberString.length() > 0 ? Integer.valueOf(pageNumberString) : Integer
                            .valueOf(1);
            request.setAttribute("pageNumber", pageNumber);
            final Collection<Person> people = searchPersonCollectionPager.getPage(pageNumber.intValue());
            request.setAttribute("people", people);
        }

        return mapping.findForward("showSearchPage");
    }

    public ActionForward viewPersonCards(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String personIdString = request.getParameter("personId");
        if (personIdString != null) {
            final Person person = (Person) FenixFramework.getDomainObject(personIdString);
            request.setAttribute("person", person);
        }
        return mapping.findForward("viewPersonCards");
    }

    public ActionForward viewPersonCard(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String cardGenerationEntryIdString = request.getParameter("cardGenerationEntryId");
        if (cardGenerationEntryIdString != null) {
            final CardGenerationEntry cardGenerationEntry = FenixFramework.getDomainObject(cardGenerationEntryIdString);
            request.setAttribute("cardGenerationEntry", cardGenerationEntry);
            final Person person = cardGenerationEntry.getPerson();
            request.setAttribute("person", person);
        }
        return mapping.findForward("viewPersonCards");
    }

}
