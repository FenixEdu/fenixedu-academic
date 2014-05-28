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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "AcademicAdminOffice", path = "academic-administration", titleKey = "portal.academicAdministration",
        hint = "Academic Administration", accessGroup = "academic(scope=ADMINISTRATION)")
@Mapping(path = "/index", module = "academicAdministration", parameter = "/academicAdministration/indexAdmin.jsp")
public class AcademicAdministrationApplication extends ForwardAction {

    private static final String BUNDLE = "AcademicAdminOffice";
    private static final String HINT = "Academic Administration";

    @StrutsApplication(bundle = BUNDLE, path = "academic-admin-office", titleKey = "label.academicAdminOffice", hint = HINT,
            accessGroup = "academic(scope=OFFICE)")
    @Mapping(path = "/indexOffice", module = "academicAdministration", parameter = "/academicAdministration/indexOffice.jsp")
    public static class AcademicAdminOfficeApp extends ForwardAction {
    }

    @StrutsApplication(bundle = BUNDLE, path = "student-listings", titleKey = "label.lists", hint = HINT,
            accessGroup = "academic(STUDENT_LISTINGS)")
    public static class AcademicAdminListingsApp {
    }

    @StrutsApplication(bundle = "ManagerResources", path = "manage-degree-curricular-plans",
            titleKey = "title.teaching.structure", hint = HINT, accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
    public static class AcademicAdminDCPApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "prices", titleKey = "label.pricesManagement", hint = HINT,
            accessGroup = "academic(MANAGE_PRICES)")
    public static class AcademicAdminPricesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "extra-curricular-activities", titleKey = "label.extraCurricularActivities",
            hint = HINT, accessGroup = "academic(MANAGE_EXTRA_CURRICULAR_ACTIVITIES)")
    public static class AcademicAdminExtraCurricularApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "scholarships", titleKey = "label.scolarships", hint = HINT,
            accessGroup = "academic(REPORT_STUDENTS_UTL_CANDIDATES)")
    public static class AcademicAdminScholarshipsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "students", titleKey = "link.studentOperations", hint = HINT,
            accessGroup = "academic(CREATE_REGISTRATION) | academic(scope=OFFICE)")
    public static class AcademicAdminStudentsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "academic-services", titleKey = "academic.services", hint = HINT,
            accessGroup = "(academic(SERVICE_REQUESTS) | academic(SERVICE_REQUESTS_RECTORAL_SENDING))")
    public static class AcademicAdminServicesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "documents", titleKey = "label.documents", hint = HINT,
            accessGroup = "academic(MANAGE_DOCUMENTS)")
    public static class AcademicAdminDocumentsApp {
    }

    @StrutsApplication(
            bundle = BUNDLE,
            path = "candidacies",
            titleKey = "label.candidacy.count",
            hint = HINT,
            accessGroup = "((academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES)) | academic(MANAGE_MOBILITY_OUTBOUND))")
    public static class AcademicAdminCandidaciesApp {
    }

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", hint = HINT,
            accessGroup = "(academic(MANAGE_PHD_ENROLMENT_PERIODS) | academic(MANAGE_PHD_PROCESSES))")
    public static class AcademicAdminPhdApp {
    }

    @StrutsApplication(
            bundle = "ManagerResources",
            path = "executions",
            titleKey = "title.executions",
            hint = HINT,
            accessGroup = "(academic(VIEW_SCHEDULING_OVERSIGHT) | (academic(MANAGE_ENROLMENT_PERIODS) | academic(MANAGE_EXECUTION_COURSES)))")
    public static class AcademicAdminExecutionsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "marksheets", titleKey = "label.navheader.marksSheet", hint = HINT,
            accessGroup = "academic(MANAGE_MARKSHEETS)")
    public static class AcademicAdminMarksheetApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "institutions", titleKey = "label.institutions", hint = HINT,
            accessGroup = "academic(MANAGE_EXTERNAL_UNITS)")
    public static class AcademicAdminInstitutionsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "academic-calendars", titleKey = "label.academic.calendars", hint = HINT,
            accessGroup = "academic(MANAGE_ACADEMIC_CALENDARS)")
    public static class AcademicAdminCalendarsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "equivalences", titleKey = "label.studentDismissal.equivalences", hint = HINT,
            accessGroup = "academic(MANAGE_EQUIVALENCES)")
    public static class AcademicAdminEquivalencesApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "contributors", hint = HINT,
            titleKey = "label.masterDegree.administrativeOffice.contributor", accessGroup = "academic(MANAGE_CONTRIBUTORS)")
    public static class AcademicAdminContributorsApp {
    }

    @StrutsApplication(
            bundle = BUNDLE,
            path = "payments",
            hint = HINT,
            titleKey = "label.payments.management",
            accessGroup = "(academic(CREATE_SIBS_PAYMENTS_REPORT) | (academic(MANAGE_EVENT_REPORTS) | academic(MANAGE_STUDENT_PAYMENTS_ADV)))")
    public static class AcademicAdminPaymentsApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = AcademicAdminDCPApp.class, path = "curricular-plans-management",
            titleKey = "label.manager.course.structure", accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
    @Mapping(path = "/bolonha/curricularPlans/curricularPlansManagement", module = "academicAdministration")
    public static class CurricularPlansManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "create-course-reports",
            titleKey = "link.manager.createCourseReports", accessGroup = "academic(MANAGE_EXECUTION_COURSES)")
    @Mapping(path = "/executionCourseManagement/createCourseReportsForExecutionPeriod", module = "academicAdministration")
    public static class CreateCourseReports extends FacesEntryPoint {
    }

}
