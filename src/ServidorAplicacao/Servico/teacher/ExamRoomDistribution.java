/*
 * ExamRoomDistribution.java
 * 
 * Created on 2003/05/28
 */

package ServidorAplicacao.Servico.teacher;

/**
 * @author João Mota
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.Sala;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ExamRoomDistribution implements IService {

    public final static int OUT_OF_ENROLLMENT_PERIOD = 1;

    /**
     * The actor of this class.
     */
    public ExamRoomDistribution() {
    }

    public Boolean run(Integer executionCourseCode, Integer examCode,
            List roomsIds, Boolean sms, Boolean enrolledStudents)
            throws FenixServiceException {
        Boolean result = new Boolean(false);
        List students = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            ISalaPersistente persistentRoom = sp.getISalaPersistente();

            IFrequentaPersistente persistentAttends = sp
                    .getIFrequentaPersistente();
            IPersistentExamStudentRoom persistentExamStudentRoom = sp
                    .getIPersistentExamStudentRoom();
            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examCode);
            if (exam == null) {
                throw new InvalidArgumentsServiceException("exam");
            }

            if (!enrolledStudents.booleanValue()
                    || exam.getEnrollmentEndDay() == null) {
                Calendar examDay = exam.getDay();
                Calendar today = Calendar.getInstance();
                //                Calendar twoWeeksBeforeExam = (Calendar) examDay.clone();
                //                twoWeeksBeforeExam.add(Calendar.DATE, -14);

                if (today.after(examDay)) {
                    throw new FenixServiceException(
                            ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD);
                }

                List executionCourses = exam.getAssociatedExecutionCourses();
                Iterator iterCourse = executionCourses.iterator();
                while (iterCourse.hasNext()) {
                    List attends = persistentAttends
                            .readByExecutionCourse((IExecutionCourse) iterCourse
                                    .next());
                    students.addAll(CollectionUtils.collect(attends,
                            new Transformer() {

                                public Object transform(Object arg0) {
                                    IFrequenta frequenta = (IFrequenta) arg0;
                                    return frequenta.getAluno();
                                }
                            }));
                }

            } else {
                Calendar endEnrollmentDay = exam.getEnrollmentEndDay();
                Calendar endHourDay = exam.getEnrollmentEndTime();

                endEnrollmentDay.set(Calendar.HOUR_OF_DAY, 0);
                endEnrollmentDay.set(Calendar.MINUTE, 0);
                endEnrollmentDay.roll(Calendar.HOUR_OF_DAY, endHourDay
                        .get(Calendar.HOUR_OF_DAY));
                endEnrollmentDay.roll(Calendar.MINUTE, endHourDay
                        .get(Calendar.MINUTE));

                Calendar examDay = exam.getDay();
                Calendar today = Calendar.getInstance();

                if (today.after(examDay) || today.before(endEnrollmentDay)) {
                    throw new FenixServiceException(
                            ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD);
                }

                List examStudentRoomList = persistentExamStudentRoom
                        .readBy(exam);
                Iterator iterExamStudentRoomList = examStudentRoomList
                        .iterator();
                while (iterExamStudentRoomList.hasNext()) {
                    students.add(((IExamStudentRoom) iterExamStudentRoomList
                            .next()).getStudent());
                }

            }

            List uniqueRooms = removeRepeatedElements(roomsIds);
            Iterator iterRoom = uniqueRooms.iterator();
            List rooms = new ArrayList();
            while (iterRoom.hasNext()) {
                ISala room = (ISala) persistentRoom.readByOID(Sala.class,
                        (Integer) iterRoom.next());
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
                        IExamStudentRoom examStudentRoom = persistentExamStudentRoom
                                .readBy(exam, student);
                        if (examStudentRoom == null) {
                            examStudentRoom = new ExamStudentRoom();
                            persistentExamStudentRoom
                                    .simpleLockWrite(examStudentRoom);
                            examStudentRoom.setExam(exam);
                            examStudentRoom.setRoom(room);
                            examStudentRoom.setStudent(student);
                        } else {
                            persistentExamStudentRoom
                                    .simpleLockWrite(examStudentRoom);
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
        // TODO: Send SMS method: fill this method when we have sms

    }

    private Object getRandomObjectFromList(List list) {
        Random randomizer = new Random();
        int pos = randomizer.nextInt(Math.abs(randomizer.nextInt()));
        return list.remove(pos % list.size());
    }

    private List removeRepeatedElements(List initialList) {
        List finalList = new ArrayList();
        Iterator iter = initialList.iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            if (!finalList.contains(object))
                finalList.add(object);
        }
        return finalList;
    }
}