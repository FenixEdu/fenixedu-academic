/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadAvailableExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionCoursesByCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
@Mapping(module = "manager", path = "/readExecutionPeriodToAssociateExecutionCoursesAction",
        input = "/readDegreeCurricularPlan.do", scope = "request")
@Forwards(value = { @Forward(name = "viewExecutionPeriods", path = "/manager/readExecutionPeriods_bd.jsp",
        tileProperties = @Tile(navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ReadExecutionPeriodToAssociateExecutionCoursesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();
        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

        List executionCoursesList = null;
        try {
            executionCoursesList = ReadExecutionCoursesByCurricularCourse.run(curricularCourseId);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), "");
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        // exclude executionPeriods of containing execution courses to assure
        // that there is only one execution course per execution period
        List unavailableExecutionPeriodsIds = new ArrayList();
        Iterator iter = executionCoursesList.iterator();
        while (iter.hasNext()) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) iter.next();

            unavailableExecutionPeriodsIds.add(infoExecutionCourse.getInfoExecutionPeriod().getExternalId());
        }

        try {
            List infoExecutionPeriods = ReadAvailableExecutionPeriods.run(unavailableExecutionPeriodsIds);

            if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

                // Collections.sort(infoExecutionPeriods, new
                // ExecutionPeriodComparator());
                ComparatorChain comparator = new ComparatorChain();
                comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
                comparator.addComparator(new BeanComparator("name"), true);
                Collections.sort(infoExecutionPeriods, comparator);

                if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
                    request.setAttribute(PresentationConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
                }

            }
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas de comunicação com a base de dados.", ex);
        }

        request.setAttribute("name", "associate");
        return mapping.findForward("viewExecutionPeriods");
    }
}