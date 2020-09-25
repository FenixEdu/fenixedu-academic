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
package org.fenixedu.academic.domain.candidacy.workflow;

import java.util.Set;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacy.degree.ShiftDistributionEntry;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.YearMonthDay;

public class RegistrationOperation extends CandidacyOperation {
    public static class RegistrationCreatedByCandidacy extends DomainObjectEvent<Registration> {
        private final StudentCandidacy candidacy;

        public RegistrationCreatedByCandidacy(Registration instance, StudentCandidacy candidacy) {
            super(instance);
            this.candidacy = candidacy;
        }

        public StudentCandidacy getCandidacy() {
            return candidacy;
        }
    }

    static private final long serialVersionUID = 1L;

    public RegistrationOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes, candidacy);
    }

    @Override
    protected void internalExecute() {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final Registration registration = createRegistration();
        enrolStudentInCurricularCourses(executionDegree, registration);
        associateShiftsFor(registration);
        //assignMeasurementTestShift(registration);
        Signal.emit("academic.candidacy.registration.created", new RegistrationCreatedByCandidacy(registration,
                getStudentCandidacy()));
    }

    protected void associateShiftsFor(final Registration registration) {
        if (getExecutionYear().getShiftDistribution() != null) {
            for (final ShiftDistributionEntry shiftEntry : getExecutionDegree().getNextFreeShiftDistributions(registration.getNumber())) {
                shiftEntry.setDistributed(Boolean.TRUE);
                shiftEntry.getShift().addStudents(registration);
                correctExecutionCourseIfNecessary(registration, shiftEntry.getShift());
            }
        }
    }

    private void correctExecutionCourseIfNecessary(Registration registration, Shift shift) {

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        final ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        final ExecutionCourse executionCourse = shift.getExecutionCourse();

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final Enrolment enrolment = studentCurricularPlan.findEnrolmentFor(curricularCourse, semester);
            if (enrolment != null) {
                final Attends attends = enrolment.getAttendsFor(semester);
                if (attends != null && !attends.isFor(executionCourse)) {
                    attends.setDisciplinaExecucao(executionCourse);
                }
                break;
            }
        }
    }

    private ExecutionDegree getExecutionDegree() {
        return getStudentCandidacy().getExecutionDegree();
    }

    protected ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
    }

    protected void enrolStudentInCurricularCourses(final ExecutionDegree executionDegree, final Registration registration) {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final StudentCurricularPlan studentCurricularPlan =
                StudentCurricularPlan.createBolonhaStudentCurricularPlan(registration, executionDegree.getDegreeCurricularPlan(),
                        new YearMonthDay(), executionSemester);

        studentCurricularPlan.createFirstTimeStudentEnrolmentsFor(executionSemester, getCurrentUsername());
        registration.updateEnrolmentDate(executionSemester.getExecutionYear());
    }

    private String getCurrentUsername() {
        if (Authenticate.getUser() != null) {
            return AccessControl.getPerson().getUsername();
        }
        return getStudentCandidacy().getPerson().getUsername();
    }

    private ExecutionSemester getExecutionPeriod() {
        return getExecutionYear().getExecutionSemesterFor(1);
    }

    protected Registration createRegistration() {
        final Registration registration = new Registration(getStudentCandidacy().getPerson(), getStudentCandidacy());

        getStudentCandidacy().getPrecedentDegreeInformation().setRegistration(registration);
        getStudentCandidacy().getPrecedentDegreeInformation().getPersonalIngressionData()
                .setStudent(getStudentCandidacy().getPerson().getStudent());

        registration.getStudent().setPersonalDataAuthorization(getStudentCandidacy().getStudentPersonalDataAuthorizationChoice());
        
        if (getStudentCandidacy().getApplyForResidence()) {
            registration.getStudent().setResidenceCandidacyForCurrentExecutionYear(
                    getStudentCandidacy().getNotesAboutResidenceAppliance());
        }

        return registration;
    }

    private StudentCandidacy getStudentCandidacy() {
        return ((StudentCandidacy) getCandidacy());
    }

    @Override
    public CandidacyOperationType getType() {
        return CandidacyOperationType.REGISTRATION;
    }

    @Override
    public boolean isInput() {
        return false;
    }
}