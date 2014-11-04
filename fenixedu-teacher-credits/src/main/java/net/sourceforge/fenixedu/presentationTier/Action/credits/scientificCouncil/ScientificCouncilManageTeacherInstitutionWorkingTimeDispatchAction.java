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
package org.fenixedu.academic.ui.struts.action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/institutionWorkingTimeManagement",
        input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0", formBean = "teacherInstitutionWorkingTimeForm",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Exceptions(value = { @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ScientificCouncilManageTeacherInstitutionWorkingTimeDispatchAction extends
        ManageTeacherInstitutionWorkingTimeDispatchAction {

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {
        return delete(mapping, form, request, response, RoleType.SCIENTIFIC_COUNCIL);
    }
}