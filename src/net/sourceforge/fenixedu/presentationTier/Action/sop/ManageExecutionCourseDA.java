package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageExecutionCourseDA extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        Object args[] = { infoExecutionCourse };
        List infoClasses = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadClassesByExecutionCourse", args);

        if (infoClasses != null && !infoClasses.isEmpty()) {
            Collections.sort(infoClasses, new BeanComparator("nome"));
            request.setAttribute(SessionConstants.LIST_INFOCLASS, infoClasses);
        }

        return mapping.findForward("ManageExecutionCourse");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        DynaActionForm editExecutionCourseForm = (DynaActionForm) form;
        infoExecutionCourse.setTheoreticalHours(new Double((String) editExecutionCourseForm
                .get("theoreticalHours")));
        infoExecutionCourse.setTheoPratHours(new Double((String) editExecutionCourseForm
                .get("theoPratHours")));
        infoExecutionCourse.setPraticalHours(new Double((String) editExecutionCourseForm
                .get("praticalHours")));
        infoExecutionCourse.setLabHours(new Double((String) editExecutionCourseForm.get("labHours")));

        Object args[] = { infoExecutionCourse };
        infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
                userView, "EditExecutionCourse", args);

        if (infoExecutionCourse != null) {
            request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
        }

        return prepare(mapping, form, request, response);
    }

}