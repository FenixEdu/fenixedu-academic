package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/executionCourseManagementStart", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "mainPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp"),
        @Forward(name = "firstPage", path = "/academicAdministration/executionCourseManagement/welcomeScreen.jsp") })
public class ExecutionCourseManagementAction extends FenixDispatchAction {
    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("firstPage");
    }

    public ActionForward mainPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("mainPage");
    }
}
