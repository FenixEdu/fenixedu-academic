package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class CommonPhdThesisProcessDA extends PhdProcessDA {

    @Override
    protected PhdThesisProcess getProcess(HttpServletRequest request) {
	return (PhdThesisProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request, final PhdThesisProcess process) {
	return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
		.getIndividualProgramProcess().getExternalId()), request);
    }

}
