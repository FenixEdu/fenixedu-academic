package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@Mapping(module = "publico", path = "/searchScormContent", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "search-scorm-content") })
public class SearchScormContentAction extends FenixDispatchAction {
    public ActionForward prepareSearchForExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ExecutionCourse course = null;

        FunctionalityContext context = AbstractFunctionalityContext.getCurrentContext(request);
        if (context != null && context.getSelectedContainer() instanceof ExecutionCourseSite) {
            course = ((ExecutionCourseSite) context.getSelectedContainer()).getSiteExecutionCourse();
        }

        String executionCourseId = request.getParameter("executionCourseID");
        if (executionCourseId != null) {
            course = (ExecutionCourse) FenixFramework.getDomainObject(executionCourseId);
        }

        SearchDSpaceCoursesBean bean = new SearchDSpaceCoursesBean();
        bean.setCourse(course);
        if (course != null) {
            bean.setExecutionYear(course.getExecutionYear());
            bean.setExecutionPeriod(course.getExecutionPeriod());
        }

        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());

        return mapping.findForward("search");

    }

    public ActionForward searchScormContents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = getRenderedObject("search");
        bean.search();
        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());

        return mapping.findForward("search");
    }

    public ActionForward changeTimeStamp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = getRenderedObject("search");
        RenderUtils.invalidateViewState("executionPeriodField");

        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());

        return mapping.findForward("search");
    }

    public ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchDSpaceCoursesBean bean = SearchDSpaceCoursesBean.reconstructFromRequest(request);
        bean.search();
        String pageNumber = request.getParameter("pageNumber");
        if (!Strings.isNullOrEmpty(pageNumber)) {
            bean.setPage(Integer.valueOf(pageNumber));
        }
        request.setAttribute("bean", bean);
        request.setAttribute("executionCourse", bean.getCourse());
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());
        return mapping.findForward("search");
    }

}
