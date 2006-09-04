package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixFilterException, FenixServiceException {

        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID = (executionCourseIDString == null || executionCourseIDString.length() == 0) ?
                null : Integer.valueOf(executionCourseIDString);

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("weeklyWorkLoadView", executionCourse.getWeeklyWorkLoadView());

        final InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(infoSiteCommon, null);
        request.setAttribute("siteView", executionCourseSiteView);

        return mapping.findForward("showWeeklyWorkLoad");
    }

}
