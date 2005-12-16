package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.GratuityValuesNotDefinedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.StudentState;
import net.sourceforge.fenixedu.util.StudentType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RegisterCandidate implements IService {

    public InfoCandidateRegistration run(Integer candidateID, Integer branchID, Integer studentNumber,
            IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        candidateID);
        IPerson person = masterDegreeCandidate.getPerson();
        IStudent student = person.getStudentByType(DegreeType.MASTER_DEGREE);

        checkCandidateSituation(masterDegreeCandidate.getActiveCandidateSituation());

        // remove master degree candidate role
        person.getPersonRoles().remove(person.getPersonRole(RoleType.MASTER_DEGREE_CANDIDATE));

        // check if old student number is free
        checkOldStudentNumber(studentNumber, person, sp);

        // create new student
        if (student == null) {
            student = createNewStudent(studentNumber, person, sp);
        }

        checkDuplicateStudentCurricularPlan(masterDegreeCandidate, student);

        IStudentCurricularPlan studentCurricularPlan = createNewStudentCurricularPlan(student, branchID,
                masterDegreeCandidate, sp);

        createEnrolments(userView, masterDegreeCandidate, studentCurricularPlan, sp);

        updateCandidateSituation(masterDegreeCandidate);

        copyQualifications(masterDegreeCandidate, person);

        createGratuitySituation(masterDegreeCandidate, studentCurricularPlan);

        return createNewInfoCandidateRegistration(masterDegreeCandidate, studentCurricularPlan);

    }

    private InfoCandidateRegistration createNewInfoCandidateRegistration(
            IMasterDegreeCandidate masterDegreeCandidate, IStudentCurricularPlan studentCurricularPlan) {
        InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndInfoBranch
                        .newInfoFromDomain(studentCurricularPlan));
        infoCandidateRegistration.setEnrolments(new ArrayList<InfoEnrolment>());
        Iterator iteratorSCPs = studentCurricularPlan.getEnrolments().iterator();
        while (iteratorSCPs.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iteratorSCPs.next();
            InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(enrolment);
            infoCandidateRegistration.getEnrolments().add(infoEnrolment);
        }
        return infoCandidateRegistration;
    }

    private void createGratuitySituation(IMasterDegreeCandidate masterDegreeCandidate,
            IStudentCurricularPlan studentCurricularPlan)
            throws GratuityValuesNotDefinedServiceException {

        IGratuityValues gratuityValues = masterDegreeCandidate.getExecutionDegree().getGratuityValues();

        if (gratuityValues == null) {
            throw new GratuityValuesNotDefinedServiceException(
                    "error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
        }

        DomainFactory.makeGratuitySituation(gratuityValues, studentCurricularPlan);

    }

    private void copyQualifications(IMasterDegreeCandidate masterDegreeCandidate, IPerson person) {
        IQualification qualification = DomainFactory.makeQualification();
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

    private void updateCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate) {
        masterDegreeCandidate.getActiveCandidateSituation().setValidation(new State(State.INACTIVE));

        ICandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
        candidateSituation.setDate(Calendar.getInstance().getTime());
        candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
        candidateSituation.setValidation(new State(State.ACTIVE));
        candidateSituation.setSituation(SituationName.ENROLLED_OBJ);
    }

    private void createEnrolments(IUserView userView, IMasterDegreeCandidate masterDegreeCandidate,
            IStudentCurricularPlan studentCurricularPlan, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List<ICandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();
        IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod()
                .readActualExecutionPeriod();
        for (ICandidateEnrolment candidateEnrolment : candidateEnrolments) {
            IEnrolment enrolment = DomainFactory.makeEnrolment();
            enrolment.initializeAsNew(studentCurricularPlan, candidateEnrolment.getCurricularCourse(),
                    executionPeriod, EnrollmentCondition.FINAL, userView.getUtilizador());
        }
    }

    private IStudentCurricularPlan createNewStudentCurricularPlan(IStudent student, Integer branchID,
            IMasterDegreeCandidate masterDegreeCandidate, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IBranch branch = (IBranch) sp.getIPersistentBranch().readByOID(Branch.class, branchID);
        IDegreeCurricularPlan degreecurricularPlan = masterDegreeCandidate.getExecutionDegree()
                .getDegreeCurricularPlan();
        Date startDate = Calendar.getInstance().getTime();

        IStudentCurricularPlan studentCurricularPlan = DomainFactory.makeStudentCurricularPlan(student,
                degreecurricularPlan, branch, startDate, StudentCurricularPlanState.ACTIVE,
                masterDegreeCandidate.getGivenCredits(), masterDegreeCandidate.getSpecialization());
        return studentCurricularPlan;
    }

    private IStudent createNewStudent(Integer studentNumber, IPerson person, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IStudent student;
        if (studentNumber == null) {
            studentNumber = sp.getIPersistentStudent().generateStudentNumber(DegreeType.MASTER_DEGREE);
        }

        IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(
                new StudentType(StudentType.NORMAL));
        StudentState state = new StudentState(StudentState.INSCRITO);
        student = DomainFactory.makeStudent(person, studentNumber, studentKind, state, false, false,
                EntryPhase.FIRST_PHASE_OBJ, DegreeType.MASTER_DEGREE);
        student.setInterruptedStudies(false);

        person.addPersonRoles(sp.getIPersistentRole().readByRoleType(RoleType.STUDENT));
        return student;
    }

    private void checkDuplicateStudentCurricularPlan(IMasterDegreeCandidate masterDegreeCandidate,
            IStudent student) throws ExistingServiceException {
        List<IStudentCurricularPlan> studentCurricularPlans = masterDegreeCandidate.getExecutionDegree()
                .getDegreeCurricularPlan().getStudentCurricularPlans();
        for (IStudentCurricularPlan scp : studentCurricularPlans) {
            if (scp.getStudent().equals(student)
                    && scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE)) {
                throw new ExistingServiceException();
            }
        }
    }

    private void checkOldStudentNumber(Integer studentNumber, IPerson person, ISuportePersistente sp)
            throws ExcepcaoPersistencia, ExistingServiceException {
        if (studentNumber != null) {
            IStudent existingStudent = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(
                    studentNumber, DegreeType.MASTER_DEGREE);
            if (!existingStudent.getPerson().equals(person)) {
                throw new ExistingServiceException();
            }
        }
    }

    /**
     * @param situation
     * @return
     * @throws InvalidChangeServiceException
     */
    private void checkCandidateSituation(ICandidateSituation situation)
            throws InvalidChangeServiceException {

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
