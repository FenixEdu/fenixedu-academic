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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.enrollments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts.StudentNotFoundServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ExecutionPeriodDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "resourceAllocationManager", path = "/enrollStudentInShifts",
        input = "/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment",
        formBean = "studentShiftEnrollmentForm", validate = false, functionality = ExecutionPeriodDA.class)
@Forwards(@Forward(name = "enrollmentConfirmation",
        path = "/resourceAllocationManager/studentShiftEnrollmentManagerLookup.do?method=proceedToShiftEnrolment"))
public class EnrollStudentInShiftsAction extends FenixAction {

    private static final Logger logger = LoggerFactory.getLogger(EnrollStudentInShiftsAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final String shiftId = request.getParameter("shiftId");
        if (!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {
            request.setAttribute("executionCourseID", request.getParameter("executionCourseID"));
        }

        try {
            ShiftEnrollmentErrorReport errorReport =
                    EnrollStudentInShifts.runEnrollStudentInShifts(getRegistration(request), shiftId);

            if (errorReport.getUnAvailableShifts().size() > 0) {
                for (final Shift shift : (List<Shift>) errorReport.getUnAvailableShifts()) {
                    if (shift.getLotacao().intValue() == 0) {
                        addActionMessage(request, "error.shift.enrollment.capacityLocked", shift.getNome());
                    } else {
                        addActionMessage(request, "error.shift.enrollment.capacityExceded", shift.getNome());
                    }
                }
            }
            if (errorReport.getUnExistingShifts().size() > 0) {
                addActionMessage(request, "error.shift.enrollment.nonExistingShift");
            }
        } catch (StudentNotFoundServiceException e) {
            logger.error(e.getMessage(), e);
            addActionMessage(request, "error.shift.enrollment.nonExistingStudent");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            logger.error(e.getMessage(), e);
            addActionMessage(request, e.getMessage());
            return mapping.getInputForward();
        }

        saveMessages(request);
        return mapping.findForward("enrollmentConfirmation");
    }

    private Registration getRegistration(HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("registrationOID"));
    }

}
