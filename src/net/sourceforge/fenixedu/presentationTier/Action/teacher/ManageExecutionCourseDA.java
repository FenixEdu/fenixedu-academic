package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageExecutionCourseDA extends FenixDispatchAction {

    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	final String executionCourseIDString = request.getParameter("executionCourseID");
    	if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
    		final ExecutionCourse executionCourse = findExecutionCourse(request, Integer.valueOf(executionCourseIDString));
    		request.setAttribute("executionCourse", executionCourse);
    	}

    	return mapping.findForward("instructions");
    }

	private ExecutionCourse findExecutionCourse(final HttpServletRequest request, final Integer executionCourseID) {
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

}