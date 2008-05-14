package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExecutionCourseInquiriesDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ExecutionCourseSearchBean executionCourseSearchBean = (ExecutionCourseSearchBean) getRenderedObject();
        if (executionCourseSearchBean == null) {
            executionCourseSearchBean = new ExecutionCourseSearchBean();
            final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionPeriod();
            executionCourseSearchBean.setExecutionPeriod(executionSemester);
        } else {
            final Collection<ExecutionCourse> executionCourses = executionCourseSearchBean.search();
            if (executionCourses != null) {
                request.setAttribute("executionCourses", executionCourses);
            }
            RenderUtils.invalidateViewState("executionCourses");
        }
        request.setAttribute("executionCourseSearchBean", executionCourseSearchBean);
        return mapping.findForward("showExecutionCoursesForInquiries");
    }

    public ActionForward selectAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final ExecutionCourseSearchBean executionCourseSearchBean = (ExecutionCourseSearchBean) getRenderedObject();
	executeService(request, "SelectAllExecutionCoursesForInquiries", new Object[] { executionCourseSearchBean });
	return search(mapping, actionForm, request, response);
    }

}
