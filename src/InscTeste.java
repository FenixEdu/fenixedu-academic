import Dominio.IStudent;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
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
		IPersistentStudent persistentStudent = null;
//		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;

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
			
			IStudent student = null;

//			IStudentCurricularPlan studentActiveCurricularPlan = null;

			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentStudent = persistentSupport.getIPersistentStudent();

//			persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			persistentSupport.iniciarTransaccao();


			student = persistentStudent.readStudentByNumberAndDegreeType(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));

			EnrolmentContext enrolmentContext = EnrolmentContextManager.initialEnrolmentContext(student, new Integer(1));
			

			strategyLERCI = EnrolmentStrategyFactory.getEnrolmentStrategyInstance(enrolmentContext);
			enrolmentContext = strategyLERCI.getAvailableCurricularCourses();

			persistentSupport.confirmarTransaccao();
			System.out.println(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
	}
}