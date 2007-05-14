package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseWithNoEvaluationMethodSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EvaluationMethodControlDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean
		= getExecutionCourseWithNoEvaluationMethodSearchBean(request);
	//RenderUtils.invalidateViewState();
	if (executionCourseWithNoEvaluationMethodSearchBean == null) {
	    executionCourseWithNoEvaluationMethodSearchBean = new ExecutionCourseWithNoEvaluationMethodSearchBean();
	}
	request.setAttribute("executionCourseWithNoEvaluationMethodSearchBean", executionCourseWithNoEvaluationMethodSearchBean);
	request.setAttribute("executionCourses", executionCourseWithNoEvaluationMethodSearchBean.getSearchResult());
	return mapping.findForward("search");
    }

    private ExecutionCourseWithNoEvaluationMethodSearchBean getExecutionCourseWithNoEvaluationMethodSearchBean(final HttpServletRequest request) {
	final ExecutionCourseWithNoEvaluationMethodSearchBean executionCourseWithNoEvaluationMethodSearchBean
		= (ExecutionCourseWithNoEvaluationMethodSearchBean) getRenderedObject();
	return executionCourseWithNoEvaluationMethodSearchBean == null ? (ExecutionCourseWithNoEvaluationMethodSearchBean)
		request.getAttribute("executionCourseWithNoEvaluationMethodSearchBean") : executionCourseWithNoEvaluationMethodSearchBean;
    }

}
