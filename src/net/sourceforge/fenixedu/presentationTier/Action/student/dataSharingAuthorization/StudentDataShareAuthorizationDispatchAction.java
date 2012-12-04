package net.sourceforge.fenixedu.presentationTier.Action.student.dataSharingAuthorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentDataShareAuthorization", module = "student")
@Forwards({
	@Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp", tileProperties = @Tile(  title = "private.student.view.dataauthorization")),
	@Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp", tileProperties = @Tile(  title = "private.student.view.dataauthorization")) })
public class StudentDataShareAuthorizationDispatchAction extends FenixDispatchAction {
    public ActionForward manageAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("student", getLoggedPerson(request).getStudent());
	return mapping.findForward("authorizations");
    }

    public ActionForward saveAuthorization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addActionMessage(request, "message.student.dataShareAuthorization.saveSuccess");
	request.setAttribute("student", getLoggedPerson(request).getStudent());
	return mapping.findForward("authorizations");
    }

    public ActionForward viewAuthorizationHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("student", getLoggedPerson(request).getStudent());
	return mapping.findForward("historic");
    }
}
