package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurno
 *
 * @author tfc130
 * @version
 **/

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurno implements IServico {

	private static LerTurno _servico = new LerTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerTurno";
	}

	public Object run(ShiftKey keyTurno) {

		InfoShift infoTurno = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			ICursoExecucao executionDegree = new CursoExecucao();
			ICurso degree = new Curso();
			InfoExecutionCourse infoExecutionCourse =
				keyTurno.getInfoExecutionCourse();
			try {
				BeanUtils.copyProperties(executionCourse, infoExecutionCourse);
				BeanUtils.copyProperties(
					executionDegree,
					infoExecutionCourse.getInfoLicenciaturaExecucao());
				BeanUtils.copyProperties(
					degree,
					infoExecutionCourse
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura());
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw new RuntimeException(e.getMessage());
			}

			executionCourse.setLicenciaturaExecucao(executionDegree);
			executionDegree.setCurso(degree);

			ITurno turno =
				sp.getITurnoPersistente().readByNomeAndExecutionCourse(
					keyTurno.getShiftName(),
					executionCourse);

			if (turno != null) {
				InfoDegree infoLicenciatura =
					new InfoDegree(
						turno
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getCurso()
							.getSigla(),
						turno
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getCurso()
							.getNome());
				InfoExecutionDegree infoLicenciaturaExecucao =
					new InfoExecutionDegree(
						turno
							.getDisciplinaExecucao()
							.getLicenciaturaExecucao()
							.getAnoLectivo(),
						infoLicenciatura);
				InfoExecutionCourse infoDisciplinaExecucao =
					new InfoExecutionCourse(
						turno.getDisciplinaExecucao().getNome(),
						turno.getDisciplinaExecucao().getSigla(),
						turno.getDisciplinaExecucao().getPrograma(),
						infoLicenciaturaExecucao,
						turno.getDisciplinaExecucao().getTheoreticalHours(),
						turno.getDisciplinaExecucao().getPraticalHours(),
						turno.getDisciplinaExecucao().getTheoPratHours(),
						turno.getDisciplinaExecucao().getLabHours());
				infoTurno =
					new InfoShift(
						turno.getNome(),
						turno.getTipo(),
						turno.getLotacao(),
						infoDisciplinaExecucao);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoTurno;
	}

}