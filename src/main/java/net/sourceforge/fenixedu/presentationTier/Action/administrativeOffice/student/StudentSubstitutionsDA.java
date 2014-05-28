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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal.CreateNewSubstitutionDismissal;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentSubstitutions", module = "academicAdministration", formBean = "studentDismissalForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseSubstitutionEquivalents.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration"),
        @Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseSubstitutionEnrolments.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateSubstitution.jsp"),
        @Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseSubstitutionNotNeedToEnrol.jsp") })
public class StudentSubstitutionsDA extends StudentDismissalsDA {

    @Override
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DismissalBean dismissalBean = getRenderedObject("dismissalBean");
        if (dismissalBean.getSelectedEnrolments().isEmpty()) {
            addActionMessage(request, "error.createSubstitution.origin.cannot.be.empty");
            return prepare(mapping, form, request, response);
        }

        return super.chooseEquivalents(mapping, form, request, response);
    }

    @Override
    protected void executeCreateDismissalService(DismissalBean dismissalBean) {
        CreateNewSubstitutionDismissal.run(dismissalBean);
    }

}
