/*
 * AdicionarAula.java
 *
 * Created on 26 de Outubro de 2002, 19:26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o AdicionarAula.
 *
 * @author tfc130
 **/
import java.util.List;

import DataBeans.InfoShiftServiceResult;
import DataBeans.ShiftAndLessonKeys;
import Dominio.IAula;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.Turno;
import Dominio.TurnoAula;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import java.util.Calendar;
import Util.TipoAula;

public class AdicionarAula implements IServico {

	private static AdicionarAula _servico = new AdicionarAula();
	/**
	 * The singleton access method of this class.
	 **/
	public static AdicionarAula getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private AdicionarAula() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "AdicionarAula";
	}

	public Object run(ShiftAndLessonKeys keysTurnoAndAula) {

		ITurnoAula turnoAula = null;
		InfoShiftServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurno turno1 =
				sp.getITurnoPersistente().readByNome(
					keysTurnoAndAula.getNomeTurno());
			ISala sala1 =
				sp.getISalaPersistente().readByName(
					keysTurnoAndAula.getKeySala().getNomeSala());
			IAula aula1 =
				sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(
					keysTurnoAndAula.getDiaSemana(),
					keysTurnoAndAula.getInicio(),
					keysTurnoAndAula.getFim(),
					sala1);

			turnoAula = new TurnoAula(turno1, aula1);

			result = valid(turno1, aula1);

			if (result.isSUCESS())
				sp.getITurnoAulaPersistente().lockWrite(turnoAula);

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private InfoShiftServiceResult valid(ITurno shift, IAula lesson)
		throws ExcepcaoPersistencia {
		InfoShiftServiceResult result = new InfoShiftServiceResult();
		result.setMessageType(InfoShiftServiceResult.SUCESS);

		double hours = getTotalHoursOfShiftType(shift);
		double lessonDuration =
			(getLessonDurationInMinutes(lesson).doubleValue()) / 60;

		System.out.println(
			" Numero de horas de turno = "
				+ hours
				+ " Numero maximo de horas = "
				+ shift
					.getDisciplinaExecucao()
					.getTheoreticalHours()
					.doubleValue());
		System.out.println(
			" Horas de turno + duracao aula = " + (hours + lessonDuration));

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

	private double getTotalHoursOfShiftType(ITurno shift)
		throws ExcepcaoPersistencia {
		ITurno shiftCriteria = new Turno();
		shiftCriteria.setNome(shift.getNome());
		shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());

		List lessonsOfShiftType =
			SuportePersistenteOJB
				.getInstance()
				.getITurnoAulaPersistente()
				.readLessonsByShift(shiftCriteria);

		//		List lessonsOfShiftType =
		//			SuportePersistenteOJB
		//				.getInstance()
		//				.getITurnoAulaPersistente()
		//				.readByCriteria(
		//				new TurnoAula(shiftCriteria, null));

		IAula lesson = null;
		double duration = 0;
		System.out.println(
			"Aulas de turno "
				+ shift.getNome()
				+ "= "
				+ lessonsOfShiftType.size());
		for (int i = 0; i < lessonsOfShiftType.size(); i++) {
			lesson = ((ITurnoAula)lessonsOfShiftType.get(i)).getAula();
			duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
			//			System.out.println("Total horas aulas do turno = " + duration);			
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
		System.out.println(
			"AULA - INICIO = "
				+ beginHour
				+ ":"
				+ beginMinutes
				+ " / FIM = "
				+ endHour
				+ ":"
				+ endMinutes
				+ " - DURACAO = "
				+ duration);
		return new Integer(duration);
	}

}