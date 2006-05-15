package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageExecutionCourseDA extends FenixDispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		getExecutionCourseFromParameterAndSetItInRequest(request);
		return super.execute(mapping, actionForm, request, response);
	}

	public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	return mapping.findForward("instructions");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	return mapping.findForward("program");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
    	final String curriculumIDString = request.getParameter("curriculumID");
    	if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
    		final Curriculum curriculum = findCurriculum(executionCourse, Integer.valueOf(curriculumIDString));
    		request.setAttribute("curriculum", curriculum);
    	}
    	return mapping.findForward("edit-program");
    }

	private void getExecutionCourseFromParameterAndSetItInRequest(final HttpServletRequest request) {
    	final String executionCourseIDString = request.getParameter("executionCourseID");
    	if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
    		final ExecutionCourse executionCourse = findExecutionCourse(request, Integer.valueOf(executionCourseIDString));
    		request.setAttribute("executionCourse", executionCourse);
    	}
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

    private Curriculum findCurriculum(final ExecutionCourse executionCourse, final Integer curriculumID) {
    	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
    		for (final Curriculum curriculum : curricularCourse.getAssociatedCurriculumsSet()) {
    			if (curriculum.getIdInternal().equals(curriculumID)) {
    				return curriculum;
    			}
    		}
    	}
    	return null;
	}

}