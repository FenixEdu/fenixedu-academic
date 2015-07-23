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
package org.fenixedu.academic.domain.student;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeState;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.events.AccountingEventsManager;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithInvocationResult;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.fenixedu.academic.domain.studentCurriculum.CreditsDismissal;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroupFactory;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.EnrolmentWrapper;
import org.fenixedu.academic.domain.studentCurriculum.Equivalence;
import org.fenixedu.academic.domain.studentCurriculum.ExtraCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.OptionalDismissal;
import org.fenixedu.academic.domain.studentCurriculum.Substitution;
import org.fenixedu.academic.domain.studentCurriculum.TemporarySubstitution;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.InvocationResult;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

//TODO remove in next major
@Deprecated
public class SeparationCyclesManagement {

    private static final Predicate<DegreeType> ACCEPTED_DEGREE_TYPES = DegreeType.oneOf(DegreeType::isBolonhaDegree,
            DegreeType::isIntegratedMasterDegree);

    public SeparationCyclesManagement() {
    }

    public Registration separateSecondCycle(final StudentCurricularPlan studentCurricularPlan) {
        checkIfCanSeparateSecondCycle(studentCurricularPlan);
        return createNewSecondCycle(studentCurricularPlan);
    }

    protected void checkIfCanSeparateSecondCycle(final StudentCurricularPlan studentCurricularPlan) {

        if (!studentCurricularPlan.isBolonhaDegree()) {
            throw new DomainException("error.SeparationCyclesManagement.not.bolonha.degree");
        }

        if (!studentCurricularPlan.isActive() && !studentCurricularPlan.getRegistration().isConcluded()) {
            throw new DomainException("error.SeparationCyclesManagement.not.active.or.concluded", studentCurricularPlan.getName());
        }

        if (studentCurricularPlan.isConclusionProcessed()) {
            throw new DomainException("error.SeparationCyclesManagement.conclusion.processed");
        }

        if (!ACCEPTED_DEGREE_TYPES.test(studentCurricularPlan.getDegreeType())) {
            throw new DomainException("error.SeparationCyclesManagement.invalid.degreeType");
        }

        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        if (secondCycle == null || !secondCycle.isExternal()) {
            throw new DomainException("error.SeparationCyclesManagement.invalid.secondCycle");
        }

        final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
        if (firstCycle == null || !firstCycle.isConcluded()) {
            throw new DomainException("error.SeparationCyclesManagement.invalid.firstCycle");
        }

        if (studentAlreadyHasNewRegistration(studentCurricularPlan)) {
            final DegreeCurricularPlan degreeCurricularPlan = secondCycle.getDegreeCurricularPlanOfDegreeModule();
            throw new DomainException("error.SeparationCyclesManagement.already.has.registration", degreeCurricularPlan.getName());
        }
    }

    private boolean studentAlreadyHasNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
        final Student student = studentCurricularPlan.getRegistration().getStudent();
        return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
    }

    protected Registration createNewSecondCycle(final StudentCurricularPlan oldStudentCurricularPlan) {
        final Student student = oldStudentCurricularPlan.getRegistration().getStudent();
        final CycleCurriculumGroup oldSecondCycle = oldStudentCurricularPlan.getSecondCycle();
        final DegreeCurricularPlan degreeCurricularPlan = oldSecondCycle.getDegreeCurricularPlanOfDegreeModule();

        final Registration newRegistration = createRegistration(student, oldStudentCurricularPlan);
        final StudentCurricularPlan newStudentCurricularPlan =
                createStudentCurricularPlan(newRegistration, degreeCurricularPlan, oldSecondCycle.getCycleType());
        final CycleCurriculumGroup newSecondCycle = newStudentCurricularPlan.getSecondCycle();

        copyCycleCurriculumGroupsInformation(oldSecondCycle, newSecondCycle);
        moveExtraCurriculumGroupInformation(oldStudentCurricularPlan, newStudentCurricularPlan);
        moveExtraAttends(oldStudentCurricularPlan, newStudentCurricularPlan);
        tryRemoveOldSecondCycle(oldSecondCycle);
        moveGratuityEventsInformation(oldStudentCurricularPlan, newStudentCurricularPlan);
        createAdministrativeOfficeFeeAndInsurance(newStudentCurricularPlan);

        markOldRegistrationWithConcludedState(oldStudentCurricularPlan);

        return newRegistration;
    }

    private void moveExtraAttends(final StudentCurricularPlan oldStudentCurricularPlan,
            final StudentCurricularPlan newStudentCurricularPlan) {

        final Set<Attends> attends = new HashSet<Attends>();
        for (final Attends attend : oldStudentCurricularPlan.getRegistration().getAssociatedAttendsSet()) {
            if (!belongsTo(oldStudentCurricularPlan, attend)
                    && isToMoveAttendsFrom(oldStudentCurricularPlan, newStudentCurricularPlan, attend)) {
                attends.add(attend);
            }
        }

        for (final Attends attend : attends) {
            if (!newStudentCurricularPlan.getRegistration().attends(attend.getExecutionCourse())) {
                attend.setRegistration(newStudentCurricularPlan.getRegistration());
            }
        }
    }

    private boolean belongsTo(final StudentCurricularPlan studentCurricularPlan, final Attends attend) {
        for (final CurricularCourse curricularCourse : attend.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            if (studentCurricularPlan.getDegreeCurricularPlan().hasDegreeModule(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    private boolean isToMoveAttendsFrom(final StudentCurricularPlan oldStudentCurricularPlan,
            final StudentCurricularPlan newStudentCurricularPlan, final Attends attend) {

        if (attend.getEnrolment() != null) {
            return !oldStudentCurricularPlan.hasEnrolments(attend.getEnrolment())
                    && newStudentCurricularPlan.hasEnrolments(attend.getEnrolment());
        }

        return !attend.getExecutionPeriod().isBefore(newStudentCurricularPlan.getStartExecutionPeriod());
    }

    private Registration createRegistration(final Student student, final StudentCurricularPlan sourceStudentCurricularPlan) {

        final CycleCurriculumGroup oldSecondCycle = sourceStudentCurricularPlan.getSecondCycle();
        Registration registration = student.getActiveRegistrationFor(oldSecondCycle.getDegreeCurricularPlanOfDegreeModule());

        if (registration != null) {
            return registration;
        }

        Degree degree = oldSecondCycle.getDegreeCurricularPlanOfDegreeModule().getDegree();
        registration = new Registration(student.getPerson(), student.getNumber(), degree);
        StudentCandidacy studentCandidacy = createStudentCandidacy(student, oldSecondCycle);
        registration.setStudentCandidacy(studentCandidacy);
        PersonalIngressionData personalIngressionData =
                student.getPersonalIngressionDataByExecutionYear(registration.getRegistrationYear());
        if (personalIngressionData == null) {
            new PersonalIngressionData(student, registration.getRegistrationYear(),
                    studentCandidacy.getPrecedentDegreeInformation());
        } else {
            personalIngressionData.addPrecedentDegreesInformations(studentCandidacy.getPrecedentDegreeInformation());
        }
        registration.addPrecedentDegreesInformations(studentCandidacy.getPrecedentDegreeInformation());

        registration.setStartDate(getBeginDate(sourceStudentCurricularPlan, getExecutionPeriod()));
        RegistrationState activeState = registration.getActiveState();
        activeState.setStateDate(getBeginDate(sourceStudentCurricularPlan, getExecutionPeriod()));
        activeState.setResponsiblePerson(null);
        registration.setSourceRegistration(sourceStudentCurricularPlan.getRegistration());
        registration.setRegistrationProtocol(sourceStudentCurricularPlan.getRegistration().getRegistrationProtocol());

        return registration;
    }

    private YearMonthDay getBeginDate(final StudentCurricularPlan sourceStudentCurricularPlan,
            final ExecutionSemester executionSemester) {

        if (!sourceStudentCurricularPlan.getRegistration().hasConcluded()) {
            throw new DomainException("error.SeparationCyclesManagement.source.studentCurricularPlan.is.not.concluded");
        }

        final YearMonthDay conclusionDate = sourceStudentCurricularPlan.getFirstCycle().calculateConclusionDate();
        final YearMonthDay stateDate = conclusionDate != null ? conclusionDate.plusDays(1) : new YearMonthDay().plusDays(1);

        return executionSemester.getBeginDateYearMonthDay().isBefore(stateDate) ? stateDate : executionSemester
                .getBeginDateYearMonthDay();
    }

    private StudentCandidacy createStudentCandidacy(final Student student, final CycleCurriculumGroup oldSecondCycle) {
        final DegreeCurricularPlan dcp = oldSecondCycle.getDegreeCurricularPlanOfDegreeModule();
        return StudentCandidacy.createStudentCandidacy(dcp.getExecutionDegreeByYear(getExecutionYear()), student.getPerson());
    }

    private StudentCurricularPlan createStudentCurricularPlan(final Registration registration,
            final DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType) {

        StudentCurricularPlan result = registration.getStudentCurricularPlan(degreeCurricularPlan);
        if (result != null) {
            return result;
        }

        result =
                StudentCurricularPlan.createWithEmptyStructure(registration, degreeCurricularPlan, cycleType,
                        registration.getStartDate());

        // set ingression after create studentcurricularPlan
        registration.setIngressionType(IngressionType.findByPredicate(IngressionType::isDirectAccessFrom1stCycle).orElse(null));

        return result;
    }

    private void copyCycleCurriculumGroupsInformation(final CycleCurriculumGroup oldSecondCycle,
            final CycleCurriculumGroup newSecondCycle) {
        for (final CurriculumModule curriculumModule : oldSecondCycle.getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                copyCurricumLineInformation((CurriculumLine) curriculumModule, newSecondCycle);
            } else {
                copyCurriculumGroupsInformation((CurriculumGroup) curriculumModule, newSecondCycle);
            }
        }
    }

    private void copyCurriculumGroupsInformation(final CurriculumGroup source, final CurriculumGroup parent) {
        final CurriculumGroup destination;
        //test if source group still exists as part of destination DCP
        if (!groupIsStillValid(source)) {
            return;
        }
        if (parent.hasChildDegreeModule(source.getDegreeModule())) {
            destination = (CurriculumGroup) parent.getChildCurriculumModule(source.getDegreeModule());
        } else {
            destination = CurriculumGroupFactory.createGroup(parent, source.getDegreeModule());
        }

        for (final CurriculumModule curriculumModule : source.getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                copyCurricumLineInformation((CurriculumLine) curriculumModule, destination);
            } else {
                copyCurriculumGroupsInformation((CurriculumGroup) curriculumModule, destination);
            }
        }
    }

    private boolean groupIsStillValid(CurriculumGroup source) {
        ExecutionYear nowadays = ExecutionYear.readCurrentExecutionYear();
        if (source.getDegreeModule().getValidChildContexts(nowadays).size() > 0) {
            return true;
        }
        for (CurriculumGroup childGroup : source.getChildCurriculumGroups()) {
            if (groupIsStillValid(childGroup)) {
                return true;
            }
        }
        return false;
    }

    private void copyCurricumLineInformation(final CurriculumLine curriculumLine, final CurriculumGroup parent) {
        if (curriculumLine.isEnrolment()) {
            final Enrolment enrolment = (Enrolment) curriculumLine;
            if (enrolment.getExecutionPeriod().isAfterOrEquals(getExecutionPeriod())) {
                moveEnrolment(enrolment, parent);
            } else if (enrolment.isApproved()) {
                createSubstitutionForEnrolment((Enrolment) curriculumLine, parent);
            }
        } else if (curriculumLine.isDismissal()) {
            if (curriculumLine.hasExecutionPeriod() && curriculumLine.getExecutionPeriod().isAfterOrEquals(getExecutionPeriod())) {
                moveDismissal((Dismissal) curriculumLine, parent);
            } else {
                createDismissal((Dismissal) curriculumLine, parent);
            }
        } else {
            throw new DomainException("error.unknown.curriculumLine");
        }
    }

    private void moveEnrolment(final Enrolment enrolment, final CurriculumGroup parent) {
        final CurriculumModule child = parent.getChildCurriculumModule(enrolment.getDegreeModule());
        if (child != null && child.isEnrolment()) {
            final Enrolment childEnrolment = (Enrolment) child;
            if (childEnrolment.getExecutionPeriod() == enrolment.getExecutionPeriod()) {
                throw new DomainException("error.SeparationCyclesManagement.enrolment.should.not.exist.for.same.executionPeriod");
            }
        }

        final Registration registration = parent.getStudentCurricularPlan().getRegistration();
        enrolment.setCurriculumGroup(parent);

        for (final Attends attend : enrolment.getAttendsSet()) {
            if (!registration.attends(attend.getExecutionCourse())) {
                attend.setRegistration(registration);
            }
        }
    }

    private void moveDismissal(final Dismissal dismissal, final CurriculumGroup parent) {
        if (!dismissal.getUsedInSeparationCycle()) {
            if (!curriculumGroupHasSimilarDismissal(parent, dismissal)) {
                dismissal.setCurriculumGroup(parent);
                dismissal.getCredits().setStudentCurricularPlan(parent.getStudentCurricularPlan());
            } else {
                dismissal.setUsedInSeparationCycle(true);
            }
        }
    }

    private void createSubstitutionForEnrolment(final Enrolment enrolment, final CurriculumGroup parent) {
        if (enrolment.getUsedInSeparationCycle() || parent.hasChildDegreeModule(enrolment.getDegreeModule())) {
            // TODO: temporary
            enrolment.setUsedInSeparationCycle(true);
            return;
        }

        enrolment.setUsedInSeparationCycle(true);

        if (enrolment.isOptional()) {
            final OptionalEnrolment optional = (OptionalEnrolment) enrolment;
            if (parent.hasChildDegreeModule(optional.getOptionalCurricularCourse())) {
                return;
            }
            final Substitution substitution = createSubstitution(enrolment, parent);
            createNewOptionalDismissal(substitution, parent, enrolment, optional.getOptionalCurricularCourse(),
                    optional.getEctsCredits());
        } else {
            createNewDismissal(createSubstitution(enrolment, parent), parent, enrolment);
        }
    }

    private Substitution createSubstitution(final Enrolment enrolment, final CurriculumGroup parent) {
        final Substitution substitution = new Substitution();
        substitution.setStudentCurricularPlan(parent.getStudentCurricularPlan());
        substitution.setExecutionPeriod(getExecutionPeriod());
        EnrolmentWrapper.create(substitution, enrolment);
        return substitution;
    }

    private Dismissal createNewDismissal(final Credits credits, final CurriculumGroup parent, final CurriculumLine curriculumLine) {

        final CurricularCourse curricularCourse = curriculumLine.getCurricularCourse();

        if (!hasCurricularCourseToDismissal(parent, curricularCourse) && !hasResponsibleForCreation(curriculumLine)) {
            throw new DomainException("error.SeparationCyclesManagement.parent.doesnot.have.curricularCourse.to.dismissal");
        }

        final Dismissal dismissal = new Dismissal();
        dismissal.setCredits(credits);
        dismissal.setCurriculumGroup(parent);
        dismissal.setCurricularCourse(curricularCourse);

        return dismissal;
    }

    private OptionalDismissal createNewOptionalDismissal(final Credits credits, final CurriculumGroup parent,
            final CurriculumLine curriculumLine, final OptionalCurricularCourse curricularCourse, final Double ectsCredits) {

        if (ectsCredits == null || ectsCredits.doubleValue() == 0) {
            throw new DomainException("error.OptionalDismissal.invalid.credits");
        }

        if (!hasCurricularCourseToDismissal(parent, curricularCourse) && !hasResponsibleForCreation(curriculumLine)) {
            throw new DomainException("error.SeparationCyclesManagement.parent.doesnot.have.curricularCourse.to.dismissal");
        }

        final OptionalDismissal dismissal = new OptionalDismissal();
        dismissal.setCredits(credits);
        dismissal.setCurriculumGroup(parent);
        dismissal.setCurricularCourse(curricularCourse);
        dismissal.setEctsCredits(ectsCredits);

        return dismissal;
    }

    private boolean hasResponsibleForCreation(final CurriculumLine line) {
        return line.hasCreatedBy();
    }

    private boolean hasCurricularCourseToDismissal(final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse) {
        final CourseGroup degreeModule = curriculumGroup.getDegreeModule();
        for (final Context context : degreeModule.getChildContexts(CurricularCourse.class)) {
            final CurricularCourse each = (CurricularCourse) context.getChildDegreeModule();
            if (each.isEquivalent(curricularCourse) && !curriculumGroup.hasChildDegreeModule(degreeModule)) {
                return true;
            }
        }
        return false;
    }

    private void createDismissal(final Dismissal dismissal, final CurriculumGroup parent) {
        if (dismissal.getUsedInSeparationCycle() || curriculumGroupHasSimilarDismissal(parent, dismissal)) {
            // TODO: temporary
            dismissal.setUsedInSeparationCycle(true);
            return;
        }

        dismissal.setUsedInSeparationCycle(true);
        final Credits credits = dismissal.getCredits();

        final Credits newCredits;
        if (credits.isTemporary()) {
            newCredits = new TemporarySubstitution();

        } else if (credits.isSubstitution()) {
            newCredits = new Substitution();

        } else if (credits.isEquivalence()) {
            final Equivalence equivalence = (Equivalence) credits;
            final Equivalence newEquivalence = new Equivalence();
            newEquivalence.setGrade(equivalence.getGrade());
            newCredits = newEquivalence;

        } else {
            newCredits = new Credits();
        }

        newCredits.setStudentCurricularPlan(parent.getStudentCurricularPlan());
        newCredits.setExecutionPeriod(getExecutionPeriod());
        newCredits.setGivenCredits(credits.getGivenCredits());

        for (final IEnrolment enrolment : credits.getIEnrolments()) {
            EnrolmentWrapper.create(newCredits, enrolment);
        }

        if (dismissal.hasCurricularCourse()) {
            if (dismissal instanceof OptionalDismissal) {
                final OptionalDismissal optionalDismissal = (OptionalDismissal) dismissal;
                createNewOptionalDismissal(newCredits, parent, dismissal, optionalDismissal.getCurricularCourse(),
                        optionalDismissal.getEctsCredits());

            } else {
                createNewDismissal(newCredits, parent, dismissal);
            }
        } else if (dismissal.isCreditsDismissal()) {
            final CreditsDismissal creditsDismissal = (CreditsDismissal) dismissal;
            new CreditsDismissal(newCredits, parent, creditsDismissal.getNoEnrolCurricularCoursesSet());
        } else {
            throw new DomainException("error.unknown.dismissal.type");
        }
    }

    private boolean curriculumGroupHasSimilarDismissal(final CurriculumGroup curriculumGroup, final Dismissal dismissal) {
        for (final Dismissal each : curriculumGroup.getChildDismissals()) {
            if (each.isSimilar(dismissal)) {
                return true;
            }
        }
        return false;
    }

    private void moveExtraCurriculumGroupInformation(final StudentCurricularPlan oldStudentCurricularPlan,
            final StudentCurricularPlan newStudentCurricularPlan) {

        final ExtraCurriculumGroup oldExtraCurriculumGroup = oldStudentCurricularPlan.getExtraCurriculumGroup();
        if (oldExtraCurriculumGroup != null) {
            final ExtraCurriculumGroup newExtraCurriculumGroup = newStudentCurricularPlan.getExtraCurriculumGroup();
            if (newExtraCurriculumGroup == null) {
                throw new DomainException("error.invalid.newExtraCurriculumGroup");
            }
            for (final CurriculumModule curriculumModule : oldExtraCurriculumGroup.getCurriculumModulesSet()) {
                if (curriculumModule.isCurriculumLine()) {
                    final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                    if (!curriculumLine.hasExecutionPeriod()
                            || curriculumLine.getExecutionPeriod().isBefore(getExecutionPeriod())) {
                        continue;
                    }
                }

                curriculumModule.setCurriculumGroup(newExtraCurriculumGroup);
            }

            for (final CurriculumLine curriculumLine : newExtraCurriculumGroup.getAllCurriculumLines()) {
                if (curriculumLine.isDismissal()) {
                    final Dismissal dismissal = (Dismissal) curriculumLine;
                    dismissal.getCredits().setStudentCurricularPlan(newStudentCurricularPlan);
                }
            }
        }
    }

    private void tryRemoveOldSecondCycle(final CycleCurriculumGroup oldSecondCycle) {
        if (canRemoveOldSecondCycle(oldSecondCycle)) {
            deleteCurriculumModules(oldSecondCycle);
        }
    }

    protected void deleteCurriculumModules(final CurriculumModule curriculumModule) {
        if (curriculumModule == null) {
            return;
        }
        if (!curriculumModule.isLeaf()) {
            final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
            for (; !curriculumGroup.getCurriculumModulesSet().isEmpty();) {
                deleteCurriculumModules(curriculumGroup.getCurriculumModulesSet().iterator().next());
            }
            curriculumGroup.delete();
        } else if (curriculumModule.isDismissal()) {
            curriculumModule.delete();
        } else {
            throw new DomainException("error.can.only.remove.groups.and.dismissals");
        }
    }

    private boolean canRemoveOldSecondCycle(final CycleCurriculumGroup oldSecondCycle) {
        for (final CurriculumLine curriculumLine : oldSecondCycle.getAllCurriculumLines()) {
            if (curriculumLine.isEnrolment()) {
                return false;
            } else if (!curriculumLine.isDismissal()) {
                throw new DomainException("error.unknown.curriculum.line");
            }
        }
        return true;
    }

    private void markOldRegistrationWithConcludedState(final StudentCurricularPlan oldStudentCurricularPlan) {
        if (oldStudentCurricularPlan.getRegistration().hasState(RegistrationStateType.CONCLUDED)) {
            return;
        }

        LocalDate stateDate = new LocalDate();
        if (stateDate.isAfter(getExecutionYear().getEndDateYearMonthDay())) {
            stateDate = getExecutionYear().getEndDateYearMonthDay().toLocalDate();
        }

        final RegistrationState state =
                RegistrationState.createRegistrationState(oldStudentCurricularPlan.getRegistration(), null,
                        stateDate.toDateTimeAtStartOfDay(), RegistrationStateType.CONCLUDED);
        state.setResponsiblePerson(null);
    }

    private void moveGratuityEventsInformation(final StudentCurricularPlan oldStudentCurricularPlan,
            final StudentCurricularPlan newStudentCurricularPlan) {

        if (!oldStudentCurricularPlan.hasGratuityEvent(getExecutionYear(), GratuityEventWithPaymentPlan.class)
                || oldStudentCurricularPlan.getGratuityEvent(getExecutionYear(), GratuityEventWithPaymentPlan.class)
                        .isCancelled()) {
            return;
        }

        if (!newStudentCurricularPlan.hasGratuityEvent(getExecutionYear(), GratuityEventWithPaymentPlan.class)) {
            correctRegistrationRegime(oldStudentCurricularPlan, newStudentCurricularPlan);
            createGratuityEvent(newStudentCurricularPlan);
        }

        final GratuityEvent firstEvent =
                oldStudentCurricularPlan.getGratuityEvent(getExecutionYear(), GratuityEventWithPaymentPlan.class);
        final GratuityEvent secondEvent =
                newStudentCurricularPlan.getGratuityEvent(getExecutionYear(), GratuityEventWithPaymentPlan.class);

        if (!firstEvent.isGratuityEventWithPaymentPlan() || !secondEvent.isGratuityEventWithPaymentPlan()) {
            throw new DomainException("error.SeparationCyclesManagement.unexpected.event.types");
        }

        movePayments((GratuityEventWithPaymentPlan) firstEvent, (GratuityEventWithPaymentPlan) secondEvent);
    }

    private void createGratuityEvent(final StudentCurricularPlan newStudentCurricularPlan) {
        final InvocationResult result =
                new AccountingEventsManager().createGratuityEvent(newStudentCurricularPlan, getExecutionYear(), false);
        if (!result.isSuccess()) {
            throw new DomainExceptionWithInvocationResult(result);
        }
    }

    private void movePayments(final GratuityEventWithPaymentPlan firstEvent, final GratuityEventWithPaymentPlan secondEvent) {

        if (mustConfigurateToDefault(secondEvent)) {
            secondEvent.configurateDefaultPaymentPlan();
        }

        if (firstEvent.hasCustomGratuityPaymentPlan()) {
            return;
        }

        if (!firstEvent.hasAnyPayments()) {
            firstEvent.cancel(getNoPaymentsReason(secondEvent));
            return;
        }

        // First Event
        final Money amountLessPenalty = firstEvent.getPayedAmountLessPenalty();
        firstEvent.configurateCustomPaymentPlan();
        createInstallment(firstEvent, firstEvent.getGratuityPaymentPlan(), firstEvent.getPayedAmount());
        for (final PaymentCode paymentCode : firstEvent.getNonProcessedPaymentCodes()) {
            paymentCode.setState(PaymentCodeState.INVALID);
        }
        firstEvent.recalculateState(new DateTime());

        // Second Event
        final Money originalTotalAmount = secondEvent.getGratuityPaymentPlan().calculateOriginalTotalAmount();
        secondEvent.addDiscount(getPerson(), Money.min(amountLessPenalty, originalTotalAmount));
        secondEvent.recalculateState(new DateTime());
    }

    private boolean mustConfigurateToDefault(GratuityEventWithPaymentPlan secondEvent) {
        return !secondEvent.getRegistration().isPartialRegime(getExecutionYear());
    }

    private void correctRegistrationRegime(final StudentCurricularPlan oldStudentCurricularPlan,
            final StudentCurricularPlan newStudentCurricularPlan) {

        if (oldStudentCurricularPlan.getRegistration().isPartialRegime(getExecutionYear())
                && !newStudentCurricularPlan.getRegistration().isPartialRegime(getExecutionYear())) {

            new RegistrationRegime(newStudentCurricularPlan.getRegistration(), getExecutionYear(),
                    RegistrationRegimeType.PARTIAL_TIME);

        }
    }

    private Person getPerson() {
        return AccessControl.getPerson();
    }

    private void createInstallment(final GratuityEvent event, final PaymentPlan paymentPlan, final Money amount) {
        new Installment(paymentPlan, amount, event.getStartDate().toYearMonthDay(), event.getLastPaymentDate().toYearMonthDay());
    }

    private String getNoPaymentsReason(final GratuityEvent second) {
        final String message = BundleUtil.getString(Bundle.APPLICATION, "label.SeparationCyclesManagement.noPayments.reason");
        return MessageFormat.format(message, second.getStudentCurricularPlan().getName());
    }

    private void createAdministrativeOfficeFeeAndInsurance(final StudentCurricularPlan newStudentCurricularPlan) {
        if (!newStudentCurricularPlan.getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(getExecutionYear())
                && newStudentCurricularPlan.hasEnrolments(getExecutionYear())) {
            new AccountingEventsManager().createAdministrativeOfficeFeeAndInsuranceEvent(newStudentCurricularPlan,
                    getExecutionYear(), false);
        }
    }

    protected ExecutionSemester getExecutionPeriod() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    private ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

}
