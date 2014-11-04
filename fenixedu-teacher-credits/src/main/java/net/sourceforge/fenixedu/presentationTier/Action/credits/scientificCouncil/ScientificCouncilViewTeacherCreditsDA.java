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
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.util.TeacherCreditsBean;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.credits.ViewTeacherCreditsDA;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "teacher-sheet", titleKey = "link.teacher.sheet")
@Mapping(module = "scientificCouncil", path = "/credits")
@Forwards({ @Forward(name = "selectTeacher", path = "/credits/selectTeacher.jsp"),
        @Forward(name = "showTeacherCredits", path = "/credits/showTeacherCredits.jsp"),
        @Forward(name = "showPastTeacherCredits", path = "/credits/showPastTeacherCredits.jsp"),
        @Forward(name = "showAnnualTeacherCredits", path = "/credits/showAnnualTeacherCredits.jsp") })
public class ScientificCouncilViewTeacherCreditsDA extends ViewTeacherCreditsDA {
    @Override
    public ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.SCIENTIFIC_COUNCIL);
    }

    @Override
    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        TeacherCreditsBean teacherBean = getRenderedObject();
        teacherBean.prepareAnnualTeachingCredits(RoleType.SCIENTIFIC_COUNCIL);
        request.setAttribute("teacherBean", teacherBean);
        return mapping.findForward("showTeacherCredits");
    }

    public ActionForward unlockTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionSemester);
        teacherService.unlockTeacherCredits();
        request.setAttribute("teacherOid", teacher.getExternalId());
        request.setAttribute("executionYearOid", executionSemester.getExecutionYear().getExternalId());
        return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.SCIENTIFIC_COUNCIL);
    }
}