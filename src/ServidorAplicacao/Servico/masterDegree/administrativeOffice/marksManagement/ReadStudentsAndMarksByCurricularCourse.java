package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.TipoCurso;

/**
 * @author Fernanda Quitério
 * 01/07/2003
 * 
 */
public class ReadStudentsAndMarksByCurricularCourse implements IServico {

	private static ReadStudentsAndMarksByCurricularCourse servico = new ReadStudentsAndMarksByCurricularCourse();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentsAndMarksByCurricularCourse getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentsAndMarksByCurricularCourse() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadStudentsAndMarksByCurricularCourse";
	}

	public InfoSiteEnrolmentEvaluation run(Integer scopeCode, String yearString) throws FenixServiceException {

		List infoEnrolmentEvaluations = new ArrayList();
		InfoTeacher infoTeacher = new InfoTeacher();
		Date lastEvaluationDate = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			//	get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(scopeCode);
			curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			//			this becomes necessary to use criteria
			InfoCurricularCourseScope infoCurricularCourseScope =
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
			ICurricularCourseScope curricularCourseScopeForCriteria =
				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);

			IEnrolment enrolment = new Enrolment();
			enrolment.setCurricularCourseScope(curricularCourseScopeForCriteria);
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(enrolment);
			List enrolmentEvaluations = persistentEnrolmentEvaluation.readByCriteria(enrolmentEvaluation);

			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				//			in case we have evaluations they can be submitted only if they are temporary
				if (((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
					.getEnrolmentEvaluationState()
					.equals(EnrolmentEvaluationState.FINAL_OBJ)) {
					throw new ExistingServiceException();
				}

				//				get teacher responsible for final evaluation - he is responsible for all evaluations for this
				//				curricularCourseScope
				IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
				if (person != null) {
					ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
					infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
				}
				
				//				transform evaluations in databeans
				ListIterator iter = enrolmentEvaluations.listIterator();
				while (iter.hasNext()) {
					IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter.next();
					InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

					infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(elem.getEnrolment()));
					infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
				}
			}
			

			if (infoEnrolmentEvaluations.size() == 0) {
				throw new NonExistingServiceException();
			}

			//				get last evaluation date to show in interface
			if (((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getExamDate() == null) {
				lastEvaluationDate =
					getLastEvaluationDate(yearString, persistentExecutionYear, persistentCurricularCourse, curricularCourseScope);
			} else {
				lastEvaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0)).getExamDate();
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
		infoSiteEnrolmentEvaluation.setLastEvaluationDate(lastEvaluationDate);

		return infoSiteEnrolmentEvaluation;
	}

	private Date getLastEvaluationDate(
		String yearString,
		IPersistentExecutionYear persistentExecutionYear,
		IPersistentCurricularCourse persistentCurricularCourse,
		ICurricularCourseScope curricularCourseScope)
		throws ExcepcaoPersistencia {
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(yearString);

		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		IExecutionPeriod executionPeriod = new ExecutionPeriod();
		executionPeriod.setExecutionYear(executionYear);
		executionCourse.setExecutionPeriod(executionPeriod);
		List curricularCourses = new ArrayList();
		InfoCurricularCourse infoCurricularCourse =
			Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourseScope.getCurricularCourse());
		ICurricularCourse curricularCourseForCriteria = Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
		curricularCourses.add(curricularCourseForCriteria);

		executionCourse.setAssociatedCurricularCourses(curricularCourses);
		executionCourse = (IDisciplinaExecucao) persistentCurricularCourse.readDomainObjectByCriteria(executionCourse);

		List evaluationsWithoutFinal = (List) CollectionUtils.select(executionCourse.getAssociatedEvaluations(), new Predicate() {
			public boolean evaluate(Object input) {
					//for now returns only exams
			if (input instanceof IExam)
					return true;
				return false;
			}
		});

		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("day.time"));
		comparatorChain.addComparator(new BeanComparator("beginning.time"));

		Collections.sort(evaluationsWithoutFinal, comparatorChain);

		if (evaluationsWithoutFinal.get(evaluationsWithoutFinal.size() - 1) instanceof IExam) {
			IExam lastEvaluation = (IExam) (evaluationsWithoutFinal.get(evaluationsWithoutFinal.size() - 1));
			return lastEvaluation.getDay().getTime();
		}
		return null;
	}

	public List run(Integer curricularCourseCode, Integer studentNumber) throws FenixServiceException {

		List enrolmentEvaluations = null;
		InfoTeacher infoTeacher = null;
		InfoStudent infoStudent = null;
		List infoSiteEnrolmentEvaluations = new ArrayList();
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente();

			//			get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(curricularCourseCode);
			curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			//			this becomes necessary to use criteria
			InfoCurricularCourseScope infoCurricularCourseScope =
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
			ICurricularCourseScope curricularCourseScopeForCriteria =
				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
			//			get student curricular Plan
			IStudentCurricularPlan studentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
					studentNumber,
					new TipoCurso(TipoCurso.MESTRADO));
			if (studentCurricularPlan == null) {
				throw new ExistingServiceException();
			}
			IEnrolment enrolment = new Enrolment();

			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

			enrolment =
				(IEnrolment) sp.getIPersistentEnrolment().readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(
					studentCurricularPlan,
					curricularCourseScope);

			if (enrolment != null) {

				//						ListIterator iter1 = enrolments.listIterator();
				//						while (iter1.hasNext()) {
				//							enrolment = (IEnrolment) iter1.next();

				EnrolmentEvaluationState enrolmentEvaluationState = new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL);
				enrolmentEvaluations =
					(List) persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationState(
						enrolment,
						enrolmentEvaluationState);
				List infoTeachers = new ArrayList();
				if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
					IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
					ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
					infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
					infoTeachers.add(infoTeacher);
				}

				List infoEnrolmentEvaluations = new ArrayList();
				if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
					ListIterator iter = enrolmentEvaluations.listIterator();
					while (iter.hasNext()) {
						enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
						InfoEnrolmentEvaluation infoEnrolmentEvaluation =
							Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
						infoEnrolmentEvaluation.setInfoEnrolment(
							Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment()));
						infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);

					}

				}
				InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
				infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
				infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);				
				infoSiteEnrolmentEvaluations.add(infoSiteEnrolmentEvaluation);
//				}

			}
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoSiteEnrolmentEvaluations;
	}

}
