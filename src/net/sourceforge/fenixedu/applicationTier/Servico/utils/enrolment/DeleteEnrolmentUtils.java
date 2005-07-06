/*
 * Created on Jan 12, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.ICreditsInScientificArea;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;

/**
 * @author nmgo
 */
public abstract class DeleteEnrolmentUtils {

    /**
     * @param enrolmentDAO
     * @param enrolmentEvaluationDAO
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    public static void deleteEnrollment(ISuportePersistente persistenceSupport,
            IEnrolment enrolment)
            throws ExcepcaoPersistencia {

        deleteEnrollmentEvaluations(persistenceSupport.getIPersistentEnrolmentEvaluation(), enrolment);

        deleteAttend(persistenceSupport, enrolment);
		
		deleteEquivalences(persistenceSupport , enrolment);
		
		deleteEnrollmentRelations(persistenceSupport,enrolment);
		
		persistenceSupport.getIPersistentEnrolment().deleteByOID(Enrolment.class, enrolment.getIdInternal());
    }
	
	protected static void deleteEquivalences(ISuportePersistente persistentSupport, IEnrolment enrollment) throws ExcepcaoPersistencia {

		IPersistentEnrolmentEquivalence persistentEnrolmentEquivalence = persistentSupport.getIPersistentEnrolmentEquivalence();
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence persistentEquivalentEnrolmentForEnrolmentEquivalence = persistentSupport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();
		
		List<IEnrolmentEquivalence> enrolmentEquivalences = enrollment.getEnrolmentEquivalences();
		for (IEnrolmentEquivalence enrolmentEquivalence : enrolmentEquivalences) {
			persistentEnrolmentEquivalence.simpleLockWrite(enrolmentEquivalence);
            enrolmentEquivalence.setEnrolment(null);
		}
				
		List<IEquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentsForEnrolmentEquivalence = enrollment.getEquivalentEnrolmentForEnrolmentEquivalences();
		for (IEquivalentEnrolmentForEnrolmentEquivalence eq : equivalentEnrolmentsForEnrolmentEquivalence) {
			persistentEquivalentEnrolmentForEnrolmentEquivalence.simpleLockWrite(eq);
			eq.setEquivalentEnrolment(null);
		}
	}
	
	protected static void deleteEnrollmentRelations(ISuportePersistente persistentSupport, IEnrolment enrollment) throws ExcepcaoPersistencia {

		IPersistentCreditsInAnySecundaryArea persistentCreditsInAnySecundaryArea = persistentSupport.getIPersistentCreditsInAnySecundaryArea();
		IPersistentCreditsInSpecificScientificArea persistentCreditsInSpecificScientificArea = persistentSupport.getIPersistentCreditsInSpecificScientificArea();
		
		IStudentCurricularPlan scp = enrollment.getStudentCurricularPlan();
		scp.getEnrolments().remove(enrollment);
		
		ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
		curricularCourse.getEnrolments().remove(enrollment);
		
		IExecutionPeriod executionPeriod = enrollment.getExecutionPeriod();
		executionPeriod.getEnrolments().remove(enrollment);
		
		List<ICreditsInAnySecundaryArea> creditsInAnySecondaryArea = enrollment.getCreditsInAnySecundaryAreas();
		for (ICreditsInAnySecundaryArea credits : creditsInAnySecondaryArea) {
			persistentCreditsInAnySecundaryArea.simpleLockWrite(credits);
			credits.setEnrolment(null);
		}
		
		List<ICreditsInScientificArea> creditsInScientificArea = enrollment.getCreditsInScientificAreas();
		for (ICreditsInScientificArea credits : creditsInScientificArea) {
			persistentCreditsInSpecificScientificArea.simpleLockWrite(credits);
			credits.setEnrolment(null);
		}
	}

    /**
     * @param enrolmentEvaluationDAO
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    protected static void deleteEnrollmentEvaluations(IPersistentEnrolmentEvaluation enrolmentEvaluationDAO,
            IEnrolment enrolment) throws ExcepcaoPersistencia {
        if (enrolment.getEvaluations() != null) {
            Iterator iterator = enrolment.getEvaluations().iterator();
            while (iterator.hasNext()) {
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();

				deleteEnrolmentEvaluationKeepingEnrolmentReference(enrolmentEvaluation, enrolmentEvaluationDAO);
            }
			enrolment.getEvaluations().clear();
        }
    }

	protected static void deleteEnrolmentEvaluationKeepingEnrolmentReference(IEnrolmentEvaluation enrolmentEvaluation,
			IPersistentEnrolmentEvaluation persistentEvaluation) throws ExcepcaoPersistencia{
		
		if (enrolmentEvaluation.getPersonResponsibleForGrade() != null) {
			enrolmentEvaluation.getPersonResponsibleForGrade().getEnrolmentEvaluations().remove(enrolmentEvaluation);
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
		}
		
		if (enrolmentEvaluation.getPersonResponsibleForGrade() != null) {
			enrolmentEvaluation.getEmployee().getEnrolmentEvaluations().remove(enrolmentEvaluation);
			enrolmentEvaluation.setEmployee(null);	
		}
		
		enrolmentEvaluation.setEnrolment(null);

		persistentEvaluation.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
	}

	
	public static void deleteEnrolmentEvaluation(IEnrolmentEvaluation enrolmentEvaluation,
			IPersistentEnrolmentEvaluation persistentEvaluation) throws ExcepcaoPersistencia{
		
		enrolmentEvaluation.getEnrolment().getEvaluations().remove(enrolmentEvaluation);
		
		deleteEnrolmentEvaluationKeepingEnrolmentReference(enrolmentEvaluation, persistentEvaluation);
		
		
	}
	
	
    /**
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    protected static void deleteAttend(ISuportePersistente persistentSuport, IEnrolment enrolment) throws ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = persistentSuport.getIPersistentExecutionCourse();
        IFrequentaPersistente attendDAO = persistentSuport.getIFrequentaPersistente();
        IPersistentMark markDAO = persistentSuport.getIPersistentMark();
        ITurnoAlunoPersistente shiftStudentDAO = persistentSuport.getITurnoAlunoPersistente();

        List executionCourses = executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(enrolment
                .getCurricularCourse().getIdInternal(), enrolment.getExecutionPeriod().getIdInternal());

        Iterator iterator = executionCourses.iterator();
        while (iterator.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterator.next();

            if (executionCourse != null) {
                IAttends attend = attendDAO.readByAlunoAndDisciplinaExecucao(enrolment
                        .getStudentCurricularPlan().getStudent(), executionCourse);

                if (attend != null) {
                    List marks = markDAO.readBy(attend);
                    if (marks == null || marks.isEmpty()) {
                        final List<IStudentGroupAttend> studentGroupAttends = attend.getStudentGroupAttends();
                        IStudentGroupAttend studentGroupAttend = (studentGroupAttends.isEmpty()) ? null : studentGroupAttends.get(0);
                        if (studentGroupAttend == null) {
                            List shiftsStudentIsIn = shiftStudentDAO.readByStudent(enrolment
                                    .getStudentCurricularPlan().getStudent().getIdInternal());

                            if (shiftsStudentIsIn == null || shiftsStudentIsIn.isEmpty()) {

                                attendDAO.deleteByOID(Attends.class, attend.getIdInternal());
                            } else {
                                attendDAO.simpleLockWrite(attend);
                                attend.setEnrolment(null);
                            }
                        } else {
                            attendDAO.simpleLockWrite(attend);
                            attend.setEnrolment(null);
                        }
                    } else {
                        attendDAO.simpleLockWrite(attend);
                        attend.setEnrolment(null);
                    }
                }
            }
        }
    }
}
