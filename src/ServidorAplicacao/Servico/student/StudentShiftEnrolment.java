/**
 * Project Sop 
 * 
 * Package ServidorAplicacao.Servico.sop
 * 
 * Created on 18/Dez/2003
 *
 */
package ServidorAplicacao.Servico.student;

import DataBeans.InfoEnrolmentServiceResult;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.TurnoAluno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EMail;

/**
 * @author jpvl
 *
 * 
 */
public class StudentShiftEnrolment implements IServico {
	private static StudentShiftEnrolment _service = new StudentShiftEnrolment();

	private StudentShiftEnrolment() {}

	public static StudentShiftEnrolment getService() {
		return _service;
	}
	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome() {
		return "StudentShiftEnrolment";
	}

	/**
	 * Works with TURNO_ALUNO table.
	 * Inserts and updates table.
	 * 
	 * */
	public Object run(InfoStudent infoStudent, InfoShift newShiftInfo)
		throws FenixServiceException {
		if (infoStudent == null){
			throw new IllegalArgumentException("InfoStudent must be not null!");
		}
		if (newShiftInfo == null){
			throw new IllegalArgumentException("Shift info must be not null!");
		}
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			/*
			 * Get student from database.
			 */

			IStudent student = getStudent(infoStudent, sp);
			ITurno newShift = getShift(newShiftInfo, sp);

			verifyEnrolmentInExecutionCourse(student, newShift, sp);
			setShiftEnrolment(student, newShift, sp);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e.getMessage());
		} catch (MessageException e) {
			return e.getResult();
		}
		return new InfoEnrolmentServiceResult(
			InfoEnrolmentServiceResult.ENROLMENT_SUCESS);

	}
	/**
	 * Method getShiftEnrolment.
	 * @param student
	 * @param newShiftInfo
	 * @param sp
	 * @return ITurnoAluno
	 */
	private void setShiftEnrolment(
		IStudent student,
		ITurno newShift,
		ISuportePersistente sp)
		throws ExcepcaoPersistencia, MessageException {

		ITurnoAlunoPersistente studentShiftDAO = sp.getITurnoAlunoPersistente();

		ITurnoAluno shiftStudent = null;
		/*
		 *  Get the shift with the same type of lesson. 
		 */

		ITurno shiftEnroled =
			studentShiftDAO.readByStudentIdAndShiftType(
				student.getNumber(),
				newShift.getTipo(),
				newShift.getDisciplinaExecucao().getNome());

		if (shiftEnroled == null) {
			/*
			 * Student isn't already enroled in this lesson type.
			 */
			shiftStudent = new TurnoAluno(newShift, student);
		} else {
			shiftStudent =
				studentShiftDAO.readByTurnoAndAluno(shiftEnroled, student);
			shiftStudent.setTurno(newShift);
		}
		studentShiftDAO.lockWrite(shiftStudent);
	}
	/**
	 * Method getShift.
	 * @param infoNewShift
	 * @param sp
	 * @return ITurno
	 */
	private ITurno getShift(InfoShift infoNewShift, ISuportePersistente sp)
		throws ExcepcaoPersistencia, MessageException {
		EMail email = new EMail("localhost", "sop_fenix@ist.utl.pt");
		double percentage = 0.80;
			
			
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoNewShift.getInfoDisciplinaExecucao());
		ITurno shift = sp.getITurnoPersistente().readByNameAndExecutionCourse(infoNewShift.getNome(), executionCourse);
			//sp.getITurnoPersistente().readByNome(infoNewShift.getNome());

		if (shift == null) {
			throw new MessageException(
				InfoEnrolmentServiceResult.NON_EXISTING_SHIFT);
		}
		int shiftOcupation =
			sp.getITurnoAlunoPersistente().readByShift(shift).size();
		if (shiftOcupation == shift.getLotacao().intValue()) {
			throw new MessageException(InfoEnrolmentServiceResult.SHIFT_FULL);
		} else {
			if (shiftOcupation == shift.getLotacao().intValue() - 1)
				email.send("lepc@mega.ist.utl.pt",
						   "AVISO - Lotação de turno atingida",
							"A lotação do turno " + shift.getNome() + "foi atingido.");
			if (shiftOcupation == Math.round(shift.getLotacao().intValue() * percentage))
				email.send("lepc@mega.ist.utl.pt",
						   "AVISO - Lotação de turno atingui os " + percentage + "%",
							"A lotação do turno " + shift.getNome() + "foi atingui os " + percentage + "%.");			
		}
		return shift;
	}
	/**
	 * Method verifyEnrolmentInExecutionCourse.
	 * @param student
	 * @param newShiftInfo
	 */
	private void verifyEnrolmentInExecutionCourse(
		IStudent student,
		ITurno newShift,
		ISuportePersistente sp)
		throws ExcepcaoPersistencia, MessageException {

		IDisciplinaExecucao executionCourse = newShift.getDisciplinaExecucao();
		/*
		 * Test if student is enroled into execution course.
		 */
		IFrequenta executionCourseEnrolment =
			sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
				student,
				executionCourse);
		if (executionCourseEnrolment == null) {
			// Normally this should throw an exception. However, in order to allow 
			// students that have not yet concluded the previous semester to enrole
			// in shifts, the following verification will not be made. Examples of
			// such students are: alunos com acesso a época especial, alunos cujas
			// notas não tenham ainda sido lançadas, etc...
			// Insted we just indicate that the student can frequent the coursre.
			// Scince this frequency does not have an enroled (inscrito) pair, later
			// system maintnence must be done.
			//throw new MessageException(
			//	InfoEnrolmentServiceResult.NOT_ENROLED_INTO_EXECUTION_COURSE);
			executionCourseEnrolment = new Frequenta(student, executionCourse);
			sp.getIFrequentaPersistente().lockWrite(executionCourseEnrolment);
		}

	}
	/**
	 * Method getStudent.
	 * @param infoStudent
	 * @return IStudent
	 */
	private IStudent getStudent(InfoStudent infoStudent, ISuportePersistente sp)
		throws MessageException, ExcepcaoPersistencia {

		IStudent student =
			sp.getIPersistentStudent().readByNumero(infoStudent.getNumber(), infoStudent.getDegreeType());

		if (student == null) {
			throw new MessageException(
				InfoEnrolmentServiceResult.NON_EXISTING_STUDENT);
		}
		return student;
	}

	private class MessageException extends Exception {
		private InfoEnrolmentServiceResult _result;
		public MessageException(int messageType) {
			_result = new InfoEnrolmentServiceResult(messageType);
		}
		/**
		 * Returns the result.
		 * @return InfoEnrolmentServiceResult
		 */
		public InfoEnrolmentServiceResult getResult() {
			return _result;
		}

		/**
		 * Sets the result.
		 * @param result The result to set
		 */
		public void setResult(InfoEnrolmentServiceResult result) {
			_result = result;
		}

	}
}
