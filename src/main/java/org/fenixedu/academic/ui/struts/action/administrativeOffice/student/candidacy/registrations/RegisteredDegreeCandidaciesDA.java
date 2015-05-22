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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student.candidacy.registrations;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminListingsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;

@StrutsFunctionality(app = AcademicAdminListingsApp.class, path = "registered-degree-candidacies",
        titleKey = "label.registeredDegreeCandidacies.first.time.list",
        accessGroup = "academic(MANAGE_REGISTERED_DEGREE_CANDIDACIES)")
@Mapping(path = "/registeredDegreeCandidacies", module = "academicAdministration")
@Forwards({ @Forward(name = "viewRegisteredDegreeCandidacies",
        path = "/academicAdminOffice/student/candidacies/registration/viewRegisteredDegreeCandidacies.jsp") })
public class RegisteredDegreeCandidaciesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        if (registeredDegreeCandidaciesSelectionBean == null) {
            request.setAttribute("bean", new RegisteredDegreeCandidaciesSelectionBean());
            return mapping.findForward("viewRegisteredDegreeCandidacies");
        }

        List<StudentCandidacy> studentCandidacies = registeredDegreeCandidaciesSelectionBean.search(getDegreesToSearch());
        request.setAttribute("studentCandidacies", studentCandidacies);

        return mapping.findForward("viewRegisteredDegreeCandidacies");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        Spreadsheet export = registeredDegreeCandidaciesSelectionBean.export(getDegreesToSearch());

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition",
                "attachment; filename=" + registeredDegreeCandidaciesSelectionBean.getFilename());
        export.exportToXLSSheet(response.getOutputStream());

        return null;
    }

    public ActionForward exportWithApplyForResidence(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        RegisteredDegreeCandidaciesSelectionBean registeredDegreeCandidaciesSelectionBean =
                getRegisteredDegreeCandidaciesSelectionBean();
        RegisteredDegreeCandidaciesWithApplyForResidence forResidence =
                new RegisteredDegreeCandidaciesWithApplyForResidence(registeredDegreeCandidaciesSelectionBean);

        Spreadsheet spreadsheet = forResidence.export(getDegreesToSearch());
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + forResidence.getFilename());
        spreadsheet.exportToXLSSheet(response.getOutputStream());

        return null;
    }

    private RegisteredDegreeCandidaciesSelectionBean getRegisteredDegreeCandidaciesSelectionBean() {
        return getRenderedObject("bean");
    }

    protected Set<Degree> getDegreesToSearch() {
        return AcademicAccessRule.getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_REGISTERED_DEGREE_CANDIDACIES,
                Authenticate.getUser()).collect(Collectors.toSet());
    }
}
