package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import DataBeans.util.Cloner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class GetAllAvailableCurricularCoursesForCoordinatorEquivalence implements IServico {

	private static GetAllAvailableCurricularCoursesForCoordinatorEquivalence service = new GetAllAvailableCurricularCoursesForCoordinatorEquivalence();

	public static GetAllAvailableCurricularCoursesForCoordinatorEquivalence getService() {
		return GetAllAvailableCurricularCoursesForCoordinatorEquivalence.service;
	}

	private GetAllAvailableCurricularCoursesForCoordinatorEquivalence() {
	}

	public final String getNome() {
		return "GetAllAvailableCurricularCoursesForCoordinatorEquivalence";
	}

	public List run(IUserView userView) throws FenixServiceException {

		List midResult = new ArrayList();
		List finalResult = new ArrayList();

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
			ICursoExecucaoPersistente persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

			ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
			List executionDegreesList = persistentExecutionDegree.readByTeacher(teacher);

			Iterator iterator1 = executionDegreesList.iterator();
			while(iterator1.hasNext()) {
				ICursoExecucao executionDegree = (ICursoExecucao) iterator1.next();
				IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();
				List studentCurricularPlansList = persistentStudentCurricularPlan.readByDegreeCurricularPlan(degreeCurricularPlan);
				
				Iterator iterator2 = studentCurricularPlansList.iterator();
				while(iterator2.hasNext()) {
					final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator2.next();
					if(studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE_OBJ)) {
						List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentCurricularPlan);

						List studentAprovedEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
							public boolean evaluate(Object obj) {
								IEnrolment enrolment = (IEnrolment) obj;
								return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
							}
						});

						List studentAprovedEnrolmentsWithDiferentDegreeCurricularPlan = (List) CollectionUtils.select(studentAprovedEnrolments, new Predicate() {
							public boolean evaluate(Object obj) {
								IEnrolment enrolment = (IEnrolment) obj;
								return !enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(studentCurricularPlan.getDegreeCurricularPlan());
							}
						});

						midResult.addAll(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlan);
					}
				}
			}

			Iterator iterator3 = midResult.iterator();
			while(iterator3.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator3.next();
				finalResult.add(Cloner.copyIEnrolment2InfoEnrolment(enrolment));
			}

			return finalResult;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}