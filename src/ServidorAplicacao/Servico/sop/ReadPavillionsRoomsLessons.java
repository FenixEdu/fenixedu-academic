/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/05/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoLessonWithInfoRoomAndInfoExecutionCourse;
import DataBeans.InfoRoom;
import DataBeans.InfoViewRoomSchedule;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

import commons.CollectionUtils;

public class ReadPavillionsRoomsLessons implements IService {

    private static ReadPavillionsRoomsLessons _servico = new ReadPavillionsRoomsLessons();

    /**
     * The singleton access method of this class.
     */
    public static ReadPavillionsRoomsLessons getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadPavillionsRoomsLessons() {
    }
    public List run(List pavillions, InfoExecutionPeriod infoExecutionPeriod) {

        final List infoViewRoomScheduleList = new ArrayList();
        final Map roomViews = new HashMap();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionPeriod executionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                    .newDomainFromInfo(infoExecutionPeriod);

            ISalaPersistente roomDAO = sp.getISalaPersistente();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            // Read pavillions rooms
            List rooms = roomDAO.readByPavillions(pavillions);

            // Read rooms classes
            List roomNames = (List) CollectionUtils.collect(rooms, new Transformer() {
                public Object transform(Object input) {
                    ISala room = (ISala) input;
                    InfoViewRoomSchedule roomSchedule = new InfoViewRoomSchedule();
                    roomSchedule.setRoomLessons(new ArrayList());
                    roomSchedule.setInfoRoom(InfoRoom.newInfoFromDomain(room));
                    roomViews.put(room.getNome(), roomSchedule);
                    // keep the reference on the return list
                    infoViewRoomScheduleList.add(roomSchedule);
                    return room.getNome();
                }
            });

            List lessonList = lessonDAO.readByRoomNamesAndExecutionPeriod(roomNames, executionPeriod);

            for (int i = 0; i < lessonList.size(); i++) {
                IAula lesson = (IAula) lessonList.get(i);
                String roomName = lesson.getSala().getNome();
                InfoViewRoomSchedule roomSchedule = (InfoViewRoomSchedule) roomViews.get(roomName);

                InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoExecutionCourse
                        .newInfoFromDomain(lesson);
                
                roomSchedule.getRoomLessons().add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewRoomScheduleList;
    }
}