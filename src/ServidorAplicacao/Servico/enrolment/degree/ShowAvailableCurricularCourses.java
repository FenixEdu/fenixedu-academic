package ServidorAplicacao.Servico.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IStudent;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.IEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class ShowAvailableCurricularCourses implements IServico {

	private static ShowAvailableCurricularCourses _servico =
		new ShowAvailableCurricularCourses();
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
	public InfoEnrolmentContext run(IUserView userView)
		throws FenixServiceException {

		try {

			ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO =	persistentSupport.getIPersistentStudent();
			IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());

			IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
			IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentContextManager.initialEnrolmentContext(student));

			EnrolmentContext enrolmentContext =	strategy.getAvailableCurricularCourses();

			return EnrolmentContextManager.getInfoEnrolmentContext(updateActualEnrolment(enrolmentContext));

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		} catch (IllegalStateException ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex);
		}
	}

	private EnrolmentContext updateActualEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia{

		ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();

		IPersistentEnrolment persistentEnrolment =persistentSupport.getIPersistentEnrolment();
		
		final List temporarilyEnrolments = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				enrolmentContext.getStudentActiveCurricularPlan(),
				new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED));

		final List enrolmentsInOptionalCurricularCourses = (List) CollectionUtils.select(temporarilyEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
			}
		});
		
		final List enrolmentsNotInOptionalCurricularCourses = (List) CollectionUtils.select(temporarilyEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return !enrolment.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
			}
		});

		List notOptionalCurricularCoursesTemporarilyEnroled = (List) CollectionUtils.collect(enrolmentsNotInOptionalCurricularCourses, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourse());
			}
		});

		List optionalCurricularCoursesTemporarilyEnroled = (List) CollectionUtils.collect(enrolmentsInOptionalCurricularCourses, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourse());
			}
		});

		List aux1 = (List) CollectionUtils.union(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled(), enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
		
		Iterator iterator = aux1.iterator();
		List aux2 = new ArrayList();
		List aux3 = new ArrayList();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (notOptionalCurricularCoursesTemporarilyEnroled.contains(curricularCourseScope.getCurricularCourse())) {
				aux2.add(curricularCourseScope);
			}
			if (optionalCurricularCoursesTemporarilyEnroled.contains(curricularCourseScope.getCurricularCourse())) {
				aux3.add(curricularCourseScope);
			}
		}
		enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(aux2);
		enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().removeAll(aux2);
		enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(aux3);
		enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().removeAll(aux3);
		enrolmentContext.setActualEnrolment(aux2);
		enrolmentContext.setOptionalCurricularCoursesEnrolments(enrolmentsInOptionalCurricularCourses);
		
		return enrolmentContext;
	}
}