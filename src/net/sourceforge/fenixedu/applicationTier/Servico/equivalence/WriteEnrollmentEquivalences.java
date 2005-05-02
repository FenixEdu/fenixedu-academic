package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoCurricularCourseGrade;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEnrollmentGrade;
import net.sourceforge.fenixedu.dataTransferObject.equivalence.InfoEquivalenceContext;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in May 12, 2004
 */

public class WriteEnrollmentEquivalences extends EnrollmentEquivalenceServiceUtils implements IService {
    public WriteEnrollmentEquivalences() {
    }

    public void run(Integer studentNumber, TipoCurso degreeType,
            InfoEquivalenceContext infoEquivalenceContext, Integer toStudentCurricularPlanID,
            IUserView userView) throws FenixServiceException {
        List args = new ArrayList();
        args.add(0, infoEquivalenceContext);
        args.add(1, toStudentCurricularPlanID);
        args.add(2, userView);

        List result1 = (List) convertDataInput(args);
        execute(result1);
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service DataBeans input objects to their
     *      respective Domain objects. These Domain objects are to be used by
     *      the service's logic.
     */
    protected Object convertDataInput(Object object) {
        return object;
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service output Domain objects to their
     *      respective DataBeans. These DataBeans are the result of executing
     *      this service logic and are to be passed on to the uper layer of the
     *      architecture.
     */
    protected Object convertDataOutput(Object object) {
        return object;
    }

    /**
     * @param List
     * @throws FenixServiceException
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method implements the buisiness logic of this service.
     */
    protected Object execute(Object object) throws FenixServiceException {
        List input = (List) object;

        HashMap enrolmentEquivalencesCreated = new HashMap();

        InfoEquivalenceContext infoEquivalenceContext = (InfoEquivalenceContext) input.get(0);
        Integer toStudentCurricularPlanID = (Integer) input.get(1);
        IUserView userView = (IUserView) input.get(2);

        try {
            ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse curricularCourseDAO = persistenceDAO
                    .getIPersistentCurricularCourse();
            IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistenceDAO
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan toStudentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                    .readByOID(StudentCurricularPlan.class, toStudentCurricularPlanID);

            for (int i = 0; i < infoEquivalenceContext
                    .getChosenInfoCurricularCourseGradesToGetEquivalence().size(); i++) {
                InfoCurricularCourseGrade infoCurricularCourseGrade = (InfoCurricularCourseGrade) infoEquivalenceContext
                        .getChosenInfoCurricularCourseGradesToGetEquivalence().get(i);

                ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourseDAO.readByOID(
                        CurricularCourse.class, infoCurricularCourseGrade.getInfoCurricularCourse()
                                .getIdInternal());

                IEnrolment newEnrollment = writeEnrollment(curricularCourse, toStudentCurricularPlan,
                        infoCurricularCourseGrade.getGrade(), userView);

                for (int j = 0; j < infoEquivalenceContext
                        .getChosenInfoEnrollmentGradesToGiveEquivalence().size(); j++) {
                    InfoEnrolment infoEnrollment = ((InfoEnrollmentGrade) infoEquivalenceContext
                            .getChosenInfoEnrollmentGradesToGiveEquivalence().get(j))
                            .getInfoEnrollment();

                    IEnrolment oldEnrollment = (IEnrolment) enrollmentDAO.readByOID(Enrolment.class,
                            infoEnrollment.getIdInternal());

                    searchForEnrollmentInExtraCurricularCourseAndDeleteIt(oldEnrollment,
                            toStudentCurricularPlan);

                    writeEquivalences(oldEnrollment, newEnrollment, enrolmentEquivalencesCreated);
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return null;
    }

    /**
     * @param userView
     * @return IPerson
     */
    private IPerson getPersonResponsibleFor(IUserView userView) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente personDAO = persistenceDAO.getIPessoaPersistente();

        return personDAO.lerPessoaPorUsername(userView.getUtilizador());
    }

    /**
     * @param person
     * @return IEmployee
     */
    private IEmployee getEmployee(IPerson person) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEmployee employeeDAO = persistenceDAO.getIPersistentEmployee();

        return employeeDAO.readByPerson(person.getIdInternal().intValue());
    }

    private IEnrolment writeEnrollment(ICurricularCourse curricularCourse,
            IStudentCurricularPlan toStudentCurricularPlan, String grade, IUserView userView)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
        IPersistentExecutionPeriod executionPeriodDAO = persistenceDAO.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        IEnrolment enrollmentToWrite = enrollmentDAO
                .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                        toStudentCurricularPlan, curricularCourse, executionPeriod);

        if (enrollmentToWrite == null) {
            enrollmentToWrite = new Enrolment();

            enrollmentDAO.simpleLockWrite(enrollmentToWrite);

            enrollmentToWrite.setCurricularCourse(curricularCourse);
            enrollmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE);
            enrollmentToWrite.setEnrollmentState(getEnrollmentStateByGrade(grade));
            enrollmentToWrite.setExecutionPeriod(executionPeriod);
            enrollmentToWrite.setStudentCurricularPlan(toStudentCurricularPlan);
            enrollmentToWrite.setCreationDate(new Date());
            enrollmentToWrite.setCondition(EnrollmentCondition.FINAL);
        }

        writeEnrollmentEvaluation(enrollmentToWrite, grade, userView);

        return enrollmentToWrite;
    }

    private void writeEnrollmentEvaluation(IEnrolment newEnrollment, String grade, IUserView userView)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = persistenceDAO
                .getIPersistentEnrolmentEvaluation();

        IPerson person = getPersonResponsibleFor(userView);

        IEmployee employee = getEmployee(person);

        IEnrolmentEvaluation enrollmentEvaluationToWrite = enrollmentEvaluationDAO
                .readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
                        newEnrollment, EnrolmentEvaluationType.EQUIVALENCE, grade, newEnrollment
                                .getCreationDate());

        if (enrollmentEvaluationToWrite == null) {
            enrollmentEvaluationToWrite = new EnrolmentEvaluation();

            enrollmentEvaluationDAO.simpleLockWrite(enrollmentEvaluationToWrite);

            enrollmentEvaluationToWrite.setEnrolment(newEnrollment);
            enrollmentEvaluationToWrite.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
            enrollmentEvaluationToWrite
                    .setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE);
            enrollmentEvaluationToWrite.setExamDate(newEnrollment.getCreationDate());
            enrollmentEvaluationToWrite.setGrade(grade);
            enrollmentEvaluationToWrite.setObservation("EQUIVALÊNCIA");
            enrollmentEvaluationToWrite.setPersonResponsibleForGrade(person);
            enrollmentEvaluationToWrite.setGradeAvailableDate(newEnrollment.getCreationDate());
            enrollmentEvaluationToWrite.setWhen(newEnrollment.getCreationDate());
            enrollmentEvaluationToWrite.setEmployee(employee);
            enrollmentEvaluationToWrite.setAckOptLock(new Integer(1));
            enrollmentEvaluationToWrite.setCheckSum(null);
        }
    }

    private void writeEquivalences(IEnrolment oldEnrollment, IEnrolment newEnrollment,
            HashMap enrolmentEquivalencesCreated) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrolmentEquivalence enrollmentEquivalenceDAO = persistenceDAO
                .getIPersistentEnrolmentEquivalence();
        IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrollmentForEnrollmentEquivalenceDAO = persistenceDAO
                .getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

        IEnrolmentEquivalence enrollmentEquivalence = enrollmentEquivalenceDAO
                .readByEnrolment(newEnrollment);
        if (enrollmentEquivalence == null) {
            enrollmentEquivalence = (IEnrolmentEquivalence) enrolmentEquivalencesCreated
                    .get(newEnrollment.getCurricularCourse().getIdInternal());

            if (enrollmentEquivalence == null) {
                enrollmentEquivalence = new EnrolmentEquivalence();
                enrollmentEquivalenceDAO.simpleLockWrite(enrollmentEquivalence);
                enrollmentEquivalence.setEnrolment(newEnrollment);

                enrolmentEquivalencesCreated.put(newEnrollment.getCurricularCourse().getIdInternal(),
                        enrollmentEquivalence);
            }
        }

        IEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrollmentForEnrollmentEquivalence = equivalentEnrollmentForEnrollmentEquivalenceDAO
                .readByEnrolmentEquivalenceAndEquivalentEnrolment(enrollmentEquivalence, oldEnrollment);

        if (equivalentEnrollmentForEnrollmentEquivalence == null) {
            equivalentEnrollmentForEnrollmentEquivalence = new EquivalentEnrolmentForEnrolmentEquivalence();
            equivalentEnrollmentForEnrollmentEquivalenceDAO
                    .simpleLockWrite(equivalentEnrollmentForEnrollmentEquivalence);
            equivalentEnrollmentForEnrollmentEquivalence.setEnrolmentEquivalence(enrollmentEquivalence);
            equivalentEnrollmentForEnrollmentEquivalence.setEquivalentEnrolment(oldEnrollment);
        }
    }

    private EnrollmentState getEnrollmentStateByGrade(String grade) {
        if (grade == null) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("NA")) {
            return EnrollmentState.NOT_EVALUATED;
        }

        if (grade.equals("RE")) {
            return EnrollmentState.NOT_APROVED;
        }

        if (grade.equals("AP")) {
            return EnrollmentState.APROVED;
        }

        int intGrade;

        intGrade = new Integer(grade).intValue();

        if (intGrade < 10) {
            return EnrollmentState.NOT_APROVED;
        }

        return EnrollmentState.APROVED;
    }

    /**
     * @param oldEnrollment
     * @param toStudentCurricularPlan
     */
    private void searchForEnrollmentInExtraCurricularCourseAndDeleteIt(IEnrolment oldEnrollment,
            IStudentCurricularPlan toStudentCurricularPlan) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();

        IEnrolment enrollment = enrollmentDAO
                .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                        toStudentCurricularPlan, oldEnrollment.getCurricularCourse(), oldEnrollment
                                .getExecutionPeriod());

        if ((enrollment != null) && (enrollment instanceof EnrolmentInExtraCurricularCourse)) {
            enrollmentDAO.deleteByOID(Enrolment.class, enrollment.getIdInternal());
        }
    }

}