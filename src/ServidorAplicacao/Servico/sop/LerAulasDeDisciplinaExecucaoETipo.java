/*
 * LerAulasDeDisciplinaExecucaoETipo.java
 *
 * Created on 28 de Outubro de 2002, 18:03
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucaoETipo.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.util.Cloner;

public class LerAulasDeDisciplinaExecucaoETipo implements IServico {

	private static LerAulasDeDisciplinaExecucaoETipo _servico =
		new LerAulasDeDisciplinaExecucaoETipo();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerAulasDeDisciplinaExecucaoETipo getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerAulasDeDisciplinaExecucaoETipo() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerAulasDeDisciplinaExecucaoETipo";
	}

	public Object run(ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao, InfoExecutionCourse infoExecutionCourse) {

		ArrayList infoAulas = null;

		try {
			
			IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			List aulas =
				sp.getIAulaPersistente().readByDisciplinaExecucaoETipo(
					executionCourse,
					tipoAulaAndKeyDisciplinaExecucao.getTipoAula());

			Iterator iterator = aulas.iterator();
			infoAulas = new ArrayList();
			while (iterator.hasNext()) {
				IAula lesson = (IAula) iterator.next();
				InfoRoom infoSala =
					new InfoRoom(
						lesson.getSala().getNome(),
						lesson.getSala().getEdificio(),
						lesson.getSala().getPiso(),
						lesson.getSala().getTipo(),
						lesson.getSala().getCapacidadeNormal(),
						lesson.getSala().getCapacidadeExame());

				InfoDegree infoLicenciatura =
					new InfoDegree(
						lesson
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getCurso()
							.getSigla(),
						lesson
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getCurso()
							.getNome());

				InfoExecutionDegree infoLicenciaturaExecucao =
					new InfoExecutionDegree(
						lesson
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getAnoLectivo(),
						infoLicenciatura);
				InfoExecutionCourse infoDisciplinaExecucao =
					new InfoExecutionCourse(
						lesson.getDisciplinaExecucao().getNome(),
						lesson.getDisciplinaExecucao().getSigla(),
						lesson.getDisciplinaExecucao().getPrograma(),
						infoLicenciaturaExecucao,
						lesson.getDisciplinaExecucao().getTheoreticalHours(),
						lesson.getDisciplinaExecucao().getPraticalHours(),
						lesson.getDisciplinaExecucao().getTheoPratHours(),
						lesson.getDisciplinaExecucao().getLabHours());
				infoAulas.add(
					new InfoLesson(
						lesson.getDiaSemana(),
						lesson.getInicio(),
						lesson.getFim(),
						lesson.getTipo(),
						infoSala,
						infoDisciplinaExecucao));
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoAulas;
	}

}