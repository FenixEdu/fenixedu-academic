package net.sourceforge.fenixedu.presentationTier.Action.student.candidacy;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editMissingCandidacyInformation", module = "student")
@Forwards( {

@Forward(name = "editMissingCandidacyInformation", path = "candidacy.edit.missing.candidacy.information")

})
public class EditMissingCandidacyInformationDA extends FenixDispatchAction {

    static protected List<CandidacyInformationBean> getCandidacyInformationsWithMissingInfo() {
	return AccessControl.getPerson().getStudent().getCandidacyInformationsWithMissingInformation();
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<CandidacyInformationBean> list = getCandidacyInformationsWithMissingInfo();

	request.setAttribute("candidaciesWithMissingInformation", list);
	request.setAttribute("candidacyInformationBean", list.get(0));

	return mapping.findForward("editMissingCandidacyInformation");
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("candidaciesWithMissingInformation", getCandidacyInformationsWithMissingInfo());
	request.setAttribute("candidacyInformationBean", getRenderedObject("candidacyInformationBean"));

	return mapping.findForward("editMissingCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final CandidacyInformationBean candidacyInformationBean = getRenderedObject("candidacyInformationBean");

	final Set<String> messages = candidacyInformationBean.validate();

	if (!messages.isEmpty()) {
	    for (final String each : messages) {
		addActionMessage(request, each);
	    }
	    return prepareEditInvalid(mapping, form, request, response);
	}

	try {
	    candidacyInformationBean.updateCandidacyWithMissingInformation();
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return prepareEditInvalid(mapping, form, request, response);
	}

	final ActionForward forward = new ActionForward();
	forward.setPath("/home.do");
	forward.setRedirect(true);
	forward.setModule("/");

	return forward;

    }
}
