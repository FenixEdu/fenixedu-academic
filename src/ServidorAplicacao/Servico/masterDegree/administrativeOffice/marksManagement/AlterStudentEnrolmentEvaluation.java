package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.EnrolmentEvaluation;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Angela 04/07/2003
 *  
 */
public class AlterStudentEnrolmentEvaluation implements IServico {

    private static AlterStudentEnrolmentEvaluation servico = new AlterStudentEnrolmentEvaluation();

    /**
     * The singleton access method of this class.
     */
    public static AlterStudentEnrolmentEvaluation getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private AlterStudentEnrolmentEvaluation() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "AlterStudentEnrolmentEvaluation";
    }

    public List run(Integer curricularCourseCode,
            Integer enrolmentEvaluationCode,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation,
            Integer teacherNumber, IUserView userView)
            throws FenixServiceException {

        List infoEvaluationsWithError = null;
        try {
            Calendar calendario = Calendar.getInstance();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
           
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentEnrolment persistentEnrolment = sp
                    .getIPersistentEnrolment();

            if ((infoEnrolmentEvaluation.getGrade().equals("0"))
                    || (infoEnrolmentEvaluation.getGrade().equals(""))
                    || (infoEnrolmentEvaluation.getGrade().length() < -1)) {
                //				new enrolment evaluation

                IEnrolmentEvaluation enrolmentEvaluationCopy = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
                        .readByOID(EnrolmentEvaluation.class,
                                enrolmentEvaluationCode);
                IEnrollment enrolment = enrolmentEvaluationCopy.getEnrolment();

                persistentEnrolment.simpleLockWrite(enrolment);

                enrolment.setEnrolmentState(EnrolmentState.ENROLLED);
                enrolment.setCurricularCourse(enrolmentEvaluationCopy
                        .getEnrolment().getCurricularCourse());
                enrolment.setEnrolmentEvaluationType(enrolmentEvaluationCopy
                        .getEnrolment().getEnrolmentEvaluationType());
                enrolment.setExecutionPeriod(enrolmentEvaluationCopy
                        .getEnrolment().getExecutionPeriod());
                enrolment.setStudentCurricularPlan(enrolmentEvaluationCopy
                        .getEnrolment().getStudentCurricularPlan());
                enrolment.setIdInternal(enrolmentEvaluationCopy.getEnrolment()
                        .getIdInternal());

                IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                enrolmentEvaluation.setEnrolment(enrolment);
                enrolmentEvaluation
                        .setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
                enrolmentEvaluation
                        .setEnrolmentEvaluationType(enrolmentEvaluationCopy
                                .getEnrolmentEvaluationType());
                enrolmentEvaluation.setCheckSum(null);
                //		employee
                IPessoa person = persistentPerson.lerPessoaPorUsername(userView
                        .getUtilizador());
                IEmployee employee = readEmployee(person);
                enrolmentEvaluation.setEmployee(employee);
                enrolmentEvaluation.setExamDate(null);
                enrolmentEvaluation.setGrade(null);
                enrolmentEvaluation.setGradeAvailableDate(null);
                enrolmentEvaluation.setObservation(infoEnrolmentEvaluation
                        .getObservation());
                enrolmentEvaluation.setPersonResponsibleForGrade(null);
                enrolmentEvaluation.setWhen(calendario.getTime());
                enrolmentEvaluation.setAckOptLock(new Integer(1));
                persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);

            } else {

                //	responsible teacher
                ITeacher teacher = persistentTeacher
                        .readByNumber(teacherNumber);
                if (teacher == null) {
                    throw new NonExistingServiceException();
                }
                //		employee
                IPessoa person = persistentPerson.lerPessoaPorUsername(userView
                        .getUtilizador());
                IEmployee employee = readEmployee(person);

               

                infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);

                infoEvaluationsWithError = new ArrayList();

                if (!isValidEvaluation(infoEnrolmentEvaluation)) {
                    infoEvaluationsWithError.add(infoEnrolmentEvaluation);
                } else {

                    // read enrolmentEvaluation to change
                    IEnrolmentEvaluation iEnrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
                            .readByOID(EnrolmentEvaluation.class,
                                    enrolmentEvaluationCode);

                    // new enrolment evaluation
                    IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                    enrolmentEvaluation = Cloner
                            .copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(infoEnrolmentEvaluation);

                    //check for an alteration
                    if (!enrolmentEvaluation.getGrade().equals(
                            iEnrolmentEvaluation.getGrade())) {
                        IEnrollment enrolment = iEnrolmentEvaluation
                                .getEnrolment();
                        persistentEnrolment.simpleLockWrite(enrolment);
                        try {
                            new Integer(enrolmentEvaluation.getGrade());
                            enrolment.setEnrolmentState(EnrolmentState.APROVED);
                        } catch (NumberFormatException e) {
                            String grade = null;
                            grade = enrolmentEvaluation.getGrade()
                                    .toUpperCase();
                            if (grade.equals("RE"))
                                enrolment
                                        .setEnrolmentState(EnrolmentState.NOT_APROVED);
                            if (grade.equals("NA"))
                                enrolment
                                        .setEnrolmentState(EnrolmentState.NOT_EVALUATED);
                        }

                        iEnrolmentEvaluation.setEnrolment(enrolment);

                    }

                    enrolmentEvaluation.setEnrolment(iEnrolmentEvaluation
                            .getEnrolment());
                    enrolmentEvaluation.setWhen(calendario.getTime());
                    if (infoEnrolmentEvaluation.getExamDate() != null) {
                        enrolmentEvaluation.setExamDate(infoEnrolmentEvaluation
                                .getExamDate());
                    } else {
                        enrolmentEvaluation.setExamDate(calendario.getTime());
                    }
                    if (infoEnrolmentEvaluation.getGradeAvailableDate() != null) {
                        enrolmentEvaluation
                                .setGradeAvailableDate(infoEnrolmentEvaluation
                                        .getGradeAvailableDate());
                    } else {
                        enrolmentEvaluation.setGradeAvailableDate(calendario
                                .getTime());
                    }
                    enrolmentEvaluation.setGrade(infoEnrolmentEvaluation
                            .getGrade());
                    EnrolmentEvaluationState enrolmentEvaluationState = EnrolmentEvaluationState.FINAL_OBJ;
                    enrolmentEvaluation.setPersonResponsibleForGrade(teacher
                            .getPerson());
                    enrolmentEvaluation
                            .setEnrolmentEvaluationType(infoEnrolmentEvaluation
                                    .getEnrolmentEvaluationType());
                    enrolmentEvaluation
                            .setEnrolmentEvaluationState(enrolmentEvaluationState);
                    enrolmentEvaluation.setEmployee(employee);
                    enrolmentEvaluation.setObservation(infoEnrolmentEvaluation
                            .getObservation());
                    enrolmentEvaluation.setCheckSum("");

                    persistentEnrolmentEvaluation
                            .lockWrite(enrolmentEvaluation);

                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoEvaluationsWithError;
    }

    private IEmployee readEmployee(IPessoa person) {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = SuportePersistenteOJB.getInstance()
                    .getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal()
                    .intValue());
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return employee;
    }

    private InfoEnrolmentEvaluation completeEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoEnrolmentEvaluation)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            //			Student
            IStudent student = new Student();
            student = persistentStudent.readStudentByNumberAndDegreeType(
                    infoEnrolmentEvaluation.getInfoEnrolment()
                            .getInfoStudentCurricularPlan().getInfoStudent()
                            .getNumber(), new TipoCurso(TipoCurso.MESTRADO));
            infoEnrolmentEvaluation.getInfoEnrolment()
                    .getInfoStudentCurricularPlan().setInfoStudent(
                            Cloner.copyIStudent2InfoStudent(student));

            return infoEnrolmentEvaluation;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private boolean isValidEvaluation(
            InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente curricularPlanPersistente = sp
                    .getIStudentCurricularPlanPersistente();

            studentCurricularPlan = curricularPlanPersistente
                    .readActiveStudentCurricularPlan(infoEnrolmentEvaluation
                            .getInfoEnrolment().getInfoStudentCurricularPlan()
                            .getInfoStudent().getNumber(),
                            infoEnrolmentEvaluation.getInfoEnrolment()
                                    .getInfoStudentCurricularPlan()
                                    .getInfoStudent().getDegreeType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (studentCurricularPlan == null) {
            return false;
        }

        IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan
                .getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(degreeCurricularPlan);
        if (infoEnrolmentEvaluation.getGrade() == null
                || infoEnrolmentEvaluation.getGrade().length() == 0) {
            return false;
        } 
            return degreeCurricularPlanStrategy
                    .checkMark(infoEnrolmentEvaluation.getGrade().toUpperCase());
        
    }

}