/*
 * ReadExamsMap.java
 * 
 * Created on 2003/05/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadAllRoomsExamsMap {

    @Atomic
    public static List run(InfoExecutionPeriod infoExecutionPeriod) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        // Object to be returned
        List<InfoRoomExamsMap> infoRoomExamMapList = new ArrayList<InfoRoomExamsMap>();

        // Exam seasons hardcoded because this information
        // is not yet available from the database
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2005);
        startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
        startSeason1.set(Calendar.DAY_OF_MONTH, 3);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2005);
        endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 12);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

        List<AllocatableSpace> rooms = AllocatableSpace.getAllActiveAllocatableSpacesExceptLaboratoriesForEducation();
        for (final AllocatableSpace room : rooms) {
            InfoRoomExamsMap infoExamsMap = new InfoRoomExamsMap();

            // Set Exam Season info
            infoExamsMap.setInfoRoom(InfoRoom.newInfoFromDomain(room));
            infoExamsMap.setStartSeason1(startSeason1);
            infoExamsMap.setEndSeason1(null);
            infoExamsMap.setStartSeason2(null);
            infoExamsMap.setEndSeason2(endSeason2);

            List exams =
                    Exam.getAllByRoomAndExecutionPeriod(room.getNome(), infoExecutionPeriod.getName(), infoExecutionPeriod
                            .getInfoExecutionYear().getYear());
            infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));

            infoRoomExamMapList.add(infoExamsMap);
        }

        return infoRoomExamMapList;
    }

    private static final Transformer TRANSFORM_EXAM_TO_INFOEXAM = new Transformer() {
        @Override
        public Object transform(Object exam) {
            InfoExam infoExam =
                    InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
                            .newInfoFromDomain((Exam) exam);
            infoExam.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(((Exam) exam).getAssociatedExecutionCourses()
                    .iterator().next()));
            return infoExam;
        }
    };

}