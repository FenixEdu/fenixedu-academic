/*
 * Created on 9/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.AssociateExecutionCourseToCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCoursesByExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/associateExecutionCourseToCurricularCourse",
        input = "/associateExecutionCourseToCurricularCourse.do?method=prepare&page=0",
        attribute = "associateExecutionCourseToCurricularCourseForm",
        formBean = "associateExecutionCourseToCurricularCourseForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "viewExecutionCoursesToAssociate", path = "/manager/readExecutionCoursesByExecutionPeriod_bd.jsp",
                tileProperties = @Tile(navLocal = "/manager/curricularCourseNavLocalManager.jsp")),
        @Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do"),
        @Forward(name = "readCurricularCourse", path = "/readCurricularCourse.do"),
        @Forward(name = "readAvailableExecutionPeriods", path = "/readExecutionPeriodToAssociateExecutionCoursesAction.do") })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class AssociateExecutionCourseToCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();

        String executionPeriodId = request.getParameter("executionPeriodId");

        List infoExecutionCoursesList = null;
        try {
            infoExecutionCoursesList = ReadExecutionCoursesByExecutionPeriod.run(executionPeriodId);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        Collections.sort(infoExecutionCoursesList, new BeanComparator("nome"));

        InfoExecutionPeriod infoExecutionPeriod =
                ((InfoExecutionCourse) infoExecutionCoursesList.iterator().next()).getInfoExecutionPeriod();

        String ExecutionPeriodNameAndYear =
                new String(infoExecutionPeriod.getName() + "-" + infoExecutionPeriod.getInfoExecutionYear().getYear());
        request.setAttribute("executionPeriodNameAndYear", ExecutionPeriodNameAndYear);
        request.setAttribute("name", "associate");
        request.setAttribute("infoExecutionCoursesList", infoExecutionCoursesList);

        return mapping.findForward("viewExecutionCoursesToAssociate");
    }

    public ActionForward associate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();

        DynaActionForm associateForm = (DynaActionForm) form;

        String curricularCourseId = request.getParameter("curricularCourseId");
        String executionPeriodId = request.getParameter("executionPeriodId");
        if (associateForm.get("executionCourseId") == null) {
            return mapping.findForward("viewExecutionCoursesToAssociate");
        }

        String executionCourseId = (String) associateForm.get("executionCourseId");

        try {
            AssociateExecutionCourseToCurricularCourse.run(executionCourseId, curricularCourseId, executionPeriodId);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping.findForward("readAvailableExecutionPeriods"));
        } catch (NonExistingServiceException ex) {
            if (ex.getMessage().equals("message.nonExistingCurricularCourse")) {
                throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readDegreeCurricularPlan"));
            } else if (ex.getMessage().equals("message.nonExisting.executionCourse")) {
                throw new NonExistingActionException(ex.getMessage(), "");
            } else {
                throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readAvailableExecutionPeriods"));
            }
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        return mapping.findForward("readCurricularCourse");
    }
}