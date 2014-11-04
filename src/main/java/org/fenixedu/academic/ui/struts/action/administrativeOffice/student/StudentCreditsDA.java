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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean;
import org.fenixedu.academic.service.services.administrativeOffice.dismissal.CreateNewCreditsDismissal;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/studentCredits", module = "academicAdministration", formBean = "studentDismissalForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseCreditEquivalents.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration"),
        @Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseCreditEnrolments.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateCredit.jsp"),
        @Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseCreditNotNeedToEnrol.jsp") })
public class StudentCreditsDA extends StudentDismissalsDA {

    @Override
    protected void executeCreateDismissalService(DismissalBean dismissalBean) {
        CreateNewCreditsDismissal.run(dismissalBean);
    }

}
