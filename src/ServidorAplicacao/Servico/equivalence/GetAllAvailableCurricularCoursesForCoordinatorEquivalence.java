package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

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

	public List run(IUserView userView, InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {

		List midResult = new ArrayList();
		List finalResult = new ArrayList();

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);

			List degreeCurricularPlansList = persistentDegreeCurricularPlan.readByDegree(degreeCurricularPlan.getDegree());

			Iterator iterator1 = degreeCurricularPlansList.iterator();
			while(iterator1.hasNext()) {
				IDegreeCurricularPlan degreeCurricularPlan2 = (IDegreeCurricularPlan) iterator1.next();

				List studentCurricularPlansList = persistentStudentCurricularPlan.readByDegreeCurricularPlan(degreeCurricularPlan2);
			
				Iterator iterator2 = studentCurricularPlansList.iterator();
				while(iterator2.hasNext()) {
					IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator2.next();
					IStudent student = studentCurricularPlan.getStudent();
					final IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

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
//System.out.println(enrolment);
							return !enrolment.getCurricularCourse().getDegreeCurricularPlan().equals(studentActiveCurricularPlan.getDegreeCurricularPlan());
						}
					});

//System.out.println(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlan);

					List studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences = GetListsOfCurricularCoursesForEquivalence.getEnrolmentsWithNoEquivalences(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlan, persistentSupport);

					midResult.addAll(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences);
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