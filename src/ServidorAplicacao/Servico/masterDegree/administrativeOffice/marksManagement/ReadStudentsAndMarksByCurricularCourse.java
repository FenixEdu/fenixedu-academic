package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

	public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode) throws FenixServiceException {

		List enrolmentEvaluations = null;
		InfoTeacher infoTeacher = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			System.out.println("entrei");
			//	get curricular course to obtain curricularCourseScope
			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
			System.out.println("li curricularcourse: " + curricularCourse.getName());

			//	get curricularCourseScope for enrolmentEvaluation
			ICurricularCourseScope curricularCourseScope =
				persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourse(curricularCourse);
			System.out.println("li curricularcoursescope: " + curricularCourseScope.getIdInternal());

			IEnrolment enrolment = new Enrolment();
			enrolment.setCurricularCourseScope(curricularCourseScope);
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluations = persistentEnrolmentEvaluation.readAll();
//			.readByCriteria(enrolmentEvaluation);
			System.out.println("li avaliacoes: ");
			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getPersonResponsibleForGrade();
				ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
				infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		System.out.println("antes de criar a lista");

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
			System.out.println("tamanho da lista: " + infoEnrolmentEvaluations.size());
		}
		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

		System.out.println("sai do servico");
		return infoSiteEnrolmentEvaluation;
	}
}
