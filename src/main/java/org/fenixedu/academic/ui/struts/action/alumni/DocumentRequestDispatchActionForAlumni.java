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
package org.fenixedu.academic.ui.struts.action.alumni;

import org.fenixedu.academic.ui.struts.action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import org.fenixedu.academic.ui.struts.action.student.administrativeOfficeServices.DocumentRequestDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "create-document-request",
        titleKey = "link.academic.services.document.requirements")
@Mapping(module = "alumni", path = "/documentRequest", formBean = "documentRequestCreateForm")
@Forwards({
        @Forward(name = "createDocumentRequests",
                path = "/alumni/administrativeOfficeServices/documentRequests/createDocumentRequests.jsp"),
        @Forward(name = "createSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/createSuccess.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseRegistration",
                path = "/alumni/administrativeOfficeServices/documentRequests/chooseRegistration.jsp") })
public class DocumentRequestDispatchActionForAlumni extends DocumentRequestDispatchAction {

}