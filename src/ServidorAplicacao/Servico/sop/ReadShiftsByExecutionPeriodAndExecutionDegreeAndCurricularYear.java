/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/09
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 **/
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.CurricularYear;
import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.IAula;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear
	implements IServico {

	private static ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear _servico =
		new ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear";
	}

	public Object run(
		InfoExecutionPeriod infoExecutionPeriod,
		InfoExecutionDegree infoExecutionDegree,
		InfoCurricularYear infoCurricularYear)
		throws FenixServiceException {

		List infoShifts = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				(IExecutionPeriod) sp
					.getIPersistentExecutionPeriod()
					.readByOID(
					ExecutionPeriod.class,
					infoExecutionPeriod.getIdInternal());

			ICursoExecucao executionDegree =
				(ICursoExecucao) sp.getICursoExecucaoPersistente().readByOID(
					CursoExecucao.class,
					infoExecutionDegree.getIdInternal());

			ICurricularYear curricularYear =
				(ICurricularYear) sp.getIPersistentCurricularYear().readByOID(
					CurricularYear.class,
					infoCurricularYear.getIdInternal());

			List shifts =
				sp
					.getITurnoPersistente()
					.readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
						executionPeriod,
						executionDegree,
						curricularYear);

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
							return Cloner.copyILesson2InfoLesson((IAula) arg0);
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