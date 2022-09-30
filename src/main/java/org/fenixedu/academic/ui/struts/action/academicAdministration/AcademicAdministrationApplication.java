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
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = "AcademicAdminOffice", path = "academic-administration", titleKey = "portal.academicAdministration", hint = "Academic Administration", accessGroup = "academic(scope=ADMINISTRATION)")
@Mapping(path = "/index", module = "academicAdministration", parameter = "/academicAdministration/indexAdmin.jsp")
public class AcademicAdministrationApplication extends ForwardAction {

	private static final String BUNDLE = "AcademicAdminOffice";
	private static final String HINT = "Academic Administration";

	@StrutsApplication(bundle = BUNDLE, path = "academic-admin-office", titleKey = "label.academicAdminOffice", hint = HINT, accessGroup = "academic(scope=OFFICE)")
	@Mapping(path = "/indexOffice", module = "academicAdministration", parameter = "/academicAdministration/indexOffice.jsp")
	public static class AcademicAdminOfficeApp extends ForwardAction {
	}

	@StrutsApplication(bundle = BUNDLE, path = "student-listings", titleKey = "label.lists", hint = HINT, accessGroup = "academic(STUDENT_LISTINGS)")
	public static class AcademicAdminListingsApp {
	}

	@StrutsApplication(bundle = "ManagerResources", path = "manage-degree-curricular-plans", titleKey = "title.teaching.structure", hint = HINT, accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
	public static class AcademicAdminDCPApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "prices", titleKey = "label.pricesManagement", hint = HINT, accessGroup = "#managers")
	public static class AcademicAdminPricesApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "extra-curricular-activities", titleKey = "label.extraCurricularActivities", hint = HINT, accessGroup = "academic(MANAGE_EXTRA_CURRICULAR_ACTIVITIES)")
	public static class AcademicAdminExtraCurricularApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "students", titleKey = "link.studentOperations", hint = HINT, accessGroup = "academic(CREATE_REGISTRATION) | academic(scope=OFFICE)")
	public static class AcademicAdminStudentsApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "academic-services", titleKey = "academic.services", hint = HINT, accessGroup = "academic(SERVICE_REQUESTS)")
	public static class AcademicAdminServicesApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "documents", titleKey = "label.documents", hint = HINT, accessGroup = "#managers")
	public static class AcademicAdminDocumentsApp {
	}

	@StrutsApplication(bundle = "ManagerResources", path = "executions", titleKey = "title.executions", hint = HINT, accessGroup = "academic(MANAGE_EXECUTION_COURSES)")
	public static class AcademicAdminExecutionsApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "institutions", titleKey = "label.institutions", hint = HINT, accessGroup = "academic(MANAGE_EXTERNAL_UNITS)")
	public static class AcademicAdminInstitutionsApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "academic-calendars", titleKey = "label.academic.calendars", hint = HINT, accessGroup = "#managers")
	public static class AcademicAdminCalendarsApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "equivalences", titleKey = "label.studentDismissal.equivalences", hint = HINT, accessGroup = "academic(MANAGE_EQUIVALENCES)")
	public static class AcademicAdminEquivalencesApp {
	}

	@StrutsApplication(bundle = BUNDLE, path = "payments", hint = HINT, titleKey = "label.payments.management", accessGroup = "academic(MANAGE_STUDENT_PAYMENTS_ADV)")
	public static class AcademicAdminPaymentsApp {
	}
}
