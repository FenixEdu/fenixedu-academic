package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageSecondCycleThesis", module = "scientificCouncil")
@Forwards( {
    @Forward(name = "firstPage", path = "/scientificCouncil/thesis/firstPage.jsp"),
    @Forward(name = "showPersonThesisDetails", path = "/scientificCouncil/thesis/showPersonThesisDetails.jsp"),
    @Forward(name = "showThesisDetails", path = "/scientificCouncil/thesis/showThesisDetails.jsp"),
    @Forward(name = "editThesisEvaluationParticipant", path = "/scientificCouncil/thesis/editThesisEvaluationParticipant.jsp")
})
public class ManageSecondCycleThesisDA extends FenixDispatchAction {

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	request.setAttribute("manageSecondCycleThesisSearchBean", new ManageSecondCycleThesisSearchBean());
	return mapping.findForward("firstPage");
    }

    public ActionForward filterSearch(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ManageSecondCycleThesisSearchBean filterSearchForm = getRenderedObject("filterSearchForm");
	request.setAttribute("manageSecondCycleThesisSearchBean", filterSearchForm);
	return mapping.findForward("firstPage");
    }

    public ActionForward searchPerson(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ManageSecondCycleThesisSearchBean searchPersonForm = getRenderedObject("searchPersonForm");
	final SortedSet<Person> people = searchPersonForm.findPersonBySearchString();
	if (people.size() == 1) {
	    final Person person = people.first();
	    return showPersonThesisDetails(mapping, request, person);
	}
	request.setAttribute("people", people);
	request.setAttribute("manageSecondCycleThesisSearchBean", searchPersonForm);
	return mapping.findForward("firstPage");
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Person person = getDomainObject(request, "personOid");
	return showPersonThesisDetails(mapping, request, person);
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final HttpServletRequest request,
	    final Person person) throws Exception {
	request.setAttribute("person", person);
	return mapping.findForward("showPersonThesisDetails");	    
    }

    public ActionForward showThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Thesis thesis = getDomainObject(request, "thesisOid");
	request.setAttribute("thesis", thesis);
	return mapping.findForward("showThesisDetails");
    }

    public ActionForward editThesisEvaluationParticipant(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ThesisEvaluationParticipant thesisEvaluationParticipant = getDomainObject(request, "thesisEvaluationParticipantOid");
	request.setAttribute("thesisEvaluationParticipant", thesisEvaluationParticipant);
	return mapping.findForward("editThesisEvaluationParticipant");
    }

}
