package ServidorAplicacao.strategy.enrolment.rules.depercated;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentValidationResult;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to validate if the student
 * has chosen to be enrolled in all the curricular courses of the curricular years
 * below, before he can choose to be enrolled in curricular courses of later
 * curricular years.
 */

public class EnrolmentValidateCurricularYearPrecedenceRule //implements IEnrolmentRule
{

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		
		int year = 0;
		int year2 = 0;

		Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			year2 = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();
			if (year2 > year) {
				year = year2;
			}
		}

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
			
			year2 = 0;
			iterator = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
			while (iterator.hasNext()) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator.next();
//				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(enrolmentInOptionalCurricularCourse.getCurricularCourseScope().getCurricularCourse());
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(enrolmentInOptionalCurricularCourse.getCurricularCourse(), false);
				year2 =  ((ICurricularCourseScope) curricularCourse.getScopes().get(0)).getCurricularSemester().getCurricularYear().getYear().intValue();
				if (year2 > year) {
					year = year2;
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new IllegalStateException("Cannot read from database");
		}

		if(year == 1) {
			return enrolmentContext;
		}

		List curricularCoursesOfOption = (List) CollectionUtils.collect(enrolmentContext.getOptionalCurricularCoursesEnrolments(), new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourse());
			}
		});

		for(int i = (year - 1); i > 0; i--) {
			final int j = i;
			List precedentCurricularCourseScopes = (List) CollectionUtils.select(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled(), new Predicate() {
				public boolean evaluate(Object obj) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
					return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() == j;
				}
			});
					
			iterator = precedentCurricularCourseScopes.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
//				ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
				if( (!enrolmentContext.getActualEnrolments().contains(curricularCourseScope)) && (!curricularCoursesOfOption.contains(curricularCourseScope.getCurricularCourse())) ){
					enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.MUST_ENROLL_IN_EARLIER_CURRICULAR_COURSES);
					return enrolmentContext;
				}				
			}
		}
		return enrolmentContext;
	}
}