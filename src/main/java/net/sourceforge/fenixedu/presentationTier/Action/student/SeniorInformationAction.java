/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.senior.ReadStudentSenior;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
@Mapping(module = "student", path = "/seniorInformation", input = "/seniorInformation.do?method=prepare&page=0",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseRegistration", path = "/student/senior/chooseRegistration.jsp", tileProperties = @Tile(
                title = "private.student.finalists.seniorinformationsheet")),
        @Forward(name = "show-result", path = "/student/senior/seniorInfo.jsp", tileProperties = @Tile(
                title = "private.student.finalists.seniorinformationsheet")),
        @Forward(name = "show-form", path = "/student/senior/seniorInfoManagement.jsp", tileProperties = @Tile(
                title = "private.student.finalists.seniorinformationsheet")) })
public class SeniorInformationAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Registration registration = null;

        final String registrationOID;
        Object registrationOIDObj = getFromRequest(request, "registrationOID");
        if (registrationOIDObj != null) {
            registrationOID = registrationOIDObj.toString();
        } else {
            registrationOID = null;
        }
        final Student loggedStudent = getUserView(request).getPerson().getStudent();

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else if (loggedStudent != null) {
            if (loggedStudent.getRegistrations().size() == 1) {
                registration = loggedStudent.getRegistrations().iterator().next();
            } else {
                request.setAttribute("student", loggedStudent);
                return mapping.findForward("chooseRegistration");
            }
        }

        if (registration == null) {
            throw new FenixActionException();
        } else {
            final Senior senior = ReadStudentSenior.run(registration);
            request.setAttribute("senior", senior);
            return mapping.findForward("show-form");
        }
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        final IViewState viewState = RenderUtils.getViewState("editSeniorExpectedInfoID");
        request.setAttribute("senior", viewState.getMetaObject().getObject());

        return mapping.findForward("show-result");
    }

}