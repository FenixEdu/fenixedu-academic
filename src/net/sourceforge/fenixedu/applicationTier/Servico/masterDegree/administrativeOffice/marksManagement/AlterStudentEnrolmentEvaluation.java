package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrollmentState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Angela 04/07/2003
 *  
 */
public class AlterStudentEnrolmentEvaluation implements IService {

    public List run(Integer curricularCourseCode, Integer enrolmentEvaluationCode,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation, Integer teacherNumber, IUserView userView)
            throws FenixServiceException {

        List infoEvaluationsWithError = null;
        try {
            Calendar calendario = Calendar.getInstance();
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

            if ((infoEnrolmentEvaluation.getGrade().equals("0"))
                    || (infoEnrolmentEvaluation.getGrade().equals(""))
                    || (infoEnrolmentEvaluation.getGrade().length() < -1)) {
                //				new enrolment evaluation

                IEnrolmentEvaluation enrolmentEvaluationCopy = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
                        .readByOID(EnrolmentEvaluation.class, enrolmentEvaluationCode);
                IEnrollment enrolment = enrolmentEvaluationCopy.getEnrolment();

                persistentEnrolment.simpleLockWrite(enrolment);

                enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
                enrolment.setCurricularCourse(enrolmentEvaluationCopy.getEnrolment()
                        .getCurricularCourse());
                enrolment.setEnrolmentEvaluationType(enrolmentEvaluationCopy.getEnrolment()
                        .getEnrolmentEvaluationType());
                enrolment
                        .setExecutionPeriod(enrolmentEvaluationCopy.getEnrolment().getExecutionPeriod());
                enrolment.setStudentCurricularPlan(enrolmentEvaluationCopy.getEnrolment()
                        .getStudentCurricularPlan());
                enrolment.setIdInternal(enrolmentEvaluationCopy.getEnrolment().getIdInternal());

                IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                enrolmentEvaluation.setEnrolment(enrolment);
                enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
                enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationCopy
                        .getEnrolmentEvaluationType());
                enrolmentEvaluation.setCheckSum(null);
                //		employee
                IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
                IEmployee employee = readEmployee(person);
                enrolmentEvaluation.setEmployee(employee);
                enrolmentEvaluation.setExamDate(null);
                enrolmentEvaluation.setGrade(null);
                enrolmentEvaluation.setGradeAvailableDate(null);
                enrolmentEvaluation.setObservation(infoEnrolmentEvaluation.getObservation());
                enrolmentEvaluation.setPersonResponsibleForGrade(null);
                enrolmentEvaluation.setWhen(calendario.getTime());
//                enrolmentEvaluation.setAckOptLock(new Integer(1));
                persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);

            } else {

                //	responsible teacher
                ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
                if (teacher == null) {
                    throw new NonExistingServiceException();
                }
                //		employee
                IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
                IEmployee employee = readEmployee(person);

                infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);

                infoEvaluationsWithError = new ArrayList();

                if (!isValidEvaluation(infoEnrolmentEvaluation)) {
                    infoEvaluationsWithError.add(infoEnrolmentEvaluation);
                } else {

                    // read enrolmentEvaluation to change
                    IEnrolmentEvaluation iEnrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
                            .readByOID(EnrolmentEvaluation.class, enrolmentEvaluationCode);

                    // new enrolment evaluation
                    IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                    enrolmentEvaluation = Cloner
                            .copyInfoEnrolmentEvaluation2IEnrolmentEvaluation(infoEnrolmentEvaluation);

                    //check for an alteration
                    if (!enrolmentEvaluation.getGrade().equals(iEnrolmentEvaluation.getGrade())) {
                        IEnrollment enrolment = iEnrolmentEvaluation.getEnrolment();
                        persistentEnrolment.simpleLockWrite(enrolment);
                        try {
                            new Integer(enrolmentEvaluation.getGrade());
                            enrolment.setEnrollmentState(EnrollmentState.APROVED);
                        } catch (NumberFormatException e) {
                            String grade = null;
                            grade = enrolmentEvaluation.getGrade().toUpperCase();
                            if (grade.equals("RE"))
                                enrolment.setEnrollmentState(EnrollmentState.NOT_APROVED);
                            if (grade.equals("NA"))
                                enrolment.setEnrollmentState(EnrollmentState.NOT_EVALUATED);
                        }

                        iEnrolmentEvaluation.setEnrolment(enrolment);

                    }

                    enrolmentEvaluation.setEnrolment(iEnrolmentEvaluation.getEnrolment());
                    enrolmentEvaluation.setWhen(calendario.getTime());
                    if (infoEnrolmentEvaluation.getExamDate() != null) {
                        enrolmentEvaluation.setExamDate(infoEnrolmentEvaluation.getExamDate());
                    } else {
                        enrolmentEvaluation.setExamDate(calendario.getTime());
                    }
                    if (infoEnrolmentEvaluation.getGradeAvailableDate() != null) {
                        enrolmentEvaluation.setGradeAvailableDate(infoEnrolmentEvaluation
                                .getGradeAvailableDate());
                    } else {
                        enrolmentEvaluation.setGradeAvailableDate(calendario.getTime());
                    }
                    enrolmentEvaluation.setGrade(infoEnrolmentEvaluation.getGrade());
                    EnrolmentEvaluationState enrolmentEvaluationState = EnrolmentEvaluationState.FINAL_OBJ;
                    enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
                    enrolmentEvaluation.setEnrolmentEvaluationType(infoEnrolmentEvaluation
                            .getEnrolmentEvaluationType());
                    enrolmentEvaluation.setEnrolmentEvaluationState(enrolmentEvaluationState);
                    enrolmentEvaluation.setEmployee(employee);
                    enrolmentEvaluation.setObservation(infoEnrolmentEvaluation.getObservation());
                    enrolmentEvaluation.setCheckSum("");

                    persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);

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

    private IEmployee readEmployee(IPerson person) {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return employee;
    }

    private InfoEnrolmentEvaluation completeEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoEnrolmentEvaluation) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            //			Student
            IStudent student = new Student();
            student = persistentStudent.readStudentByNumberAndDegreeType(infoEnrolmentEvaluation
                    .getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getNumber(),
                    new TipoCurso(TipoCurso.MESTRADO));
            infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().setInfoStudent(
                    Cloner.copyIStudent2InfoStudent(student));

            return infoEnrolmentEvaluation;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private boolean isValidEvaluation(InfoEnrolmentEvaluation infoEnrolmentEvaluation) {
        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan curricularPlanPersistente = sp
                    .getIStudentCurricularPlanPersistente();

            studentCurricularPlan = curricularPlanPersistente.readActiveStudentCurricularPlan(
                    infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan()
                            .getInfoStudent().getNumber(), infoEnrolmentEvaluation.getInfoEnrolment()
                            .getInfoStudentCurricularPlan().getInfoStudent().getDegreeType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (studentCurricularPlan == null) {
            return false;
        }

        IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(degreeCurricularPlan);
        if (infoEnrolmentEvaluation.getGrade() == null
                || infoEnrolmentEvaluation.getGrade().length() == 0) {
            return false;
        }
        return degreeCurricularPlanStrategy.checkMark(infoEnrolmentEvaluation.getGrade().toUpperCase());

    }

}