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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoViewRoomSchedule;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
			for (int i = 0; i < rooms.size(); i++) {
				InfoViewRoomSchedule infoViewRoomSchedule =
					new InfoViewRoomSchedule();
				ISala room = (ISala) rooms.get(i);
				List lessonList =
					lessonDAO.readByRoomAndExecutionPeriod(
						room,
						executionPeriod);
				Iterator iterator = lessonList.iterator();
				List infoLessonsList = new ArrayList();
				while (iterator.hasNext()) {
					IAula elem = (IAula) iterator.next();
					InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);					
					ITurno shift = elem.getShift();
					if (shift == null)
					{
						continue;
					}
					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
					infoLesson.setInfoShift(infoShift);
					
					infoLessonsList.add(infoLesson);
				}

				infoViewRoomSchedule.setInfoRoom(
					Cloner.copyRoom2InfoRoom(room));
				infoViewRoomSchedule.setRoomLessons(infoLessonsList);				
				infoViewRoomScheduleList.add(infoViewRoomSchedule);
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoViewRoomScheduleList;
	}
}