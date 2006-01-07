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

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsMapByRooms implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod, List<InfoRoom> infoRooms) throws Exception {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final List<InfoRoomExamsMap> infoRoomExamMapList = new ArrayList<InfoRoomExamsMap>();

        final InfoPeriod period = calculateExamsSeason(persistentSupport, infoExecutionPeriod
                .getInfoExecutionYear().getYear(), infoExecutionPeriod.getSemester().intValue());

        final Calendar startSeason1 = period.getStartDate();
        final Calendar endSeason2 = period.getEndDate();

        if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            // The calendar must start at a monday
            int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
            startSeason1.add(Calendar.DATE, shiftDays);
        }

        for (final InfoRoom infoRoom : infoRooms) {
            final InfoRoomExamsMap infoRoomExamsMap = new InfoRoomExamsMap();

            infoRoomExamsMap.setInfoRoom(infoRoom);
            infoRoomExamsMap.setStartSeason1(startSeason1);
            infoRoomExamsMap.setEndSeason1(null);
            infoRoomExamsMap.setStartSeason2(null);
            infoRoomExamsMap.setEndSeason2(endSeason2);

            final List<Exam> exams = persistentSupport.getIPersistentExam()
                    .readByRoomAndExecutionPeriod(infoRoom.getNome(), infoExecutionPeriod.getName(),
                            infoExecutionPeriod.getInfoExecutionYear().getYear());
            infoRoomExamsMap.setExams(getInfoExams(exams));

            infoRoomExamMapList.add(infoRoomExamsMap);
        }
        return infoRoomExamMapList;
    }

    private List<InfoExam> getInfoExams(List<Exam> exams) {
        final List<InfoExam> result = new ArrayList<InfoExam>(exams.size());
        for (final Exam exam : exams) {
            InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
            // Use one execution course
            infoExam.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exam
                    .getAssociatedExecutionCourses().get(0)));
            result.add(infoExam);
        }
        return result;
    }

    private InfoPeriod calculateExamsSeason(final ISuportePersistente persistentSupport,
            final String year, final int semester) throws ExcepcaoPersistencia {

        final List<ExecutionDegree> executionDegreesList = persistentSupport
                .getIPersistentExecutionDegree().readByExecutionYear(year);

        Calendar startSeason1 = null, endSeason2 = null;
        Calendar startExams, endExams;

        for (final ExecutionDegree executionDegree : executionDegreesList) {
            if (semester == 1) {
                startExams = executionDegree.getPeriodExamsFirstSemester().getStartDate();
                endExams = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
            } else {
                startExams = executionDegree.getPeriodExamsSecondSemester().getStartDate();
                endExams = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
            }
            if (startSeason1 == null || startExams.before(startSeason1)) {
                startSeason1 = startExams;
            }
            if (endSeason2 == null || endExams.after(endSeason2)) {
                endSeason2 = endExams;
            }
        }
        return new InfoPeriod(startSeason1, endSeason2);
    }
}