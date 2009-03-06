/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantProjectAction extends FenixDispatchAction {

    public ActionForward prepareEditGrantProjectForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer idGrantProject = getIntegerFromRequest(request, "idGrantProject");
	if (idGrantProject != null) {
	    GrantProject grantProject = (GrantProject) rootDomainObject.readGrantPaymentEntityByOID(idGrantProject);
	    request.setAttribute("grantProject", grantProject);
	}
	return mapping.findForward("edit-grant-project");
    }
}