/*
 * ApagarTurno.java
 *
 * Created on 27 de Outubro de 2002, 18:24
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço ApagarTurno.
 *
 * @author tfc130
 **/
import DataBeans.InfoExecutionCourse;
import DataBeans.ShiftKey;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ApagarTurno implements IServico {

	private static ApagarTurno _servico = new ApagarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static ApagarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ApagarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ApagarTurno";
	}

	public Object run(ShiftKey keyTurno, InfoExecutionCourse IEC) {

		ITurno turno1 = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao IDE =
				new DisciplinaExecucao(
					IEC.getNome(),
					IEC.getSigla(),
					IEC.getPrograma(),
					new CursoExecucao(
						IEC.getInfoLicenciaturaExecucao().getAnoLectivo(),
						new Curso(
							IEC
								.getInfoLicenciaturaExecucao()
								.getInfoLicenciatura()
								.getSigla(),
							IEC
								.getInfoLicenciaturaExecucao()
								.getInfoLicenciatura()
								.getNome(),
							null)),
					IEC.getTheoreticalHours(),
					IEC.getPraticalHours(),
					IEC.getTheoPratHours(),
					IEC.getLabHours());

			turno1 =
				sp.getITurnoPersistente().readByNomeAndExecutionCourse(
					keyTurno.getShiftName(),
					IDE);
			if (turno1 != null) {
				sp.getITurnoPersistente().delete(turno1);
				result = true;
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return new Boolean(result);
	}

}