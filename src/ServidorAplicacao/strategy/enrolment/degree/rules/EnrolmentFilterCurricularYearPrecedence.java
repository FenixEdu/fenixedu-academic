package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterCurricularYearPrecedence implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List curricularCoursesNeverEnroled = null;
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			final List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());
			final List studentEnrolmentsWithStateDiferentOfTemporarilyEnroled = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return !enrolment.getState().equals(new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED));
				}
			});
			List correspondingCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateDiferentOfTemporarilyEnroled, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getCurricularCourse();
				}
			});
			curricularCoursesNeverEnroled = new ArrayList();
			curricularCoursesNeverEnroled.addAll(enrolmentContext.getCurricularCoursesFromStudentCurricularPlan());
			curricularCoursesNeverEnroled.removeAll(correspondingCurricularCourses);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new IllegalStateException("Cannot read from database");
		}

		List curricularCoursesScopesNeverEnroled = EnrolmentContextManager.computeScopesOfCurricularCourses(curricularCoursesNeverEnroled);

// ------------------------------------------------------------
// NOTE: DAVID-RICARDO: Este pedaço de código é uma maneira de filtrar pelo ramo e semestre de modo a tornar esta regra independente.
		List finalSpanBackup = new ArrayList();
		finalSpanBackup.addAll(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCoursesScopesNeverEnroled);

		IEnrolmentRule enrolmentRule = null;
		enrolmentRule = new EnrolmentFilterBranchRule();
		enrolmentContext = enrolmentRule.apply(enrolmentContext);
		enrolmentRule = new EnrolmentFilterSemesterRule();
		enrolmentContext = enrolmentRule.apply(enrolmentContext);
		
		curricularCoursesScopesNeverEnroled = new ArrayList();
		curricularCoursesScopesNeverEnroled.addAll(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(finalSpanBackup);
// ------------------------------------------------------------

		List aux = new ArrayList();
		Iterator iterator1 = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (iterator1.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator1.next();
			int year = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();
			Iterator iterator2 = curricularCoursesScopesNeverEnroled.iterator();
			while (iterator2.hasNext()) {
				ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) iterator2.next();
				if(
					(curricularCourseScope2.getCurricularSemester().getCurricularYear().getYear().intValue() < year) &&
					(!enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().contains(curricularCourseScope2)) &&
					(!enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().contains(curricularCourseScope2))
				) {
					aux.add(curricularCourseScope);
					break;
				}
			}
		}
		enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(aux);
		return enrolmentContext;
	}
}