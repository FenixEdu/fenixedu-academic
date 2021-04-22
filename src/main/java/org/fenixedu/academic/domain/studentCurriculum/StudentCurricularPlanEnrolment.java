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

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.EnrollmentDomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.enrolments.PhdStudentCurricularPlanEnrolmentManager;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;
import org.fenixedu.bennu.core.groups.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

abstract public class StudentCurricularPlanEnrolment {

    private static Logger logger = LoggerFactory.getLogger(StudentCurricularPlanEnrolment.class);

    protected EnrolmentContext enrolmentContext;
    private static ConcurrentLinkedQueue<CurricularCourseEnrollmentCondition> conditions = new ConcurrentLinkedQueue<>();

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

    private static InheritableThreadLocal<Boolean> checkDebts = new InheritableThreadLocal<>();

    public static void disableCheckDebts() {
        checkDebts.set(Boolean.TRUE);
    }

    public static void enableCheckDebts() {
        checkDebts.remove();
    }

    protected void assertEnrolmentPreConditions() {
        final Boolean disabled = checkDebts.get();
        if (disabled == null && !disabled.booleanValue()) {
            checkDebts();
        }

        if (isResponsiblePersonAllowedToEnrolStudents() || isResponsibleInternationalRelationOffice()) {
            assertAcademicAdminOfficePreConditions();

        } else if (isResponsiblePersonStudent()) {
            assertStudentEnrolmentPreConditions();

        } else {
            assertOtherRolesPreConditions();
        }

        for (CurricularCourseEnrollmentCondition condition : conditions) {
            condition.verify(getStudentCurricularPlan());
        }
    }

    @FunctionalInterface
    public static interface CurricularCourseEnrollmentCondition {
        public void verify(StudentCurricularPlan scp) throws DomainException;
    }

    public static void registerCondition(CurricularCourseEnrollmentCondition condition) {
        conditions.add(condition);
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
                && !AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.ENROLMENT_WITHOUT_RULES,
                        getStudentCurricularPlan().getDegree(), getResponsiblePerson().getUser())
                && !isResponsibleInternationalRelationOffice()) {
            throw new DomainException("error.permissions.cannot.enrol.without.rules");
        }
    }

    protected void checkUpdateRegistrationAfterConclusion() {
        if (!AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
                getStudentCurricularPlan().getDegree(), getResponsiblePerson().getUser())) {
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

    @Deprecated
    protected boolean isResponsiblePersonManager() {
        return Group.managers().isMember(getResponsiblePerson().getUser());
    }

    // Old AcademicAdminOffice role check
    protected boolean isResponsiblePersonAllowedToEnrolStudents() {
    	final Degree degree = getStudentCurricularPlan().getDegree();
        return AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, getResponsiblePerson().getUser())
                .anyMatch(p -> p == degree);
    }

    protected boolean isResponsibleInternationalRelationOffice() {
        return RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(getResponsiblePerson().getUser());
    }

    protected boolean isResponsiblePersonStudent() {
        return RoleType.STUDENT.isMember(getResponsiblePerson().getUser());
    }

    protected boolean isResponsiblePersonCoordinator() {
        return RoleType.COORDINATOR.isMember(getResponsiblePerson().getUser());
    }

    abstract protected void unEnrol();

    abstract protected void addEnroled();

    abstract protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate();

    abstract protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap);

    // -------------------
    // static information
    // -------------------

    static public StudentCurricularPlanEnrolment createManager(final EnrolmentContext enrolmentContext) {
        return ENROLMENT_MANAGER_FACTORY.get().createManager(enrolmentContext);
    }

    public static void setEnrolmentManagerFactory(final Supplier<EnrolmentManagerFactory> input) {
        if (input != null && input.get() != null) {
            ENROLMENT_MANAGER_FACTORY = input;
        } else {
            logger.error("Could not set factory to null");
        }
    }

    static private Supplier<EnrolmentManagerFactory> ENROLMENT_MANAGER_FACTORY = () -> new EnrolmentManagerFactory() {

        public StudentCurricularPlanEnrolment createManager(final EnrolmentContext enrolmentContext) {

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

    };

    public static interface EnrolmentManagerFactory {

        public StudentCurricularPlanEnrolment createManager(final EnrolmentContext enrolmentContext);
    }

}
