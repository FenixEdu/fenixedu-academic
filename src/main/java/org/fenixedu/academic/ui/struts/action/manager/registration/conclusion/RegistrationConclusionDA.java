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
package org.fenixedu.academic.ui.struts.action.manager.registration.conclusion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.enrolments.BolonhaEnrolmentsManagementDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;

@Mapping(module = "manager", path = "/registrationConclusion", input = "show.markSheetManagement.search.page", scope = "request",
        parameter = "method", functionality = BolonhaEnrolmentsManagementDA.class)
@Forwards(value = { @Forward(name = "editForRegistration", path = "/manager/registration/conclusion/editForRegistration.jsp"),
        @Forward(name = "showForRegistration", path = "/manager/registration/conclusion/showForRegistration.jsp"),
        @Forward(name = "showByCycles", path = "/manager/registration/conclusion/showByCycles.jsp"),
        @Forward(name = "editForCycle", path = "/manager/registration/conclusion/editForCycle.jsp") })
public class RegistrationConclusionDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Registration registration = getRegistration(request);
        if (registration.isBolonha()) {
            request.setAttribute("student", registration.getStudent());
            request.setAttribute("registrationConclusionBeans", buildRegistrationConclusionBeansForCycles(registration));
            return mapping.findForward("showByCycles");
        } else {
            request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(registration));
            return mapping.findForward("showForRegistration");
        }

    }

    private List<RegistrationConclusionBean> buildRegistrationConclusionBeansForCycles(final Registration registration) {
        final List<RegistrationConclusionBean> result = new ArrayList<RegistrationConclusionBean>();
        for (final CycleCurriculumGroup cycleCurriculumGroup : registration.getLastStudentCurricularPlan()
                .getInternalCycleCurriculumGrops()) {
            result.add(new RegistrationConclusionBean(registration, cycleCurriculumGroup));
        }

        return result;
    }

    private Registration getRegistration(final HttpServletRequest request) {
        return getDomainObject(request, "registrationId");
    }

    public ActionForward prepareEditForRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("registration", getRegistration(request));

        return mapping.findForward("editForRegistration");
    }

    public ActionForward prepareRemoveConclusionForRegistration(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("registration", getRegistration(request));

        return mapping.findForward("confirmRemoveConclusion");
    }

    public ActionForward prepareEditForCycle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("cycleCurriculumGroup", getCycleCurriculumGroup(request));

        return mapping.findForward("editForCycle");
    }

    private CycleCurriculumGroup getCycleCurriculumGroup(HttpServletRequest request) {
        return getDomainObject(request, "cycleCurriculumGroupId");
    }
}
