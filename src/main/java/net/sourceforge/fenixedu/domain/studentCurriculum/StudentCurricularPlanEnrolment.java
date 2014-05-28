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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.enrolments.PhdStudentCurricularPlanEnrolmentManager;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;

abstract public class StudentCurricularPlanEnrolment {

    protected EnrolmentContext enrolmentContext;

    protected StudentCurricularPlanEnrolment(final EnrolmentContext enrolmentContext) {
        checkParameters(enrolmentContext);
        this.enrolmentContext = enrolmentContext;
    }

    private void checkParameters(final EnrolmentContext enrolmentContext) {

        if (enrolmentContext == null) {
            throw new DomainException("error.StudentCurricularPlanEnrolment.invalid.enrolmentContext");
        }

        if (enrolmentContext.getStudentCurricularPlan() == null) {
            throw new DomainException("error.StudentCurricularPlanEnrolment.invalid.studentCurricularPlan");
        }

        if (!enrolmentContext.hasResponsiblePerson()) {
            throw new DomainException("error.StudentCurricularPlanEnrolment.enrolmentContext.invalid.person");
        }

    }

    final public RuleResult manage() {

        assertEnrolmentPreConditions();

        unEnrol();
        addEnroled();

        final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap =
                new HashMap<EnrolmentResultType, List<IDegreeModuleToEvaluate>>();
        final RuleResult result = evaluateDegreeModules(degreeModulesToEnrolMap);
        performEnrolments(degreeModulesToEnrolMap);

        return result;
    }

    protected void assertEnrolmentPreConditions() {

        if (isResponsiblePersonManager()) {
            return;
        }

        checkDebts();

        if (isResponsiblePersonAllowedToEnrolStudents() || isResponsibleInternationalRelationOffice()) {
            assertAcademicAdminOfficePreConditions();

        } else if (isResponsiblePersonStudent()) {
            assertStudentEnrolmentPreConditions();

        } else {
            assertOtherRolesPreConditions();
        }
    }

    protected void checkDebts() {

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkDebts(getStudentCurricularPlan());

        if (!result.isValid()) {
            throw new DomainException(result.message(), result.args());
        }
    }

    protected Person getPerson() {
        return getStudent().getPerson();
    }

    protected void assertAcademicAdminOfficePreConditions() {

        checkEnrolmentWithoutRules();

        if (updateRegistrationAfterConclusionProcessPermissionEvaluated()) {
            return;
        }

        if (!getRegistration().hasActiveLastState(getExecutionSemester())) {
            throw new DomainException("error.StudentCurricularPlan.registration.is.not.active.for.semester",
                    getExecutionSemester().getQualifiedName());
        }
    }

    protected boolean updateRegistrationAfterConclusionProcessPermissionEvaluated() {

        if (areModifiedCyclesConcluded() || isStudentCurricularPlanConcluded()) {
            checkUpdateRegistrationAfterConclusion();
            return true;
        } else {
            return false;
        }

    }

    protected boolean areModifiedCyclesConcluded() {
        for (final CycleCurriculumGroup curriculumGroup : getModifiedCycles()) {
            if (curriculumGroup.isConclusionProcessed()) {
                return true;
            }
        }

        return false;
    }

    protected boolean isStudentCurricularPlanConcluded() {
        return !getStudentCurricularPlan().isEmptyDegree() && getStudentCurricularPlan().isConclusionProcessed();
    }

    protected void checkEnrolmentWithoutRules() {
        if (isEnrolmentWithoutRules()
                && !AcademicAuthorizationGroup.getProgramsForOperation(getResponsiblePerson(), AcademicOperationType.ENROLMENT_WITHOUT_RULES).contains(getStudentCurricularPlan().getDegree())
                && !isResponsibleInternationalRelationOffice()) {
            throw new DomainException("error.permissions.cannot.enrol.without.rules");
        }
    }

    protected void checkUpdateRegistrationAfterConclusion() {
        if (!AcademicAuthorizationGroup.getProgramsForOperation(getResponsiblePerson(), AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION).contains(getStudentCurricularPlan().getDegree())) {
            throw new DomainException("error.permissions.cannot.update.registration.after.conclusion.process");
        }
    }

    protected Set<CycleCurriculumGroup> getModifiedCycles() {

        final Set<CycleCurriculumGroup> result = new HashSet<CycleCurriculumGroup>();

        for (final CycleCurriculumGroup cycle : getStudentCurricularPlan().getCycleCurriculumGroups()) {
            if (isRemovingModulesFromCycle(cycle)) {
                result.add(cycle);
                break;
            }

            if (isEnrolingInCycle(cycle)) {
                result.add(cycle);
                break;
            }
        }

        return result;
    }

    protected boolean isRemovingModulesFromCycle(final CycleCurriculumGroup cycle) {
        for (final CurriculumModule module : enrolmentContext.getToRemove()) {
            if (cycle.hasCurriculumModule(module)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isEnrolingInCycle(final CycleCurriculumGroup cycle) {
        for (final IDegreeModuleToEvaluate dmte : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (dmte.isEnroling() && cycle.hasCurriculumModule(dmte.getCurriculumGroup())) {
                return true;
            }
        }
        return false;
    }

    protected void assertStudentEnrolmentPreConditions() {

        if (!getResponsiblePerson().getStudent().getRegistrationsToEnrolByStudent().contains(getRegistration())) {
            throw new DomainException("error.StudentCurricularPlan.student.is.not.allowed.to.perform.enrol");
        }

        if (getCurricularRuleLevel() != CurricularRuleLevel.ENROLMENT_WITH_RULES) {
            throw new DomainException("error.StudentCurricularPlan.invalid.curricular.rule.level");
        }

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkEnrolmentPeriods(getStudentCurricularPlan(),
                        getExecutionSemester());

        if (!result.isValid()) {
            throw new DomainException(result.message(), result.args());
        }
    }

    protected void assertOtherRolesPreConditions() {
        throw new DomainException("error.invalid.user");
    }

    private RuleResult evaluateDegreeModules(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {

        RuleResult finalResult = RuleResult.createInitialTrue();
        final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> rulesToEvaluate = getRulesToEvaluate();
        for (final Entry<IDegreeModuleToEvaluate, Set<ICurricularRule>> entry : rulesToEvaluate.entrySet()) {
            RuleResult result = evaluateRules(entry.getKey(), entry.getValue());
            finalResult = finalResult.and(result);
        }

        finalResult = evaluateExtraRules(finalResult);

        if (!finalResult.isFalse()) {
            for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : rulesToEvaluate.keySet()) {
                addDegreeModuleToEvaluateToMap(degreeModulesEnrolMap,
                        finalResult.getEnrolmentResultTypeFor(degreeModuleToEvaluate.getDegreeModule()), degreeModuleToEvaluate);
            }

        }

        if (finalResult.isFalse()) {
            throw new EnrollmentDomainException(finalResult);
        }

        return finalResult;
    }

    protected RuleResult evaluateExtraRules(final RuleResult actualResult) {
        // no extra rules to be executed
        return actualResult;

    }

    private RuleResult evaluateRules(final IDegreeModuleToEvaluate degreeModuleToEvaluate,
            final Set<ICurricularRule> curricularRules) {
        RuleResult ruleResult = RuleResult.createTrue(degreeModuleToEvaluate.getDegreeModule());

        for (final ICurricularRule rule : curricularRules) {
            ruleResult = ruleResult.and(rule.evaluate(degreeModuleToEvaluate, enrolmentContext));
        }

        return ruleResult;
    }

    private void addDegreeModuleToEvaluateToMap(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> result,
            final EnrolmentResultType enrolmentResultType, final IDegreeModuleToEvaluate degreeModuleToEnrol) {

        List<IDegreeModuleToEvaluate> information = result.get(enrolmentResultType);
        if (information == null) {
            result.put(enrolmentResultType, information = new ArrayList<IDegreeModuleToEvaluate>());
        }
        information.add(degreeModuleToEnrol);
    }

    protected ExecutionSemester getExecutionSemester() {
        return enrolmentContext.getExecutionPeriod();
    }

    protected ExecutionYear getExecutionYear() {
        return getExecutionSemester().getExecutionYear();
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
        return enrolmentContext.getStudentCurricularPlan();
    }

    protected Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    protected RootCurriculumGroup getRoot() {
        return getStudentCurricularPlan().getRoot();
    }

    protected Student getStudent() {
        return getRegistration().getStudent();
    }

    protected DegreeCurricularPlan getDegreeCurricularPlan() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    protected CurricularRuleLevel getCurricularRuleLevel() {
        return enrolmentContext.getCurricularRuleLevel();
    }

    protected Person getResponsiblePerson() {
        return enrolmentContext.getResponsiblePerson();
    }

    private boolean isEnrolmentWithoutRules() {
        return enrolmentContext.isEnrolmentWithoutRules();
    }

    protected boolean isResponsiblePersonManager() {
        return getResponsiblePerson().hasRole(RoleType.MANAGER);
    }

    // Old AcademicAdminOffice role check
    protected boolean isResponsiblePersonAllowedToEnrolStudents() {
        return AcademicAuthorizationGroup.getProgramsForOperation(getResponsiblePerson(), AcademicOperationType.STUDENT_ENROLMENTS).contains(getStudentCurricularPlan().getDegree());
    }

    protected boolean isResponsibleInternationalRelationOffice() {
        return getResponsiblePerson().hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE);
    }

    protected boolean isResponsiblePersonStudent() {
        return getResponsiblePerson().hasRole(RoleType.STUDENT);
    }

    protected boolean isResponsiblePersonCoordinator() {
        return getResponsiblePerson().hasRole(RoleType.COORDINATOR);
    }

    abstract protected void unEnrol();

    abstract protected void addEnroled();

    abstract protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate();

    abstract protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap);

    // -------------------
    // static information
    // -------------------

    static public StudentCurricularPlanEnrolment createManager(final EnrolmentContext enrolmentContext) {

        if (enrolmentContext.isNormal()) {

            if (enrolmentContext.isPhdDegree()) {
                return new PhdStudentCurricularPlanEnrolmentManager(enrolmentContext);
            } else {
                return new StudentCurricularPlanEnrolmentManager(enrolmentContext);
            }

        } else if (enrolmentContext.isImprovement()) {
            return new StudentCurricularPlanImprovementOfApprovedEnrolmentManager(enrolmentContext);

        } else if (enrolmentContext.isSpecialSeason()) {
            return new StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager(enrolmentContext);

        } else if (enrolmentContext.isExtra()) {
            return new StudentCurricularPlanExtraEnrolmentManager(enrolmentContext);

        } else if (enrolmentContext.isPropaeudeutics()) {
            return new StudentCurricularPlanPropaeudeuticsEnrolmentManager(enrolmentContext);

        } else if (enrolmentContext.isStandalone()) {
            return new StudentCurricularPlanStandaloneEnrolmentManager(enrolmentContext);
        }

        throw new DomainException("StudentCurricularPlanEnrolment");
    }
}
