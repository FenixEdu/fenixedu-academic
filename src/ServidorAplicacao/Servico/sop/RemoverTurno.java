package ServidorAplicacao.Servico.sop;

/**
 * Serviço RemoverTurno
 *
 * @author tfc130
 * @version
 **/

import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoverTurno implements IServico {

	private static RemoverTurno _servico = new RemoverTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static RemoverTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private RemoverTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "RemoverTurno";
	}

	public Object run(InfoShift infoShift, InfoClass infoClass) {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurno shift = Cloner.copyInfoShift2IShift(infoShift);
			ITurma classTemp = Cloner.copyInfoClass2Class(infoClass);

			// Read From Database

			ITurno shiftToDelete =
				sp.getITurnoPersistente().readByNameAndExecutionCourse(
					shift.getNome(),
					shift.getDisciplinaExecucao());
			ITurma classToDelete =
				sp
					.getITurmaPersistente()
					.readByNameAndExecutionDegreeAndExecutionPeriod(
					classTemp.getNome(),
					classTemp.getExecutionDegree(),
					classTemp.getExecutionPeriod());
			ITurmaTurno turmaTurnoToDelete = null;
			if ((shiftToDelete != null) && (classToDelete != null)) {
				turmaTurnoToDelete =
					sp.getITurmaTurnoPersistente().readByTurmaAndTurno(
						classToDelete,
						shiftToDelete);
			} else
				return Boolean.FALSE;

			// Check if exists	  
			if (turmaTurnoToDelete != null)
				sp.getITurmaTurnoPersistente().delete(turmaTurnoToDelete);
			else
				return Boolean.FALSE;

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return Boolean.TRUE;
	}

}