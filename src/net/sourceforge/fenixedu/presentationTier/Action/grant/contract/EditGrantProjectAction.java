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
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Barbosa
 * @author Pica
 */
@Mapping(module = "facultyAdmOffice", path = "/editGrantProject", input = "/editGrantProject.do?page=0&method=prepareEditGrantProjectForm", attribute = "editGrantProjectForm", formBean = "editGrantProjectForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "manage-grant-project", path = "/manageGrantProject.do?method=prepareManageGrantProject"),
		@Forward(name = "edit-grant-project", path = "/facultyAdmOffice/grant/contract/editGrantProject.jsp") })
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