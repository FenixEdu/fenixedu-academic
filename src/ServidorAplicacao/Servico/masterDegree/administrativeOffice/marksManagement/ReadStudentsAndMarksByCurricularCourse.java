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
}
