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
package org.fenixedu.academic.ui.struts.action.manager.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.manager.InsertProfessorShip;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/insertProfessorShipByNumber",
        input = "/insertProfessorShipByNumber.do?method=prepareInsert&page=0", formBean = "masterDegreeCreditsForm",
        functionality = MasterDegreeCreditsManagementDispatchAction.class)
@Forwards(value = { @Forward(name = "insertProfessorShip", path = "/manager/insertTeacherByNumber_bd.jsp"),
        @Forward(name = "readTeacherInCharge", path = "/scientificCouncil/readTeacherInCharge.do") })
@Exceptions(value = { @ExceptionHandling(
        type = org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException.class,
        key = "presentationTier.Action.exceptions.NonExistingActionException",
        handler = org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertProfessorShipByNumberDAForScientificCouncil extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final DynaActionForm form = (DynaValidatorForm) actionForm;
        final String teacherId = form.getString("id");
        final String executionCourseId = request.getParameter("executionCourseId");

        try {
            InsertProfessorShip.runInsertProfessorShip(executionCourseId, teacherId, Boolean.FALSE, 0.0);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
        return mapping.findForward("readTeacherInCharge");
    }
}