package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
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

	public InfoSiteEnrolmentEvaluation run(Integer scopeCode) throws FenixServiceException {

		List infoEnrolmentEvaluations = new ArrayList();
		InfoTeacher infoTeacher = new InfoTeacher();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
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
					.equals(EnrolmentEvaluationState.NORMAL_OBJ)) {
					throw new ExistingServiceException();
				}

				//				get teacher responsible for final evaluation - he is responsible for all evaluations for this
				//				curricularCourseScope
				IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
				ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
				infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

				ListIterator iter = enrolmentEvaluations.listIterator();
				while (iter.hasNext()) {
					IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter.next();
					InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

					infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(elem.getEnrolment()));
					infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
				}
			} else {
				//			if there is no evaluation yet we have to get all students enroled in curricularCourseScope
				List enrolments = persistentEnrolment.readByCriteria(enrolment);

				ListIterator iterEnrolments = enrolments.listIterator();
				while (iterEnrolments.hasNext()) {
					IEnrolment element = (IEnrolment) iterEnrolments.next();
					InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
					infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(element));
					infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
				}
			}
			if (infoEnrolmentEvaluations.size() == 0) {
				throw new NonExistingServiceException();
			}
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

		return infoSiteEnrolmentEvaluation;
	}
	
	
	public List run(Integer curricularCourseCode,Integer studentNumber) throws FenixServiceException {

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
				curricularCourseScope =	(ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

				//			this becomes necessary to use criteria
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				ICurricularCourseScope curricularCourseScopeForCriteria =
					Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				//			get student curricular Plan
				IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(studentNumber,new TipoCurso(TipoCurso.MESTRADO));
				if (studentCurricularPlan == null) {	
					throw new ExistingServiceException();
				}
				IEnrolment enrolment = new Enrolment();
				
				IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
	
				 enrolment = (IEnrolment) sp.getIPersistentEnrolment().readEnrolmentByStudentCurricularPlanAndCurricularCourseScope(studentCurricularPlan,curricularCourseScope);

				if (enrolment != null ){ 
				
//						ListIterator iter1 = enrolments.listIterator();
//						while (iter1.hasNext()) {
//							enrolment = (IEnrolment) iter1.next();

							EnrolmentEvaluationState enrolmentEvaluationState = new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL);
							enrolmentEvaluations = (List) persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationState(enrolment,enrolmentEvaluationState);
		
						
							if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
								IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
								ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
								infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
							}
			
							List infoEnrolmentEvaluations = new ArrayList();
							if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
								ListIterator iter = enrolmentEvaluations.listIterator();
								while (iter.hasNext()) {
									enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
									InfoEnrolmentEvaluation infoEnrolmentEvaluation =
										Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
									infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment()));
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
