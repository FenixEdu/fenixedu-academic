package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageProgram", module = "teacher", functionality = ManageExecutionCourseDA.class, formBean = "programForm")
public class ExecutionCourseProgramDA extends ManageExecutionCourseDA {

    @Input
    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return forward(request, "/teacher/executionCourse/program.jsp");
    }

    public ActionForward prepareCreateProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareCurricularCourse(request);
        return forward(request, "/teacher/executionCourse/createProgram.jsp");
    }

    public ActionForward createProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        executeFactoryMethod();
        return forward(request, "/teacher/executionCourse/program.jsp");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        final Teacher teacher = getUserView(request).getPerson().getTeacher();
        if (teacher.isResponsibleFor(executionCourse) == null) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.teacherNotResponsibleOrNotCoordinator"));
            saveErrors(request, messages);
            return forward(request, "/teacher/executionCourse/program.jsp");
        }

        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, curriculumIDString);
            if (curriculum != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("program", curriculum.getProgram());
                dynaActionForm.set("programEn", curriculum.getProgramEn());
            }
            request.setAttribute("curriculum", curriculum);
        }
        return forward(request, "/teacher/executionCourse/editProgram.jsp");
    }

    public ActionForward editProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        executeFactoryMethod();
        return forward(request, "/teacher/executionCourse/program.jsp");
    }

}
