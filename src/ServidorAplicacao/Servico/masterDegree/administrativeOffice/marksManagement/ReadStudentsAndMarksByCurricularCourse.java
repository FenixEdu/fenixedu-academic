package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
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
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;

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

		List enrolmentEvaluations = null;
		InfoTeacher infoTeacher = new InfoTeacher();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			//	get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(scopeCode);
			curricularCourseScope =	(ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			//			this becomes necessary to use criteria
			InfoCurricularCourseScope infoCurricularCourseScope =
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
			ICurricularCourseScope curricularCourseScopeForCriteria =
				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);

			IEnrolment enrolment = new Enrolment();
			enrolment.setCurricularCourseScope(curricularCourseScopeForCriteria);
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluations = persistentEnrolmentEvaluation.readByCriteria(enrolmentEvaluation);
			
//			in case we have evaluations they can be submitted only if they are temporary
			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				if(((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.NORMAL_OBJ)){
					throw new ExistingServiceException();
				}
				IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
				ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
				infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
				
			} else {
				List enrolments = persistentEnrolment.readByCriteria(enrolment);
				
				enrolmentEvaluations = new ArrayList();
				ListIterator iterEnrolments = enrolments.listIterator();
				while (iterEnrolments.hasNext()) {
					IEnrolment element = (IEnrolment) iterEnrolments.next();
					IEnrolmentEvaluation enrolmentEvaluation2 = new EnrolmentEvaluation();
					enrolmentEvaluation2.setEnrolment(element);
					enrolmentEvaluations.add(enrolmentEvaluation2);
				}
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		List infoEnrolmentEvaluations = new ArrayList();
		if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
			ListIterator iter = enrolmentEvaluations.listIterator();
			while (iter.hasNext()) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
				InfoEnrolmentEvaluation infoEnrolmentEvaluation =
					Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
				infoEnrolmentEvaluation.setInfoEnrolment(Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment()));
				infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
			}
		} else{
			throw new NonExistingServiceException();
		}
		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

		return infoSiteEnrolmentEvaluation;
	}
}
