package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/announcementSwap", module = "manager")
@Forwards({ @Forward(name = "chooseExecutionCourse", path = "/manager/executionCourseManagement/chooseExecutionCourse.jsp") })
public class AnnouncementSwap extends FenixDispatchAction {

    public ActionForward prepareSwap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Boolean chooseNotLinked = Boolean.valueOf(request.getParameter("executionCoursesNotLinked"));
        Integer executionCourseId = Integer.valueOf(request.getParameter("executionCourseId"));
        Integer executionPeriodId = Integer.valueOf(request.getParameter("executionPeriodId"));

        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        ExecutionSemester executionPeriod = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();

        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));
            Integer curricularYearId = Integer.valueOf(request.getParameter("curYearId"));

            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
            CurricularYear curYear = RootDomainObject.getInstance().readCurricularYearByOID(curricularYearId);

            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
        }

        request.setAttribute("bean", new ExecutionCourseBean(executionCourse));
        request.setAttribute("sessionBean", sessionBean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");
    }

    public ActionForward swap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("executionCourseBean");

        for (Announcement announcement : bean.getAnnouncements()) {
            announcement.swap(bean.getSourceExecutionCourse().getBoard(), bean.getDestinationExecutionCourse().getBoard());
        }

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");

    }

    public ActionForward postBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ExecutionCourseBean bean = getRenderedObject("executionCourseBean");

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("executionCourseBean");

        return mapping.findForward("chooseExecutionCourse");

    }

}
