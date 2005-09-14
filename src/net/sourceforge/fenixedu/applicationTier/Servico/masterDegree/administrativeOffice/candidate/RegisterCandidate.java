package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.GratuityValuesNotDefinedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.StudentState;
import net.sourceforge.fenixedu.util.StudentType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RegisterCandidate implements IService {

    // boolean personIsLocked = false;

    public InfoCandidateRegistration run(Integer candidateID, Integer branchID, Integer studentNumber,
            IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IStudentCurricularPlan studentCurricularPlanResult = null;
        IStudent student = null;
        
        if (studentNumber != null) {
            student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(studentNumber,
                    DegreeType.MASTER_DEGREE);
        }

        IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                        candidateID);

        if (student != null) {

            if ((!masterDegreeCandidate.getPerson().getIdInternal().equals(
                    student.getPerson().getIdInternal()))) {
                throw new ExistingServiceException();
            }
        }

        if (!validSituation(masterDegreeCandidate.getActiveCandidateSituation())) {
            throw new InvalidChangeServiceException();
        }

        // Check if a Master Degree Student Already Exists
        if (student == null) {
            student = sp.getIPersistentStudent().readByPersonAndDegreeType(
                    masterDegreeCandidate.getPerson().getIdInternal(), DegreeType.MASTER_DEGREE);
        }

        IRole role = (IRole) CollectionUtils.find(masterDegreeCandidate.getPerson().getPersonRoles(),
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IRole role = (IRole) arg0;
                        return role.getRoleType() == RoleType.MASTER_DEGREE_CANDIDATE;
                    }
                });
        if (role != null) {
            masterDegreeCandidate.getPerson().getPersonRoles().remove(role);
        }
        Integer newStudentNumber = sp.getIPersistentStudent().generateStudentNumber(
                DegreeType.MASTER_DEGREE);

        if (studentNumber != null && studentNumber.intValue() > newStudentNumber.intValue())
            throw new InvalidStudentNumberServiceException();

        if (student == null) {

            Integer number = ((studentNumber == null) ? newStudentNumber : studentNumber);
            IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(
                    new StudentType(StudentType.NORMAL));
            StudentState state = new StudentState(StudentState.INSCRITO);
            student = DomainFactory.makeStudent(masterDegreeCandidate.getPerson(), number, studentKind, state, false,
                    false, EntryPhase.FIRST_PHASE_OBJ, DegreeType.MASTER_DEGREE);
            student.setInterruptedStudies(false);

            List roles = new ArrayList();
            Iterator iterator = masterDegreeCandidate.getPerson().getPersonRolesIterator();
            while (iterator.hasNext()) {
                // roles.add(Cloner.copyIRole2InfoRole((IRole)
                // iterator.next()));
                roles.add(InfoRole.newInfoFromDomain((IRole) iterator.next()));
            }

            // Give The Student Role if Necessary
            if (!AuthorizationUtils.containsRole(roles, RoleType.STUDENT)) {
                role = sp.getIPersistentRole().readByRoleType(RoleType.STUDENT);
                masterDegreeCandidate.getPerson().addPersonRoles(role);
            }
        }

        List<IStudentCurricularPlan> studentCurricularPlans = masterDegreeCandidate.getExecutionDegree()
                .getDegreeCurricularPlan().getStudentCurricularPlans();

        IStudentCurricularPlan existingStudentCurricularPlan = null;
        for (IStudentCurricularPlan scp : studentCurricularPlans) {
            if (scp.getStudent().getIdInternal().equals(student.getIdInternal())
                    && scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE)) {
                existingStudentCurricularPlan = scp;
                break;
            }
        }

        if (existingStudentCurricularPlan != null) {
            throw new ExistingServiceException();
        }

        IBranch branch = (IBranch) sp.getIPersistentBranch().readByOID(Branch.class, branchID);
        IDegreeCurricularPlan degreecurricularPlan = masterDegreeCandidate.getExecutionDegree()
                .getDegreeCurricularPlan();
        Date startDate = Calendar.getInstance().getTime();

        IStudentCurricularPlan studentCurricularPlan = DomainFactory.makeStudentCurricularPlan(student,
                degreecurricularPlan, branch, startDate, StudentCurricularPlanState.ACTIVE,
                masterDegreeCandidate.getGivenCredits(), masterDegreeCandidate.getSpecialization());

        // Get the Candidate Enrolments

        List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(
                masterDegreeCandidate.getIdInternal());

        Iterator iterator = candidateEnrolments.iterator();
        while (iterator.hasNext()) {
            ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) iterator.next();

            // TODO refactor
            IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod()
                    .readActualExecutionPeriod();
            IEnrolment enrolment = DomainFactory.makeEnrolment();

            enrolment.initializeAsNew(studentCurricularPlan, candidateEnrolment.getCurricularCourse(),
                    executionPeriod, EnrollmentCondition.FINAL, userView.getUtilizador());
            // TODO end refactor
        }

        // Change the Candidate Situation

        ICandidateSituation oldCandidateSituation = (ICandidateSituation) sp
                .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                        masterDegreeCandidate.getActiveCandidateSituation().getIdInternal(), true);
        oldCandidateSituation.setValidation(new State(State.INACTIVE));

        ICandidateSituation candidateSituation = DomainFactory.makeCandidateSituation();
        candidateSituation.setDate(Calendar.getInstance().getTime());
        candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
        candidateSituation.setValidation(new State(State.ACTIVE));
        candidateSituation.setSituation(SituationName.ENROLLED_OBJ);

        // Copy Qualifications

        IQualification qualification = DomainFactory.makeQualification();
        if (masterDegreeCandidate.getAverage() != null) {
            qualification.setMark(masterDegreeCandidate.getAverage().toString());
        }
        qualification.setPerson(masterDegreeCandidate.getPerson());
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

        // Change the Username If neccessary
        changeUsernameIfNeccessary(studentCurricularPlan.getStudent());

        studentCurricularPlanResult = studentCurricularPlan;

        // Create Gratuity Situations
        IGratuityValues gratuityValues = masterDegreeCandidate.getExecutionDegree().getGratuityValues();

        if (gratuityValues == null) {
            throw new GratuityValuesNotDefinedServiceException(
                    "error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
        }

        IGratuitySituation gratuitySituation = DomainFactory.makeGratuitySituation();

        gratuitySituation.setGratuityValues(gratuityValues);
        gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);
        gratuitySituation.setWhen(Calendar.getInstance().getTime());
        Double totalValue = null;

        if (studentCurricularPlan.getSpecialization().equals(Specialization.MASTER_DEGREE)) {
            totalValue = calculateTotalValueForMasterDegree(gratuityValues);
        } else if (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION)) {
            totalValue = new Double(0);
        }

        if (totalValue == null) {
            throw new GratuityValuesNotDefinedServiceException(
                    "error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
        }

        gratuitySituation.setRemainingValue(totalValue);
        gratuitySituation.setTotalValue(totalValue);

        InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndInfoBranch
                        .newInfoFromDomain(studentCurricularPlanResult));
        infoCandidateRegistration.setEnrolments(new ArrayList());
        Iterator iteratorSCPs = studentCurricularPlanResult.getEnrolments().iterator();
        while (iteratorSCPs.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iteratorSCPs.next();
            InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(enrolment);
            infoCandidateRegistration.getEnrolments().add(infoEnrolment);
        }

        return infoCandidateRegistration;
    }

    private void changeUsernameIfNeccessary(IStudent student) throws ExcepcaoPersistencia {

        String currentUsername = student.getPerson().getUsername();
        if (!currentUsername.matches("[a-zA-Z][0-9]+")) {
            student.getPerson().setUsername("M" + student.getNumber());
        }
    }

    /**
     * @param situation
     * @return
     */
    private boolean validSituation(ICandidateSituation situation) {
        boolean result = false;
        if (situation.getSituation().equals(SituationName.ADMITIDO_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_OTHER_OBJ)
                || situation.getSituation().equals(SituationName.ADMITED_SPECIALIZATION_OBJ)) {
            result = true;
        }
        return result;
    }

    /**
     * @param gratuityValues
     * @return
     */
    private Double calculateTotalValueForMasterDegree(IGratuityValues gratuityValues) {
        Double totalValue = null;

        Double annualValue = gratuityValues.getAnualValue();

        if ((annualValue != null) && (annualValue.doubleValue() != 0)) {
            // we have data to calculate using annual value
            totalValue = annualValue;
        } else {
            // we have to use the components (scholarship + final proof)
            // information
            totalValue = new Double(gratuityValues.getScholarShipValue().doubleValue()
                    + (gratuityValues.getFinalProofValue() == null ? 0 : gratuityValues
                            .getFinalProofValue().doubleValue()));

        }

        return totalValue;
    }

}
