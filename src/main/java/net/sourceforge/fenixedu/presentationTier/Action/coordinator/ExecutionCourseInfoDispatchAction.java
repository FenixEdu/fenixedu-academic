/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularCourseScopesByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodsByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodsByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadShiftsByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.SearchExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftGroupStatistics;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *
 */
@Mapping(path = "/executionCoursesInformation", module = "coordinator", input = "/courses/searchCurricularCourses.jsp",
        formBean = "searchExecutionCourses", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "ReadyToSearch", path = "/coordinator/courses/searchCurricularCourses_bd.jsp"),
        @Forward(name = "ShowCurricularCourseList", path = "/coordinator/courses/showCurricularCourses_bd.jsp"),
        @Forward(name = "showOccupancy", path = "/coordinator/courses/showOccupancyLevels_bd.jsp"),
        @Forward(name = "showLoads", path = "/coordinator/courses/showLoads_bd.jsp"),
        @Forward(name = "notAuthorized", path = "/coordinator/notAuthorizedSimple.jsp") })
public class ExecutionCourseInfoDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareChoice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(PresentationConstants.MASTER_DEGREE);

        List executionPeriods =
                ReadExecutionPeriodsByExecutionYear.run(infoExecutionDegree.getInfoExecutionYear().getExternalId());

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getExternalId().toString()));
        }

        request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
        request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        return mapping.findForward("ReadyToSearch");
    }

    public ActionForward prepareChoiceForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = getUserView(request);

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        List executionPeriods = ReadExecutionPeriodsByDegreeCurricularPlan.run(degreeCurricularPlanID);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getExternalId().toString()));
        }

        request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = ContextUtils.getLabelListOfOptionalCurricularYears();
        request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        return mapping.findForward("ReadyToSearch");
    }

    public ActionForward getExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionDegree infoExecutionDegree = (ExecutionDegree) request.getAttribute(PresentationConstants.MASTER_DEGREE);

        DynaActionForm searchExecutionCourse = (DynaActionForm) form;

        String degreeCurricularPlanID = (String) searchExecutionCourse.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        // Mandatory Selection
        String executionPeriodOID = null;
        if (!StringUtils.isEmpty(((String) searchExecutionCourse.get("executionPeriodOID")))) {
            executionPeriodOID = (String) searchExecutionCourse.get("executionPeriodOID");
        } else {
            executionPeriodOID = request.getParameter("executionPeriodOID");
        }
        request.setAttribute("executionPeriodOID", executionPeriodOID);

        // Optional Selection
        String curricularYearOID = null;
        InfoCurricularYear infoCurricularYear = null;
        if (searchExecutionCourse.get("curricularYearOID") != null && !searchExecutionCourse.get("curricularYearOID").equals("")
                && !searchExecutionCourse.get("curricularYearOID").equals("null")) {
            curricularYearOID = (String) searchExecutionCourse.get("curricularYearOID");
            infoCurricularYear = new InfoCurricularYear(FenixFramework.<CurricularYear> getDomainObject(curricularYearOID));
            request.setAttribute("curricularYearOID", curricularYearOID);
        } else {
            if ((request.getParameter("curricularYearOID") != null) && (request.getParameter("curricularYearOID").length() != 0)
                    && (!searchExecutionCourse.get("curricularYearOID").equals("null"))) {
                infoCurricularYear =
                        new InfoCurricularYear(FenixFramework.<CurricularYear> getDomainObject(request
                                .getParameter("curricularYearOID")));
            }
        }

        // Optional Selection
        String executionCourseName = (String) searchExecutionCourse.get("executionCourseName");
        if ((executionCourseName != null) && (executionCourseName.length() == 0)) {
            executionCourseName = request.getParameter("executionCourseName");
        }
        request.setAttribute("executionCourseName", executionCourseName);

        InfoExecutionPeriod infoExecutionPeriod =
                InfoExecutionPeriod.newInfoFromDomain(FenixFramework.<ExecutionSemester> getDomainObject(executionPeriodOID));

        if ((executionCourseName != null) && (executionCourseName.length() == 0)) {
            executionCourseName = null;
        }

        List infoExecutionCourses = null;
        try {
            infoExecutionCourses =
                    SearchExecutionCourses.runSearchExecutionCourses(infoExecutionPeriod,
                            InfoExecutionDegree.newInfoFromDomain(infoExecutionDegree), infoCurricularYear, executionCourseName);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("notAuthorized");
        }

        if (infoExecutionCourses != null) {
            sortList(request, infoExecutionCourses);
            request.setAttribute(PresentationConstants.LIST_INFOEXECUTIONCOURSE, infoExecutionCourses);
        }

        return mapping.findForward("ShowCurricularCourseList");
    }

    public ActionForward showOccupancyLevels(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy =
                ReadShiftsByExecutionCourseID.runReadShiftsByExecutionCourseID(request.getParameter("executionCourseOID"));

        arranjeShifts(infoExecutionCourseOccupancy);

        request.setAttribute("infoExecutionCourseOccupancy", infoExecutionCourseOccupancy);
        return mapping.findForward("showOccupancy");

    }

    private void sortList(HttpServletRequest request, List infoExecutionCourses) {
        String sortParameter = request.getParameter("sortBy");
        if ((sortParameter != null) && (sortParameter.length() != 0)) {
            if (sortParameter.equals("occupancy")) {
                Collections.sort(infoExecutionCourses, new ReverseComparator(new BeanComparator(sortParameter)));
            } else {
                Collections.sort(infoExecutionCourses, new BeanComparator(sortParameter));
            }
        } else {
            Collections.sort(infoExecutionCourses, new ReverseComparator(new BeanComparator("occupancy")));
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
            if (infoShift.containsType(ShiftType.TEORICA)) {
                theoreticalShifts.add(infoShift);
            } else if (infoShift.containsType(ShiftType.PRATICA)) {
                praticalShifts.add(infoShift);
            } else if (infoShift.containsType(ShiftType.DUVIDAS)) {
                doubtsShifts.add(infoShift);
            } else if (infoShift.containsType(ShiftType.LABORATORIAL)) {
                labShifts.add(infoShift);
            } else if (infoShift.containsType(ShiftType.RESERVA)) {
                reserveShifts.add(infoShift);
            } else if (infoShift.containsType(ShiftType.TEORICO_PRATICA)) {
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

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(request.getParameter("executionCourseOID"));

        List curricularCoursesWithScopes =
                ReadCurricularCourseScopesByExecutionCourseID.run(request.getParameter("executionCourseOID"));

        request.setAttribute("infoExecutionCourse", infoExecutionCourse);
        request.setAttribute("curricularCourses", curricularCoursesWithScopes);

        return mapping.findForward("showLoads");

    }

}