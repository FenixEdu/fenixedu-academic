package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.Enrolment;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
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
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			persistentEnrolment = sp.getIPersistentEnrolment();
			persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();

			List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());

			// List of all enrolments with state 'enrolled' and 'temporarily enrolled'.
			List studentEnroledAndTemporarilyEnroledEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return (enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED) || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED));
				}
			});

//			List enrolmentsToDelete = (List) CollectionUtils.subtract(studentEnroledAndTemporarilyEnroledEnrolments, enrolmentContext.getOptionalCurricularCoursesEnrolments());
			List enrolmentsToDelete = this.subtract(studentEnroledAndTemporarilyEnroledEnrolments, enrolmentContext.getActualEnrolments());
			// Delete from data base the enrolments that don't mather.
			Iterator iterator = enrolmentsToDelete.iterator();
			while(iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				persistentEnrolment.delete(enrolment);
			}

			iterator = enrolmentContext.getActualEnrolments().iterator();
			while(iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();

				IEnrolment enrolment =
//					(IEnrolment) persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
					(IEnrolment) persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(
						enrolmentContext.getStudentActiveCurricularPlan(),
						curricularCourseScope);
//						enrolmentContext.getExecutionPeriod());

				if(enrolment == null) {

					ICurricularCourseScope curricularCourseScopeToWrite =
						(ICurricularCourseScope) persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
							curricularCourseScope.getCurricularCourse(),
							curricularCourseScope.getCurricularSemester(),
							curricularCourseScope.getBranch());

					enrolment = new Enrolment();
					enrolment.setCurricularCourseScope(curricularCourseScopeToWrite);
					enrolment.setExecutionPeriod(enrolmentContext.getExecutionPeriod());
					enrolment.setStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());
					enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolment.setEnrolmentState(EnrolmentState.ENROLED);
					persistentEnrolment.lockWrite(enrolment);
				} else {
					persistentEnrolment.lockWrite(enrolment);
					enrolment.setEnrolmentState(EnrolmentState.ENROLED);
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

	private List subtract(List fromList, List toRemoveList) {
		List result = new ArrayList();
		if( (fromList != null) && (toRemoveList != null) && (!fromList.isEmpty()) ) {
			result.addAll(fromList);
			Iterator iterator = fromList.iterator();
			while(iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				if(toRemoveList.contains(enrolment.getCurricularCourseScope())) {
					result.remove(enrolment);
				}
			}
		}
		return result;
	}


//	private void writeTemporaryEnrolment(EnrolmentContext enrolmentContext, Integer semester, Integer year) throws ExcepcaoPersistencia {
//		ISuportePersistente sp = null;
//		IPersistentEnrolment persistentEnrolment = null;
//		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
//		IPersistentExecutionPeriod persistentExecutionPeriod = null;
//		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
//
//		try {
//			sp = SuportePersistenteOJB.getInstance();
//			persistentEnrolment = sp.getIPersistentEnrolment();
//			persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();
//			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
//			persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
//
//			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(enrolmentContext.getStudentActiveCurricularPlan());
//			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());
//
//			ICurso degreeCriteria = new Curso();
//			degreeCriteria.setNome(enrolmentContext.getChosenOptionalDegree().getNome());
//			degreeCriteria.setSigla(enrolmentContext.getChosenOptionalDegree().getSigla());
//			degreeCriteria.setTipoCurso(enrolmentContext.getChosenOptionalDegree().getTipoCurso());
//			IDegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
//			degreeCurricularPlanCriteria.setDegree(degreeCriteria);
//			final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(degreeCurricularPlanCriteria);
//
//			// list of all enrolments that may have been deleted.
//			List enrolmentsWithStateTemporarilyEnroled = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.TEMPORARILY_ENROLED);
//			List enrolmentsWithStateEnroled = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.ENROLED);
//			List enrolmentsRead = new ArrayList();
//			enrolmentsRead.addAll(enrolmentsWithStateTemporarilyEnroled);
//			enrolmentsRead.addAll(enrolmentsWithStateEnroled);
//
//			final Integer semester2 = semester;
//			final Integer year2 = year;
//			List validEnrolmentsRead = (List) CollectionUtils.select(enrolmentsRead, new Predicate() {
//				public boolean evaluate(Object obj) {
//					IEnrolment enrolment = (IEnrolment) obj;
//					return	enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan) &&
//					enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().equals(semester2) &&
//					enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear().equals(year2);
//				}
//			});
//
//			List curricularCoursesScopesFromEnrolmentsRead = (List) CollectionUtils.collect(validEnrolmentsRead, new Transformer() {
//				public Object transform(Object obj) {
//					IEnrolment enrolment = (IEnrolment) obj;
//					return enrolment.getCurricularCourseScope();
//				}
//			});
//			
//			List curricularCoursesScopesFromEnrolmentsToRemove = (List) CollectionUtils.subtract(curricularCoursesScopesFromEnrolmentsRead, enrolmentContext.getActualEnrolments());
//
//			// list of all enrolments to be writen.
//			List enrolmentsToWrite = new ArrayList();
//			Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
//			IEnrolment enrolmentToWrite = null;
//			while (iterator.hasNext()) {
//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
//				if(!curricularCoursesScopesFromEnrolmentsRead.contains(curricularCourseScope)) {
//					enrolmentToWrite = new Enrolment();
//					enrolmentToWrite.setCurricularCourseScope(curricularCourseScope);
//					enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
//					enrolmentToWrite.setExecutionPeriod(executionPeriod);
//					enrolmentToWrite.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED);
//					enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
//					enrolmentsToWrite.add(enrolmentToWrite);
//				}
//			}
//
//			// list of all enrolments to be deleted.
//			List enrolmentsToDelete = new ArrayList();
//			Iterator iterator2 = validEnrolmentsRead.iterator();
//			while (iterator2.hasNext()) {
//				IEnrolment enrolment = (IEnrolment) iterator2.next();
//				if(curricularCoursesScopesFromEnrolmentsToRemove.contains(enrolment.getCurricularCourseScope())) {
//					enrolmentsToDelete.add(enrolment);
//				}
//			}
//
//			// delete from data base the enrolments that don't mather.
//			iterator = enrolmentsToDelete.iterator();
//			while (iterator.hasNext()) {
//				IEnrolment enrolment = (IEnrolment) iterator.next();
//				persistentEnrolment.delete(enrolment);
//			}
//			
//			// add to data base the new enrolments.
//			iterator = enrolmentsToWrite.iterator();
//			while (iterator.hasNext()) {
//				IEnrolment enrolment = (IEnrolment) iterator.next();
//				persistentEnrolment.lockWrite(enrolment);
//			}
//		} catch (ExistingPersistentException e1) {
//			e1.printStackTrace();
////			throw e1;
//		} catch (ExcepcaoPersistencia e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
}