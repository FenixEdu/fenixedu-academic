/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/05/26
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoViewRoomSchedule;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAllRoomsLessons implements IServico {

	private static ReadAllRoomsLessons _servico = new ReadAllRoomsLessons();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAllRoomsLessons getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAllRoomsLessons() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadAllRoomsLessons";
	}

	public List run(InfoExecutionPeriod infoExecutionPeriod) {

		List infoViewRoomScheduleList = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);

			ISalaPersistente roomDAO = sp.getISalaPersistente();
			IAulaPersistente lessonDAO = sp.getIAulaPersistente();
			ITurnoAulaPersistente shiftLessonDAO =
				sp.getITurnoAulaPersistente();

			List rooms = roomDAO.readAll();
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