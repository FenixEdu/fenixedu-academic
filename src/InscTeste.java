import java.util.ArrayList;

import Dominio.IBranch;
import Dominio.ICurso;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 31/Mar/2003
 */

public class InscTeste {

	public static void main(String[] args) {

		SuportePersistenteOJB persistentSupport = null;
		IPersistentBranch persistentBranch = null;
		IPersistentStudent persistentStudent = null;
		ICursoPersistente persistentDegree = null;
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
		ArrayList list = null;
		IBranch branch = null;

		//		try {
		//			persistentSupport = SuportePersistenteOJB.getInstance();
		//			persistentBranch = persistentSupport.getIPersistentBranch();
		//
		//			persistentSupport.iniciarTransaccao();
		//			branch = persistentBranch.readBranchByNameAndCode("ÁREA DE ARQUITECTURA E GESTÃO DE REDES", "AAGR");
		//			persistentSupport.confirmarTransaccao();
		//			
		//			System.out.println(branch.toString());
		//			
		//			Iterator iterator = branch.getScopes().iterator();
		//			while (iterator.hasNext()) {
		//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
		//				ICurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
		//				ICurricularYear curricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();
		//				ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
		//				System.out.print("ANO: " + curricularYear.getYear() + " SEMESTRE: " + curricularSemester.getSemester() + " ");
		//				System.out.println(curricularCourse.toString());
		//			}
		//
		//			persistentSupport.iniciarTransaccao();
		//			branch = persistentBranch.readBranchByNameAndCode("ÁREA DE APLICAÇÕES E SERVIÇOS", "AAS");
		//			persistentSupport.confirmarTransaccao();
		//			
		//			System.out.println(branch.toString());
		//			
		//			iterator = branch.getScopes().iterator();
		//			while (iterator.hasNext()) {
		//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
		//				ICurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
		//				ICurricularYear curricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();
		//				ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
		//				System.out.print("ANO: " + curricularYear.getYear() + " SEMESTRE: " + curricularSemester.getSemester() + " ");
		//				System.out.println(curricularCourse.toString());
		//			}
		//
		//		} catch (ExcepcaoPersistencia e) {
		//			e.printStackTrace();
		//		}

		try {
			IEnrolmentStrategy strategyLERCI = null;
			EnrolmentContext enrolmentContext = new EnrolmentContext();
			IStudent student = null;
			ICurso degree = null;
			IStudentCurricularPlan studentActiveCurricularPlan = null;

			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentStudent = persistentSupport.getIPersistentStudent();
			persistentDegree = persistentSupport.getICursoPersistente();
			persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			persistentSupport.iniciarTransaccao();

			degree = persistentDegree.readBySigla("LERCI");
			student = persistentStudent.readStudentByNumberAndDegreeType(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			
			enrolmentContext.setStudent(student);
			enrolmentContext.setSemester(new Integer(1));
			enrolmentContext.setDegree(degree);
			enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);

			strategyLERCI = EnrolmentStrategyFactory.getEnrolmentStrategyInstance(EnrolmentStrategyFactory.LERCI, enrolmentContext);
			enrolmentContext = strategyLERCI.getAvailableCurricularCourses();

			persistentSupport.confirmarTransaccao();
			System.out.println(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
	}
}