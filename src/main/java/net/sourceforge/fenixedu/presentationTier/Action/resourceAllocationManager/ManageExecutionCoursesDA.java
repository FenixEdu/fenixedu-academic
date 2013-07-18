package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularCourseScopesByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadShiftsByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.SearchExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftGroupStatistics;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageExecutionCoursesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ShowSearchForm");
    }

    public ActionForward choosePostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ShowSearchForm");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ContextSelectionBean contextSelectionBean =
                (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);

        request.setAttribute(
                PresentationConstants.CURRICULAR_YEAR_OID,
                contextSelectionBean.getCurricularYear() != null ? contextSelectionBean.getCurricularYear().getIdInternal() : null);
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, contextSelectionBean.getExecutionDegree()
                .getIdInternal());

        request.setAttribute("execution_course_name", contextSelectionBean.getCourseName().replaceAll("%", "%25"));

        List<InfoExecutionCourse> infoExecutionCourses = null;

        if (contextSelectionBean.getCurricularYear() == null) {
            infoExecutionCourses =
                    SearchExecutionCourses.runSearchExecutionCourses(contextSelectionBean.getAcademicInterval(),
                            contextSelectionBean.getExecutionDegree(), contextSelectionBean.getCourseName());
        } else {
            infoExecutionCourses =
                    SearchExecutionCourses.runSearchExecutionCourses(contextSelectionBean.getAcademicInterval(),
                            contextSelectionBean.getExecutionDegree(), contextSelectionBean.getCurricularYear(),
                            contextSelectionBean.getCourseName());
        }

        if (infoExecutionCourses != null) {

            sortList(request, infoExecutionCourses);

            request.setAttribute(PresentationConstants.LIST_INFOEXECUTIONCOURSE, infoExecutionCourses);

        }
        return mapping.findForward("ShowSearchForm");
    }

    /**
     * @param request
     * @param infoExecutionCourses
     */
    private void sortList(HttpServletRequest request, List<InfoExecutionCourse> infoExecutionCourses) {
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

    public ActionForward showOccupancyLevels(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy =
                ReadShiftsByExecutionCourseID.runReadShiftsByExecutionCourseID(new Integer(request
                        .getParameter("executionCourseOID")));

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
            } else if (infoShift.containsType(ShiftType.FIELD_WORK)) {
                fieldWork.add(infoShift);
            } else if (infoShift.containsType(ShiftType.PROBLEMS)) {
                problems.add(infoShift);
            } else if (infoShift.containsType(ShiftType.SEMINARY)) {
                seminary.add(infoShift);
            } else if (infoShift.containsType(ShiftType.TRAINING_PERIOD)) {
                trainingPeriod.add(infoShift);
            } else if (infoShift.containsType(ShiftType.TUTORIAL_ORIENTATION)) {
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

        Integer executionCourceOId = new Integer(request.getParameter("executionCourseOID"));
        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourceOId);

        List scopes = ReadCurricularCourseScopesByExecutionCourseID.run(executionCourceOId);

        request.setAttribute("infoExecutionCourse", infoExecutionCourse);
        request.setAttribute("curricularCourses", scopes);

        return mapping.findForward("showLoads");

    }

}