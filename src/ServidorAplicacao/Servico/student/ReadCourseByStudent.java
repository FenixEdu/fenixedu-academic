package ServidorAplicacao.Servico.student;

import java.util.ArrayList;

import DataBeans.InfoCourse;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 *
 */

public class ReadCourseByStudent implements IServico {

	private static ReadCourseByStudent _servico =
		new ReadCourseByStudent();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCourseByStudent getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCourseByStudent() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadCourseByStudent";
	}

	public Object run(Integer number,TipoCurso degreeType) {


		ArrayList disciplines = new ArrayList();
		InfoCourse course=null;

		System.out.println("chegueiServicoCourse");
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudent student = sp.getIPersistentStudent().readByNumero(number,degreeType);

			if (student != null) {
				
				System.out.println("passei1");
					int numero=number.intValue();
					System.out.println("passei2");
					IStudentCurricularPlan StudentCurricularPlan=sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(numero);
					System.out.println("passei3");
					if (StudentCurricularPlan != null) 
						System.out.println(StudentCurricularPlan.getCourseCurricularPlan().getCurso().getNome().toString());
					else
						System.out.println("vazio");
							
					System.out.println("passei4");
					if (StudentCurricularPlan != null) {
								course = new InfoCourse(
											StudentCurricularPlan.getCourseCurricularPlan().getCurso().getSigla(),
											StudentCurricularPlan.getCourseCurricularPlan().getCurso().getNome(),
											StudentCurricularPlan.getCourseCurricularPlan().getCurso().getTipoCurso()
											);
							}		
					
				  }
				  
			  System.out.println("acabeiCicloServicoCourse");
			
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		return course;

	}

}

