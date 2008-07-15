package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

@Mapping(path = "/researcherManagement", module = "researcher")
@Forwards( {
	@Forward(name = "viewDetails", path = "view-researcher-details"),
	@Forward(name = "editDetails", path = "edit-researcher-details") })
public class ResearcherManagement extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	Person person = getLoggedPerson(request);
	request.setAttribute("researcher", person.getResearcher());

	return mapping.findForward("viewDetails");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	Person person = getLoggedPerson(request);
	request.setAttribute("researcher", person.getResearcher());

	return mapping.findForward("editDetails");
    }
}
