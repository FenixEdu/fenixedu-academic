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
package org.fenixedu.academic.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.util.PersonFunctionBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;

import pt.ist.fenixframework.FenixFramework;

@Forwards(value = { @Forward(name = "addPersonFunctionShared", path = "/credits/personFunction/addPersonFunctionShared.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class ManagePersonFunctionsDA extends FenixDispatchAction {

    public ActionForward prepareToAddPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunctionBean personFunctionBean = getRenderedObject();
        if (personFunctionBean == null) {
            Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
            ExecutionSemester executionSemester =
                    FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
            personFunctionBean = new PersonFunctionBean(teacher, executionSemester);
        }
        request.setAttribute("personFunctionBean", personFunctionBean);
        return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward prepareToEditPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunctionBean personFunctionBean = getRenderedObject();
        if (personFunctionBean == null) {
            PersonFunction personFunction = FenixFramework.getDomainObject((String) getFromRequest(request, "personFunctionOid"));
            personFunctionBean = new PersonFunctionBean(personFunction);
        }
        request.setAttribute("personFunctionBean", personFunctionBean);
        return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward editPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunctionBean personFunctionBean = getRenderedObject();
        try {
            personFunctionBean.createOrEditPersonFunction();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        request.setAttribute("personFunctionBean", personFunctionBean);
        return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward deletePersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunction personFunction = FenixFramework.getDomainObject((String) getFromRequest(request, "personFunctionOid"));
        PersonFunctionBean personFunctionBean = new PersonFunctionBean(personFunction);
        request.setAttribute("teacherOid", personFunction.getPerson().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", getFromRequest(request, "executionYearOid"));
        if (personFunction != null) {
            personFunctionBean.deletePersonFunction();
        }
        return mapping.findForward("viewAnnualTeachingCredits");
    }

}
