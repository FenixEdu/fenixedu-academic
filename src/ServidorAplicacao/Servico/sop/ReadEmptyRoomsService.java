package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ISala;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadEmptyRoomsService implements IServico {
	private static ReadEmptyRoomsService serviceInstance = new ReadEmptyRoomsService();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadEmptyRoomsService getService() {
	  return serviceInstance;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadEmptyRoomsService() { }

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
	  return "ReadEmptyRoomsService";
	}

	public Object run(InfoRoom infoRoom, InfoLesson infoLesson) throws FenixServiceException{

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IAulaPersistente lessonDAO = sp.getIAulaPersistente();
			ISalaPersistente roomDAO = sp.getISalaPersistente();


			// Check is time interval is valid
			
			if (!validTimeInterval(infoLesson)) {
				throw new InvalidTimeIntervalServiceException();
			}

			

			// Read all Rooms with a capacity
			List roomList = roomDAO.readSalas(
							null,
							null,
							null,
							null,
							infoRoom.getCapacidadeNormal(),
							null);
			
			
			Iterator roomListIterator = roomList.iterator();
			
			List infoRoomList = new ArrayList();
			infoRoomList = ListUtils.predicatedList(infoRoomList, new RoomLessonPredicate());			
			
			while (roomListIterator.hasNext()) {
				ISala element = (ISala) roomListIterator.next();
				try{
					infoRoomList.add(Cloner.copyRoom2InfoRoom(element));					
				}catch (IllegalArgumentException e){
					// ignored
				}
				
			}
			
			IAula lesson = Cloner.copyInfoLesson2Lesson(infoLesson);
			
			List lessonList = lessonDAO.readLessonsInPeriod(lesson);
			
			List infoLessonList = new ArrayList();
						
			Iterator lessonIterator = lessonList.iterator();
			
			/* remove lesson's rooms from room list */
			while (lessonIterator.hasNext()) {
				IAula lessonAux = (IAula) lessonIterator.next();
				InfoLesson infoLessonAux = Cloner.copyILesson2InfoLesson(lessonAux);
				infoRoomList.remove(infoLessonAux.getInfoSala());
			}
			return infoRoomList;
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} 
	}
	
	private class RoomLessonPredicate implements Predicate{
		public RoomLessonPredicate(){
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

}
