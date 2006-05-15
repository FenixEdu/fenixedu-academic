package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

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
    		if (curriculum != null) {
    			final DynaActionForm dynaActionForm = (DynaActionForm) form;
    			dynaActionForm.set("program", curriculum.getProgram());
    			dynaActionForm.set("programEn", curriculum.getProgramEn());
    		}
    		request.setAttribute("curriculum", curriculum);
    	}
    	return mapping.findForward("edit-program");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final DynaActionForm dynaActionForm = (DynaActionForm) form;
		final String curriculumIDString = request.getParameter("curriculumID");
		final String program = dynaActionForm.getString("program");
		final String programEn = dynaActionForm.getString("programEn");

		final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
		final Curriculum curriculum = findCurriculum(executionCourse, Integer.valueOf(curriculumIDString));
		final InfoCurriculum infoCurriculum = new InfoCurriculum();
		infoCurriculum.setProgram(program);
		infoCurriculum.setProgramEn(programEn);
		final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(), curriculum.getCurricularCourse().getIdInternal(), infoCurriculum, userView.getUtilizador() };
        ServiceManagerServiceFactory.executeService(userView, "EditProgram", args);

    	return mapping.findForward("program");
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