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
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

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

		try {
			newShiftIsValid(
				infoShiftOld,
				infoShiftNew.getTipo(),
				infoShiftNew.getInfoDisciplinaExecucao(),
				infoShiftNew.getLotacao());
		} catch (InvalidNewShiftExecutionCourse ex) {
			throw new InvalidNewShiftExecutionCourse();
		} catch (InvalidNewShiftType ex) {
			throw new InvalidNewShiftType();
		} catch (InvalidNewShiftCapacity ex) {
			throw new InvalidNewShiftCapacity();
		}


		try {
			ISuportePersistente sp;
			sp = SuportePersistenteOJB.getInstance();

			ITurno shift =
				(ITurno) sp.getITurnoPersistente().readByOID(
					Turno.class,
					infoShiftOld.getIdInternal());

			sp.getITurnoPersistente().lockWrite(shift);

			shift.setNome(infoShiftNew.getNome());
			shift.setTipo(infoShiftNew.getTipo());
			shift.setLotacao(infoShiftNew.getLotacao());
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) sp
					.getIDisciplinaExecucaoPersistente()
					.readByOID(
					DisciplinaExecucao.class,
					infoShiftOld.getInfoDisciplinaExecucao().getIdInternal());
			shift.setDisciplinaExecucao(executionCourse);

			infoShift = Cloner.copyShift2InfoShift(shift);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return infoShift;
	}

	private void newShiftIsValid(
		InfoShift infoShiftOld,
		TipoAula newShiftType,
		InfoExecutionCourse newShiftExecutionCourse,
		Integer newShiftCapacity)
		throws FenixServiceException {
			
		// 1. Read shift lessons
		List shiftLessons = null;
		ITurno shift = null;
		try {
			ISuportePersistente sp;
			sp = SuportePersistenteOJB.getInstance();
			shift =
				(ITurno) sp.getITurnoPersistente().readByOID(
					Turno.class,
					infoShiftOld.getIdInternal());
			shiftLessons = shift.getAssociatedLessons();
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		// 2. Count shift total duration and get maximum lesson room capacity
		Integer maxCapacity = new Integer(0);
		double shiftDuration = 0;
		for (int i = 0; i < shiftLessons.size(); i++) {
			IAula lesson = ((IAula) shiftLessons.get(i));
			shiftDuration
				+= (getLessonDurationInMinutes(lesson).doubleValue() / 60);
			if (lesson.getSala().getCapacidadeNormal().intValue() > maxCapacity.intValue()) {
				maxCapacity = lesson.getSala().getCapacidadeNormal();
			}
		}

		// 3a. If NEW shift type is diferent from CURRENT shift type
		//     check if shift total duration exceeds new shift type duration 
		if (!newShiftType.equals(infoShiftOld.getTipo())) {
			if (!newShiftTypeIsValid(shift, newShiftType, shiftDuration)) {
				throw new InvalidNewShiftType();
			}
		}

		// 3b. If NEW shift executionCourse is diferent from CURRENT shift executionCourse
		//     check if shift total duration exceeds new executionCourse duration
		if (!newShiftExecutionCourse
			.equals(infoShiftOld.getInfoDisciplinaExecucao())) {
			if (!newShiftExecutionCourseIsValid(shift,
				newShiftExecutionCourse,
				shiftDuration)) {
				throw new InvalidNewShiftExecutionCourse();
			}
		}
		
		// 4. Check if NEW shift capacity is bigger then maximum lesson room capacity
		if (newShiftCapacity.intValue() > maxCapacity.intValue()) {
			throw new InvalidNewShiftCapacity();
		}
		
	}

	private boolean newShiftTypeIsValid(
		ITurno shift,
		TipoAula newShiftType,
		double shiftDuration) {
		// Verify if shift total duration exceeds new shift type duration
		if (newShiftType.equals(new TipoAula(TipoAula.TEORICA))) {
			if (shiftDuration
				> shift
					.getDisciplinaExecucao()
					.getTheoreticalHours()
					.doubleValue()) {
				return false;
			}
		}
		if (newShiftType.equals(new TipoAula(TipoAula.PRATICA))) {
			if (shiftDuration
				> shift
					.getDisciplinaExecucao()
					.getPraticalHours()
					.doubleValue()) {
				return false;
			}
		}
		if (newShiftType.equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
			if (shiftDuration
				> shift
					.getDisciplinaExecucao()
					.getTheoPratHours()
					.doubleValue()) {
				return false;
			}
		}
		if (newShiftType.equals(new TipoAula(TipoAula.LABORATORIAL))) {
			if (shiftDuration
				> shift.getDisciplinaExecucao().getLabHours().doubleValue()) {
				return false;
			}
		}
		return true;
	}

	private boolean newShiftExecutionCourseIsValid(
		ITurno shift,
		InfoExecutionCourse newShiftExecutionCourse,
		double shiftDuration) {

		// Verify if shift total duration exceeds new executionCourse uration
		if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
			if (shiftDuration
				> newShiftExecutionCourse.getTheoreticalHours().doubleValue()) {
				return false;
			}
		}
		if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
			if (shiftDuration
				> newShiftExecutionCourse.getPraticalHours().doubleValue()) {
				return false;
			}
		}
		if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
			if (shiftDuration
				> newShiftExecutionCourse.getTheoPratHours().doubleValue()) {
				return false;
			}
		}
		if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
			if (shiftDuration
				> newShiftExecutionCourse.getLabHours().doubleValue()) {
				return false;
			}
		}
		return true;
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
	public class InvalidNewShiftType extends FenixServiceException {

		/**
		 * 
		 */
		private InvalidNewShiftType() {
			super();
		}

		/**
		 * @param errorType
		 */
		private InvalidNewShiftType(int errorType) {
			super(errorType);
		}

		/**
		 * @param s
		 */
		private InvalidNewShiftType(String s) {
			super(s);
		}

		/**
		 * @param cause
		 */
		private InvalidNewShiftType(Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private InvalidNewShiftType(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InvalidNewShiftExecutionCourse extends FenixServiceException {

		/**
		 * 
		 */
		private InvalidNewShiftExecutionCourse() {
			super();
		}

		/**
		 * @param errorType
		 */
		private InvalidNewShiftExecutionCourse(int errorType) {
			super(errorType);
		}

		/**
		 * @param s
		 */
		private InvalidNewShiftExecutionCourse(String s) {
			super(s);
		}

		/**
		 * @param cause
		 */
		private InvalidNewShiftExecutionCourse(Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private InvalidNewShiftExecutionCourse(
			String message,
			Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */
	public class InvalidNewShiftCapacity extends FenixServiceException {

		/**
		 * 
		 */
		private InvalidNewShiftCapacity() {
			super();
		}

		/**
		 * @param errorType
		 */
		private InvalidNewShiftCapacity(int errorType) {
			super(errorType);
		}

		/**
		 * @param s
		 */
		private InvalidNewShiftCapacity(String s) {
			super(s);
		}

		/**
		 * @param cause
		 */
		private InvalidNewShiftCapacity(Throwable cause) {
			super(cause);
		}

		/**
		 * @param message
		 * @param cause
		 */
		private InvalidNewShiftCapacity(
			String message,
			Throwable cause) {
			super(message, cause);
		}

	}

}