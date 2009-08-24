package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewPersonsWithRoleDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	request.setAttribute(PresentationConstants.ROLES, rootDomainObject.getRoles());
	return mapping.findForward("SelectRole");
    }

    public ActionForward searchWithRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Role role = rootDomainObject.readRoleByOID(Integer.valueOf(request.getParameter("roleID")));
	request.setAttribute("persons", role.getAssociatedPersons());
	return mapping.findForward("ShowPersons");
    }
}
