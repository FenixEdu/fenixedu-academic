package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.Enrolment;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author David Santos
 * 16/Jun/2003
 */

public class ConfirmActualEnrolmentWithoutRules implements IServico {

	private static ConfirmActualEnrolmentWithoutRules _servico = new ConfirmActualEnrolmentWithoutRules();

	public static ConfirmActualEnrolmentWithoutRules getService() {
		return _servico;
	}

	private ConfirmActualEnrolmentWithoutRules() {
	}

	public final String getNome() {
		return "ConfirmActualEnrolmentWithoutRules";
	}

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
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			persistentEnrolment = sp.getIPersistentEnrolment();
			persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();
			persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(enrolmentContext.getStudentActiveCurricularPlan());
			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());

			// list of all temporarily enrolments
			final List temporarilyEnrolmentsRead = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.TEMPORARILY_ENROLED_OBJ);
			
			// enrolments in (anual) curricular courses that the student is doing.
			final List doingEnrolmentsRead = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.ENROLED_OBJ);
			List doingCurricularCoursesRead = (List) CollectionUtils.collect(doingEnrolmentsRead, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return (enrolment.getCurricularCourseScope().getCurricularCourse());
				}
			});
			
			// list of all enrolments to be writen
			List temporarilyEnrolmentsToWrite = new ArrayList();
			Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
			IEnrolment enrolmentToWrite = null;
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) persistentCurricularCourseScope.readDomainObjectByCriteria(curricularCourseScope);
				if(!doingCurricularCoursesRead.contains(curricularCourseScope.getCurricularCourse())) {
					enrolmentToWrite = new Enrolment();
					enrolmentToWrite.setCurricularCourseScope(curricularCourseScope2);
					enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentToWrite.setExecutionPeriod(executionPeriod);
					enrolmentToWrite.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED_OBJ);
					enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
					// FIXME DAVID-RICARDO: Nao ha informação sobre o University Code por isso criei esta class temporária
					temporarilyEnrolmentsToWrite.add(enrolmentToWrite);
				}
			}

			// get the intersection of the 2 lists of enrolments
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

			// delete from data base the enrolments that don't mather.
			iterator = temporarilyEnrolmentsRead.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.delete(enrolment);
			}
			
			// add to data base the new enrolments
			iterator = temporarilyEnrolmentsToWrite.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.lockWrite(enrolment);
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