/*
 * EditarAula.java
 *
 * Created on 27 de Outubro de 2002, 19:05
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o EditarAula.
 *
 * @author tfc130
 **/
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.InfoShift;
import DataBeans.InfoShiftServiceResult;
import DataBeans.KeyLesson;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class EditLesson implements IServico {

	private static EditLesson _servico = new EditLesson();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditLesson getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditLesson() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditLesson";
	}

	public Object run(
		KeyLesson aulaAntiga,
		InfoLesson aulaNova,
		InfoShift infoShift)
		throws FenixServiceException {

		IAula aula = null;
		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente aulaPersistente = sp.getIAulaPersistente();

			ISala salaAntiga =
				sp.getISalaPersistente().readByName(
					aulaAntiga.getKeySala().getNomeSala());
			ISala salaNova =
				sp.getISalaPersistente().readByName(
					aulaNova.getInfoSala().getNome());

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					aulaNova
						.getInfoDisciplinaExecucao()
						.getInfoExecutionPeriod());

			aula =
				aulaPersistente.readByDiaSemanaAndInicioAndFimAndSala(
					aulaAntiga.getDiaSemana(),
					aulaAntiga.getInicio(),
					aulaAntiga.getFim(),
					salaAntiga,
					executionPeriod);

			IAula newLesson =
				new Aula(
					aulaNova.getDiaSemana(),
					aulaNova.getInicio(),
					aulaNova.getFim(),
					aulaNova.getTipo(),
					salaNova,
					null);
			newLesson.setIdInternal(aula.getIdInternal());

			if (aula != null) {
				result = valid(newLesson);
				if (result.getMessageType() == 1) {
					throw new InvalidTimeIntervalServiceException();
				}
				boolean resultB =
					validNoInterceptingLesson(newLesson, aula, executionPeriod);

				ITurno shift =
					(ITurno) sp.getITurnoPersistente().readByOID(
						Turno.class,
						infoShift.getIdInternal());

				InfoShiftServiceResult infoShiftServiceResult =
					valid(shift, newLesson);

				if (result.isSUCESS() && resultB && infoShiftServiceResult.isSUCESS()) {
					//aula = (IAula) aulaPersistente.readByOId(aula, true);
					aula = (IAula) aulaPersistente.readByOID(Aula.class, aula.getIdInternal());
					aulaPersistente.simpleLockWrite(aula);
					aulaPersistente.simpleLockWrite(shift);
					aula.setDiaSemana(aulaNova.getDiaSemana());
					aula.setInicio(aulaNova.getInicio());
					aula.setFim(aulaNova.getFim());
					aula.setTipo(aulaNova.getTipo());
					aula.setSala(salaNova);
				} else if (!infoShiftServiceResult.isSUCESS()) {
					throw new InvalidLoadException(infoShiftServiceResult.toString());
				}
			}

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}
		return result;
	}

	private InfoLessonServiceResult valid(IAula lesson) {
		InfoLessonServiceResult result = new InfoLessonServiceResult();

		if (lesson.getInicio().getTime().getTime()
			>= lesson.getFim().getTime().getTime()) {
			result.setMessageType(
				InfoLessonServiceResult.INVALID_TIME_INTERVAL);
		}

		return result;
	}

	/**
		   * @param aula
		   * @return InfoLessonServiceResult
		   */
	/*	private boolean validNoInterceptingLesson(IAula lesson) {
	
			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	
				IAulaPersistente persistentLesson = sp.getIAulaPersistente();
	
				List lessonMatchList =
					persistentLesson.readLessonsInBroadPeriod(lesson);
	
				System.out.println("Tenho aulas:" + lessonMatchList.size());
				
				if ((lessonMatchList.size() >0 && !lessonMatchList.contains(lesson)) || (lessonMatchList.size() >1 && lessonMatchList.contains(lesson))) {
					
					return false;
				} else {
					return true;
				}
			} catch (ExcepcaoPersistencia e) {
				return false;
	
			}
		}
	*/

	private boolean validNoInterceptingLesson(
		IAula newLesson,
		IAula oldLesson,
		IExecutionPeriod executionPeriod)
		throws ExistingServiceException, InterceptingServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			List lessonMatchList =
				persistentLesson.readLessonsInBroadPeriod(
					newLesson,
					oldLesson,
					executionPeriod);

			if (lessonMatchList.size() > 0) {
				if (lessonMatchList.contains(newLesson)) {
					throw new ExistingServiceException();
				} else {
					throw new InterceptingServiceException();
				}
			} else {
				return true;
			}
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}

	private InfoShiftServiceResult valid(ITurno shift, IAula lesson)
		throws ExcepcaoPersistencia {
		InfoShiftServiceResult result = new InfoShiftServiceResult();
		result.setMessageType(InfoShiftServiceResult.SUCESS);

		double hours = getTotalHoursOfShiftType(shift, lesson);
		double lessonDuration =
			(getLessonDurationInMinutes(lesson).doubleValue()) / 60;

		if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
			if (hours
				== shift
					.getDisciplinaExecucao()
					.getTheoreticalHours()
					.doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
			else if (
				(hours + lessonDuration)
					> shift
						.getDisciplinaExecucao()
						.getTheoreticalHours()
						.doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
		} else if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
			if (hours
				== shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
			else if (
				(hours + lessonDuration)
					> shift
						.getDisciplinaExecucao()
						.getPraticalHours()
						.doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
		} else if (
			shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
			if (hours
				== shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
			else if (
				(hours + lessonDuration)
					> shift
						.getDisciplinaExecucao()
						.getTheoPratHours()
						.doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
		} else if (
			shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
			if (hours
				== shift.getDisciplinaExecucao().getLabHours().doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
			else if (
				(hours + lessonDuration)
					> shift.getDisciplinaExecucao().getLabHours().doubleValue())
				result.setMessageType(
					InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
		}

		return result;
	}

	private double getTotalHoursOfShiftType(ITurno shift, IAula alteredLesson)
		throws ExcepcaoPersistencia {
		ITurno shiftCriteria = new Turno();
		shiftCriteria.setNome(shift.getNome());
		shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());

		List lessonsOfShiftType =
			SuportePersistenteOJB
				.getInstance()
				.getITurnoAulaPersistente()
				.readLessonsByShift(shiftCriteria);

		IAula lesson = null;
		double duration = 0;
		for (int i = 0; i < lessonsOfShiftType.size(); i++) {
			lesson = ((ITurnoAula) lessonsOfShiftType.get(i)).getAula();
			if (!lesson.getIdInternal().equals(alteredLesson.getIdInternal())) {
				duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
			}

		}
		return duration;
	}

	private Integer getLessonDurationInMinutes(IAula lesson) {
		int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
		int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
		int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
		int endMinutes = lesson.getFim().get(Calendar.MINUTE);
		int duration = 0;

		duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
		return new Integer(duration);
	}

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InvalidLoadException extends FenixServiceException {

		/**
		 * 
		 */
		private InvalidLoadException() {
			super();
		}

		/**
		 * @param s
		 */
		InvalidLoadException(String s) {
			super(s);
		}

	}

}