package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentRuleNAC implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		SuportePersistenteOJB persistentSupport = null;
		IPersistentCurricularCourse persistentCurricularCourse = null;
		IStudentCurricularPlan studentCurricularPlan = null;
		
		IBranch branch = null;
		List curricularCoursesBySemesterAndYearAndBranch = null;
		List possibleCurricularCourses = new ArrayList();

		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		
		studentCurricularPlan = enrolmentContext.getStudentActiveCurricularPlan();
				
		branch = studentCurricularPlan.getBranch();

		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;
		ICurricularCourse curricularCourse = null;
		Iterator iterator = null;
		
		while ( (possibleND < 7) && (year < 6) ) {
			if ( (year == 1) || (year == 2) ){
				curricularCoursesBySemesterAndYearAndBranch = persistentCurricularCourse.readCurricularCoursesBySemesterAndYear(enrolmentContext.getSemester(), new Integer(year));
			}	else{
			curricularCoursesBySemesterAndYearAndBranch = persistentCurricularCourse.readCurricularCoursesBySemesterAndYearAndBranch(enrolmentContext.getSemester(), new Integer(year),branch);
			}
			
//			System.out.println(curricularCoursesBySemesterAndYearAndBranch);
			
			iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
			while (iterator.hasNext()) {
				curricularCourse = (ICurricularCourse) iterator.next();
				if (curricularCoursesBySemesterAndYearAndBranch.contains(curricularCourse)) {
					possibleCurricularCourses.add(curricularCourse);
				}
			}
			possibleND = possibleCurricularCourses.size();
			year++;
		}

		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(possibleCurricularCourses);
		//		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled();
		return enrolmentContext;
	}
}