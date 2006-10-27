package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author naat
 * 
 */
public class ExecutionCourseForumManagementDispatchAction extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ManageExecutionCourseDA.propageContextIds(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward viewForuns(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {

        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        List<ExecutionCourseForum> foruns = executionCourse.getForuns();

        request.setAttribute("foruns", foruns);

        return mapping.findForward("viewForuns");
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/executionCourseForumManagement.do?executionCourseID="
                + request.getParameter("executionCourseID");
    }

}