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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.CriarTurno;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DeleteShift;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DeleteShifts;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/manageShifts", module = "resourceAllocationManager", input = "/manageShifts.do?method=listShifts",
formBean = "createShiftForm", functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "ShowShiftList", path = "/resourceAllocationManager/manageShifts_bd.jsp"),
    @Forward(name = "EditShift", path = "/resourceAllocationManager/manageShift.do?method=prepareEditShift") })
@Exceptions(@ExceptionHandling(handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class,
key = "resources.Action.exceptions.ExistingActionException", scope = "request"))
public class ManageShiftsDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    @Mapping(path = "/deleteShifts", module = "resourceAllocationManager", input = "/manageShifts.do?method=listShifts&page=0",
            formBean = "selectMultipleItemsForm", functionality = ExecutionPeriodDA.class)
    public static class DeleteShiftsDA extends ManageShiftsDA {

        private String getQueryParam(HttpServletRequest request, String name) {
            return Stream.of(name, (String) request.getAttribute(name)).collect(Collectors.joining("="));
        }

        private ActionForward redirectToShiftsList(HttpServletRequest request) {
            String url = Stream.of("/manageShifts.do?method=listShifts&page=0" , getQueryParam(request, PresentationConstants.ACADEMIC_INTERVAL),
                    getQueryParam(request, PresentationConstants.CURRICULAR_YEAR_OID),
                    getQueryParam(request, PresentationConstants.EXECUTION_DEGREE_OID)).collect(Collectors.joining("&"));

            return redirect(url , request);
        }

        public ActionForward deleteShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {

            ContextUtils.setShiftContext(request);

            InfoShift infoShiftToDelete = (InfoShift) request.getAttribute(PresentationConstants.SHIFT);

            try {
                DeleteShift.run(infoShiftToDelete);
            } catch (FenixServiceException exception) {
                ActionErrors actionErrors = new ActionErrors();
                if (exception.getMessage() != null && exception.getMessage().length() > 0) {
                    actionErrors.add("errors.deleteshift", new ActionError(exception.getMessage(), exception.getArgs()));
                } else {
                    actionErrors.add("errors.deleteshift", new ActionError("error.deleteShift"));
                }
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            return redirectToShiftsList(request);
        }

        public ActionForward deleteShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            DynaActionForm deleteShiftsForm = (DynaActionForm) form;
            String[] selectedShifts = (String[]) deleteShiftsForm.get("selectedItems");

            if (selectedShifts.length == 0) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("errors.shifts.notSelected", new ActionError("errors.shifts.notSelected"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }

            final List<String> shiftOIDs = new ArrayList<String>();
            for (String selectedShift : selectedShifts) {
                shiftOIDs.add(selectedShift);
            }

            try {
                DeleteShifts.run(shiftOIDs);
            } catch (FenixServiceMultipleException e) {
                final ActionErrors actionErrors = new ActionErrors();

                for (final DomainException domainException : e.getExceptionList()) {
                    actionErrors.add(domainException.getMessage(),
                            new ActionError(domainException.getMessage(), domainException.getArgs()));
                }
                saveErrors(request, actionErrors);

                return mapping.getInputForward();
            }

            return redirectToShiftsList(request);
        }
    }

    public ActionForward listShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        readAndSetInfoToManageShifts(request);
        return mapping.findForward("ShowShiftList");
    }

    public ActionForward listExecutionCourseCourseLoads(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        readAndSetInfoToManageShifts(request);

        DynaActionForm createShiftForm = (DynaActionForm) form;
        InfoExecutionCourse infoExecutionCourse =
                RequestUtils.getExecutionCourseBySigla(request, (String) createShiftForm.get("courseInitials"));

        if (infoExecutionCourse != null) {
            final List<LabelValueBean> tiposAula = new ArrayList<LabelValueBean>();
            for (final ShiftType shiftType : infoExecutionCourse.getExecutionCourse().getShiftTypes()) {
                tiposAula
                        .add(new LabelValueBean(BundleUtil.getString(Bundle.ENUMERATION, shiftType.getName()), shiftType.name()));
            }

            request.setAttribute("tiposAula", tiposAula);
        }

        return mapping.findForward("ShowShiftList");
    }

    public ActionForward createShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm createShiftForm = (DynaActionForm) form;

        InfoShiftEditor infoShift = new InfoShiftEditor();
        infoShift.setAvailabilityFinal(new Integer(0));
        InfoExecutionCourse infoExecutionCourse =
                RequestUtils.getExecutionCourseBySigla(request, (String) createShiftForm.get("courseInitials"));
        infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
        infoShift.setInfoLessons(null);
        infoShift.setLotacao((Integer) createShiftForm.get("lotacao"));
        infoShift.setNome((String) createShiftForm.get("nome"));

        String[] selectedShiftTypes = (String[]) createShiftForm.get("shiftTiposAula");
        if (selectedShiftTypes.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.shift.types.notSelected", new ActionError("errors.shift.types.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        final List<ShiftType> shiftTypes = new ArrayList<ShiftType>();
        for (String selectedShiftType : selectedShiftTypes) {
            shiftTypes.add(ShiftType.valueOf(selectedShiftType.toString()));
        }

        infoShift.setTipos(shiftTypes);

        // try {
        final InfoShift newInfoShift = CriarTurno.run(infoShift);
        request.setAttribute(PresentationConstants.SHIFT, newInfoShift);

        // } catch (ExistingServiceException ex) {
        // throw new ExistingActionException("O Shift", ex);
        // }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);

        return mapping.findForward("EditShift");
    }

    private void readAndSetInfoToManageShifts(HttpServletRequest request) throws FenixServiceException, Exception {
        ContextSelectionBean context = (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);
        List<InfoShift> infoShifts =
                ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear.run(context.getAcademicInterval(),
                        new InfoExecutionDegree(context.getExecutionDegree()),
                        new InfoCurricularYear(context.getCurricularYear()));

        Collections.sort(infoShifts, InfoShift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);

        if (infoShifts != null && !infoShifts.isEmpty()) {
            request.setAttribute(PresentationConstants.SHIFTS, infoShifts);
        }

        ManageShiftDA.getExecutionCourses(request);
    }
}