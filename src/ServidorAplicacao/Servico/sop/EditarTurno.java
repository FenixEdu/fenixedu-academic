/*
 * EditarTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:00
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarTurno.
 *
 * @author tfc130
 **/
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarTurno implements IServico {

	private static EditarTurno _servico = new EditarTurno();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditarTurno getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditarTurno() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditarTurno";
	}

	public Object run(InfoShift infoShiftOld, InfoShift infoShiftNew)
		throws FenixServiceException {

		InfoShift infoShift = null;

		// TODO : if type and/or execution course change, then verifications must be made
		//        relative to the validity of the hours of associated lessons for the
		//        corresponding execution course.

		try {
			ISuportePersistente sp;
				sp = SuportePersistenteOJB.getInstance();

			ITurno shift = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class, infoShiftOld.getIdInternal());

			sp.getITurnoPersistente().lockWrite(shift);

			shift.setNome(infoShiftNew.getNome());
			shift.setTipo(infoShiftNew.getTipo());
			IDisciplinaExecucao executionCourse = 
				(IDisciplinaExecucao) sp.getIDisciplinaExecucaoPersistente().readByOID(DisciplinaExecucao.class, infoShiftOld.getInfoDisciplinaExecucao().getIdInternal());
			shift.setDisciplinaExecucao(executionCourse);

			infoShift = Cloner.copyShift2InfoShift(shift);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return infoShift;
	}
}