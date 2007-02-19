package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftGroupStatistics;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageExecutionCoursesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        // TODO : find a way to refactor this code so it is shared with context
        // selection.
        // this implies changing the form value from index to executionPeriodOID
        // etc.
        // The same should be done with the selection of the execution course
        // and
        // of the curricular year.

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsReadExecutionPeriods[] = {};
        List executionPeriods = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                "ReadExecutionPeriods", argsReadExecutionPeriods);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        if (selectedExecutionPeriod != null) {
            DynaActionForm searchExecutionCourse = (DynaActionForm) form;
            searchExecutionCourse.set("executionPeriodOID", selectedExecutionPeriod.getIdInternal()
                    .toString());
        }

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod
                    .getIdInternal().toString()));
        }

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        // //////////////////////////////////////////////////////////////////////////

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        /* Obtain a list of degrees for the specified execution year */
        Object argsLerLicenciaturas[] = { selectedExecutionPeriod.getInfoExecutionYear() };
        List executionDegreeList = null;
        try {                                  
            executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

            /* Sort the list of degrees */
            Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        } catch (FenixServiceException e) {
            e.printStackTrace();
        }

        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        /* Generate a label list for the above list of degrees */
        List labelListOfExecutionDegrees = ExecutionDegreesFormat.buildExecutionDegreeLabelValueBean(
                executionDegreeList, messageResources, request);// ContextUtils.getLabelListOfExecutionDegrees(executionDegreeList);
        request.setAttribute(SessionConstants.LIST_INFOEXECUTIONDEGREE, labelListOfExecutionDegrees);

        return mapping.findForward("ShowSearchForm");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        DynaActionForm searchExecutionCourse = (DynaActionForm) form;
        // Mandatory Selection
        Integer executionPeriodOID = new Integer((String) searchExecutionCourse
                .get("executionPeriodOID"));
        // Optional Selection
        Integer executionDegreeOID = null;
        String executionDegreeOIDString = (String) searchExecutionCourse.get("executionDegreeOID");

        if ((executionDegreeOIDString != null) && (!executionDegreeOIDString.equals("null"))) {
            if (executionDegreeOIDString.length() > 0) {
                executionDegreeOID = new Integer(executionDegreeOIDString);
            }
        }

        // Optional Selection
        Integer curricularYearOID = null;
        if (searchExecutionCourse.get("curricularYearOID") != null
                && !searchExecutionCourse.get("curricularYearOID").equals("")
                && !searchExecutionCourse.get("curricularYearOID").equals("null")) {
            curricularYearOID = new Integer((String) searchExecutionCourse.get("curricularYearOID"));
        }
        // Optional Selection
        String executionCourseName = (String) searchExecutionCourse.get("executionCourseName");

        // Set Context
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
        ContextUtils.setExecutionPeriodContext(request);

        if (executionDegreeOID != null) {
            request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeOID.toString());
        }
        ContextUtils.setExecutionDegreeContext(request);
        if (curricularYearOID != null) {
            request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYearOID.toString());
            ContextUtils.setCurricularYearContext(request);
        }
        request.setAttribute("execution_course_name", executionCourseName);

        // Call some service that queries the list of execution course.
        List infoExecutionCourses = null;

        Object args[] = { request.getAttribute(SessionConstants.EXECUTION_PERIOD),
                request.getAttribute(SessionConstants.EXECUTION_DEGREE),
                request.getAttribute(SessionConstants.CURRICULAR_YEAR), executionCourseName };
        infoExecutionCourses = (List) ServiceManagerServiceFactory.executeService(userView,
                "SearchExecutionCourses", args);

        // if query result is a list then go to a page where they are listed
        // if (infoExecutionCourses == null
        // || infoExecutionCourses.isEmpty()
        // || infoExecutionCourses.size() > 1) {

        if (infoExecutionCourses != null) {

            sortList(request, infoExecutionCourses);

            request.setAttribute(SessionConstants.LIST_INFOEXECUTIONCOURSE, infoExecutionCourses);

        }
        return prepareSearch(mapping, form, request, response);
        // if query result is a sigle item then go directly to the execution
        // course page
        // } else {
        // request.setAttribute(
        // SessionConstants.EXECUTION_COURSE,
        // infoExecutionCourses.get(0));
        // return mapping.findForward("ManageExecutionCourse");
        // }
    }

    /**
     * @param request
     * @param infoExecutionCourses
     */
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

    public ActionForward changeExecutionPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm searchExecutionCourse = (DynaActionForm) form;
        Integer executionPeriodOID = new Integer((String) searchExecutionCourse
                .get("executionPeriodOID"));
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOID.toString());
        ContextUtils.setExecutionPeriodContext(request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward showOccupancyLevels(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        Object args[] = { new Integer(request.getParameter("executionCourseOID")) };

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = (InfoExecutionCourseOccupancy) ServiceManagerServiceFactory
                .executeService(userView, "ReadShiftsByExecutionCourseID", args);

        arranjeShifts(infoExecutionCourseOccupancy);

        // Collections.sort(infoExecutionCourseOccupancy.getInfoShifts(), new
        // ReverseComparator(new BeanComparator("percentage")));

        request.setAttribute("infoExecutionCourseOccupancy", infoExecutionCourseOccupancy);
        return mapping.findForward("showOccupancy");

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
        List fieldWork = new ArrayList();
        List problems = new ArrayList();
        List seminary = new ArrayList();
        List trainingPeriod = new ArrayList();
        List tutorialOrientation = new ArrayList();

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
            } else if (infoShift.getTipo().equals(ShiftType.FIELD_WORK)) {
                fieldWork.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.PROBLEMS)) {
                problems.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.SEMINARY)) {
                seminary.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.TRAINING_PERIOD)) {
                trainingPeriod.add(infoShift);
            } else if (infoShift.getTipo().equals(ShiftType.TUTORIAL_ORIENTATION)) {
                tutorialOrientation.add(infoShift);
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

        if (!fieldWork.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(fieldWork);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }
        if (!problems.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(problems);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }
        if (!seminary.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(seminary);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }
        if (!trainingPeriod.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(trainingPeriod);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }
        if (!tutorialOrientation.isEmpty()) {
            infoShiftGroupStatistics = new InfoShiftGroupStatistics();
            infoShiftGroupStatistics.setShiftsInGroup(tutorialOrientation);
            infoExecutionCourseOccupancy.getShiftsInGroups().add(infoShiftGroupStatistics);
        }

    }

    public ActionForward showLoads(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute("UserView");

        Object args[] = { new Integer(request.getParameter("executionCourseOID")) };

        InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory
                .executeService(userView, "ReadExecutionCourseByOID", args);

        List scopes = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadCurricularCourseScopesByExecutionCourseID", args);

        request.setAttribute("infoExecutionCourse", infoExecutionCourse);
        request.setAttribute("curricularCourses", scopes);

        return mapping.findForward("showLoads");

    }

}