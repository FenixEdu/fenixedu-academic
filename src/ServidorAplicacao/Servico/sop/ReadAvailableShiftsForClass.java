/*
 * 
 * Created on 2003/08/13
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 **/
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoClass;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAvailableShiftsForClass implements IServico {

	private static ReadAvailableShiftsForClass _servico =
		new ReadAvailableShiftsForClass();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAvailableShiftsForClass getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAvailableShiftsForClass() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadAvailableShiftsForClass";
	}

	public Object run(InfoClass infoClass) throws FenixServiceException {

		List infoShifts = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurma schoolClass =
				(ITurma) sp.getITurmaPersistente().readByOID(
					Turma.class,
					infoClass.getIdInternal());

			List shifts =
				sp.getITurnoPersistente().readAvailableShiftsForClass(schoolClass);		
			
			infoShifts =
				(List) CollectionUtils.collect(shifts, new Transformer() {
				public Object transform(Object arg0) {
					ITurno shift = (ITurno) arg0;
					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
					infoShift
						.setInfoLessons(
							(List) CollectionUtils
							.collect(
								shift.getAssociatedLessons(),
								new Transformer() {
						public Object transform(Object arg0) {
							InfoLesson infoLesson = Cloner.copyILesson2InfoLesson((IAula) arg0);
//							ITurno shift = ((IAula) arg0).getShift();
//							InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
//							infoLesson.setInfoShift(infoShift);
							return infoLesson;
						}
					}));
					return infoShift;
				}
			});
			
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return infoShifts;
	}

}