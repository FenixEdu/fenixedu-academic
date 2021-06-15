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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EnrolmentBlocker;
import org.fenixedu.academic.domain.accounting.events.EnrolmentEvaluationEvent;
import org.fenixedu.academic.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import org.fenixedu.academic.domain.curricularRules.SeniorStatuteSpecialSeasonEnrolmentScope;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.SeniorStatute;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;

public class StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager(final EnrolmentContext enrolmentContext) {
        super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
        if (!getRegistration().hasStateType(getExecutionYear(), RegistrationStateType.REGISTERED)) {
            throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
        }

        super.assertEnrolmentPreConditions();
    }

    @Override
    protected void checkDebts() {
    }

    @Override
    protected void assertAcademicAdminOfficePreConditions() {

        checkEnrolmentWithoutRules();

        if (updateRegistrationAfterConclusionProcessPermissionEvaluated()) {
            return;
        }
    }

    @Override
    protected void assertStudentEnrolmentPreConditions() {

        if (!getRegistrationsToEnrolByStudent(getResponsiblePerson().getStudent()).contains(getRegistration())) {
            throw new DomainException("error.StudentCurricularPlan.student.is.not.allowed.to.perform.enrol");
        }

        if (!hasSpecialSeasonStatute()) {
            throw new DomainException("error.StudentCurricularPlan.student.has.no.special.season.statute");
        }

        if (getCurricularRuleLevel() != CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT) {
            throw new DomainException("error.StudentCurricularPlan.invalid.curricular.rule.level");
        }

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkEnrolmentPeriodsForSpecialSeason(getStudentCurricularPlan(),
                        getExecutionSemester());

        if (!result.isValid()) {
            throw new DomainException(result.message(), result.args());
        }
    }

    private Collection<Registration> getRegistrationsToEnrolByStudent(final Student student) {
        final Collection<Registration> registrations = new HashSet<Registration>();

        for (final Registration registration : student.getRegistrationsSet()) {
            if (isRegistrationEnrolmentByStudentAllowed(registration)) {

                if (registration.isActive() || isRegistrationAvailableToEnrol(registration)) {
                    registrations.add(registration);
                }
            }
        }

        return registrations;
    }

    public boolean isRegistrationEnrolmentByStudentAllowed(final Registration registration) {
        return registration.getRegistrationProtocol().isEnrolmentByStudentAllowed();
    }

    private boolean isRegistrationAvailableToEnrol(final Registration registration) {
        return registration.hasAnyEnrolmentsIn(getExecutionYear())
                && registration.getLastStudentCurricularPlan().hasExternalCycleCurriculumGroups();
    }

    @Override
    protected void unEnrol() {
        for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                enrolment.deleteSpecialSeasonEvaluation();
            } else {
                throw new DomainException(
                        "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
            }
        }
    }

    @Override
    protected void addEnroled() {
        for (final Enrolment enrolment : getStudentCurricularPlan().getSpecialSeasonEnrolments(getExecutionYear())) {
            enrolmentContext.addDegreeModuleToEvaluate(new EnroledCurriculumModuleWrapper(enrolment, getExecutionSemester()));
        }
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
        final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result =
                new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {

            if (degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.canCollectRules()) {
                final EnroledCurriculumModuleWrapper moduleEnroledWrapper =
                        (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

                if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
                    final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();

                    final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
                    if (!enrolment.hasSpecialSeason()) {
                        curricularRules.add(new EnrolmentInSpecialSeasonEvaluation(enrolment));
                    }

                    curricularRules.add(new MaximumNumberOfECTSInSpecialSeasonEvaluation());

                    if (isEnrolingAsSenior(enrolment)) {
                        curricularRules.add(new SeniorStatuteSpecialSeasonEnrolmentScope(enrolment,
                                getRegistrationFromSeniorStatute(enrolment)));
                    }

                    result.put(degreeModuleToEvaluate, curricularRules);
                } else {
                    throw new DomainException(
                            "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
                }
            }
        }

        return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEvaluate) {
        for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEvaluate.entrySet()) {

            for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
                if (degreeModuleToEvaluate.isEnroled()) {
                    final EnroledCurriculumModuleWrapper moduleEnroledWrapper =
                            (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

                    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
                        final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();

                        if (!enrolment.hasSpecialSeason()) {
                            EnrolmentEvaluation specialSeasonEvaluation =
                                    enrolment.createSpecialSeasonEvaluation(getResponsiblePerson());
                            EnrolmentEvaluationEvent.create(specialSeasonEvaluation);
                        }
                    } else {
                        throw new DomainException(
                                "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
                    }
                }
            }
        }
    }

    private boolean hasSpecialSeasonStatute() {
        Collection<StudentStatute> statutes = getResponsiblePerson().getStudent().getStudentStatutesSet();
        for (StudentStatute statute : statutes) {
            if (!statute.getType().isSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(getRegistration())) {
                continue;
            }
            if (!statute.isValidInExecutionPeriod(getExecutionSemester())) {
                continue;
            }

            return true;

        }
        return false;
    }

    private boolean isEnrolingAsSenior(Enrolment enrolment) {
        if (isResponsiblePersonAllowedToEnrolStudents()) {
            return false;
        }
        List<StudentStatute> statutesReader = new ArrayList<StudentStatute>(enrolment.getStudent().getStudentStatutesSet());
        List<StudentStatute> validSeniorStatutes = new ArrayList<StudentStatute>();
        List<StudentStatute> validOtherStatutes = new ArrayList<StudentStatute>();
        for (StudentStatute statute : statutesReader) {
            if (statute instanceof SeniorStatute && statute.isValidInExecutionPeriod(getExecutionSemester())) {
                validSeniorStatutes.add(statute);
            } else if (statute.getType().isSpecialSeasonGranted() && statute.isValidInExecutionPeriod(getExecutionSemester())) {
                validOtherStatutes.add(statute);
            }
        }
        if (validOtherStatutes.size() > 0) {
            return false;
        }
        if (validSeniorStatutes.size() == 1 && validOtherStatutes.size() == 0) {
            return true;
        } else {
            throw new DomainException(
                    "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.inconsistent.student.statutes.states");
        }
    }

    private Registration getRegistrationFromSeniorStatute(Enrolment enrolment) {
        List<StudentStatute> statutesReader = new ArrayList<StudentStatute>(enrolment.getStudent().getStudentStatutesSet());
        List<StudentStatute> statutesWriter = new ArrayList<StudentStatute>();
        for (StudentStatute statute : statutesReader) {
            if (statute instanceof SeniorStatute && statute.isValidInExecutionPeriod(getExecutionSemester())) {
                statutesWriter.add(statute);
            }
        }
        if (statutesWriter.size() == 1) {
            SeniorStatute senior = (SeniorStatute) statutesWriter.iterator().next();
            return senior.getRegistration();
        } else {
            throw new DomainException(
                    "StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.student.has.more.than.one.senior.statute.for.same.period");
        }
    }

}
