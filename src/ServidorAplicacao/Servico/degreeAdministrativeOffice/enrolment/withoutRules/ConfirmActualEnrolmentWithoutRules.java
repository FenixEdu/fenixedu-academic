package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.DegreeCurricularPlan;
import Dominio.Enrolment;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
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
import ServidorPersistente.IPersistentDegreeCurricularPlan;
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

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, Integer semester, Integer year) throws FenixServiceException {

		EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

		if (enrolmentContext.getEnrolmentValidationResult().isSucess()) {
			try {
				writeTemporaryEnrolment(enrolmentContext, semester, year);
				enrolmentContext.getEnrolmentValidationResult().setSucessMessage(EnrolmentValidationResult.SUCCESS_ENROLMENT);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				throw new FenixServiceException(e);
			}
		}
		return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
	}

	private void writeTemporaryEnrolment(EnrolmentContext enrolmentContext, Integer semester, Integer year) throws ExcepcaoPersistencia {
		ISuportePersistente sp = null;
		IPersistentEnrolment persistentEnrolment = null;
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			persistentEnrolment = sp.getIPersistentEnrolment();
			persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();
			persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(enrolmentContext.getStudentActiveCurricularPlan());
			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());

			IDegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
			degreeCurricularPlanCriteria.setDegree(enrolmentContext.getChosenOptionalDegree());
			final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(degreeCurricularPlanCriteria);

			// list of all enrolments that may have been deleted.
			List enrolmentsWithStateTemporarilyEnroled = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.TEMPORARILY_ENROLED_OBJ);
			List enrolmentsWithStateEnroled = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.ENROLED_OBJ);
			List enrolmentsRead = new ArrayList();
			enrolmentsRead.addAll(enrolmentsWithStateTemporarilyEnroled);
			enrolmentsRead.addAll(enrolmentsWithStateEnroled);
			
			final Integer semester2 = semester;
			final Integer year2 = year;
			List validEnrolmentsRead = (List) CollectionUtils.select(enrolmentsRead, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return	enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan) &&
					enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().equals(semester2) &&
					enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear().equals(year2);
				}
			});

			List curricularCoursesScopesFromEnrolmentsRead = (List) CollectionUtils.collect(validEnrolmentsRead, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getCurricularCourseScope();
				}
			});
			
			List curricularCoursesScopesFromEnrolmentsToRemove = (List) CollectionUtils.subtract(curricularCoursesScopesFromEnrolmentsRead, enrolmentContext.getActualEnrolments());

			// list of all enrolments to be writen.
			List enrolmentsToWrite = new ArrayList();
			Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
			IEnrolment enrolmentToWrite = null;
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) persistentCurricularCourseScope.readDomainObjectByCriteria(curricularCourseScope);
				if(!curricularCoursesScopesFromEnrolmentsRead.contains(curricularCourseScope)) {
					enrolmentToWrite = new Enrolment();
					enrolmentToWrite.setCurricularCourseScope(curricularCourseScope2);
					enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentToWrite.setExecutionPeriod(executionPeriod);
					enrolmentToWrite.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED_OBJ);
					enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
					// FIXME DAVID-RICARDO: Nao ha informação sobre o University Code por isso criei esta class temporária
					enrolmentsToWrite.add(enrolmentToWrite);
				}
			}

			// list of all enrolments to be deleted.
			List enrolmentsToDelete = new ArrayList();
			Iterator iterator2 = validEnrolmentsRead.iterator();
			while (iterator2.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator2.next();
				if(curricularCoursesScopesFromEnrolmentsToRemove.contains(enrolment.getCurricularCourseScope())) {
					enrolmentsToDelete.add(enrolment);
				}
			}

			// delete from data base the enrolments that don't mather.
			iterator = enrolmentsToDelete.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.delete(enrolment);
			}
			
			// add to data base the new enrolments.
			iterator = enrolmentsToWrite.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.lockWrite(enrolment);
			}
		} catch (ExistingPersistentException e1) {
			e1.printStackTrace();
//			throw e1;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw e;
		}
	}
}