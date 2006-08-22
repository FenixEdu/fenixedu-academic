package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageShiftsDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);

        /*
         * Obtain a list of shifts of specified degree for indicated curricular
         * year and execution period
         */
        Object args[] = { infoExecutionPeriod, infoExecutionDegree, infoCurricularYear };
        List infoShifts = (List) ServiceUtils.executeService(userView,
                "ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear", args);

        /* Sort the list of shifts */
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoDisciplinaExecucao.nome"));
        chainComparator.addComparator(new BeanComparator("tipo"));
        chainComparator.addComparator(new BeanComparator("nome"));
        Collections.sort(infoShifts, chainComparator);

        /* Place list of shifts in request */
        if (infoShifts != null && !infoShifts.isEmpty()) {
            request.setAttribute(SessionConstants.SHIFTS, infoShifts);
        }

        /* Place list of execution courses in request */
        SessionUtils.getExecutionCourses(request);

        /* Place label list of types of shifts/lessons in request */
        RequestUtils.setLessonTypes(request);

        return mapping.findForward("ShowShiftList");
    }

    public ActionForward createShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm createShiftForm = (DynaActionForm) form;

        InfoShiftEditor infoShift = new InfoShiftEditor();
        infoShift.setAvailabilityFinal(new Integer(0));
        InfoExecutionCourse infoExecutionCourse = RequestUtils.getExecutionCourseBySigla(request,
                (String) createShiftForm.get("courseInitials"));
        infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
        infoShift.setInfoLessons(null);
        infoShift.setLotacao((Integer) createShiftForm.get("lotacao"));
        infoShift.setNome((String) createShiftForm.get("nome"));
        infoShift.setTipo(ShiftType.valueOf((String) createShiftForm.get("tipoAula")));
        Object argsCriarTurno[] = { infoShift };
        try {
            final InfoShift newInfoShift = (InfoShift) ServiceUtils.executeService(userView, "CriarTurno", argsCriarTurno);
            request.setAttribute(SessionConstants.SHIFT, newInfoShift);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("O Shift", ex);
        }
        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);

        return mapping.findForward("EditShift");
    }

    public ActionForward deleteShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        ContextUtils.setShiftContext(request);

        InfoShift infoShiftToDelete = (InfoShift) request.getAttribute(SessionConstants.SHIFT);

        Object args[] = { infoShiftToDelete };
        try {
            ServiceUtils.executeService(userView, "DeleteShift", args);
        } catch (FenixServiceException exception) {
            ActionErrors actionErrors = new ActionErrors();
            if (exception.getMessage() != null && exception.getMessage().length() > 0) {
                actionErrors.add("errors.deleteshift", new ActionError(exception.getMessage()));
            } else {
                actionErrors.add("errors.deleteshift", new ActionError("error.deleteShift"));
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return listShifts(mapping, form, request, response);
    }

    public ActionForward deleteShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm deleteShiftsForm = (DynaActionForm) form;
        String[] selectedShifts = (String[]) deleteShiftsForm.get("selectedItems");

        if (selectedShifts.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.shifts.notSelected", new ActionError("errors.shifts.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        List shiftOIDs = new ArrayList();
        for (int i = 0; i < selectedShifts.length; i++) {
            shiftOIDs.add(new Integer(selectedShifts[i]));
        }

        Object args[] = { shiftOIDs };

        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteShifts", args);
        } catch (FenixServiceException exception) {
            ActionErrors actionErrors = new ActionErrors();
            if (exception.getMessage() != null && exception.getMessage().length() > 0) {
                actionErrors.add("errors.deleteshift", new ActionError(exception.getMessage()));
            } else {
                actionErrors.add("errors.deleteshift", new ActionError("error.deleteShift"));
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return mapping.findForward("ShowShiftList");

    }
}