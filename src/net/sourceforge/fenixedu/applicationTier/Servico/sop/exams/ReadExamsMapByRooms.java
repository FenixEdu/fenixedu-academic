/*
 * ReadExamsMapByRoom.java
 * 
 * Created on 2004/02/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsMapByRooms implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod, List infoRooms) {

        // Object to be returned
        List infoRoomExamMapList = new ArrayList();

        // Exam seasons hardcoded because this information
        // is not yet available from the database
        /*
         * Calendar startSeason1 = Calendar.getInstance();
         * startSeason1.set(Calendar.YEAR, 2004);
         * startSeason1.set(Calendar.MONTH, Calendar.JUNE);
         * startSeason1.set(Calendar.DAY_OF_MONTH, 14);
         * startSeason1.set(Calendar.HOUR_OF_DAY, 0);
         * startSeason1.set(Calendar.MINUTE, 0);
         * startSeason1.set(Calendar.SECOND, 0);
         * startSeason1.set(Calendar.MILLISECOND, 0); Calendar endSeason2 =
         * Calendar.getInstance(); endSeason2.set(Calendar.YEAR, 2004);
         * endSeason2.set(Calendar.MONTH, Calendar.JULY);
         * endSeason2.set(Calendar.DAY_OF_MONTH, 24);
         * endSeason2.set(Calendar.HOUR_OF_DAY, 0);
         * endSeason2.set(Calendar.MINUTE, 0); endSeason2.set(Calendar.SECOND,
         * 0); endSeason2.set(Calendar.MILLISECOND, 0);
         */
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            //List rooms = sp.getISalaPersistente().readForRoomReservation();
            InfoRoom room = null;
            InfoRoomExamsMap infoExamsMap = null;

            // Translate to execute following queries
            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            IPeriod period = calculateExamsSeason(executionPeriod);
            Calendar startSeason1 = period.getStartDate();
            Calendar endSeason2 = period.getEndDate();
            // The calendar must start at a monday
            if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
                startSeason1.add(Calendar.DATE, shiftDays);
            }

            for (int i = 0; i < infoRooms.size(); i++) {
                room = (InfoRoom) infoRooms.get(i);
                infoExamsMap = new InfoRoomExamsMap();
                // Set Exam Season info
                infoExamsMap.setInfoRoom(room);
                infoExamsMap.setStartSeason1(startSeason1);
                infoExamsMap.setEndSeason1(null);
                infoExamsMap.setStartSeason2(null);
                infoExamsMap.setEndSeason2(endSeason2);
                List exams = sp.getIPersistentExam().readByRoomAndExecutionPeriod(
                        Cloner.copyInfoRoom2Room(room), executionPeriod);
                infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));
                infoRoomExamMapList.add(infoExamsMap);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return infoRoomExamMapList;
    }

    private Transformer TRANSFORM_EXAM_TO_INFOEXAM = new Transformer() {
        public Object transform(Object exam) {
            InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam) exam);
            infoExam.setInfoExecutionCourse((InfoExecutionCourse) Cloner
                    .get((IExecutionCourse) ((IExam) exam).getAssociatedExecutionCourses().get(0)));
            return infoExam;
        }
    };

    private Period calculateExamsSeason(IExecutionPeriod executionPeriod) throws Exception {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            int semester = executionPeriod.getSemester().intValue();

            List executionDegreesList = sp.getIPersistentExecutionDegree().readByExecutionYear(
                    executionPeriod.getExecutionYear().getYear());
            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreesList.get(0);
            
            Calendar startSeason1 = null;
            Calendar endSeason2 = null;
            if (semester == 1) {
                startSeason1 = executionDegree.getPeriodExamsFirstSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
            } else {
                startSeason1 = executionDegree.getPeriodExamsSecondSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
            }

            for (int i = 1; i < executionDegreesList.size(); i++) {
                executionDegree = (IExecutionDegree) executionDegreesList.get(i);
                Calendar startExams;
                Calendar endExams;
                if (semester == 1) {
                    startExams = executionDegree.getPeriodExamsFirstSemester().getStartDate();
                    endExams = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
                } else {
                    startExams = executionDegree.getPeriodExamsSecondSemester().getStartDate();
                    endExams = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
                }
                if (startExams.before(startSeason1)) {
                    startSeason1 = startExams;
                }
                if (endExams.after(endSeason2)) {
                    endSeason2 = endExams;
                }

            }
            return new Period(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season", e);
        }
    }
}