package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ShowAvailableCurricularCourses implements IServico {

	private static ShowAvailableCurricularCourses _servico = new ShowAvailableCurricularCourses();
	/**
	 * The singleton access method of this class.
	 **/
	public static ShowAvailableCurricularCourses getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ShowAvailableCurricularCourses() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ShowAvailableCurricularCourses";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
	public EnrolmentContext run(UserView userView) throws FenixServiceException {

		EnrolmentContext enrolmentContext = null;
		IEnrolmentStrategy strategy = null;
		
		try {

			enrolmentContext = prepareEnrolmentContext(userView);

			strategy = EnrolmentStrategyFactory.getEnrolmentStrategyInstance(enrolmentContext);
			enrolmentContext = strategy.getAvailableCurricularCourses();

			return enrolmentContext;

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}
	}

	private static EnrolmentContext prepareEnrolmentContext(UserView userView) throws ExcepcaoPersistencia {
		
		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
		IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
		ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();;
		IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();;
		IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport.getIPersistentCurricularCourseScope();
		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		IStudent student = persistentStudent.readByUsername(userView.getUtilizador());

		IStudentCurricularPlan studentActiveCurricularPlan =
			persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

		List studentCurricularPlanCurricularCourses = studentActiveCurricularPlan.getDegreeCurricularPlan().getCurricularCourses();

		final List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

		final List studentEnrolmentsWithStateApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getState().equals(new EnrolmentState(EnrolmentState.APROVED));
			}
		});

		final List studentDoneCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		List enrolmentsWithStateNotApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
				return !studentDoneCurricularCourses.contains(curricularCourse)
					&& enrolment.getState().equals(new EnrolmentState(EnrolmentState.NOT_APROVED));
			}

		});

		List curricularCoursesEnrolled = (List) CollectionUtils.collect(enrolmentsWithStateNotApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		EnrolmentContext enrolmentContext = new EnrolmentContext();

		enrolmentContext.setStudent(student);
		// FIXME: David-Ricardo: ler o semestre do execution Period quando este tiver esta informacao
		enrolmentContext.setSemester(new Integer(1));		
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(
			computeCurricularCoursesScopesNotYetDoneByStudent(studentCurricularPlanCurricularCourses, studentDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);

		return enrolmentContext;
	}
	
	
	private static List computeCurricularCoursesScopesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent)
		throws ExcepcaoPersistencia {

		List scopesNotDone = new ArrayList();

		List coursesNotDone = (List) CollectionUtils.subtract(curricularCoursesFromStudentDegreeCurricularPlan, aprovedCurricularCoursesFromStudent);

		Iterator iteratorCourses = coursesNotDone.iterator();
		while (iteratorCourses.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iteratorCourses.next();
			List scopes = curricularCourse.getScopes();
			Iterator iteratorScopes = scopes.iterator();
			while (iteratorScopes.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopes.next();
				scopesNotDone.add(curricularCourseScope);
			}
		}

		return scopesNotDone;
	}
}