package ServidorAplicacao.Servico.enrolment.degree;

import java.util.Iterator;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
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
 * 9/Abr/2003
 */

public class ConfirmActualEnrolment implements IServico {

	private static ConfirmActualEnrolment _servico = new ConfirmActualEnrolment();
	/**
	 * The singleton access method of this class.
	 **/
	public static ConfirmActualEnrolment getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ConfirmActualEnrolment() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ConfirmActualEnrolment";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) {

		EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

		if (enrolmentContext.getEnrolmentValidationResult().isSucess()) {
			try {
				writeTemporaryEnrolment(enrolmentContext);
				//FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
				enrolmentContext.getEnrolmentValidationResult().setSucessMessage("Inscrição realizada com sucesso");
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				//FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
				enrolmentContext.getEnrolmentValidationResult().setSucessMessage("Erro no acesso à base de dados");
			}
		}
		return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
	}

	private void writeTemporaryEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
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

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(enrolmentContext.getStudentActiveCurricularPlan());
			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());
				
			Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseScope.getCurricularCourse());
				IEnrolment enrolment = new Enrolment(studentCurricularPlan, curricularCourse, new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED), executionPeriod);
				persistentEnrolment.lockWrite(enrolment);
			}

			Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
			while (iterator2.hasNext()) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourse());
				ICurricularCourse curricularCourseForOption = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourseForOption());
				enrolmentInOptionalCurricularCourse.setCurricularCourse(curricularCourse);
				enrolmentInOptionalCurricularCourse.setCurricularCourseForOption(curricularCourseForOption);
				enrolmentInOptionalCurricularCourse.setExecutionPeriod(executionPeriod);
				enrolmentInOptionalCurricularCourse.setStudentCurricularPlan(studentCurricularPlan);
				persistentEnrolment.lockWrite(enrolmentInOptionalCurricularCourse);
			}
			
		} catch (ExistingPersistentException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw e;
		}
	}
}