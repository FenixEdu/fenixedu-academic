/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lmac1
 */
public class ReadCurricularCourseAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        request.setAttribute("degreeId", request.getParameter("degreeId"));
        request.setAttribute("degreeCurricularPlanId", request.getParameter("degreeCurricularPlanId"));
        request.setAttribute("curricularCourseId", curricularCourseId);

        Object args[] = { curricularCourseId };

        InfoCurricularCourse infoCurricularCourse = null;

        try {
            infoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService(userView,
                    "ReadCurricularCourse", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        // in case the curricular course really exists
        List executionCourses = null;
        try {
            executionCourses = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByCurricularCourse", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (executionCourses != null)
            Collections.sort(executionCourses, new BeanComparator("nome"));

        List curricularCourseScopes = new ArrayList();
        try {
            curricularCourseScopes = (List) ServiceUtils.executeService(userView,
                    "ReadActiveCurricularCourseScopes", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (curricularCourseScopes != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator(
                    "infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            Collections.sort(curricularCourseScopes, comparatorChain);
        }

        if (infoCurricularCourse.getBasic().booleanValue())
            request.setAttribute("basic", "");

        request.setAttribute("executionCoursesList", executionCourses);

        request.setAttribute("infoCurricularCourse", infoCurricularCourse);
        request.setAttribute("curricularCourseScopesList", curricularCourseScopes);
        return mapping.findForward("viewCurricularCourse");
    }
}