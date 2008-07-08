package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class SearchExecutionCourseAttendsAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchExecutionCourseAttendsBean searchExecutionCourseAttendsBean = new SearchExecutionCourseAttendsBean(rootDomainObject
		.readExecutionCourseByOID(Integer.valueOf(request.getParameter("objectCode"))));
	request.setAttribute("searchBean", searchExecutionCourseAttendsBean);
	request.setAttribute("executionCourse", searchExecutionCourseAttendsBean.getExecutionCourse());
	return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchExecutionCourseAttendsBean bean = (SearchExecutionCourseAttendsBean) getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", bean);
	request.setAttribute("executionCourse", bean.getExecutionCourse());
	return mapping.findForward("search");
    }

}
