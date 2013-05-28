package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "teacher", path = "/weeklyWorkLoad", input = "/weeklyWorkLoad.do?method=prepare", scope = "session",
        parameter = "method")
@Forwards(value = { @Forward(name = "showWeeklyWorkLoad", path = "/teacher/weeklyWorkLoad.jsp", tileProperties = @Tile(
        navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp")) })
public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException {

        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID =
                (executionCourseIDString == null || executionCourseIDString.length() == 0) ? null : Integer
                        .valueOf(executionCourseIDString);

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("weeklyWorkLoadView", executionCourse.getWeeklyWorkLoadView());

        final InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(infoSiteCommon, null);
        request.setAttribute("siteView", executionCourseSiteView);

        return mapping.findForward("showWeeklyWorkLoad");
    }

}
