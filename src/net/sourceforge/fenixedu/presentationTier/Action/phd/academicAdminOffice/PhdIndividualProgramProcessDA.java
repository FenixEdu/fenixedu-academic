package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "manageProcesses", path = "/phd/academicAdminOffice/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/academicAdminOffice/viewProcess.jsp")

})
public class PhdIndividualProgramProcessDA extends FenixDispatchAction {

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("processes", ExecutionYear.readCurrentExecutionYear().getPhdIndividualProgramProcesses());

	return mapping.findForward("manageProcesses");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("process", getProcess(request));

	return mapping.findForward("viewProcess");
    }

    private PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return getDomainObject(request, "processId");
    }

}
