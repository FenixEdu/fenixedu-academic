/*
 * Created on 19 de Julho de 2004, 15:30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servico ReadLessonsAndExamsInWeekAndRoom.
 * 
 * @author Ana e Ricardo
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExam;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ICursoExecucao;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.Period;
import Dominio.RoomOccupation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadLessonsAndExamsInWeekAndRoom implements IService {

    public ReadLessonsAndExamsInWeekAndRoom() {
    }

    public final String getNome() {
        return "ReadLessonsAndExamsInWeekAndRoom";
    }

    public List run(InfoRoom infoRoom, Calendar day) {
        List infoShowOccupations = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            IPersistentExam examDAO = sp.getIPersistentExam();

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

            Calendar startDay = Calendar.getInstance();
            startDay.setTimeInMillis(day.getTimeInMillis());
            startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
            Calendar endDay = Calendar.getInstance();
            endDay.setTimeInMillis(startDay.getTimeInMillis());
            endDay.add(Calendar.DATE, 6);
            Period weekPeriod = new Period(day, endDay);
            ISala room = Cloner.copyInfoRoom2Room(infoRoom);

            Period lessonsPeriod = calculateLessonsSeason(executionPeriod);
            if (lessonsPeriod.intersectPeriods(weekPeriod)) {
                //adicionar as aulas
                List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);
                Iterator iterator = lessonList.iterator();

                while (iterator.hasNext()) {
                    IAula aula = (IAula) iterator.next();
                    IRoomOccupation roomOccupation = aula.getRoomOccupation();
                    IPeriod period = roomOccupation.getPeriod();
                    if (period.intersectPeriods(weekPeriod)) {

                        boolean add = true;
                        if ((roomOccupation.getFrequency().intValue() == RoomOccupation.QUINZENAL)
                                && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period,
                                        roomOccupation.getWeekOfQuinzenalStart().intValue(),
                                        roomOccupation.getDayOfWeek(), weekPeriod))) {
                            add = false;
                        }
                        if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
                            Calendar dayOfLesson = Calendar.getInstance();
                            dayOfLesson.setTimeInMillis(day.getTimeInMillis());
                            dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana()
                                    .intValue()
                                    - Calendar.MONDAY);
                            if (!period.intersectPeriods(new Period(dayOfLesson, dayOfLesson))) {
                                add = false;
                            }
                        }
                        if (add) {
                            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(aula);
                            ITurno shift = aula.getShift();
                            if (shift == null) {
                                continue;
                            }
                            InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                            infoLesson.setInfoShift(infoShift);

                            infoShowOccupations.add(infoLesson);
                        }
                    }
                }
            }

            //adicionar os exames
            List examList = examDAO.readByRoomAndWeek(room, day);
            Iterator iteratorExams = examList.iterator();

            while (iteratorExams.hasNext()) {
                IExam elem = (IExam) iteratorExams.next();
                InfoExam infoExam = Cloner.copyIExam2InfoExam(elem);

                infoShowOccupations.add(infoExam);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return infoShowOccupations;
    }

    private Period calculateLessonsSeason(IExecutionPeriod executionPeriod) throws Exception {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            int semester = executionPeriod.getSemester().intValue();

            List executionDegreesList = sp.getIPersistentExecutionDegree().readByExecutionYear(
                    executionPeriod.getExecutionYear().getYear());
            ICursoExecucao executionDegree = (ICursoExecucao) executionDegreesList.get(0);

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
                executionDegree = (ICursoExecucao) executionDegreesList.get(i);
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
            return new Period(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season");
        }
    }

}