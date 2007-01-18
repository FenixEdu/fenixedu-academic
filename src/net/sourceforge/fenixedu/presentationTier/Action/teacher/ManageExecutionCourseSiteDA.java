package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageExecutionCourseSiteDA extends SiteManagementDA {
    
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        propageContextIds(request);
        return super.execute(mapping, actionForm, request, response);
    }
    
    public static void propageContextIds(final HttpServletRequest request) {
        String executionCourseIDString = request.getParameter("executionCourseID");

        if (executionCourseIDString == null || executionCourseIDString.length() == 0) {
            executionCourseIDString = request.getParameter("objectCode");
        }

        if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
            final ExecutionCourse executionCourse = findExecutionCourse(request, Integer
                    .valueOf(executionCourseIDString));
            request.setAttribute("executionCourse", executionCourse);
        }
    }
    
    private static ExecutionCourse findExecutionCourse(final HttpServletRequest request,
            final Integer executionCourseID) {
        final IUserView userView = getUserView(request);

        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                final Teacher teacher = person.getTeacher();
                if (teacher != null) {
                    for (final Professorship professorship : teacher.getProfessorshipsSet()) {
                        final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                        if (executionCourse.getIdInternal().equals(executionCourseID)) {
                            return executionCourse;
                        }
                    }
                }
            }
        }

        return null;
    }
    
    @Override
    protected String getAuthorNameForFile(Item item) {
        return ((ExecutionCourseSite)item.getSection().getSite()).getExecutionCourse().getNome();
    }

    
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
        ExecutionCourse executionCourse = ((ExecutionCourseSite)section.getSite()).getExecutionCourse();
        
        String resourceLocation = request.getScheme() + "://" + request.getServerName() + request.getContextPath() + ItemProcessor.getItemAbsolutePath(executionCourse, item);
        return resourceLocation;
    }

}
