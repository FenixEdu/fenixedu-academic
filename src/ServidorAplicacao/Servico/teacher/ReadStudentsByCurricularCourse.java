package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteStudentsCurricularPlan;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.Frequenta;
import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * @author Tânia Pousão 
 * @author Ângela
 *
 */
public class ReadStudentsByCurricularCourse implements IServico {
	private static ReadStudentsByCurricularCourse _servico = new ReadStudentsByCurricularCourse();

	/**
		* The actor of this class.
		**/
	private ReadStudentsByCurricularCourse() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadStudentsByCurricularCourse";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadStudentsByCurricularCourse getService() {
		return _servico;
	}

	public Object run(Integer executionCourseCode, Integer scopeCode) throws ExcepcaoInexistente, FenixServiceException {

		List infoStudentList = new ArrayList();
		ISite site = null;
		IDisciplinaExecucao executionCourse = null;
		ICurricularCourseScope curricularCourseScope = null;
		try {
			executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(executionCourseCode);

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

			IPersistentSite persistentSite = sp.getIPersistentSite();
			site = persistentSite.readByExecutionCourse(executionCourse);

			List enrolments = new ArrayList();
			if (scopeCode == null) {

				//				all students that attend this execution course
				IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
				List studentsAttend = (List) frequentaPersistente.readByExecutionCourse(executionCourse);

				ListIterator iterStudent = studentsAttend.listIterator();
				while (iterStudent.hasNext()) {
					IFrequenta attend = (Frequenta) iterStudent.next();
					IEnrolment enrolment = attend.getEnrolment();
					if (enrolment != null) {
						enrolments.add(enrolment);
					}
				}
			} else {

				curricularCourseScope = new CurricularCourseScope();
				curricularCourseScope.setIdInternal(scopeCode);
				IEnrolment enrolmentForCriteria = new Enrolment();
				enrolmentForCriteria.setCurricularCourseScope(curricularCourseScope);
				IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
				enrolments = (List) persistentEnrolment.readByCriteria(enrolmentForCriteria);

				IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();
				curricularCourseScope =
					(ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);

			}

			Iterator iterEnrolments = enrolments.listIterator();
			while (iterEnrolments.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterEnrolments.next();
				IStudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
				InfoStudentCurricularPlan infoStudentCurricularPlan =
					Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
				infoStudentList.add(infoStudentCurricularPlan);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		InfoSiteStudentsCurricularPlan infoSiteStudents = new InfoSiteStudentsCurricularPlan();
		infoSiteStudents.setStudents(infoStudentList);
		if (curricularCourseScope != null) {
			infoSiteStudents.setInfoCurricularCourseScope(
				Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope));
		}
		TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
		ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

		TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteStudents);
		return siteView;
	}
}
