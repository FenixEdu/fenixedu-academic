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
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidChangeServiceException;
import org.fenixedu.academic.service.services.exceptions.gratuity.masterDegree.GratuityValuesNotDefinedServiceException;
import org.fenixedu.academic.dto.InfoCandidateRegistration;
import org.fenixedu.academic.dto.InfoEnrolment;
import org.fenixedu.academic.dto.InfoMasterDegreeCandidateWithInfoPerson;
import org.fenixedu.academic.dto.InfoStudentCurricularPlan;
import org.fenixedu.academic.domain.Branch;
import org.fenixedu.academic.domain.CandidateEnrolment;
import org.fenixedu.academic.domain.CandidateSituation;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.GratuitySituation;
import org.fenixedu.academic.domain.GratuityValues;
import org.fenixedu.academic.domain.MasterDegreeCandidate;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.SituationName;
import org.fenixedu.academic.util.State;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RegisterCandidate {

    @Atomic
    public static InfoCandidateRegistration run(String candidateID, String branchID, Integer studentNumber, User userView)
            throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        Person person = masterDegreeCandidate.getPerson();

        checkCandidateSituation(masterDegreeCandidate.getActiveCandidateSituation());

        // check if old student number is free
        checkOldStudentNumber(studentNumber, person);

        // create new student
        final ExecutionDegree executionDegree = masterDegreeCandidate.getExecutionDegree();
        Registration registration = createNewRegistration(person, studentNumber, executionDegree.getDegree());

        // person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));

        StudentCurricularPlan studentCurricularPlan =
                createNewStudentCurricularPlan(registration, branchID, masterDegreeCandidate);

        // person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));

        createEnrolments(userView, masterDegreeCandidate, studentCurricularPlan);

        updateCandidateSituation(masterDegreeCandidate);

        copyQualifications(masterDegreeCandidate, person);

        createGratuitySituation(masterDegreeCandidate, studentCurricularPlan);

        return createNewInfoCandidateRegistration(masterDegreeCandidate, studentCurricularPlan);

    }

    private static InfoCandidateRegistration createNewInfoCandidateRegistration(MasterDegreeCandidate masterDegreeCandidate,
            StudentCurricularPlan studentCurricularPlan) {
        InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
        infoCandidateRegistration.setEnrolments(new ArrayList<InfoEnrolment>());
        Iterator<Enrolment> iteratorSCPs = studentCurricularPlan.getEnrolmentsSet().iterator();
        while (iteratorSCPs.hasNext()) {
            Enrolment enrolment = iteratorSCPs.next();
            infoCandidateRegistration.getEnrolments().add(InfoEnrolment.newInfoFromDomain(enrolment));
        }
        return infoCandidateRegistration;
    }

    private static void createGratuitySituation(MasterDegreeCandidate masterDegreeCandidate,
            StudentCurricularPlan studentCurricularPlan) throws GratuityValuesNotDefinedServiceException {

        GratuityValues gratuityValues = masterDegreeCandidate.getExecutionDegree().getGratuityValues();

        if (gratuityValues == null) {
            throw new GratuityValuesNotDefinedServiceException("error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
        }

        new GratuitySituation(gratuityValues, studentCurricularPlan);
    }

    private static void copyQualifications(MasterDegreeCandidate masterDegreeCandidate, Person person) {
        Qualification qualification = new Qualification();
        if (masterDegreeCandidate.getAverage() != null) {
            qualification.setMark(masterDegreeCandidate.getAverage().toString());
        }
        qualification.setPerson(person);
        if (masterDegreeCandidate.getMajorDegreeSchool() == null) {
            qualification.setSchool("");
        } else {
            qualification.setSchool(masterDegreeCandidate.getMajorDegreeSchool());
        }
        qualification.setTitle(masterDegreeCandidate.getMajorDegree());

        Calendar calendar = Calendar.getInstance();
        if (masterDegreeCandidate.getMajorDegreeYear() == null) {
            qualification.setDate(calendar.getTime());
        } else {
            calendar.set(Calendar.YEAR, masterDegreeCandidate.getMajorDegreeYear().intValue());
            qualification.setDate(calendar.getTime());
        }
        qualification.setDegree(masterDegreeCandidate.getMajorDegree());
    }

    private static void updateCandidateSituation(MasterDegreeCandidate masterDegreeCandidate) {
        masterDegreeCandidate.getActiveCandidateSituation().setValidation(new State(State.INACTIVE));

        CandidateSituation candidateSituation = new CandidateSituation();
        candidateSituation.setDate(Calendar.getInstance().getTime());
        candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
        candidateSituation.setValidation(new State(State.ACTIVE));
        candidateSituation.setSituation(SituationName.ENROLLED_OBJ);
    }

    private static void createEnrolments(User userView, MasterDegreeCandidate masterDegreeCandidate,
            StudentCurricularPlan studentCurricularPlan) {
        Collection<CandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolmentsSet();
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        for (CandidateEnrolment candidateEnrolment : candidateEnrolments) {
            new Enrolment(studentCurricularPlan, candidateEnrolment.getCurricularCourse(), executionSemester,
                    EnrollmentCondition.FINAL, userView.getUsername());
        }
    }

    private static StudentCurricularPlan createNewStudentCurricularPlan(Registration registration, String branchID,
            MasterDegreeCandidate masterDegreeCandidate) {
        Branch branch = FenixFramework.getDomainObject(branchID);
        DegreeCurricularPlan degreecurricularPlan = masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan();

        StudentCurricularPlan studentCurricularPlan =
                StudentCurricularPlan.createPreBolonhaMasterDegree(registration, degreecurricularPlan, new YearMonthDay(),
                        branch, masterDegreeCandidate.getGivenCredits(), masterDegreeCandidate.getSpecialization());
        return studentCurricularPlan;
    }

    private static Registration createNewRegistration(Person person, Integer studentNumber, Degree degree) {
        return new Registration(person, studentNumber, degree);
    }

    private static void checkOldStudentNumber(Integer studentNumber, Person person) throws ExistingServiceException {
        if (studentNumber != null) {

            Registration existingStudent = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE);

            if (existingStudent != null && !existingStudent.getPerson().equals(person)) {
                throw new ExistingServiceException();
            }
        }
    }

    private static void checkCandidateSituation(CandidateSituation situation) throws InvalidChangeServiceException {
        if (situation.getSituation().equals(SituationName.ADMITIDO_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_OTHER_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_SPECIALIZATION_OBJ)) {
            return;
        }

        throw new InvalidChangeServiceException();
    }

}