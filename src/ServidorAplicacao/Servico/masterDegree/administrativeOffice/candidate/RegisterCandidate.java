package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCandidateRegistration;
import DataBeans.util.Cloner;
import Dominio.Branch;
import Dominio.CandidateSituation;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Gratuity;
import Dominio.IBranch;
import Dominio.ICandidateEnrolment;
import Dominio.ICandidateSituation;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IGratuity;
import Dominio.IMasterDegreeCandidate;
import Dominio.IQualification;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.MasterDegreeCandidate;
import Dominio.Qualification;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.ActiveStudentCurricularPlanAlreadyExistsServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidStudentNumberServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EntryPhase;
import Util.GratuityState;
import Util.RoleType;
import Util.SituationName;
import Util.State;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.enrollment.EnrollmentCondition;

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

    public InfoCandidateRegistration run(Integer candidateID, Integer branchID,
            Integer studentNumber) throws FenixServiceException {

        ISuportePersistente sp = null;

        IStudentCurricularPlan studentCurricularPlanResult = null;
        IMasterDegreeCandidate masterDegreeCandidate = null;
        IStudent student = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            if (studentNumber != null) {
                student = sp.getIPersistentStudent()
                        .readStudentByNumberAndDegreeType(studentNumber,
                                TipoCurso.MESTRADO_OBJ);

                if (student != null) {
                    throw new ExistingServiceException();
                }
            }

            masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(
                            MasterDegreeCandidate.class, candidateID);

            if (!validSituation(masterDegreeCandidate
                    .getActiveCandidateSituation())) {
                throw new InvalidChangeServiceException();
            }

            // Check if a Master Degree Student Already Exists
            if (student == null) {
            	student = sp.getIPersistentStudent()
                    .readByPersonAndDegreeType(
                            masterDegreeCandidate.getPerson(),
                            TipoCurso.MESTRADO_OBJ);
            }

            IRole role = (IRole) CollectionUtils.find(masterDegreeCandidate.getPerson().getPersonRoles(), new Predicate() {
				public boolean evaluate(Object arg0) {
					IRole role = (IRole) arg0;
					return role.getRoleType().getValue() == RoleType.MASTER_DEGREE_CANDIDATE.getValue();
				}
            	});
            if (role != null) {
            	sp.getIPessoaPersistente().simpleLockWrite(masterDegreeCandidate.getPerson());
            	personIsLocked = true;
            	masterDegreeCandidate.getPerson().getPersonRoles().remove(role);
            }
            Integer newStudentNumber = null;
            newStudentNumber = sp.getIPersistentStudent()
                    .generateStudentNumber(TipoCurso.MESTRADO_OBJ);

            if (studentNumber != null
                    && studentNumber.intValue() > newStudentNumber.intValue())
                throw new InvalidStudentNumberServiceException();

            if (student == null) {
                student = new Student();
                sp.getIPersistentStudent().simpleLockWrite(student);
                student.setPayedTuition(new Boolean(false));
                student.setEnrollmentForbidden(new Boolean(false));
                student.setEntryPhase(EntryPhase.FIRST_PHASE_OBJ);
                student.setDegreeType(TipoCurso.MESTRADO_OBJ);
                student.setPerson(masterDegreeCandidate.getPerson());
                student.setState(new StudentState(StudentState.INSCRITO));

                if (studentNumber == null) {
                    student.setNumber(newStudentNumber);
                } else {
                    student.setNumber(studentNumber);
                }

                IStudentKind studentKind = sp.getIPersistentStudentKind()
                        .readByStudentType(new StudentType(StudentType.NORMAL));
                student.setStudentKind(studentKind);

                List roles = new ArrayList();
                Iterator iterator = masterDegreeCandidate.getPerson()
                        .getPersonRoles().iterator();
                while (iterator.hasNext()) {
                    roles.add(Cloner
                            .copyIRole2InfoRole((IRole) iterator.next()));
                }

                // Give The Student Role if Necessary
                if (!AuthorizationUtils.containsRole(roles, RoleType.STUDENT)) {
                	role = sp.getIPersistentRole().readByRoleType(
                            RoleType.STUDENT);
                	if (!personIsLocked) {
                		sp.getIPessoaPersistente().simpleLockWrite(masterDegreeCandidate.getPerson());
                		masterDegreeCandidate.getPerson().getPersonRoles().add(role);
                	}
                }
            }

            IStudentCurricularPlan studentCurricularPlanOld = sp
                    .getIStudentCurricularPlanPersistente()
                    .readActiveStudentCurricularPlan(student.getNumber(),
                            TipoCurso.MESTRADO_OBJ);

            if ((studentCurricularPlanOld != null)
                    && (studentCurricularPlanOld.getCurrentState()
                            .equals(StudentCurricularPlanState.ACTIVE_OBJ))) {
                throw new ActiveStudentCurricularPlanAlreadyExistsServiceException();
            }

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            sp.getIStudentCurricularPlanPersistente().simpleLockWrite(
                    studentCurricularPlan);

            IBranch branch = (IBranch) sp.getIPersistentBranch().readByOID(
                    Branch.class, branchID);

            studentCurricularPlan.setBranch(branch);
            studentCurricularPlan.setEnrolments(new ArrayList());
            studentCurricularPlan
                    .setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
            studentCurricularPlan.setDegreeCurricularPlan(masterDegreeCandidate
                    .getExecutionDegree().getCurricularPlan());
            studentCurricularPlan.setGivenCredits(masterDegreeCandidate
                    .getGivenCredits());
            studentCurricularPlan.setSpecialization(masterDegreeCandidate
                    .getSpecialization());
            studentCurricularPlan
                    .setStartDate(Calendar.getInstance().getTime());
            studentCurricularPlan.setStudent(student);

            // Get the Candidate Enrolments

            List candidateEnrolments = sp.getIPersistentCandidateEnrolment()
                    .readByMDCandidate(masterDegreeCandidate);

            Iterator iterator = candidateEnrolments.iterator();
            while (iterator.hasNext()) {
                ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) iterator
                        .next();

                IEnrollment enrolment = new Enrolment();
                sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
                enrolment.setCurricularCourse(candidateEnrolment
                        .getCurricularCourse());
                enrolment
                        .setEnrolmentEvaluationType(new EnrolmentEvaluationType(
                                EnrolmentEvaluationType.NORMAL));
                enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
                enrolment.setExecutionPeriod(sp.getIPersistentExecutionPeriod()
                        .readActualExecutionPeriod());
                enrolment.setStudentCurricularPlan(studentCurricularPlan);
                enrolment.setEvaluations(new ArrayList());
                enrolment.setCondition(EnrollmentCondition.FINAL);

                IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(
                        enrolmentEvaluation);
                enrolmentEvaluation.setEnrolment(enrolment);
                enrolmentEvaluation
                        .setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
                enrolmentEvaluation
                        .setEnrolmentEvaluationType(new EnrolmentEvaluationType(
                                EnrolmentEvaluationType.NORMAL));

                enrolment.getEvaluations().add(enrolmentEvaluation);
            }

            // Change the Candidate Situation

            ICandidateSituation oldCandidateSituation = (ICandidateSituation) sp
                    .getIPersistentCandidateSituation().readByOID(
                            CandidateSituation.class,
                            masterDegreeCandidate.getActiveCandidateSituation()
                                    .getIdInternal(), true);
            oldCandidateSituation.setValidation(new State(State.INACTIVE));

            ICandidateSituation candidateSituation = new CandidateSituation();
            sp.getIPersistentCandidateSituation().simpleLockWrite(
                    candidateSituation);
            candidateSituation.setDate(Calendar.getInstance().getTime());
            candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
            candidateSituation.setValidation(new State(State.ACTIVE));
            candidateSituation.setSituation(SituationName.ENROLLED_OBJ);

            // Inicial Gratuity State
            IGratuity gratuity = new Gratuity();
            sp.getIPersistentGratuity().simpleLockWrite(gratuity);
            gratuity.setDate(Calendar.getInstance().getTime());
            gratuity.setGratuityState(GratuityState.NOT_PAYED);
            gratuity.setState(new State(State.ACTIVE));
            gratuity.setStudentCurricularPlan(studentCurricularPlan);

            // Copy Qualifications

            IQualification qualification = new Qualification();
            sp.getIPersistentQualification().simpleLockWrite(qualification);
            if (masterDegreeCandidate.getAverage() != null) {
                qualification.setMark(masterDegreeCandidate.getAverage()
                        .toString());
            }
            qualification.setPerson(masterDegreeCandidate.getPerson());
            if (masterDegreeCandidate.getMajorDegreeSchool() == null) {
                qualification.setSchool("");
            } else {
                qualification.setSchool(masterDegreeCandidate
                        .getMajorDegreeSchool());
            }
            qualification.setTitle(masterDegreeCandidate.getMajorDegree());

            Calendar calendar = Calendar.getInstance();
            if (masterDegreeCandidate.getMajorDegreeYear() == null) {
                qualification.setDate(calendar.getTime());
            } else {
                calendar.set(Calendar.YEAR, masterDegreeCandidate
                        .getMajorDegreeYear().intValue());
                qualification.setDate(calendar.getTime());
            }
            qualification.setDegree(masterDegreeCandidate.getMajorDegree());

            // Change the Username If neccessary
            changeUsernameIfNeccessary(studentCurricularPlan.getStudent());

            studentCurricularPlanResult = studentCurricularPlan;
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            throw newEx;
        }

        InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
        infoCandidateRegistration
                .setInfoMasterDegreeCandidate(Cloner
                        .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate));
        infoCandidateRegistration
                .setInfoStudentCurricularPlan(Cloner
                        .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlanResult));
        infoCandidateRegistration.setEnrolments(new ArrayList());
        Iterator iterator = studentCurricularPlanResult.getEnrolments()
                .iterator();
        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();
            infoCandidateRegistration.getEnrolments().add(
                    Cloner.copyIEnrolment2InfoEnrolment(enrolment));
        }

        return infoCandidateRegistration;
    }

    private void changeUsernameIfNeccessary(IStudent student)
            throws ExcepcaoPersistencia {
        try {
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

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
                || situation.getSituation().equals(
                        SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ)
                || situation.getSituation().equals(
                        SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ)
                || situation.getSituation().equals(
                        SituationName.ADMITED_CONDICIONAL_OTHER_OBJ)
                || situation.getSituation().equals(
                        SituationName.ADMITED_SPECIALIZATION_OBJ)) {
            result = true;
        }
        return result;
    }
}