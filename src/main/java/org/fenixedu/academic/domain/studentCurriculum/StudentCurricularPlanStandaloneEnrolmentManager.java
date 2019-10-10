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
package org.fenixedu.academic.domain.studentCurriculum;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;

import com.google.common.collect.Sets;

public class StudentCurricularPlanStandaloneEnrolmentManager extends StudentCurricularPlanEnrolment {

	public StudentCurricularPlanStandaloneEnrolmentManager(final EnrolmentContext enrolmentContext) {
		super(enrolmentContext);
	}

	@Override
	protected void assertEnrolmentPreConditions() {
		if (!isResponsiblePersonAllowedToEnrolStudents() && !isResponsibleInternationalRelationOffice()) {
			throw new DomainException("error.StudentCurricularPlan.cannot.enrol.in.propaeudeutics");
		}

		if (!(AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.ENROLMENT_WITHOUT_RULES,
				getStudentCurricularPlan().getDegree(), getResponsiblePerson().getUser())
				|| PermissionService.isMember("ENROLMENT_WITHOUT_RULES", getStudentCurricularPlan().getDegree(),
						getResponsiblePerson().getUser()))) {
			checkRegistrationRegime();
		}

		if (getRegistration().isRegistrationConclusionProcessed()) {
			checkUpdateRegistrationAfterConclusion();
		}

		checkEnrolingDegreeModules();
	}

	private void checkRegistrationRegime() {
		if (getRegistration().isPartialRegime(getExecutionYear())) {
			throw new DomainException("error.StudentCurricularPlan.with.part.time.regime.cannot.enrol");
		}
	}

	private void checkEnrolingDegreeModules() {
		for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
			if (degreeModuleToEvaluate.isEnroling()) {
				if (!degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
					throw new DomainException(
							"error.StudentCurricularPlanPropaeudeuticsEnrolmentManager.can.only.enrol.in.curricularCourses");
				}
				checkIDegreeModuleToEvaluate((CurricularCourse) degreeModuleToEvaluate.getDegreeModule());
			}
		}
	}

	private void checkIDegreeModuleToEvaluate(final CurricularCourse curricularCourse) {
		if (getStudentCurricularPlan().isApproved(curricularCourse, getExecutionSemester())) {
			throw new DomainException("error.already.aproved", curricularCourse.getName());
		}

		if (getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, getExecutionSemester())) {
			throw new DomainException("error.already.enroled.in.executionPeriod", curricularCourse.getName(),
					getExecutionSemester().getQualifiedName());
		}
	}

	@Override
	protected void addEnroled() {
		addEnroledFromStudentCurricularPlan();
		addEnroledFromStandaloneGroup();
		changeCurricularRuleLevel();
	}

	/*
	 * Change level accordding to current level
	 */
	private void changeCurricularRuleLevel() {
		final CurricularRuleLevel currentLevel = enrolmentContext.getCurricularRuleLevel();

		if (currentLevel.equals(CurricularRuleLevel.STANDALONE_ENROLMENT_NO_RULES)) {
			enrolmentContext.setCurricularRuleLevel(CurricularRuleLevel.ENROLMENT_NO_RULES);
		} else {
			enrolmentContext.setCurricularRuleLevel(CurricularRuleLevel.ENROLMENT_WITH_RULES);
		}
	}

	private void addEnroledFromStudentCurricularPlan() {
		for (final ExecutionInterval interval : enrolmentContext.getExecutionIntervalsToEvaluate()) {
			for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : getStudentCurricularPlan()
					.getDegreeModulesToEvaluate(interval)) {
				enrolmentContext.addDegreeModuleToEvaluate(degreeModuleToEvaluate);
			}
		}
	}

	private void addEnroledFromStandaloneGroup() {
		final StandaloneCurriculumGroup group = getStudentCurricularPlan().getStandaloneCurriculumGroup();
		for (final CurriculumLine curriculumLine : group.getChildCurriculumLines()) {
			for (final ExecutionInterval interval : enrolmentContext.getExecutionIntervalsToEvaluate()) {
				for (final IDegreeModuleToEvaluate module : curriculumLine.getDegreeModulesToEvaluate(interval)) {
					enrolmentContext.addDegreeModuleToEvaluate(module);
				}
			}
		}
	}

	@Override
	protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
		if (!enrolmentContext.hasDegreeModulesToEvaluate()) {
			return Collections.emptyMap();
		}

		final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();
		for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
			if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
				result.put(degreeModuleToEvaluate, Collections.<ICurricularRule>emptySet());
			}
		}

		return result;
	}

	@Override
	protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap) {
		final Set<Enrolment> enrolmentsToNotify = Sets.newHashSet();

		for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEnrolMap
				.entrySet()) {
			for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
				if (degreeModuleToEvaluate.isEnroling()
						&& degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
					final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate
							.getDegreeModule();

					checkIDegreeModuleToEvaluate(curricularCourse);
					final Enrolment enrolment = new Enrolment(getStudentCurricularPlan(),
							degreeModuleToEvaluate.getCurriculumGroup(), curricularCourse, getExecutionSemester(),
							EnrollmentCondition.VALIDATED, getResponsiblePerson().getUsername());

					enrolmentsToNotify.add(enrolment);
				}
			}
		}

		for (final Enrolment enrolment : enrolmentsToNotify) {
			Signal.emit(ITreasuryBridgeAPI.STANDALONE_ENROLMENT, new DomainObjectEvent<Enrolment>(enrolment));
		}
		getRegistration().updateEnrolmentDate(getExecutionYear());
	}

	@Override
	protected void unEnrol() {

		// First remove Enrolments
		for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
			if (curriculumModule.isLeaf()) {
				TreasuryBridgeAPIFactory.implementation().standaloneUnenrolment((Enrolment) curriculumModule);

				// Signal.emit(ITreasuryBridgeAPI.STANDALONE_UNENROLMENT, new
				// DomainObjectEvent<Enrolment>((Enrolment) curriculumModule));
				curriculumModule.delete();
			}
		}

		// After, remove CurriculumGroups
		for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
			if (!curriculumModule.isLeaf()) {
				curriculumModule.delete();
			}
		}
	}

}
