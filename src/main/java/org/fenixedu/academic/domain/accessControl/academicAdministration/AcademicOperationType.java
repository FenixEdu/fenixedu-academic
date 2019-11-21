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
package org.fenixedu.academic.domain.accessControl.academicAdministration;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AcademicAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AcademicProgramAccessTarget;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule.AdministrativeOfficeAccessTarget;
import org.fenixedu.academic.domain.accessControl.rules.AccessOperation;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.collect.Sets;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum AcademicOperationType
		implements IPresentableEnum, AccessOperation<AcademicAccessRule, AcademicAccessTarget> {
	MANAGE_AUTHORIZATIONS(false, false, Scope.ADMINISTRATION),

	MANAGE_EQUIVALENCES(true, true, Scope.ADMINISTRATION), // Migrated from Manager

//	TODO: remove from menu and delete
	MANAGE_ACADEMIC_CALENDARS(false, false, Scope.ADMINISTRATION), // Migrated from Manager

	/*
	 * Student stuff
	 */

	EDIT_STUDENT_PERSONAL_DATA(true, true, Scope.OFFICE),

	STUDENT_ENROLMENTS(true, true, Scope.OFFICE),

	MANAGE_REGISTRATIONS(true, true, Scope.OFFICE),

	MANAGE_STATUTES(false, false, Scope.OFFICE),

	MANAGE_CONCLUSION(true, true, Scope.OFFICE),

	@Deprecated
	UPDATE_REGISTRATION_AFTER_CONCLUSION(true, true, Scope.OFFICE),

	REPEAT_CONCLUSION_PROCESS(true, true, Scope.OFFICE),

	ENROLMENT_WITHOUT_RULES(true, true, Scope.OFFICE),

	MOVE_CURRICULUM_LINES_WITHOUT_RULES(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	REPORT_STUDENTS_UTL_CANDIDATES(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	MANAGE_REGISTERED_DEGREE_CANDIDACIES(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	MANAGE_ENROLMENT_PERIODS(true, true, Scope.ADMINISTRATION), // Migrated from Manager

	/*
	 * Mark Sheets
	 */

//	TODO: remove from menu and delete
	MANAGE_MARKSHEETS(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	RECTIFICATION_MARKSHEETS(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	REMOVE_GRADES(true, true, Scope.ADMINISTRATION), // Migrated from Manager

//	TODO: remove from menu and delete
	DISSERTATION_MARKSHEETS(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	REGISTRATION_CONCLUSION_CURRICULUM_VALIDATION(true, true, Scope.OFFICE),

	CREATE_REGISTRATION(true, true, Scope.OFFICE),

	STUDENT_LISTINGS(true, true, Scope.ADMINISTRATION),

	SERVICE_REQUESTS(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	SERVICE_REQUESTS_RECTORAL_SENDING(true, true, Scope.OFFICE),

	MANAGE_EXECUTION_COURSES(true, true, Scope.ADMINISTRATION), // Migrated from Manager

	MANAGE_EXECUTION_COURSES_ADV(true, true, Scope.ADMINISTRATION), // Migrated from Manager

	MANAGE_DEGREE_CURRICULAR_PLANS(true, true, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	MANAGE_EVENT_REPORTS(true, false, Scope.ADMINISTRATION),

	// Student Section

	MANAGE_STUDENT_PAYMENTS(true, false, Scope.OFFICE),

	MANAGE_STUDENT_PAYMENTS_ADV(true, true, Scope.ADMINISTRATION), // Migrated from Manager

//	TODO: remove from menu and delete
	CREATE_SIBS_PAYMENTS_REPORT(false, false, Scope.ADMINISTRATION), // Migrated from Manager

//	TODO: remove from menu and delete
	MANAGE_ACCOUNTING_EVENTS(true, true, Scope.OFFICE),

	/* End of Payments */

//	TODO: remove from menu and delete
	MANAGE_PRICES(true, false, Scope.ADMINISTRATION),

	MANAGE_EXTRA_CURRICULAR_ACTIVITIES(false, false, Scope.ADMINISTRATION),

	MANAGE_EXTERNAL_UNITS(false, false, Scope.ADMINISTRATION),

	/* Candidacies Management */

//	TODO: remove from menu and delete
	MANAGE_INDIVIDUAL_CANDIDACIES(true, true, Scope.ADMINISTRATION),

	MANAGE_CANDIDACY_PROCESSES(true, true, Scope.ADMINISTRATION),

	/* End of Candidacies Management */

	@Deprecated
	VIEW_FULL_STUDENT_CURRICULUM(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	MANAGE_DOCUMENTS(true, true, Scope.OFFICE),

	/* Phd Management */

//	TODO: remove from menu and delete
	VIEW_PHD_CANDIDACY_ALERTS(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	VIEW_PHD_PUBLIC_PRESENTATION_ALERTS(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	VIEW_PHD_THESIS_ALERTS(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	MANAGE_PHD_ENROLMENT_PERIODS(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	MANAGE_PHD_PROCESSES(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	MANAGE_PHD_PROCESS_STATE(true, true, Scope.OFFICE),

//	TODO: remove from menu and delete
	MANAGE_MOBILITY_OUTBOUND(false, false, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	VALIDATE_MOBILITY_OUTBOUND_CANDIDACIES(false, false, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	VIEW_SCHEDULING_OVERSIGHT(false, false, Scope.ADMINISTRATION),

	/* MANAGE TEACHER AUTHORIZATIONS */

//	TODO: remove from menu and delete
	MANAGE_TEACHER_AUTHORIZATIONS(false, false, Scope.ADMINISTRATION),

//	TODO: remove from menu and delete
	MANAGE_TEACHER_PROFESSORSHIPS(false, false, Scope.ADMINISTRATION),

	SUMMARIES_CONTROL(false, false, Scope.ADMINISTRATION),

	PAYMENTS_MODIFY_SETTLEMENTS(true, false, Scope.OFFICE),

	PAYMENTS_MODIFY_INVOICES(true, false, Scope.OFFICE);

	public static enum Scope {
		OFFICE, ADMINISTRATION;

		public boolean contains(AcademicOperationType function) {
			if (function instanceof AcademicOperationType) {
				return function.scope == this;
			}
			return false;
		}
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

	@Override
	public String exportAsString() {
		return getClass().getName() + ":" + name();
	}

	@Override
	public Optional<AcademicAccessRule> grant(Group whoCanAccess, Set<AcademicAccessTarget> whatCanAffect) {
		if (whoCanAccess.equals(Group.nobody())) {
			return Optional.empty();
		}
		Optional<AcademicAccessRule> match = AcademicAccessRule.accessRules()
				.filter(r -> r.getOperation().equals(this) && r.getWhoCanAccess().equals(whoCanAccess)
						&& Sets.symmetricDifference(r.getWhatCanAffect(), whatCanAffect).isEmpty())
				.findAny();
		return Optional.of(match.orElseGet(() -> new AcademicAccessRule(this, whoCanAccess, whatCanAffect)));
	}

	public Optional<AcademicAccessRule> grant(Group whoCanAccess, Set<AcademicProgram> programs,
			Set<AdministrativeOffice> offices) {
		Set<AcademicAccessTarget> targets = Stream.concat(programs.stream().map(AcademicProgramAccessTarget::new),
				offices.stream().map(AdministrativeOfficeAccessTarget::new)).collect(Collectors.toSet());
		return grant(whoCanAccess, targets);
	}

	@Override
	public Optional<AcademicAccessRule> grant(User user) {
		Optional<AcademicAccessRule> match = AcademicAccessRule.accessRules()
				.filter(r -> r.getOperation().equals(this) && r.getWhatCanAffect().isEmpty()).findAny();
		return match.map(r -> r.<AcademicAccessRule>grant(user))
				.orElseGet(() -> Optional.of(new AcademicAccessRule(this, user.groupOf(), Collections.emptySet())));
	}

	@Override
	public Optional<AcademicAccessRule> revoke(User user) {
		Optional<AcademicAccessRule> match = AcademicAccessRule.accessRules()
				.filter(r -> r.getOperation().equals(this) && r.getWhatCanAffect().isEmpty()).findAny();
		return match.map(r -> r.<AcademicAccessRule>revoke(user)).orElse(Optional.empty());
	}
}