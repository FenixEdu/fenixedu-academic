/*
 * ExamRoomDistribution.java
 *
 * Created on 2003/05/28
 */

package ServidorAplicacao.Servico.teacher;

/**
 *
 * @author João Mota
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ExamRoomDistribution implements IServico {

	private static ExamRoomDistribution _servico = new ExamRoomDistribution();
	/**
	 * The singleton access method of this class.
	 **/
	public static ExamRoomDistribution getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ExamRoomDistribution() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ExamRoomDistribution";
	}

	public Boolean run(Integer executionCourseCode, Integer examCode, List roomsIds, boolean sms)
		throws FenixServiceException {

		Boolean result = new Boolean(false);
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			ISalaPersistente persistentRoom = sp.getISalaPersistente();

			IFrequentaPersistente persistentAttends =
				sp.getIFrequentaPersistente();
			IPersistentExamStudentRoom persistentExamStudentRoom =
				sp.getIPersistentExamStudentRoom();
			IExam exam =
				(IExam) persistentExam.readByOId(new Exam(examCode), true);
			if (exam == null) {
				throw new InvalidArgumentsServiceException("exam");
			}

			List students = exam.getStudentsEnrolled();
			if (students == null) {
				List executionCourses = exam.getAssociatedExecutionCourses();
				Iterator iterCourse = executionCourses.iterator();
				while (iterCourse.hasNext()) {
					students.addAll(
						persistentAttends.readByExecutionCourse(
							(IDisciplinaExecucao) iterCourse.next()));
				}

			}

			Iterator iterRoom = roomsIds.iterator();
			List rooms = new ArrayList();
			while (iterRoom.hasNext()) {
				ISala room =
					(ISala) persistentRoom.readByOId(
						new Sala((Integer) iterRoom.next()),
						true);
				if (room == null) {
					throw new InvalidArgumentsServiceException("room");
				}
				rooms.add(room);
			}
			if (!exam.getAssociatedRooms().containsAll(rooms)) {
				throw new InvalidArgumentsServiceException("rooms");
			}

			Iterator iter = rooms.iterator();
			while (iter.hasNext()) {
				ISala room = (ISala) iter.next();
				int i = 0;
				while (i <= room.getCapacidadeExame().intValue()) {
					IStudent student =
						(IStudent) getRandomObjectFromList(students);
					IExamStudentRoom examStudentRoom =
						persistentExamStudentRoom.readBy(exam, student, room);
					if (examStudentRoom == null) {
						examStudentRoom =
							new ExamStudentRoom(
								exam,
								(IStudent) getRandomObjectFromList(students),
								room);
						persistentExamStudentRoom.lockWrite(examStudentRoom);		
					} else {
						persistentExamStudentRoom.lockWrite(examStudentRoom);
						examStudentRoom.setRoom(room);
					}
					
					if (sms) {
						sendSMSToStudent(examStudentRoom);
					}
					i++;
				}
			}
			if (students.size() > 0) {
				throw new InvalidArgumentsServiceException("students");
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return result;

	}

	/**
	 * @param examStudentRoom
	 */
	private void sendSMSToStudent(IExamStudentRoom examStudentRoom) {
		// TODO fill this method when we have sms

	}

	private Object getRandomObjectFromList(List list) {
		Random randomizer = new Random();
		int pos = randomizer.nextInt(list.size() + 1);
		return list.remove(pos);

	}

}