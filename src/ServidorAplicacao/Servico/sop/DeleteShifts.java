/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 **/
import java.util.List;

import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteShifts implements IServico {

	private static DeleteShifts _servico = new DeleteShifts();
	/**
	 * The singleton access method of this class.
	 **/
	public static DeleteShifts getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private DeleteShifts() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "DeleteShifts";
	}

	public Object run(List shiftOIDs) throws FenixServiceException {

		boolean result = false;

		for (int j = 0; j < shiftOIDs.size(); j++) {
			Integer shiftOID = (Integer) shiftOIDs.get(j);

			try {
				final ISuportePersistente sp =
					SuportePersistenteOJB.getInstance();

				ITurno shift =
					(ITurno) sp.getITurnoPersistente().readByOID(
						Turno.class,
						shiftOID);

				if (shift != null) {
					for (int i = 0;
						i < shift.getAssociatedLessons().size();
						i++) {
						sp.getIAulaPersistente().delete(
							(IAula) shift.getAssociatedLessons().get(i));
					}

					for (int i = 0;
						i < shift.getAssociatedClasses().size();
						i++) {
						ITurma schoolClass = (ITurma) shift.getAssociatedClasses().get(i);
						sp.getITurmaPersistente().simpleLockWrite(schoolClass);
						schoolClass.getAssociatedShifts().remove(shift);
					}

					sp.getITurnoPersistente().delete(shift);

					result = true;
				}
			} catch (ExcepcaoPersistencia ex) {
				throw new FenixServiceException("Error deleting shift");
			}
		}

		return new Boolean(result);

	}

}