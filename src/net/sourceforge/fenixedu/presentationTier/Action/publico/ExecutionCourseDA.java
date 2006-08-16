package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExecutionCourseDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID = Integer.valueOf(executionCourseIDString);
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        request.setAttribute("executionCourse", executionCourse);
        return super.execute(mapping, actionForm, request, response);
    }

    protected ExecutionCourse getExecutionCourse(final HttpServletRequest request) {
    	return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-first-page");
    }

    public ActionForward announcements(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-announcements");
    }

    public ActionForward summaries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-summaries");
    }

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-evaluation-method");
    }

    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-bibliographic-reference");
    }

    public ActionForward schedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
    	final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
    	for (final Lesson lesson : executionCourse.getLessons()) {
    		infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
    	}
    	request.setAttribute("infoLessons", infoLessons);
        return mapping.findForward("execution-course-schedule");
    }

    public ActionForward shifts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-shifts");
    }

    public ActionForward evaluations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-evaluations");
    }

    public ActionForward groupings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-groupings");
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-section");
    }

    public ActionForward rss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("execution-course-rss");
    }

}