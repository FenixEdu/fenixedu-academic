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
package net.sourceforge.fenixedu.domain.accessControl.academicAdministration;

import java.util.Comparator;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum AcademicOperationType implements IPresentableEnum {
    MANAGE_AUTHORIZATIONS(false, false, Scope.ADMINISTRATION),

    MANAGE_EQUIVALENCES(true, true, Scope.ADMINISTRATION),  // Migrated from Manager

    MANAGE_ACADEMIC_CALENDARS(false, false, Scope.ADMINISTRATION),  // Migrated from Manager

    /*
     * Student stuff
     */

    EDIT_STUDENT_PERSONAL_DATA(true, true, Scope.OFFICE),

    STUDENT_ENROLMENTS(true, true, Scope.OFFICE),

    MANAGE_REGISTRATIONS(true, true, Scope.OFFICE),

    MANAGE_STATUTES(false, false, Scope.OFFICE),

    MANAGE_CONCLUSION(true, true, Scope.OFFICE),

    UPDATE_REGISTRATION_AFTER_CONCLUSION(true, true, Scope.OFFICE),

    REPEAT_CONCLUSION_PROCESS(true, true, Scope.OFFICE),

    ENROLMENT_WITHOUT_RULES(true, true, Scope.OFFICE),

    MOVE_CURRICULUM_LINES_WITHOUT_RULES(true, true, Scope.OFFICE),

    REPORT_STUDENTS_UTL_CANDIDATES(true, true, Scope.ADMINISTRATION),

    MANAGE_REGISTERED_DEGREE_CANDIDACIES(true, true, Scope.ADMINISTRATION),

    MANAGE_ENROLMENT_PERIODS(true, true, Scope.ADMINISTRATION),     // Migrated from Manager

    /*
     * Mark Sheets
     */

    MANAGE_MARKSHEETS(true, true, Scope.ADMINISTRATION),

    RECTIFICATION_MARKSHEETS(true, true, Scope.ADMINISTRATION),

    REMOVE_GRADES(true, true, Scope.ADMINISTRATION),    // Migrated from Manager

    DISSERTATION_MARKSHEETS(true, true, Scope.ADMINISTRATION),

    REGISTRATION_CONCLUSION_CURRICULUM_VALIDATION(true, true, Scope.OFFICE),

    CREATE_REGISTRATION(true, true, Scope.OFFICE),

    STUDENT_LISTINGS(true, true, Scope.ADMINISTRATION),

    SERVICE_REQUESTS(true, true, Scope.OFFICE),

    SERVICE_REQUESTS_RECTORAL_SENDING(true, true, Scope.OFFICE),

    MANAGE_EXECUTION_COURSES(true, true, Scope.ADMINISTRATION), // Migrated from Manager

    MANAGE_EXECUTION_COURSES_ADV(true, true, Scope.ADMINISTRATION), // Migrated from Manager

    MANAGE_DEGREE_CURRICULAR_PLANS(true, true, Scope.ADMINISTRATION),

    MANAGE_EVENT_REPORTS(true, false, Scope.ADMINISTRATION),

    // Student Section

    MANAGE_STUDENT_PAYMENTS(true, false, Scope.OFFICE),

    MANAGE_STUDENT_PAYMENTS_ADV(true, true, Scope.ADMINISTRATION),  // Migrated from Manager

    CREATE_SIBS_PAYMENTS_REPORT(false, false, Scope.ADMINISTRATION),  // Migrated from Manager

    MANAGE_ACCOUNTING_EVENTS(true, true, Scope.OFFICE),

    /* End of Payments */

    MANAGE_PRICES(true, false, Scope.ADMINISTRATION),

    MANAGE_EXTRA_CURRICULAR_ACTIVITIES(false, false, Scope.ADMINISTRATION),

    MANAGE_EXTERNAL_UNITS(false, false, Scope.ADMINISTRATION),

    /* Candidacies Management */

    MANAGE_INDIVIDUAL_CANDIDACIES(true, true, Scope.OFFICE),

    MANAGE_CANDIDACY_PROCESSES(true, true, Scope.OFFICE),

    /* End of Candidacies Management */

    VIEW_FULL_STUDENT_CURRICULUM(true, true, Scope.OFFICE),

    MANAGE_CONTRIBUTORS(true, true, Scope.ADMINISTRATION),

    MANAGE_DOCUMENTS(true, true, Scope.OFFICE),

    /* Phd Management */

    VIEW_PHD_CANDIDACY_ALERTS(true, true, Scope.OFFICE),

    VIEW_PHD_PUBLIC_PRESENTATION_ALERTS(true, true, Scope.OFFICE),

    VIEW_PHD_THESIS_ALERTS(true, true, Scope.OFFICE),

    MANAGE_PHD_ENROLMENT_PERIODS(true, true, Scope.OFFICE),

    MANAGE_PHD_PROCESSES(true, true, Scope.OFFICE),

    MANAGE_PHD_PROCESS_STATE(true, true, Scope.OFFICE),

    MANAGE_MOBILITY_OUTBOUND(false, false, Scope.OFFICE),

    VIEW_SCHEDULING_OVERSIGHT(false, false, Scope.ADMINISTRATION);

    public static enum Scope {
        OFFICE, ADMINISTRATION;
    }

    private boolean allowOffices;

    private boolean allowPrograms;

    private Scope scope;

    static public Comparator<AcademicOperationType> COMPARATOR_BY_LOCALIZED_NAME = new Comparator<AcademicOperationType>() {
        @Override
        public int compare(final AcademicOperationType p1, final AcademicOperationType p2) {
            String operationName1 = p1.getLocalizedName();
            String operationName2 = p2.getLocalizedName();
            int res = operationName1.compareTo(operationName2);
            return res;
        }
    };

    private AcademicOperationType(boolean allowOffices, boolean allowPrograms, Scope scope) {
        this.allowOffices = allowOffices;
        this.allowPrograms = allowPrograms;
        this.scope = scope;
    }

    public boolean isOfficeAllowedAsTarget() {
        return allowOffices;
    }

    public boolean isProgramAllowedAsTarget() {
        return allowPrograms;
    }

    public boolean isOfScope(Scope scope) {
        return this.scope.equals(scope);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getClass().getName() + "." + name());
    }
}