package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentRuleBranch implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		SuportePersistenteOJB persistentSupport = null;
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
		IPersistentCurricularCourse persistentCurricularCourse = null;
		IPersistentBranch persistentBranch = null;

		IBranch withBranch = null;
		List coursesWithBranch = null;
		IBranch withoutBranch = null;
		List coursesWithoutBranch = null;
		IStudentCurricularPlan studentCurricularPlan = null;

		List curricularCoursesFromStudentCurricularPlan = new ArrayList();

		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentBranch = persistentSupport.getIPersistentBranch();

		studentCurricularPlan =
			persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
				enrolmentContext.getStudent().getNumber(),
				enrolmentContext.getStudent().getDegreeType());
				
		withBranch = studentCurricularPlan.getBranch();
		coursesWithBranch = persistentCurricularCourse.readAllCurricularCoursesByBranch(withBranch);
		withoutBranch = persistentBranch.readBranchByNameAndCode("", "");
		coursesWithoutBranch = persistentCurricularCourse.readAllCurricularCoursesByBranch(withoutBranch);

		ICurricularCourse curricularCourse = null;
		Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			curricularCourse = (ICurricularCourse) iterator.next();
			if ((coursesWithBranch.contains(curricularCourse)) || (coursesWithoutBranch.contains(curricularCourse))) {
				curricularCoursesFromStudentCurricularPlan.add(curricularCourse);
			}
		}

		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(curricularCoursesFromStudentCurricularPlan);
		return enrolmentContext;
	}
}