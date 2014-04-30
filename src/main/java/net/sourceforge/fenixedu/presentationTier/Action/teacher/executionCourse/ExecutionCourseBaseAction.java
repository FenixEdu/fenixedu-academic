package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.common.base.Strings;

public abstract class ExecutionCourseBaseAction extends FenixDispatchAction {

    private static final ActionForward FORWARD = new ActionForward("/executionCourse/executionCourseFrame.jsp");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        propageContextIds(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public static ActionForward forward(HttpServletRequest request, String page) {
        request.setAttribute("teacher$actual$page", page);
        return FORWARD;
    }

    public static void propageContextIds(final HttpServletRequest request) {
        String executionCourseID = request.getParameter("executionCourseID");

        if (Strings.isNullOrEmpty(executionCourseID)) {
            executionCourseID = request.getParameter("objectCode");
        }

        if (executionCourseID != null && executionCourseID.length() > 0) {
            final Professorship professorship = findProfessorship(request, executionCourseID);
            request.setAttribute("professorship", professorship);
            request.setAttribute("executionCourse", professorship.getExecutionCourse());
            request.setAttribute("executionCourseID", executionCourseID);
        }
    }

    protected ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    private static Professorship findProfessorship(final HttpServletRequest request, final String executionCourseID) {
        final Person person = AccessControl.getPerson();
        if (person != null) {
            for (final Professorship professorship : person.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                if (executionCourse.getExternalId().equals(executionCourseID)) {
                    return professorship;
                }
            }
        }
        throw new RuntimeException("User is not authorized to manage the selected course!");
    }

}
