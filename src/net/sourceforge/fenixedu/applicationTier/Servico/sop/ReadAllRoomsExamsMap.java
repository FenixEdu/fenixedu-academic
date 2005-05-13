/*
 * ReadExamsMap.java
 * 
 * Created on 2003/05/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllRoomsExamsMap implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {

        // Object to be returned
        List infoRoomExamMapList = new ArrayList();

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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List rooms = sp.getISalaPersistente().readForRoomReservation();

        for (int i = 0; i < rooms.size(); i++) {

            IRoom room = (IRoom) rooms.get(i);

            InfoRoomExamsMap infoExamsMap = new InfoRoomExamsMap();

            // Set Exam Season info
            infoExamsMap.setInfoRoom(Cloner.copyRoom2InfoRoom(room));
            infoExamsMap.setStartSeason1(startSeason1);
            infoExamsMap.setEndSeason1(null);
            infoExamsMap.setStartSeason2(null);
            infoExamsMap.setEndSeason2(endSeason2);

            // Translate to execute following queries
            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            List exams = sp.getIPersistentExam().readByRoomAndExecutionPeriod(room.getNome(),
                    executionPeriod.getName(), executionPeriod.getExecutionYear().getYear());
            infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));

            infoRoomExamMapList.add(infoExamsMap);
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

}