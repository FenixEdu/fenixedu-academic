package ServidorAplicacao.strategy.enrolment.degree;

import java.util.Iterator;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentState;
/**
 * @author dcs-rjao
 *
 * 24/Abr/2003
 */
public abstract class EnrolmentTemporarilyEnrol {

	public static void apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		IPersistentEnrolment persistentEnrolment = null;
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
		IPersistentCurricularCourse persistentCurricularCourse = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			persistentEnrolment = sp.getIPersistentEnrolment();
			persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();
			persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(
					enrolmentContext.getStudentActiveCurricularPlan());
			IExecutionPeriod executionPeriod =
				(IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());
				
			Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();

				ICurricularCourse curricularCourse =
					(ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseScope.getCurricularCourse());

				IEnrolment enrolment =
					new Enrolment(studentCurricularPlan, curricularCourse, new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED), executionPeriod);

				persistentEnrolment.lockWrite(enrolment);
			}
		} catch (ExistingPersistentException e1) {
			e1.printStackTrace();
			throw new ExcepcaoPersistencia();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
		}
	}
}
