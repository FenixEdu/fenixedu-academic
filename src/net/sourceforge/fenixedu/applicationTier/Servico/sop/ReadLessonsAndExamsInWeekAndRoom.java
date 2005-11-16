/*
 * Created on 19 de Julho de 2004, 15:30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servico ReadLessonsAndExamsInWeekAndRoom.
 * 
 * @author Ana e Ricardo
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.CalendarUtil;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadLessonsAndExamsInWeekAndRoom implements IService {

    // FIXME duplicated code: this method is (almost?) identical to RoomSiteComponentBuilder.getInfoSiteRoomTimeTable
    public List run(InfoRoom infoRoom, Calendar day, InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia, FenixServiceException {
        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IAulaPersistente lessonDAO = sp.getIAulaPersistente();
        IPersistentExam examDAO = sp.getIPersistentExam();
        ISalaPersistente persistentRoom = sp.getISalaPersistente();

        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());

        Calendar startDay = Calendar.getInstance();
        startDay.setTimeInMillis(day.getTimeInMillis());
        startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
        Calendar endDay = Calendar.getInstance();
        endDay.setTimeInMillis(startDay.getTimeInMillis());
        endDay.add(Calendar.DATE, 6);
        //Period weekPeriod = new Period(day, endDay);

        InfoPeriod lessonsInfoPeriod = calculateLessonsSeason(executionPeriod);
        if (this.intersectPeriods(day, endDay,lessonsInfoPeriod)) {
            //adicionar as aulas
            List lessonList = lessonDAO.readByRoomAndExecutionPeriod(infoRoom.getIdInternal(), executionPeriod.getIdInternal());
            Iterator iterator = lessonList.iterator();

            while (iterator.hasNext()) {
                ILesson aula = (ILesson) iterator.next();
                IRoomOccupation roomOccupation = aula.getRoomOccupation();
                InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);

                IPeriod period = roomOccupation.getPeriod();
                InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                infoRoomOccupation.setInfoPeriod(infoPeriod);

                infoRoomOccupation.setInfoRoom(infoRoom);

                if (this.intersectPeriods(day, endDay, infoPeriod)) {

                    boolean add = true;
                    if ((roomOccupation.getFrequency().intValue() == RoomOccupation.QUINZENAL)
                            && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period.getStartDate(), period.getEndDate(), 
	                            roomOccupation.getWeekOfQuinzenalStart().intValue(),
                                    roomOccupation.getDayOfWeek(), day, endDay, null))) {
                        add = false;
                    }
                    if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
                        Calendar dayOfLesson = Calendar.getInstance();
                        dayOfLesson.setTimeInMillis(day.getTimeInMillis());
                        dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana()
                                .intValue()
                                - Calendar.MONDAY);
                        if (!this.intersectPeriods(dayOfLesson, dayOfLesson, infoPeriod)) {
                            add = false;
                        }
                    }
                    if (add) {
                        InfoLesson infoLesson = InfoLesson.newInfoFromDomain(aula);
                        IShift shift = aula.getShift();
                        if (shift == null) {
                            continue;
                        }
                        InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                        infoLesson.setInfoShift(infoShift);

                        infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                        final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                        infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

                        infoShowOccupations.add(infoLesson);
                    }
                }
            }
        }

        final IRoom room = (IRoom) persistentRoom.readByOID(Room.class, infoRoom.getIdInternal());
        final Date startDate = startDay.getTime();
        final Date endDate = endDay.getTime();
        for (final IRoomOccupation roomOccupation : room.getRoomOccupations()) {
            final IWrittenEvaluation writtenEvaluation = roomOccupation.getWrittenEvaluation();
            if (writtenEvaluation != null) {
                final Date evaluationDate = writtenEvaluation.getDayDate();
                if (!evaluationDate.before(startDate) && !evaluationDate.after(endDate)) {
                    if (writtenEvaluation instanceof IExam) {
                        final IExam exam = (IExam) writtenEvaluation;
                        infoShowOccupations.add(InfoExam.newInfoFromDomain(exam));
                    } else if (writtenEvaluation instanceof IWrittenTest) {
                        final IWrittenTest writtenTest = (IWrittenTest) writtenEvaluation;
                        infoShowOccupations.add(InfoWrittenTest.newInfoFromDomain(writtenTest));
                    }
                }
            }
        }

        return infoShowOccupations;
    }

    private boolean intersectPeriods(Calendar startDate, Calendar endDate, InfoPeriod secondPeriod) {
        while (secondPeriod != null) {
            if (CalendarUtil.intersectDates(startDate, endDate, secondPeriod
                    .getStartDate(), secondPeriod.getEndDate())) {
                return true;
            }
            secondPeriod = secondPeriod.getNextPeriod();
        }
        return false;
    }

    private InfoPeriod calculateLessonsSeason(IExecutionPeriod executionPeriod) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            int semester = executionPeriod.getSemester().intValue();

            List executionDegreesList = sp.getIPersistentExecutionDegree().readByExecutionYear(
                    executionPeriod.getExecutionYear().getYear());
            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreesList.get(0);

            Calendar startSeason1 = null;
            Calendar endSeason2 = null;
            if (semester == 1) {
                startSeason1 = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
            } else {
                startSeason1 = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsSecondSemester().getEndDateOfComposite();
            }

            for (int i = 1; i < executionDegreesList.size(); i++) {
                executionDegree = (IExecutionDegree) executionDegreesList.get(i);
                Calendar startLessons;
                Calendar endLessons;
                if (semester == 1) {
                    startLessons = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
                } else {
                    startLessons = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsSecondSemester()
                            .getEndDateOfComposite();
                }
                if (startLessons.before(startSeason1)) {
                    startSeason1 = startLessons;
                }
                if (endLessons.after(endSeason2)) {
                    endSeason2 = endLessons;
                }

            }
            return new InfoPeriod(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season");
        }
    }

}