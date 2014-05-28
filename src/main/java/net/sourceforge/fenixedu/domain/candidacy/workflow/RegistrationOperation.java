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
package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipIntention;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.MeasurementTest;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RegistrationOperation extends CandidacyOperation {

    static private final long serialVersionUID = 1L;
    static private final List<String> DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION = new ArrayList<String>();

    static {
        DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.add("MEMec");
        DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.add("MEC");
    }

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
        associateTutor(registration);
    }

    private void associateTutor(Registration registration) {
        if (getStudentCandidacy().getEntryPhase().equals(EntryPhase.FIRST_PHASE)) {
            if (!DEGREES_WITHOUT_AUTOMATIC_TUTOR_DISTRIBUTION.contains(registration.getDegree().getSigla())) {
                Teacher teacher = getAvailableTutorTeacher();
                if (teacher != null) {
                    StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                    Tutorship.createTutorship(teacher, scp, new LocalDate().getMonthOfYear(),
                            Tutorship.getLastPossibleTutorshipYear());
                }
            }
        }
    }

    private Teacher getAvailableTutorTeacher() {
        for (TutorshipIntention tutorshipIntention : getExecutionDegree().getTutorshipIntentions()) {
            if (tutorshipIntention.getMaxStudentsToTutor() > 0) {
                tutorshipIntention.setMaxStudentsToTutor(tutorshipIntention.getMaxStudentsToTutor() - 1);
                return tutorshipIntention.getTeacher();
            }
        }
        return null;
    }

    private void assignMeasurementTestShift(Registration registration) {
        final MeasurementTest test =
                MeasurementTest.readBy(getStudentCandidacy().getEntryPhase(), getExecutionYear(), registration.getCampus());

        if (test != null) {
            test.assignToRoom(registration);
        }
    }

    protected void associateShiftsFor(final Registration registration) {

        if (getExecutionYear().hasShiftDistribution()) {
            for (final ShiftDistributionEntry shiftEntry : getExecutionDegree().getNextFreeShiftDistributions()) {
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
        registration.getStudent().setStudentPersonalDataStudentsAssociationAuthorization(
                getStudentCandidacy().getStudentPersonalDataStudentsAssociationAuthorization());

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

    @Override
    public boolean isAuthorized(Person person) {
        if (getCandidacy().getPerson().hasRole(RoleType.PERSON)) {
            return person.hasRole(RoleType.EMPLOYEE);
        } else {
            return super.isAuthorized(person);
        }
    }

}