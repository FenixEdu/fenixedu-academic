package ServidorAplicacao.Servico.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.UniversityCode;

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
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {

		EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

		if (enrolmentContext.getEnrolmentValidationResult().isSucess()) {
			try {
				writeTemporaryEnrolment(enrolmentContext);
				enrolmentContext.getEnrolmentValidationResult().setSucessMessage(EnrolmentValidationResult.SUCCESS_ENROLMENT);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				throw new FenixServiceException(e);
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

			// lista de todos os enrolments temporarios				
			final List temporarilyEnrolmentsRead = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					enrolmentContext.getStudentActiveCurricularPlan(),
					EnrolmentState.TEMPORARILY_ENROLED_OBJ);
			
			// enrolments in (anual) curricular courses that the student is doing.
			final List doingEnrolmentsRead = 
				persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.ENROLED_OBJ);
			List doingCurricularCoursesRead = (List) CollectionUtils.collect(doingEnrolmentsRead, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return (enrolment.getCurricularCourse());
				}
			});

			
			// lista de todos os enrolments a escrever
			List temporarilyEnrolmentsToWrite = new ArrayList();
			Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
			IEnrolment enrolmentToWrite = null;
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseScope.getCurricularCourse());
				if(!doingCurricularCoursesRead.contains(curricularCourse)) {
					enrolmentToWrite = new Enrolment();
					enrolmentToWrite.setCurricularCourse(curricularCourse);
					enrolmentToWrite.setEvaluationType(EnrolmentEvaluationType.NORMAL_OJB);
					enrolmentToWrite.setExecutionPeriod(executionPeriod);
					enrolmentToWrite.setState(EnrolmentState.TEMPORARILY_ENROLED_OBJ);
					enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
					// FIXME: David-Ricardo: Nao ha informação sobre o University Code por isso criei esta class temporária
					enrolmentToWrite.setUniversityCode(UniversityCode.IST);					
					temporarilyEnrolmentsToWrite.add(enrolmentToWrite);
				}
			}

			// interseccao das duas listas de enrolments
			List enrolmentsIntersection = new ArrayList();
			iterator = temporarilyEnrolmentsRead.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				if(temporarilyEnrolmentsToWrite.contains(enrolment)){
					enrolmentsIntersection.add(enrolment);
				}
			}
			temporarilyEnrolmentsRead.removeAll(enrolmentsIntersection);
			temporarilyEnrolmentsToWrite.removeAll(enrolmentsIntersection);

			// apagar da base de dados os enrolments que nao interessam
			iterator = temporarilyEnrolmentsRead.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.delete(enrolment);
			}
			
			// adicionar a base de dados os novos enrolments
			iterator = temporarilyEnrolmentsToWrite.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.lockWrite(enrolment);
			}
			
			// opcoes
			Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
			while (iterator2.hasNext()) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();
				
				IEnrolmentInOptionalCurricularCourse enrolment = (IEnrolmentInOptionalCurricularCourse) persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(enrolmentInOptionalCurricularCourse.getStudentCurricularPlan(), enrolmentInOptionalCurricularCourse.getCurricularCourse(), enrolmentInOptionalCurricularCourse.getExecutionPeriod());
				ICurricularCourse curricularCourseForOption = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourseForOption());
				
				if(enrolment == null) {
					ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourse());
					enrolmentInOptionalCurricularCourse.setCurricularCourse(curricularCourse);
					enrolmentInOptionalCurricularCourse.setCurricularCourseForOption(curricularCourseForOption);
					enrolmentInOptionalCurricularCourse.setExecutionPeriod(executionPeriod);
					enrolmentInOptionalCurricularCourse.setStudentCurricularPlan(studentCurricularPlan);
					
					enrolmentInOptionalCurricularCourse.setEvaluationType(EnrolmentEvaluationType.NORMAL_OJB);
					enrolmentInOptionalCurricularCourse.setUniversityCode(new String("IST"));
					
					persistentEnrolment.lockWrite(enrolmentInOptionalCurricularCourse);
				} else {
					enrolment.setCurricularCourseForOption(curricularCourseForOption);
					persistentEnrolment.lockWrite(enrolment);
				}
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