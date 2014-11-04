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

import org.fenixedu.academic.ui.struts.action.alumni.AlumniApplication.AlumniAcademicPathApp;
import org.fenixedu.academic.ui.struts.action.student.dataSharingAuthorization.StudentDataShareAuthorizationDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "data-share-authorization",
        titleKey = "title.student.dataShareAuthorizations.short", bundle = "StudentResources")
@Mapping(path = "/studentDataShareAuthorization", module = "alumni")
@Forwards({ @Forward(name = "authorizations", path = "/student/dataShareAuthorization/manageAuthorizations.jsp"),
        @Forward(name = "historic", path = "/student/dataShareAuthorization/authorizationHistory.jsp") })
public class AlumniDataShareAuthorizationDispatchAction extends StudentDataShareAuthorizationDispatchAction {
}
