package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadEmptyRoomsService implements IService {
	
	public ReadEmptyRoomsService() {
	}

	

	public Object run(
		InfoRoom infoRoom,
		InfoLesson infoLesson,
		InfoExecutionPeriod infoExecutionPeriod)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente lessonDAO = sp.getIAulaPersistente();
			ISalaPersistente roomDAO = sp.getISalaPersistente();

			// Check is time interval is valid

			if (!validTimeInterval(infoLesson)) {
				throw new InvalidTimeInterval();
			}

			// Read all Rooms with a capacity
			List roomList =
				roomDAO.readSalas(
					null,
					null,
					null,
					null,
					infoRoom.getCapacidadeNormal(),
					null);

			Iterator roomListIterator = roomList.iterator();

			List infoRoomList = new ArrayList();

			while (roomListIterator.hasNext()) {
				ISala element = (ISala) roomListIterator.next();
				try {
					InfoRoom infoRoomElement =
						Cloner.copyRoom2InfoRoom(element);
					infoRoomList.add(infoRoomElement);
				} catch (IllegalArgumentException e) {
					// ignored
				}
			}
			// remove predicate
			infoRoomList =
				(List) CollectionUtils.select(
					infoRoomList,
					new RoomLessonPredicate());

			IAula lesson = Cloner.copyInfoLesson2Lesson(infoLesson);
            
			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);
			
			//List lessonList = lessonDAO.readLessonsInPeriod(lesson);
			List lessonList =
				lessonDAO.readLessonsInBroadPeriodInAnyRoom(lesson,executionPeriod);

			Iterator lessonIterator = lessonList.iterator();

			/* remove lesson's rooms from room list */
			while (lessonIterator.hasNext()) {
				IAula lessonAux = (IAula) lessonIterator.next();
				InfoLesson infoLessonAux =
					Cloner.copyILesson2InfoLesson(lessonAux);
				if (infoLesson.getIdInternal() != null && !infoLesson.getIdInternal().equals(infoLessonAux.getIdInternal())) {
					infoRoomList.remove(infoLessonAux.getInfoSala());
				} else if (infoLesson.getIdInternal() == null) {
					infoRoomList.remove(infoLessonAux.getInfoSala());
				}
			}
			return infoRoomList;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private class RoomLessonPredicate implements Predicate {
		public RoomLessonPredicate() {
		}
		/**
		 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
		 */
		public boolean evaluate(Object listElement) {
			InfoRoom infoRoom = (InfoRoom) listElement;
			return !infoRoom.getNome().endsWith(".");
		}
	}

	private boolean validTimeInterval(InfoLesson lesson) {
		boolean result = true;

		if (lesson.getInicio().getTime().getTime()
			>= lesson.getFim().getTime().getTime()) {
			result = false;
		}

		return result;
	}

	/**
	 */
	public class InvalidTimeInterval extends FenixServiceException {

		/**
		 * 
		 */
		InvalidTimeInterval() {
			super();
		}

	}
}
