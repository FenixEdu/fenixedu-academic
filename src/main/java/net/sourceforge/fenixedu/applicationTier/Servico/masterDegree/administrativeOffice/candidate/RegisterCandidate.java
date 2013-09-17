package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.GratuityValuesNotDefinedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RegisterCandidate {

    @Atomic
    public static InfoCandidateRegistration run(String candidateID, String branchID, Integer studentNumber, IUserView userView)
            throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

        Person person = masterDegreeCandidate.getPerson();

        checkCandidateSituation(masterDegreeCandidate.getActiveCandidateSituation());

        // remove master degree candidate role
        person.removeRoleByType(RoleType.MASTER_DEGREE_CANDIDATE);

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

    private static void createEnrolments(IUserView userView, MasterDegreeCandidate masterDegreeCandidate,
            StudentCurricularPlan studentCurricularPlan) {
        Collection<CandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        for (CandidateEnrolment candidateEnrolment : candidateEnrolments) {
            new Enrolment(studentCurricularPlan, candidateEnrolment.getCurricularCourse(), executionSemester,
                    EnrollmentCondition.FINAL, userView.getUtilizador());
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