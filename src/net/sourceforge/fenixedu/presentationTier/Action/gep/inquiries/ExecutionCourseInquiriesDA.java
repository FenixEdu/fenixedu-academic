package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries.SelectAllExecutionCoursesForInquiries;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "gep", path = "/executionCourseInquiries", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showExecutionCoursesForInquiries", path = "/gep/inquiries/showExecutionCoursesForInquiries.jsp", tileProperties = @Tile(title = "private.gep.surveys.settoevaluatedisciplines")) })
public class ExecutionCourseInquiriesDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ExecutionCourseSearchBean executionCourseSearchBean = getRenderedObject();
	if (executionCourseSearchBean == null) {
	    executionCourseSearchBean = new ExecutionCourseSearchBean();
	    final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
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

    public ActionForward selectAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final ExecutionCourseSearchBean executionCourseSearchBean = getRenderedObject();
	SelectAllExecutionCoursesForInquiries.run(executionCourseSearchBean);
	return search(mapping, actionForm, request, response);
    }

    public ActionForward unselectAll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SelectAllExecutionCoursesForInquiries.unselectAll((ExecutionCourseSearchBean) getRenderedObject());
	return search(mapping, actionForm, request, response);
    }

}