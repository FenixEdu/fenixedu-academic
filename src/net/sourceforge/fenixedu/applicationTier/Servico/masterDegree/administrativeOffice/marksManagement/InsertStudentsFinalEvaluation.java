package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class InsertStudentsFinalEvaluation implements IService {

    public InsertStudentsFinalEvaluation() {

    }

    public List run(List evaluations, Integer teacherNumber, Date evaluationDate, IUserView userView)
            throws FenixServiceException {

        List infoEvaluationsWithError = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            //			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            //			employee
            //			IPerson person =
            // persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            //			Funcionario employee = readEmployee(person);

            infoEvaluationsWithError = new ArrayList();
            ListIterator iterEnrolmentEvaluations = evaluations.listIterator();
            while (iterEnrolmentEvaluations.hasNext()) {
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) iterEnrolmentEvaluations
                        .next();

                infoEnrolmentEvaluation = completeEnrolmentEvaluation(infoEnrolmentEvaluation);
                IEnrolmentEvaluation enrolmentEvaluationFromDb = getEnrolmentEvaluation(
                        persistentEnrolmentEvaluation, infoEnrolmentEvaluation);
                if (infoEnrolmentEvaluation.getGrade() == null
                        || infoEnrolmentEvaluation.getGrade().length() == 0) {
                    if (enrolmentEvaluationFromDb.getGrade() != null
                            && enrolmentEvaluationFromDb.getGrade().length() > 0) {
                        // if there was a grade and now there is not we have to
                        // delete written information
                        cleanEnrolmentEvaluation(persistentEnrolmentEvaluation,
                                enrolmentEvaluationFromDb);
                    }
                } else if (infoEnrolmentEvaluation.getGrade() != null
                        && infoEnrolmentEvaluation.getGrade().length() > 0) {
                    if (!isValidEvaluation(infoEnrolmentEvaluation)) {
                        infoEvaluationsWithError.add(infoEnrolmentEvaluation);
                    } else {
                        fillEnrolmentEvaluation(evaluationDate, persistentEnrolmentEvaluation,
                                persistentTeacher, teacherNumber, infoEnrolmentEvaluation,
                                enrolmentEvaluationFromDb);
                    }
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

    private void fillEnrolmentEvaluation(Date evaluationDate,
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
            IPersistentTeacher persistentTeacher, Integer teacherNumber,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation,
            IEnrolmentEvaluation enrolmentEvaluationFromDb) throws ExcepcaoPersistencia,
            ExistingPersistentException, NonExistingServiceException {

        //			responsible teacher
        ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException();
        }
        Calendar calendario = Calendar.getInstance();

        persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationFromDb);

        if (evaluationDate != null) {
            enrolmentEvaluationFromDb.setExamDate(evaluationDate);
        } else {
            enrolmentEvaluationFromDb.setExamDate(calendario.getTime());
        }
        enrolmentEvaluationFromDb.setGrade(infoEnrolmentEvaluation.getGrade());
        enrolmentEvaluationFromDb.setPersonResponsibleForGrade(teacher.getPerson());
        enrolmentEvaluationFromDb.setGradeAvailableDate(calendario.getTime());
        //					enrolmentEvaluation.setEmployee(employee);
        //					enrolmentEvaluation.setWhen(calendario.getTime());
        //					enrolmentEvaluation.setObservation("Lançamento de Notas na
        // Secretaria");
        enrolmentEvaluationFromDb.setCheckSum("");
    }

    private void cleanEnrolmentEvaluation(IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
            IEnrolmentEvaluation enrolmentEvaluationFromDb) throws ExcepcaoPersistencia,
            ExistingPersistentException {
        persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationFromDb);
        enrolmentEvaluationFromDb.setCheckSum(null);
        enrolmentEvaluationFromDb.setExamDate(null);
        enrolmentEvaluationFromDb.setGrade(null);
        enrolmentEvaluationFromDb.setGradeAvailableDate(null);
        enrolmentEvaluationFromDb.setPersonResponsibleForGrade(null);
    }

    private IEnrolmentEvaluation getEnrolmentEvaluation(
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
            InfoEnrolmentEvaluation infoEnrolmentEvaluation) throws ExcepcaoPersistencia,
            FenixServiceException {

        IEnrollment enrolmentToSearch = new Enrolment();
        enrolmentToSearch.setIdInternal(infoEnrolmentEvaluation.getInfoEnrolment().getIdInternal());
        List enrolmentEvaluationsForEnrolment = persistentEnrolmentEvaluation
                .readEnrolmentEvaluationByEnrolment(enrolmentToSearch);
        if (enrolmentEvaluationsForEnrolment == null || enrolmentEvaluationsForEnrolment.size() == 0) {
            //		it will never happen!!
            throw new FenixServiceException();
        }
        Collections.sort(enrolmentEvaluationsForEnrolment, new BeanComparator("enrolment.idInternal"));

        //		we want last enrolment evaluation in case of improvement
        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluationsForEnrolment
                .get(enrolmentEvaluationsForEnrolment.size() - 1);
        return enrolmentEvaluation;
    }

    //	private Employee readEmployee(IPerson person) {
    //		Employee employee = null;
    //		IPersistentEmployee persistentEmployee;
    //		try {
    //			persistentEmployee =
    // PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEmployee();
    //			employee =
    // persistentEmployee.readByPerson(person.getIdInternal().intValue());
    //		} catch (ExcepcaoPersistencia e) {
    //			e.printStackTrace();
    //		}
    //		return employee;
    //	}

    private InfoEnrolmentEvaluation completeEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoEnrolmentEvaluation) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            //			Student
            IStudent student;
            student = (IStudent) persistentStudent.readByOID(Student.class, infoEnrolmentEvaluation
                    .getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getIdInternal());

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
        //		if (infoEnrolmentEvaluation.getGrade() == null ||
        // infoEnrolmentEvaluation.getGrade().length() == 0) {
        //			return false;
        //		} else {
        return degreeCurricularPlanStrategy.checkMark(infoEnrolmentEvaluation.getGrade());
        //		}
    }
}