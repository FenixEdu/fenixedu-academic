package ServidorAplicacao.Servico.student;

import java.util.ArrayList;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author jmota
 *
 */
public class ReadShiftsByTypeFromExecutionCourse implements IServico {

	private static ReadShiftsByTypeFromExecutionCourse _servico =
		new ReadShiftsByTypeFromExecutionCourse();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadShiftsByTypeFromExecutionCourse getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadShiftsByTypeFromExecutionCourse() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadShiftsByTypeFromExecutionCourse";
	}

	public Object run(InfoExecutionCourse iDE, TipoAula type) {
		ArrayList shifts = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ArrayList dshifts =
				sp.getITurnoPersistente().readByDisciplinaExecucaoAndType(
					iDE.getSigla(),
					iDE.getInfoLicenciaturaExecucao().getAnoLectivo(),
					iDE
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura()
						.getSigla(),
						 type.getTipo());

			if (dshifts != null)
				for (int i = 0; i < dshifts.size(); i++) {
					ITurno dshift = (ITurno) dshifts.get(i);
					InfoShift shift =
						new InfoShift(
							dshift.getNome(),
							dshift.getTipo(),
							dshift.getLotacao(),
							iDE);
					shifts.add(shift);
				}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		return shifts;

	}

}
