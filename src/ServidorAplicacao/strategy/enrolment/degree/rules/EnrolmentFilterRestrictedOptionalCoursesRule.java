package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IPossibleCurricularCourseForOptionalCurricularCourse;
import Dominio.PossibleCurricularCourseForOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentChosenCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterRestrictedOptionalCoursesRule
	implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		try {
			ICurricularCourse chosenOptionalCurricularCourse =	enrolmentContext.getChosenOptionalCurricularCourseScope().getCurricularCourse();
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentChosenCurricularCourseForOptionalCurricularCourse persistentChosenCurricularCourseForOptionalCurricularCourse =	sp.getIPersistentChosenCurricularCourseForOptionalCurricularCourse();
			IPossibleCurricularCourseForOptionalCurricularCourse chosenCurricularCourseForOptionalCurricularCourse =new PossibleCurricularCourseForOptionalCurricularCourse();
			chosenCurricularCourseForOptionalCurricularCourse.setOptionalCurricularCourse(chosenOptionalCurricularCourse);
			List possibleCurricularCourseForOptionalCurricularCourseList = persistentChosenCurricularCourseForOptionalCurricularCourse.readByCriteria(chosenCurricularCourseForOptionalCurricularCourse);

			List opionalCurricularCoursesEnrolmentsList = (List) CollectionUtils.collect(enrolmentContext.getOptionalCurricularCoursesEnrolments(), new Transformer() {
				public Object transform(Object obj) {
					IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) obj;
					return enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();
				}
			});

			List possibleCurricularCourses = new ArrayList();
			Iterator iterator =	possibleCurricularCourseForOptionalCurricularCourseList.iterator();
			while (iterator.hasNext()) {
				IPossibleCurricularCourseForOptionalCurricularCourse possibleCurricularCourseForOptionalCurricularCourse = (IPossibleCurricularCourseForOptionalCurricularCourse) iterator.next();
				ICurricularCourse curricularCourse = possibleCurricularCourseForOptionalCurricularCourse.getPossibleCurricularCourse();
				if (!opionalCurricularCoursesEnrolmentsList.contains(curricularCourse)) {
					possibleCurricularCourses.add(curricularCourse);
				}
			}
			enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(possibleCurricularCourses);
			return enrolmentContext;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from database");
		}
	}
}