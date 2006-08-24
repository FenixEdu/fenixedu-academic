package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SiteViewerDispatchAction extends FenixContextDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getExecutionCourseID(request));
        request.setAttribute("executionCourse", executionCourse);
        response.setStatus(301);
        response.addIntHeader("Moved Permanently", 301);
        return mapping.findForward("moved-permanently");
    }

    private Integer getExecutionCourseID(final HttpServletRequest request) {
        final String parameter = request.getParameter("objectCode");
        final String attribute = (String) request.getAttribute("objectCode");
        final String iString = parameter == null ? attribute : parameter;
        return Integer.valueOf(iString);
    }

}