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
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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

	public Object run(InfoRoom infoRoom, InfoLesson infoLesson) {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IAulaPersistente lessonDAO = sp.getIAulaPersistente();
			ISalaPersistente roomDAO = sp.getISalaPersistente();
			
			
			List roomList = roomDAO.readSalas(
							null,
							null,
							null,
							null,
							infoRoom.getCapacidadeNormal(),
							null);
			
			System.out.println("Tenho salas:"+roomList.size());
			
			
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

			System.out.println("Tenho info salas:"+infoRoomList.size());
			
			IAula lesson = Cloner.copyInfoLesson2Lesson(infoLesson);
	
			
			List lessonList = lessonDAO.readLessonsInPeriod(lesson);
			
			System.out.println("Tenho aulas:"+lessonList.size());
			
			List infoLessonList = new ArrayList();
						
			Iterator lessonIterator = lessonList.iterator();
			
			while (lessonIterator.hasNext()) {
				IAula lessonAux = (IAula) lessonIterator.next();
				InfoLesson infoLessonAux = Cloner.copyLesson2InfoLesson(lessonAux);
				infoRoomList.remove(infoLessonAux.getInfoSala());
			}
			return infoRoomList;
			
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			return null;
		} catch (Exception e){
			e.printStackTrace(System.out);
			return null;
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
 	

}
