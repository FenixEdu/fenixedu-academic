package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class CardGenerationSearchDA extends FenixDispatchAction {

    public ActionForward search(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	final SearchParameters searchParameters = (SearchParameters) getRenderedObject();
	if (searchParameters == null) {
	    request.setAttribute("searchParameters", new SearchPerson.SearchParameters());
	} else {
	    final SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);
	    final Object[] args = { searchParameters, predicate };
	    final CollectionPager<Person> searchPersonCollectionPager = (CollectionPager<Person>) executeService(request, "SearchPerson", args);
	    request.setAttribute("searchPersonCollectionPager", searchPersonCollectionPager);
	}

	return mapping.findForward("showSearchPage");
    }

}
