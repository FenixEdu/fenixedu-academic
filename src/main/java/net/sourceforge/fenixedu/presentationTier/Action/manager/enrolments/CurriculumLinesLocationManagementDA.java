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
package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.AbstractCurriculumLinesLocationManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/curriculumLinesLocationManagement", module = "manager", functionality = BolonhaEnrolmentsManagementDA.class)
@Forwards({
        @Forward(name = "showCurriculum", path = "/manager/curriculum/curriculumLines/location/showCurriculum.jsp"),
        @Forward(name = "chooseNewLocation", path = "/manager/curriculum/curriculumLines/location/chooseNewLocation.jsp"),
        @Forward(name = "backToChooseStudentCurricularPlan",
                path = "/manager/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans") })
public class CurriculumLinesLocationManagementDA extends AbstractCurriculumLinesLocationManagementDA {

    @Override
    protected boolean isWithRules(final HttpServletRequest request) {
        return false;
    }

    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("studentId", getStudentCurricularPlan(request).getRegistration().getStudent().getExternalId());
        return mapping.findForward("backToChooseStudentCurricularPlan");
    }

}
