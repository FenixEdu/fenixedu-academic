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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.manager.SaveTeachersBody;
import org.fenixedu.academic.service.services.manager.UpdateNonAffiliatedTeachersProfessorship;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.InvalidArgumentsActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/saveTeachersBody", input = "/readTeacherInCharge.do",
        formBean = "masterDegreeCreditsForm", functionality = MasterDegreeCreditsManagementDispatchAction.class)
@Forwards(value = { @Forward(name = "readCurricularCourse",
        path = "/scientificCouncil/masterDegreeCreditsManagement.do?method=prepareEdit") })
@Exceptions(value = {
        @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
                handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = org.fenixedu.academic.ui.struts.action.exceptions.InvalidArgumentsActionException.class,
                key = "presentationTier.Action.exceptions.InvalidArgumentsActionException",
                handler = org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler.class, scope = "request") })
public class SaveTeachersBodyActionForScientificCouncil extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");
        DynaActionForm actionForm = (DynaActionForm) form;

        String[] responsibleTeachersIds = (String[]) actionForm.get("responsibleTeachersIds");
        String[] professorShipTeachersIds = (String[]) actionForm.get("professorShipTeachersIds");
        String[] nonAffiliatedTeachersIds = (String[]) actionForm.get("nonAffiliatedTeachersIds");

        List<String> respTeachersIds = Arrays.asList(responsibleTeachersIds);
        List<String> profTeachersIds = Arrays.asList(professorShipTeachersIds);
        List<String> nonAffilTeachersIds = Arrays.asList(nonAffiliatedTeachersIds);

        // TODO: Collections.sort(profTeachersIds, new BeanComparator("name"));
        Boolean result;

        try {
            result = SaveTeachersBody.runSaveTeachersBody(respTeachersIds, profTeachersIds, executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }

        try {
            UpdateNonAffiliatedTeachersProfessorship.runUpdateNonAffiliatedTeachersProfessorship(nonAffilTeachersIds,
                    executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }
        if (!result.booleanValue()) {
            throw new InvalidArgumentsActionException("message.non.existing.teachers");
        }

        return mapping.findForward("readCurricularCourse");
    }
}