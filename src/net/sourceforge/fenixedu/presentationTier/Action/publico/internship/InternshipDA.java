package net.sourceforge.fenixedu.presentationTier.Action.publico.internship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.internship.InternshipCandidacyBean;
import net.sourceforge.fenixedu.domain.internship.DuplicateInternshipCandidacy;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacy;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/internship", module = "publico")
@Forwards( { @Forward(name = "candidacy", path = "internship.candidacy"),
	@Forward(name = "rules", path = "internship.candidacy.rules"),
	@Forward(name = "final", path = "internship.candidacy.final") })
public class InternshipDA extends FenixDispatchAction {
    public ActionForward prepareCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	InternshipCandidacySession session = InternshipCandidacySession.getMostRecentCandidacySession();
	if (session.getCandidacyInterval().containsNow()) {
	    request.setAttribute("candidacy", new InternshipCandidacyBean(session));
	}
	return mapping.findForward("candidacy");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	InternshipCandidacyBean bean = getRenderedObject();
	request.setAttribute("candidacy", bean);
	return mapping.findForward("rules");
    }

    public ActionForward confirmCandidacyRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    InternshipCandidacyBean bean = getRenderedObject();
	    request.setAttribute("candidacy", bean);
	    Integer candidacyNumber = InternshipCandidacy.create(bean);
	    request.setAttribute("candidacyNumber", candidacyNumber);
	    return mapping.findForward("final");
	} catch (DuplicateInternshipCandidacy e) {
	    addActionMessage(request, "error.internationalrelations.internship.candidacy.duplicateStudentNumber", e.getNumber(),
		    e.getUniversity());
	    return mapping.findForward("rules");
	}
    }

    public ActionForward backToCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	InternshipCandidacyBean bean = getRenderedObject();
	request.setAttribute("candidacy", bean);
	return mapping.findForward("candidacy");
    }
}
