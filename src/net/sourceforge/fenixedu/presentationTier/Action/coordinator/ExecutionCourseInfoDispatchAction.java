package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftGroupStatistics;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *  
 */
public class ExecutionCourseInfoDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChoice(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session
                .getAttribute(SessionConstants.MASTER_DEGREE);

        Object argsReadExecutionPeriods[] = { infoExecutionDegree.getInfoExecutionYear().getIdInternal() };
        List executionPeriods = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadExecutionPeriodsByExecutionYear", argsReadExecutionPeriods);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod
                    .getIdInternal().toString()));
        }

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
        
        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        return mapping.findForward("ReadyToSearch");
    }

    
    public ActionForward prepareChoiceForCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        Object argsReadExecutionPeriods[] = {degreeCurricularPlanID};
        List executionPeriods = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadExecutionPeriodsByDegreeCurricularPlan", argsReadExecutionPeriods);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod
                    .getIdInternal().toString()));
        }

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
        
        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        return mapping.findForward("ReadyToSearch");
    }
    
    
    
    public ActionForward getExecutionCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session
                .getAttribute(SessionConstants.MASTER_DEGREE);

        DynaActionForm searchExecutionCourse = (DynaActionForm) form;
        
        Integer degreeCurricularPlanID = (Integer) searchExecutionCourse.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        // Mandatory Selection
        Integer executionPeriodOID = null;
        if (((String) searchExecutionCourse.get("executionPeriodOID")) != null) {
            executionPeriodOID = new Integer((String) searchExecutionCourse.get("executionPeriodOID"));
        } else {
            executionPeriodOID = new Integer(request.getParameter("executionPeriodOID"));
        }
        request.setAttribute("executionPeriodOID", executionPeriodOID.toString());

        // Optional Selection
        Integer curricularYearOID = null;
        InfoCurricularYear infoCurricularYear = null;
        if (searchExecutionCourse.get("curricularYearOID") != null
                && !searchExecutionCourse.get("curricularYearOID").equals("")
                && !searchExecutionCourse.get("curricularYearOID").equals("null")) {
            curricularYearOID = new Integer((String) searchExecutionCourse.get("curricularYearOID"));
            infoCurricularYear = new InfoCurricularYear(RootDomainObject.getInstance().readCurricularYearByOID(curricularYearOID));
            request.setAttribute("curricularYearOID", curricularYearOID);
        } else {
            if ((request.getParameter("curricularYearOID") != null)
                    && (request.getParameter("curricularYearOID").length() != 0)
                    && (!searchExecutionCourse.get("curricularYearOID").equals("null"))) {
            	infoCurricularYear = new InfoCurricularYear(RootDomainObject.getInstance().readCurricularYearByOID(new Integer(request.getParameter("curricularYearOID"))));
            }
        }

        // Optional Selection
        String executionCourseName = (String) searchExecutionCourse.get("executionCourseName");
        if ((executionCourseName != null) && (executionCourseName.length() == 0)) {
            executionCourseName = request.getParameter("executionCourseName");
        }
        request.setAttribute("executionCourseName", executionCourseName);

        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(RootDomainObject.getInstance().readExecutionPeriodByOID(executionPeriodOID));

        if ((executionCourseName != null) && (executionCourseName.length() == 0)) {
            executionCourseName = null;
        }

        Object args[] = { infoExecutionPeriod, infoExecutionDegree, infoCurricularYear,
                executionCourseName };

        List infoExecutionCourses = null;
        try {
            infoExecutionCourses = (List) ServiceManagerServiceFactory.executeService(userView,
                    "SearchExecutionCourses", args);
        } catch (NotAuthorizedFilterException e) {
            return mapping.findForward("notAuthorized");
        }
        
        if (infoExecutionCourses != null) {
            sortList(request, infoExecutionCourses);
            request.setAttribute(SessionConstants.LIST_INFOEXECUTIONCOURSE, infoExecutionCourses);
        }

        return mapping.findForward("ShowCurricularCourseList");
    }

    public ActionForward showOccupancyLevels(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        Object args[] = { new Integer(request.getParameter("executionCourseOID")) };

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = (InfoExecutionCourseOccupancy) ServiceManagerServiceFactory
                .executeService(userView, "ReadShiftsByExecutionCourseID", args);

        arranjeShifts(infoExecutionCourseOccupancy);

        request.setAttribute("infoExecutionCourseOccupancy", infoExecutionCourseOccupancy);
        return mapping.findForward("showOccupancy");

    }

    private void sortList(HttpServletRequest request, List infoExecutionCourses) {
        String sortParameter = request.getParameter("sortBy");
        if ((sortParameter != null) && (sortParameter.length() != 0)) {
            if (sortParameter.equals("occupancy")) {
                Collections.sort(infoExecutionCourses, new ReverseComparator(new BeanComparator(
                        sortParameter)));
            } else {
                Collections.sort(infoExecutionCourses, new BeanComparator(sortParameter));
            }
        } else {
            Collections.sort(infoExecutionCourses,
                    new ReverseComparator(new BeanComparator("occupancy")));
        }
    }

    /**
     * @param infoExecutionCourseOccupancy
     */
    private void arranjeShifts(InfoExecutionCourseOccupancy infoExecutionCourseOccupancy) {

        // Note : This must be synched with TipoAula.java

        List theoreticalShifts = new ArrayList();
        List theoPraticalShifts = new ArrayList();
        List praticalShifts = new ArrayList();
        List labShifts = new ArrayList();
        List reserveShifts = new ArrayList();
        List doubtsShifts = new ArrayList();

        infoExecutionCourseOccupancy.setShiftsInGroups(new ArrayList());

        Iterator iterator = infoExecutionCourseOccupancy.getInfoShifts().iterator();
        while (iterator.hasNext()) {
            InfoShift infoShift = (InfoShift) iterator.next();
            if (infoShift.getTipo().equals(ShiftType.TEORICA)) {
                theoreticalShifts.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.PRATICA)) {
                praticalShifts.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.DUVIDAS)) {
                doubtsShifts.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.LABORATORIAL)) {
                labShifts.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.RESERVA)) {
                reserveShifts.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
                theoPraticalShifts.add(infoShift);
            }
        }
        infoExecutionCourseOccupancy.setInfoShifts(null);
        InfoShiftGroupStatistics infoShiftGroupStatistics = new InfoShiftGroupStatistics();
        if (!theoreticalShifts.isEmpty()) {
            infoShiftGroupStatistics.setShiftsInGroup(theoreticalShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

        if (!theoPraticalShifts.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(theoPraticalShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

        if (!labShifts.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(labShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

        if (!praticalShifts.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(praticalShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

        if (!reserveShifts.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(reserveShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

        if (!doubtsShifts.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(doubtsShifts);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

    }

    public ActionForward showLoads(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        Object args[] = { new Integer(request.getParameter("executionCourseOID")) };

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory
                .executeService(userView, "ReadExecutionCourseByOID", args);

        List curricularCoursesWithScopes = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadCurricularCourseScopesByExecutionCourseID", args);

        request.setAttribute("infoExecutionCourse", infoExecutionCourse);
        request.setAttribute("curricularCourses", curricularCoursesWithScopes);

        return mapping.findForward("showLoads");

    }

}