/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.dto.InfoLessonInstanceAggregation;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.service.services.resourceAllocationManager.EditarTurma;
import org.fenixedu.academic.service.services.resourceAllocationManager.LerAulasDeTurma;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadAvailableShiftsForClass;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadShiftByOID;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadShiftsByClass;
import org.fenixedu.academic.service.services.resourceAllocationManager.RemoveShifts;
import org.fenixedu.academic.service.services.resourceAllocationManager.RemoverTurno;
import org.fenixedu.academic.ui.struts.action.exceptions.ExistingActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/manageClass", module = "resourceAllocationManager", formBean = "classForm",
        functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "EditClass", path = "/resourceAllocationManager/manageClass_bd.jsp"),
        @Forward(name = "AddShifts", path = "/resourceAllocationManager/addShifts_bd.jsp"),
        @Forward(name = "ViewSchedule", path = "/resourceAllocationManager/viewClassSchedule_bd.jsp") })
@Exceptions(@ExceptionHandling(handler = FenixErrorExceptionHandler.class, type = ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException", scope = "request"))
public class ManageClassDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    @Mapping(path = "/removeShifts", module = "resourceAllocationManager", input = "/manageClass.do?method=prepare",
            formBean = "selectMultipleItemsForm", functionality = ExecutionPeriodDA.class)
    @Forwards(@Forward(name = "EditClass", path = "/resourceAllocationManager/manageClass.do?method=prepare"))
    public static class RemoveShiftsDA extends ManageClassDA {
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());
        request.setAttribute("schoolClass", schoolClass);

        // Fill out the form with the name of the class
        DynaActionForm classForm = (DynaActionForm) form;
        classForm.set("className", schoolClass.getEditablePartOfName());

        // Get list of shifts and place them in request

        List<InfoShift> infoShifts = (List<InfoShift>) ReadShiftsByClass.run(infoClass);

        if (infoShifts != null && !infoShifts.isEmpty()) {
            Collections.sort(infoShifts, InfoShift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
            request.setAttribute(PresentationConstants.SHIFTS, infoShifts);
        }

        return mapping.findForward("EditClass");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaValidatorForm classForm = (DynaValidatorForm) form;

        String className = (String) classForm.get("className");

        User userView = Authenticate.getUser();

        InfoClass infoClassOld = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        InfoClass infoClassNew = null;
        try {
            infoClassNew = (InfoClass) EditarTurma.run(infoClassOld.getExternalId(), className);
        } catch (DomainException e) {
            throw new ExistingActionException("A SchoolClass", e);
        }

        request.removeAttribute(PresentationConstants.CLASS_VIEW);
        request.setAttribute(PresentationConstants.CLASS_VIEW, infoClassNew);

        return prepare(mapping, form, request, response);
    }

    public ActionForward removeShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        String shiftOID = request.getParameter(PresentationConstants.SHIFT_OID);

        InfoShift infoShift = ReadShiftByOID.run(shiftOID);

        RemoverTurno.run(infoShift, infoClass);

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareAddShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        // Get list of available shifts and place them in request

        List<InfoShift> infoShifts = (List<InfoShift>) ReadAvailableShiftsForClass.run(infoClass);

        /* Sort the list of shifts */
        Collections.sort(infoShifts, InfoShift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);

        /* Place list of shifts in request */
        request.setAttribute(PresentationConstants.SHIFTS, infoShifts);

        return mapping.findForward("AddShifts");
    }

    public ActionForward viewSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        // Fill out the form with the name of the class
        DynaActionForm classForm = (DynaActionForm) form;
        classForm.set("className", infoClass.getNome());

        request.setAttribute("className", infoClass.getNome());

        // Place list of lessons in request

        /** InfoLesson List */
        List<InfoLessonInstanceAggregation> lessonList = LerAulasDeTurma.run(infoClass);

        request.setAttribute(PresentationConstants.LESSON_LIST_ATT, lessonList);

        return mapping.findForward("ViewSchedule");
    }

    public ActionForward removeShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm removeShiftsForm = (DynaActionForm) form;
        String[] selectedShifts = (String[]) removeShiftsForm.get("selectedItems");

        if (selectedShifts.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.shifts.notSelected", new ActionError("errors.shifts.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        List<String> shiftOIDs = new ArrayList<String>();
        for (String selectedShift : selectedShifts) {
            shiftOIDs.add(selectedShift);
        }

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        RemoveShifts.run(infoClass, shiftOIDs);

        return mapping.findForward("EditClass");

    }

}