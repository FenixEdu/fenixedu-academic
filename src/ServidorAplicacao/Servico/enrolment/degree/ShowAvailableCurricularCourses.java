package ServidorAplicacao.Servico.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;
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
		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			throw e;
		}
	}

	private EnrolmentContext updateActualEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia{

		ISuportePersistente persistentSupport =	SuportePersistenteOJB.getInstance();

		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
		
		final List temporarilyEnrolments = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				enrolmentContext.getStudentActiveCurricularPlan(),
				EnrolmentState.TEMPORARILY_ENROLED_OBJ);

		final List enrolmentsInOptionalCurricularCourses = (List) CollectionUtils.select(temporarilyEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				if(obj instanceof IEnrolmentInOptionalCurricularCourse) {
					IEnrolmentInOptionalCurricularCourse enrolment = (IEnrolmentInOptionalCurricularCourse) obj;
					return enrolment.getCurricularCourseScope().getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
				} else {
					return false;
				}
			}
		});
		

		final List enrolmentsNotInOptionalCurricularCourses = (List) CollectionUtils.subtract(temporarilyEnrolments, enrolmentsInOptionalCurricularCourses);


		List notOptionalCurricularCoursesTemporarilyEnroled = (List) CollectionUtils.collect(enrolmentsNotInOptionalCurricularCourses, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourseScope().getCurricularCourse());
			}
		});

		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		List noOptionalCourseScopes = new ArrayList();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (notOptionalCurricularCoursesTemporarilyEnroled.contains(curricularCourseScope.getCurricularCourse())) {
				noOptionalCourseScopes.add(curricularCourseScope);
			}
		}
		enrolmentContext.setActualEnrolments(noOptionalCourseScopes);
		enrolmentContext.getActualEnrolments().addAll(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled());
		enrolmentContext.setOptionalCurricularCoursesEnrolments(enrolmentsInOptionalCurricularCourses);
		
		return enrolmentContext;
	}
}