package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoShift;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class SelectShifts implements IServico {

	private static SelectShifts _servico = new SelectShifts();

	/**
	  * The actor of this class.
	  **/

	private SelectShifts() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SelectShifts";
	}

	/**
	 * Returns the _servico.
	 * @return SelectShifts
	 */
	public static SelectShifts getService() {
		return _servico;
	}

	public Object run(InfoShift infoShift) {
		List shifts = new ArrayList();
		List infoShifts = new ArrayList();
		ITurno shift = new Turno();
		shift.setNome(infoShift.getNome());
		shift.setTipo(infoShift.getTipo());
		shift.setLotacao(infoShift.getLotacao());
		if (infoShift.getInfoDisciplinaExecucao() != null) {
			IDisciplinaExecucao exeCourse = new DisciplinaExecucao();
			exeCourse.setNome(infoShift.getInfoDisciplinaExecucao().getNome());
			exeCourse.setSigla(
				infoShift.getInfoDisciplinaExecucao().getSigla());
			exeCourse.setPrograma(
				infoShift.getInfoDisciplinaExecucao().getPrograma());
			if (infoShift
				.getInfoDisciplinaExecucao()
				.getInfoLicenciaturaExecucao()
				!= null) {
				ICursoExecucao cursoExec = new CursoExecucao();
				cursoExec.setAnoLectivo(
					infoShift
						.getInfoDisciplinaExecucao()
						.getInfoLicenciaturaExecucao()
						.getAnoLectivo());
				if (infoShift
					.getInfoDisciplinaExecucao()
					.getInfoLicenciaturaExecucao()
					.getInfoLicenciatura()
					!= null) {
					ICurso curso = new Curso();
					curso.setNome(
						infoShift
							.getInfoDisciplinaExecucao()
							.getInfoLicenciaturaExecucao()
							.getInfoLicenciatura()
							.getNome());
					curso.setSigla(
						infoShift
							.getInfoDisciplinaExecucao()
							.getInfoLicenciaturaExecucao()
							.getInfoLicenciatura()
							.getSigla());
					cursoExec.setCurso(curso);
				}
				exeCourse.setLicenciaturaExecucao(cursoExec);
			}
			shift.setDisciplinaExecucao(exeCourse);
		}

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			shifts =
				sp.getITurnoPersistente().readByDisciplinaExecucao(
					infoShift.getInfoDisciplinaExecucao().getSigla(),
					infoShift
						.getInfoDisciplinaExecucao()
						.getInfoLicenciaturaExecucao()
						.getAnoLectivo(),
					infoShift
						.getInfoDisciplinaExecucao()
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura()
						.getSigla());
			shifts = sp.getITurnoPersistente().readByCriteria(shift);
			
			for (int i = 0; i < shifts.size(); i++) {
				ITurno taux = (ITurno) shifts.get(i);
				// falta selecionar os turnos pelo ano curricular

				infoShifts.add(
					new InfoShift(
						taux.getNome(),
						taux.getTipo(),
						taux.getLotacao(),
						new InfoExecutionCourse(
							taux.getDisciplinaExecucao().getNome(),
							taux.getDisciplinaExecucao().getSigla(),
							taux.getDisciplinaExecucao().getPrograma(),
							new InfoExecutionDegree(
								taux
									.getDisciplinaExecucao()
									.getLicenciaturaExecucao()
									.getAnoLectivo(),
								new InfoDegree(
									taux
										.getDisciplinaExecucao()
										.getLicenciaturaExecucao()
										.getCurso()
										.getSigla(),
									taux
										.getDisciplinaExecucao()
										.getLicenciaturaExecucao()
										.getCurso()
										.getNome())),
							new Double(0),
							new Double(0),
							new Double(0),
							new Double(0))));

			}
		} catch (ExcepcaoPersistencia e) {
		}

		return infoShifts;
	}

}