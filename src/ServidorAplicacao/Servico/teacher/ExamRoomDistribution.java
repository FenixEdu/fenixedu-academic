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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.IFrequenta;
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
	public final static int NON_DEFINED_ENROLLMENT_PERIOD = 1;
	public final static int OUT_OF_ENROLLMENT_PERIOD = 2;

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

	public Boolean run(Integer executionCourseCode, Integer examCode, List roomsIds, Boolean sms) throws FenixServiceException {

		Boolean result = new Boolean(false);
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			ISalaPersistente persistentRoom = sp.getISalaPersistente();

			IFrequentaPersistente persistentAttends = sp.getIFrequentaPersistente();
			IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
			IExam exam = (IExam) persistentExam.readByOId(new Exam(examCode), false);
			if (exam == null) {
				throw new InvalidArgumentsServiceException("exam");
			}

			Calendar endEnrollmentDay = exam.getEnrollmentEndDay();
			if (endEnrollmentDay == null) {
				throw new FenixServiceException(ExamRoomDistribution.NON_DEFINED_ENROLLMENT_PERIOD);
			}

			Calendar endHourDay = exam.getEnrollmentEndTime();

			endEnrollmentDay.set(Calendar.HOUR_OF_DAY, 0);
			endEnrollmentDay.set(Calendar.MINUTE, 0);
			endEnrollmentDay.roll(Calendar.HOUR_OF_DAY, endHourDay.get(Calendar.HOUR_OF_DAY));
			endEnrollmentDay.roll(Calendar.MINUTE, endHourDay.get(Calendar.MINUTE));

			Calendar examDay = exam.getDay();
			Calendar today = Calendar.getInstance();

			if (today.after(examDay) || today.before(endEnrollmentDay)) {
				throw new FenixServiceException(ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD);
			}

			List examStudentRoomList = persistentExamStudentRoom.readBy(exam);
			Iterator iterExamStudentRoomList = examStudentRoomList.iterator();
			List students = new ArrayList();
			while (iterExamStudentRoomList.hasNext()) {
				students.add(((IExamStudentRoom) iterExamStudentRoomList.next()).getStudent());
			}

			if (students.isEmpty()) {
				List executionCourses = exam.getAssociatedExecutionCourses();
				Iterator iterCourse = executionCourses.iterator();
				while (iterCourse.hasNext()) {
					List attends = persistentAttends.readByExecutionCourse((IDisciplinaExecucao) iterCourse.next());
					students.addAll(CollectionUtils.collect(attends, new Transformer() {
						public Object transform(Object arg0) {
							IFrequenta frequenta = (IFrequenta) arg0;
							return frequenta.getAluno();
						}
					}));
				}
			}
			Iterator iterRoom = roomsIds.iterator();
			List rooms = new ArrayList();
			while (iterRoom.hasNext()) {
				ISala room = (ISala) persistentRoom.readByOId(new Sala((Integer) iterRoom.next()), true);
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
				int i = 1;
				while (i <= room.getCapacidadeExame().intValue()) {
					if (students.size() > 0) {
						IStudent student = (IStudent) getRandomObjectFromList(students);
						IExamStudentRoom examStudentRoom = persistentExamStudentRoom.readBy(exam, student);
						if (examStudentRoom == null) {
							examStudentRoom = new ExamStudentRoom();
							persistentExamStudentRoom.simpleLockWrite(examStudentRoom);
							examStudentRoom.setExam(exam);
							examStudentRoom.setRoom(room);
							examStudentRoom.setStudent(student);
						} else {
							persistentExamStudentRoom.simpleLockWrite(examStudentRoom);
							examStudentRoom.setRoom(room);
						}

						if (sms.booleanValue()) {
							sendSMSToStudent(examStudentRoom);
						}
					} else {
						break;
					}
					i++;
				}
			}
			if (students.size() > 0) {
				throw new InvalidArgumentsServiceException("students");
			}
			result = new Boolean(true);
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
		int pos = randomizer.nextInt(list.size());
		return list.remove(pos);

	}

}