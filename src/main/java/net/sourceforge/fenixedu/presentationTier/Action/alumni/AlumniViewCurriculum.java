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
package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicPathApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "view-curriculum", titleKey = "link.student.curriculum")
@Mapping(path = "/viewStudentCurriculum", module = "alumni", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/curriculum/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "alumni.view.curriculum.not.authorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "alumni.view.curriculum.validate.identity", path = "/alumni/notAuthorizedValidateIdentity.jsp") })
public class AlumniViewCurriculum extends CurriculumDispatchAction {

    @EntryPoint
    public ActionForward checkValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (getLoggedPerson(request).getStudent().getAlumni() != null) {

            if (getLoggedPerson(request).getStudent().getAlumni().hasAnyPendingIdentityRequests()) {
                return mapping.findForward("alumni.view.curriculum.validate.identity");
            }
            return super.prepare(mapping, form, request, response);
        } else {
            if (getLoggedPerson(request).hasRole(RoleType.ALUMNI)) {
                return super.prepare(mapping, form, request, response);
            } else {
                return mapping.findForward("alumni.view.curriculum.not.authorized");
            }
        }
    }
}
