package ServidorAplicacao.Servico.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Util.TipoCurso;
import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
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
		//		IPersistentCurricularCourse persistentCurricularCourse = null;
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		IPersistentExecutionPeriod persistentExecutionPeriod = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			persistentEnrolment = sp.getIPersistentEnrolment();
			persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();
			//			persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

			//			IStudentCurricularPlan studentCurricularPlan =
			//				(IStudentCurricularPlan) persistentStudentCurricularPlan.readDomainObjectByCriteria(enrolmentContext.getStudentActiveCurricularPlan());
			Integer studentNumber = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getNumber();
			TipoCurso tipoCurso = TipoCurso.LICENCIATURA_OBJ;

			IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(studentNumber, tipoCurso);

			IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readDomainObjectByCriteria(enrolmentContext.getExecutionPeriod());

			// list of all temporarily enrolments.
			final List temporarilyEnrolmentsRead =
				persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					enrolmentContext.getStudentActiveCurricularPlan(),
					EnrolmentState.TEMPORARILY_ENROLED);

			// enrolments in (anual) curricular courses that the student is doing.
			final List doingEnrolmentsRead =
				persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					enrolmentContext.getStudentActiveCurricularPlan(),
					EnrolmentState.ENROLED);
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
				ICurricularCourseScope curricularCourseScopeChosen = (ICurricularCourseScope) iterator.next();

				//				ICurricularCourse curricularCourseForCriteria = new CurricularCourse();
				//				curricularCourseForCriteria.setCode(curricularCourseScopeChosen.getCurricularCourse().getCode());
				//				curricularCourseForCriteria.setName(curricularCourseScopeChosen.getCurricularCourse().getName());
				//				curricularCourseForCriteria.setDegreeCurricularPlan(curricularCourseScopeChosen.getCurricularCourse().getDegreeCurricularPlan());
				//				ICurricularCourseScope curricularCourseScopeForCriteria = new CurricularCourseScope();
				//				curricularCourseScopeForCriteria.setBranch(curricularCourseScopeChosen.getBranch());
				//				curricularCourseScopeForCriteria.setCurricularCourse(curricularCourseForCriteria);
				//				curricularCourseScopeForCriteria.setCurricularSemester(curricularCourseScopeChosen.getCurricularSemester());

				//				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(curricularCourseScope.getCurricularCourse());
				ICurricularCourseScope curricularCourseScopeToWrite =
					persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                        	curricularCourseScopeChosen.getCurricularCourse(),
                        	curricularCourseScopeChosen.getCurricularSemester(),
                        	curricularCourseScopeChosen.getBranch(), null);
				//				ICurricularCourseScope curricularCourseScopeToWrite =
				//					(ICurricularCourseScope) persistentCurricularCourseScope.readDomainObjectByCriteria(curricularCourseScopeForCriteria);
				if (!doingCurricularCoursesRead.contains(curricularCourseScopeToWrite.getCurricularCourse())) {
					enrolmentToWrite = new Enrolment();
					//					enrolmentToWrite.setCurricularCourse(curricularCourse);
					enrolmentToWrite.setCurricularCourseScope(curricularCourseScopeToWrite);
					enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentToWrite.setExecutionPeriod(executionPeriod);
					enrolmentToWrite.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED);
					enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
					temporarilyEnrolmentsToWrite.add(enrolmentToWrite);
				}
			}

			// get the intersection of the 2 lists of enrolments
			List enrolmentsIntersection = new ArrayList();
			iterator = temporarilyEnrolmentsRead.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				if (temporarilyEnrolmentsToWrite.contains(enrolment)) {
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

			// options
			Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
			while (iterator2.hasNext()) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();

				IEnrolmentInOptionalCurricularCourse enrolment =
					(IEnrolmentInOptionalCurricularCourse) persistentEnrolment.readEnrolmentByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
						enrolmentInOptionalCurricularCourse.getStudentCurricularPlan(),
						enrolmentInOptionalCurricularCourse.getCurricularCourseScope(),
						enrolmentInOptionalCurricularCourse.getExecutionPeriod());
				//				ICurricularCourse curricularCourseForOption = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourseForOption());
				ICurricularCourse curricularCourseForOption = enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();

				if (enrolment == null) {
					//					ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourse());
					//					enrolmentInOptionalCurricularCourse.setCurricularCourse(curricularCourse);
					ICurricularCourseScope optionalCurricularCourseScopeChosen = enrolmentInOptionalCurricularCourse.getCurricularCourseScope();

					ICurricularCourseScope optionalCurricularCourseScopeToWrite =
						persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                            	optionalCurricularCourseScopeChosen.getCurricularCourse(),
                            	optionalCurricularCourseScopeChosen.getCurricularSemester(),
                            	optionalCurricularCourseScopeChosen.getBranch(), null);

					enrolmentInOptionalCurricularCourse.setCurricularCourseScope(optionalCurricularCourseScopeToWrite);
					enrolmentInOptionalCurricularCourse.setCurricularCourseForOption(curricularCourseForOption);
					enrolmentInOptionalCurricularCourse.setExecutionPeriod(executionPeriod);
					enrolmentInOptionalCurricularCourse.setStudentCurricularPlan(studentCurricularPlan);
					enrolmentInOptionalCurricularCourse.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentInOptionalCurricularCourse.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED);
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