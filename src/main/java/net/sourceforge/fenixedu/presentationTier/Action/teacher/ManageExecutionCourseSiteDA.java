package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageExecutionCourseSiteDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        propageContextIds(request);

        return super.execute(mapping, actionForm, request, response);
    }

    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public static void propageContextIds(final HttpServletRequest request) {
        String executionCourseIDString = request.getParameter("executionCourseID");

        if (executionCourseIDString == null || executionCourseIDString.length() == 0) {
            executionCourseIDString = request.getParameter("objectCode");
        }

        if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
            final ExecutionCourse executionCourse = findExecutionCourse(request, executionCourseIDString);
            request.setAttribute("executionCourse", executionCourse);
        }
    }

    private static ExecutionCourse findExecutionCourse(final HttpServletRequest request, final String executionCourseID) {
        final User userView = getUserView(request);

        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                for (final Professorship professorship : person.getProfessorshipsSet()) {
                    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                    if (executionCourse.getExternalId().equals(executionCourseID)) {
                        return executionCourse;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getExecutionCourse(request).getNome();
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        if (executionCourse != null) {
            return executionCourse.getSite();
        } else {
            return null;
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {

        String path = section.getSite().getReversePath();
        if (path == null) {
            return null;
        }

        String resourceLocation =
                request.getScheme() + "://" + request.getServerName() + request.getContextPath() + path + item.getReversePath();
        return resourceLocation;
    }

}
