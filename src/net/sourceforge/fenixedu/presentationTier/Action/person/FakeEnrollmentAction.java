package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person", path = "/fakeEnrollment", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "prepareCreate", path = "/person/createFakeEnrollment.jsp"),
	@Forward(name = "create", path = "/person/createFakeEnrollment.jsp") })
public class FakeEnrollmentAction extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("loggedPerson", AccessControl.getPerson());
	return mapping.findForward("prepareCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("loggedPerson", AccessControl.getPerson());
	return mapping.findForward("create");
    }

}
