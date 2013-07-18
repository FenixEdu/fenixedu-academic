package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.SelectExportExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherAdministrationSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author joaosa & rmalo
 */
public class PrepareSelectExecutionCourseAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        ISiteComponent shiftsAndGroupsView = new InfoSiteShiftsAndGroups();
        readSiteView(request, shiftsAndGroupsView, null, groupPropertiesCode, null);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        InfoExecutionDegree infoExecutionDegree =
                RequestUtils.getExecutionDegreeFromRequest(request, infoExecutionPeriod.getInfoExecutionYear());

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        List infoExecutionCourses =
                (List) SelectExportExecutionCourse.run(infoExecutionDegree, infoExecutionPeriod, curricularYear);
        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute("exeCourseList", infoExecutionCourses);
        return mapping.findForward("sucess");
    }

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent, Integer infoExecutionCourseCode,
            Object obj1, Object obj2) throws FenixActionException {

        IUserView userView = getUserView(request);

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            objectCode = getObjectCode(request);
            infoExecutionCourseCode = objectCode;
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        try {
            TeacherAdministrationSiteView siteView =
                    TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(
                            infoExecutionCourseCode, commonComponent, firstPageComponent, objectCode, obj1, obj2);
            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                    .getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent()).getSection());
            }

            return siteView;

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

}