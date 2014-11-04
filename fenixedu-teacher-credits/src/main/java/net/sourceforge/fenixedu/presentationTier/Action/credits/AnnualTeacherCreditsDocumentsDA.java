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
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.AnnualTeachingCredits;
import org.fenixedu.academic.domain.credits.util.AnnualTeachingCreditsBean;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;

@Forwards(@Forward(name = "showAnnualTeacherCreditsDocument", path = "/credits/showAnnualTeacherCreditsDocument.jsp"))
public abstract class AnnualTeacherCreditsDocumentsDA extends FenixDispatchAction {

    public abstract ActionForward getAnnualTeachingCreditsPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    protected ActionForward getTeacherCreditsDocument(ActionMapping mapping, HttpServletRequest request, RoleType roleType) {
        Teacher teacher = (Teacher) request.getAttribute("teacher");
        ExecutionYear executionYear = (ExecutionYear) request.getAttribute("executionYear");

        AnnualTeachingCreditsBean annualTeachingCreditsBean = null;
        AnnualTeachingCredits annualTeachingCredits = AnnualTeachingCredits.readByYearAndTeacher(executionYear, teacher);
        if (annualTeachingCredits != null) {
            annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits, roleType);
        } else {
            annualTeachingCreditsBean = new AnnualTeachingCreditsBean(executionYear, teacher, roleType);
        }
        request.setAttribute("annualTeachingCreditsBean", annualTeachingCreditsBean);
        return mapping.findForward("showAnnualTeacherCreditsDocument");
    }

}