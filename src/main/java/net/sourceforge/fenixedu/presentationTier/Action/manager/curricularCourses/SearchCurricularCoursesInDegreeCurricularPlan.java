package net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourses;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/searchCurricularCourses", module = "manager")
@Forwards({ @Forward(name = "searchCurricularCourses",
        path = "/manager/bolonha/curricularCourses/search/searchCurricularCourses.jsp") })
public class SearchCurricularCoursesInDegreeCurricularPlan extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");

        SearchCurricularCourseBean searchBean = new SearchCurricularCourseBean(degreeCurricularPlan);

        request.setAttribute("results", new ArrayList<Context>());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("searchCurricularCourses");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");
        SearchCurricularCourseBean searchBean = getRenderedObject("searchBean");

        request.setAttribute("results", searchBean.search());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

        RenderUtils.invalidateViewState();
        return mapping.findForward("searchCurricularCourses");
    }

    public ActionForward searchInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "dcpId");
        SearchCurricularCourseBean searchBean = getRenderedObject("searchBean");

        request.setAttribute("results", new ArrayList<Context>());
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("currentExecutionYear", ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("searchCurricularCourses");
    }

}
