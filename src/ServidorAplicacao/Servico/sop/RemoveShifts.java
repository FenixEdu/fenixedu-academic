/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço AdicionarTurno.
 *
 * @author Luis Cruz & Sara Ribeiro
 **/
import java.util.List;

import DataBeans.InfoClass;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoveShifts implements IServico {

	private static RemoveShifts _servico = new RemoveShifts();
	/**
	 * The singleton access method of this class.
	 **/
	public static RemoveShifts getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private RemoveShifts() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "RemoveShifts";
	}

	public Boolean run(InfoClass infoClass, List shiftOIDs)
		throws FenixServiceException {

		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurma schoolClass =
				(ITurma) sp.getITurmaPersistente().readByOID(
					Turma.class,
					infoClass.getIdInternal());			
			sp.getITurmaPersistente().simpleLockWrite(schoolClass);

			for (int i = 0; i < shiftOIDs.size(); i++) {
				ITurno shift =
					(ITurno) sp.getITurnoPersistente().readByOID(
						Turno.class,
						(Integer) shiftOIDs.get(i));
				ITurmaTurno classShift = sp.getITurmaTurnoPersistente().readByTurmaAndTurno(
										schoolClass,
										shift);
				if (classShift != null) {
					sp.getITurmaTurnoPersistente().delete(classShift);
				}
				schoolClass.getAssociatedShifts().remove(shift);
				sp.getITurmaTurnoPersistente().simpleLockWrite(shift);
				shift.getAssociatedClasses().remove(schoolClass);
			}

			result = true;
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return new Boolean(result);
	}

}