/*
 * Created on 1/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.sop;

import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 1/Jul/2003
 * fenix-branch
 * ServidorAplicacao.Servico.sop
 * 
 */
public class AddShiftToClasses implements IServico {

	/**
	 * 
	 */
	public AddShiftToClasses() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "AddShiftToClasses";
	}

	private static AddShiftToClasses service = new AddShiftToClasses();

	public static AddShiftToClasses getService() {
		return service;
	}

	public void run(Integer keyShift, String[] classesList)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ITurnoPersistente persistentShift = sp.getITurnoPersistente();
			ITurmaPersistente persistentClass = sp.getITurmaPersistente();
			ITurmaTurnoPersistente persistentClassShift =
				sp.getITurmaTurnoPersistente();
			ITurno shift = new Turno(keyShift);
			shift = (ITurno) persistentShift.readByOId(shift, false);
			if (shift == null || classesList == null) {
				throw new InvalidArgumentsServiceException();
			}
			int iter = 0;
			int length = classesList.length;
			while (iter < length) {
				Integer keyClass = new Integer(classesList[iter]);
				ITurma dClass = new Turma(keyClass);
				dClass = (ITurma) persistentClass.readByOId(dClass, false);
				if (dClass == null) {
					throw new InvalidArgumentsServiceException();
				}
				if (persistentClassShift.readByTurmaAndTurno(dClass, shift)
					== null) {
					ITurmaTurno classShift = new TurmaTurno();
					classShift.setTurma(dClass);
					classShift.setTurno(shift);
					persistentClassShift.lockWrite(classShift);

				}
				iter++;
			}

		} catch (ExcepcaoPersistencia e) {

		}

	}

}
