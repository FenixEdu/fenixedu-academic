package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class SelectExecutionCourse implements IServico {

	private static SelectExecutionCourse _servico = new SelectExecutionCourse();

	/**
	  * The actor of this class.
	  **/

	private SelectExecutionCourse() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SelectExecutionCourse";
	}

	/**
	 * Returns the _servico.
	 * @return SelectExecutionCourse
	 */
	public static SelectExecutionCourse getService() {
		return _servico;
	}

	public Object run(
		InfoExecutionCourse infoExecutionCourse,
		Integer curricularYear) {

		
		List infoExecutionCourses = new ArrayList();
		IDisciplinaExecucao exeCourse = new DisciplinaExecucao();
		

			
		

		try {
			List executionCourses = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			executionCourses =
				sp
					.getIDisciplinaExecucaoPersistente()
					.readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(
					curricularYear,
						infoExecutionCourse
							.getInfoLicenciaturaExecucao()
							.getAnoLectivo(),
						infoExecutionCourse.getSemester(),
						infoExecutionCourse
							.getInfoLicenciaturaExecucao()
							.getInfoLicenciatura()
							.getSigla());

			//readByCriteria(
			//	exeCourse);

			for (int i = 0; i < executionCourses.size(); i++) {
				IDisciplinaExecucao aux =
					(IDisciplinaExecucao) executionCourses.get(i);
				infoExecutionCourses.add(
					new InfoExecutionCourse(
						aux.getNome(),
						aux.getSigla(),
						aux.getPrograma(),
						new InfoExecutionDegree(
							aux.getLicenciaturaExecucao().getAnoLectivo(),
							new InfoDegree(
								aux
									.getLicenciaturaExecucao()
									.getCurso()
									.getSigla(),
								aux
									.getLicenciaturaExecucao()
									.getCurso()
									.getNome())),
						aux.getTheoreticalHours(),
						aux.getPraticalHours(),
						aux.getTheoPratHours(),
						aux.getLabHours()));
			}

		} catch (ExcepcaoPersistencia e) {

			e.printStackTrace();
		}

		return infoExecutionCourses;

	}

}
