package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "createCandidacy", path = "/phd/candidacy/academicAdminOffice/createCandidacy.jsp")

})
public class PhdProgramCandidacyProcessDA extends FenixDispatchAction {

    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCandidacyBean", new PhdProgramCandidacyProcessBean());

	return mapping.findForward("createCandidacy");
    }

    public ActionForward prepareCreateCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCandidacyBean", getRenderedObject("createCandidacyBean"));

	return mapping.findForward("createCandidacy");
    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Process result = Process.createNewProcess(AccessControl.getUserView(), PhdIndividualProgramProcess.class,
		getRenderedObject("createCandidacyBean"));

	return null;
    }

}
