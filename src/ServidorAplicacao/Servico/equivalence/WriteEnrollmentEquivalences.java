package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.equivalence.InfoCurricularCourseGrade;
import DataBeans.equivalence.InfoEnrollmentGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EnrolmentEvaluation;
import Dominio.EnrolmentInExtraCurricularCourse;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.ICurricularCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IExecutionPeriod;
import Dominio.IPerson;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;
import Util.enrollment.EnrollmentCondition;

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
            ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
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

                IEnrollment newEnrollment = writeEnrollment(curricularCourse, toStudentCurricularPlan,
                        infoCurricularCourseGrade.getGrade(), userView);

                for (int j = 0; j < infoEquivalenceContext
                        .getChosenInfoEnrollmentGradesToGiveEquivalence().size(); j++) {
                    InfoEnrolment infoEnrollment = ((InfoEnrollmentGrade) infoEquivalenceContext
                            .getChosenInfoEnrollmentGradesToGiveEquivalence().get(j))
                            .getInfoEnrollment();

                    IEnrollment oldEnrollment = (IEnrollment) enrollmentDAO.readByOID(Enrolment.class,
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
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        IPessoaPersistente personDAO = persistenceDAO.getIPessoaPersistente();

        return personDAO.lerPessoaPorUsername(userView.getUtilizador());
    }

    /**
     * @param person
     * @return IEmployee
     */
    private IEmployee getEmployee(IPerson person) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        IPersistentEmployee employeeDAO = persistenceDAO.getIPersistentEmployee();

        return employeeDAO.readByPerson(person.getIdInternal().intValue());
    }

    private IEnrollment writeEnrollment(ICurricularCourse curricularCourse,
            IStudentCurricularPlan toStudentCurricularPlan, String grade, IUserView userView)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();
        IPersistentExecutionPeriod executionPeriodDAO = persistenceDAO.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        IEnrollment enrollmentToWrite = enrollmentDAO
                .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                        toStudentCurricularPlan, curricularCourse, executionPeriod);

        if (enrollmentToWrite == null) {
            enrollmentToWrite = new Enrolment();

            enrollmentDAO.simpleLockWrite(enrollmentToWrite);

            enrollmentToWrite.setCurricularCourse(curricularCourse);
            enrollmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
            enrollmentToWrite.setEnrollmentState(getEnrollmentStateByGrade(grade));
            enrollmentToWrite.setExecutionPeriod(executionPeriod);
            enrollmentToWrite.setStudentCurricularPlan(toStudentCurricularPlan);
            enrollmentToWrite.setCreationDate(new Date());
            enrollmentToWrite.setCondition(EnrollmentCondition.FINAL);
        }

        writeEnrollmentEvaluation(enrollmentToWrite, grade, userView);

        return enrollmentToWrite;
    }

    private void writeEnrollmentEvaluation(IEnrollment newEnrollment, String grade, IUserView userView)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = persistenceDAO
                .getIPersistentEnrolmentEvaluation();

        IPerson person = getPersonResponsibleFor(userView);

        IEmployee employee = getEmployee(person);

        IEnrolmentEvaluation enrollmentEvaluationToWrite = enrollmentEvaluationDAO
                .readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
                        newEnrollment, EnrolmentEvaluationType.EQUIVALENCE_OBJ, grade, newEnrollment
                                .getCreationDate());

        if (enrollmentEvaluationToWrite == null) {
            enrollmentEvaluationToWrite = new EnrolmentEvaluation();

            enrollmentEvaluationDAO.simpleLockWrite(enrollmentEvaluationToWrite);

            enrollmentEvaluationToWrite.setEnrolment(newEnrollment);
            enrollmentEvaluationToWrite.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
            enrollmentEvaluationToWrite
                    .setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
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

    private void writeEquivalences(IEnrollment oldEnrollment, IEnrollment newEnrollment,
            HashMap enrolmentEquivalencesCreated) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
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
    private void searchForEnrollmentInExtraCurricularCourseAndDeleteIt(IEnrollment oldEnrollment,
            IStudentCurricularPlan toStudentCurricularPlan) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        IPersistentEnrollment enrollmentDAO = persistenceDAO.getIPersistentEnrolment();

        IEnrollment enrollment = enrollmentDAO
                .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
                        toStudentCurricularPlan, oldEnrollment.getCurricularCourse(), oldEnrollment
                                .getExecutionPeriod());

        if ((enrollment != null) && (enrollment instanceof EnrolmentInExtraCurricularCourse)) {
            enrollmentDAO.deleteByOID(Enrolment.class, enrollment.getIdInternal());
        }
    }

}