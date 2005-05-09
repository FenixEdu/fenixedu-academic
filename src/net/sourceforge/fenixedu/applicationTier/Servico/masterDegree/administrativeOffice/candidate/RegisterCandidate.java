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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.Specialization;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
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

    boolean personIsLocked = false;

    /**
     * The actor of this class.
     */
    public RegisterCandidate() {
    }

    public InfoCandidateRegistration run(Integer candidateID, Integer branchID, Integer studentNumber,
            IUserView userView) throws FenixServiceException {

        ISuportePersistente sp = null;

        IStudentCurricularPlan studentCurricularPlanResult = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        IStudent student = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            if (studentNumber != null) {
                student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(studentNumber,
                        DegreeType.MASTER_DEGREE);

                // if (student != null) {
                // throw new ExistingServiceException();
                // }
            }

            masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate()
                    .readByOID(MasterDegreeCandidate.class, candidateID);

            if (student != null) {

                if ((!masterDegreeCandidate.getPerson().getIdInternal().equals(
                        student.getPerson().getIdInternal()))) {
                    throw new ExistingServiceException();
                }

                // List studentCurricularPlans =
                // student.getStudentCurricularPlans();
                // for (Iterator iter = studentCurricularPlans.iterator();
                // iter.hasNext();) {
                // IStudentCurricularPlan studentCurricularPlan =
                // (IStudentCurricularPlan) iter.next();
                // if (studentCurricularPlan.getCurrentState().equals(
                // StudentCurricularPlanState.ACTIVE_OBJ)) {
                // if
                // (masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan()
                // .getIdInternal().equals(
                // studentCurricularPlan.getDegreeCurricularPlan().getIdInternal()))
                // {
                // throw new ExistingServiceException();
                // }
                // }
                // }
            }

            if (!validSituation(masterDegreeCandidate.getActiveCandidateSituation())) {
                throw new InvalidChangeServiceException();
            }

            // Check if a Master Degree Student Already Exists
            if (student == null) {
                student = sp.getIPersistentStudent().readByPersonAndDegreeType(
                        masterDegreeCandidate.getPerson(), DegreeType.MASTER_DEGREE);
            }

            IRole role = (IRole) CollectionUtils.find(
                    masterDegreeCandidate.getPerson().getPersonRoles(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            IRole role = (IRole) arg0;
                            return role.getRoleType() == RoleType.MASTER_DEGREE_CANDIDATE;
                        }
                    });
            if (role != null) {
                sp.getIPessoaPersistente().simpleLockWrite(masterDegreeCandidate.getPerson());
                personIsLocked = true;
                masterDegreeCandidate.getPerson().getPersonRoles().remove(role);
            }
            Integer newStudentNumber = null;
            newStudentNumber = sp.getIPersistentStudent().generateStudentNumber(DegreeType.MASTER_DEGREE);

            if (studentNumber != null && studentNumber.intValue() > newStudentNumber.intValue())
                throw new InvalidStudentNumberServiceException();

            if (student == null) {
                student = new Student();
                sp.getIPersistentStudent().simpleLockWrite(student);
                student.setPayedTuition(new Boolean(false));
                student.setEnrollmentForbidden(new Boolean(false));
                student.setEntryPhase(EntryPhase.FIRST_PHASE_OBJ);
                student.setDegreeType(DegreeType.MASTER_DEGREE);
                student.setPerson(masterDegreeCandidate.getPerson());
                student.setState(new StudentState(StudentState.INSCRITO));

                if (studentNumber == null) {
                    student.setNumber(newStudentNumber);
                } else {
                    student.setNumber(studentNumber);
                }

                IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(
                        new StudentType(StudentType.NORMAL));
                student.setStudentKind(studentKind);

                List roles = new ArrayList();
                Iterator iterator = masterDegreeCandidate.getPerson().getPersonRoles().iterator();
                while (iterator.hasNext()) {
                    roles.add(Cloner.copyIRole2InfoRole((IRole) iterator.next()));
                }

                // Give The Student Role if Necessary
                if (!AuthorizationUtils.containsRole(roles, RoleType.STUDENT)) {
                    role = sp.getIPersistentRole().readByRoleType(RoleType.STUDENT);
                    if (!personIsLocked) {
                        sp.getIPessoaPersistente().simpleLockWrite(masterDegreeCandidate.getPerson());
                        masterDegreeCandidate.getPerson().getPersonRoles().add(role);
                    }
                }
            }

            // IStudentCurricularPlan studentCurricularPlanOld =
            // sp.getIStudentCurricularPlanPersistente()
            // .readActiveStudentCurricularPlan(student.getNumber(),
            // DegreeType.MESTRADO_OBJ);
            //
            // if ((studentCurricularPlanOld != null)
            // && (studentCurricularPlanOld.getCurrentState()
            // .equals(StudentCurricularPlanState.ACTIVE_OBJ))) {
            // throw new
            // ActiveStudentCurricularPlanAlreadyExistsServiceException();
            // }

            IStudentCurricularPlan existingStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente().readByStudentDegreeCurricularPlanAndState(
                            student,
                            masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan(),
                            StudentCurricularPlanState.ACTIVE_OBJ);
            if (existingStudentCurricularPlan != null) {
                throw new ExistingServiceException();
            }

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);

            IBranch branch = (IBranch) sp.getIPersistentBranch().readByOID(Branch.class, branchID);

            studentCurricularPlan.setBranch(branch);
            studentCurricularPlan.setEnrolments(new ArrayList());
            studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
            studentCurricularPlan.setDegreeCurricularPlan(masterDegreeCandidate.getExecutionDegree()
                    .getDegreeCurricularPlan());
            studentCurricularPlan.setGivenCredits(masterDegreeCandidate.getGivenCredits());
            studentCurricularPlan.setSpecialization(masterDegreeCandidate.getSpecialization());
            studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
            studentCurricularPlan.setStudent(student);

            // Get the Candidate Enrolments

            List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(
                    masterDegreeCandidate.getIdInternal());

            Iterator iterator = candidateEnrolments.iterator();
            while (iterator.hasNext()) {
                ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) iterator.next();

                IEnrolment enrolment = new Enrolment();
                sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
                enrolment.setCurricularCourse(candidateEnrolment.getCurricularCourse());
                enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
                enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
                enrolment.setExecutionPeriod(sp.getIPersistentExecutionPeriod()
                        .readActualExecutionPeriod());
                enrolment.setStudentCurricularPlan(studentCurricularPlan);
                enrolment.setEvaluations(new ArrayList());
                enrolment.setCondition(EnrollmentCondition.FINAL);

                IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
                enrolmentEvaluation.setEnrolment(enrolment);
                enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
                enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);

                enrolment.getEvaluations().add(enrolmentEvaluation);
                enrolment.setCreationDate(new Date());
                enrolment.setCreatedBy(userView.getUtilizador());
            }

            // Change the Candidate Situation

            ICandidateSituation oldCandidateSituation = (ICandidateSituation) sp
                    .getIPersistentCandidateSituation().readByOID(CandidateSituation.class,
                            masterDegreeCandidate.getActiveCandidateSituation().getIdInternal(), true);
            oldCandidateSituation.setValidation(new State(State.INACTIVE));

            ICandidateSituation candidateSituation = new CandidateSituation();
            sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
            candidateSituation.setDate(Calendar.getInstance().getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setValidation(new State(State.ACTIVE));
            candidateSituation.setSituation(SituationName.ENROLLED_OBJ);

            // Copy Qualifications

            IQualification qualification = new Qualification();
            sp.getIPersistentQualification().simpleLockWrite(qualification);
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
            IPersistentGratuityValues gratuityValuesDAO = sp.getIPersistentGratuityValues();
            IPersistentGratuitySituation gratuitySituationDAO = sp.getIPersistentGratuitySituation();
            IGratuityValues gratuityValues = gratuityValuesDAO
                    .readGratuityValuesByExecutionDegree(masterDegreeCandidate.getExecutionDegree());

            if (gratuityValues == null) {
                throw new GratuityValuesNotDefinedServiceException(
                        "error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
            }

            IGratuitySituation gratuitySituation = new GratuitySituation();

            gratuitySituation.setGratuityValues(gratuityValues);
            gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);
            gratuitySituation.setWhen(Calendar.getInstance().getTime());
            Double totalValue = null;

            if (studentCurricularPlan.getSpecialization().equals(Specialization.MESTRADO_TYPE)) {
                totalValue = calculateTotalValueForMasterDegree(gratuityValues);
            } else if (studentCurricularPlan.getSpecialization().equals(
                    Specialization.ESPECIALIZACAO_TYPE)) {
                totalValue = new Double(0);
            }

            // else if
            // (studentCurricularPlan.getSpecialization().equals(Specialization.ESPECIALIZACAO_TYPE))
            // {
            // totalValue =
            // calculateTotalValueForSpecialization(masterDegreeCandidate.getExecutionDegree().getExecutionYear(),
            // gratuityValues,
            // studentCurricularPlan);
            // }

            if (totalValue == null) {
                throw new GratuityValuesNotDefinedServiceException(
                        "error.exception.masterDegree.gratuity.gratuityValuesNotDefined");
            }

            gratuitySituation.setRemainingValue(totalValue);
            gratuitySituation.setTotalValue(totalValue);

            gratuitySituationDAO.simpleLockWrite(gratuitySituation);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            throw newEx;
        }

        InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
        infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
                .newInfoFromDomain(masterDegreeCandidate));
        infoCandidateRegistration.setInfoStudentCurricularPlan(Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlanResult));
        infoCandidateRegistration.setEnrolments(new ArrayList());
        Iterator iterator = studentCurricularPlanResult.getEnrolments().iterator();
        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();
            InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(enrolment);
            infoCandidateRegistration.getEnrolments().add(infoEnrolment);
        }

        return infoCandidateRegistration;
    }

    private void changeUsernameIfNeccessary(IStudent student) throws ExcepcaoPersistencia {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            if ((student.getPerson().getUsername().indexOf("Mes") != -1)
                    || (student.getPerson().getUsername().indexOf("Esp") != -1)
                    || (student.getPerson().getUsername().indexOf("Int") != -1)) {

                sp.getIPessoaPersistente().simpleLockWrite(student.getPerson());
                student.getPerson().setUsername("M" + student.getNumber());
            }
        } catch (ExcepcaoPersistencia e) {
            throw new ExcepcaoPersistencia();
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