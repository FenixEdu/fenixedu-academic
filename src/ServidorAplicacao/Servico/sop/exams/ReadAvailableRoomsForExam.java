package ServidorAplicacao.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IRoomOccupation;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRoomOccupation;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam implements IServico {
	
	private static ReadAvailableRoomsForExam serviceInstance =
		new ReadAvailableRoomsForExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAvailableRoomsForExam getService() {
		return serviceInstance;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAvailableRoomsForExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadAvailableRoomsForExam";
	}

	public List run(Calendar startDate, Calendar endDate,
					Calendar startTime, Calendar endTime, 
					DiaSemana dayOfWeek)
		throws FenixServiceException {
		
		
		Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
			public Object transform(Object input) {
				return Cloner.copyRoom2InfoRoom((ISala) input);
			}
		};
		
		List availableRooms = null;
//		List availableWithoutLabsRooms = null;
		
		try{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentRoomOccupation persistentRoomOccupation = 
										sp.getIPersistentRoomOccupation();
			List roomOccupations = persistentRoomOccupation.readAll();
			List occupiedInfoRooms = new ArrayList();
//			List labsInfoRooms = new ArrayList();
			
//			Integer laboratorio = new Integer(TipoSala.LABORATORIO);
			
			for(int iterRO=0; iterRO < roomOccupations.size(); iterRO++){
				IRoomOccupation roomOccupation = 
						(IRoomOccupation) roomOccupations.get(iterRO);

//				TipoSala tipoSala = roomOccupation.getRoom().getTipo();
								
//				if(tipoSala.getTipo() == laboratorio)								
//				{
//					InfoRoom infoRoom = 
//							Cloner.copyRoom2InfoRoom(roomOccupation.getRoom());
//					
//					labsInfoRooms.add(infoRoom);
//				}
//				else 
//				{
					boolean occupiedRO = roomOccupation.roomOccupationForDateAndTime(
													startDate,endDate,
													startTime,endTime,dayOfWeek);				
					if(occupiedRO){	
						InfoRoom infoRoom = 
							Cloner.copyRoom2InfoRoom(roomOccupation.getRoom());	
					
						occupiedInfoRooms.add(infoRoom);				
					}			
				
//				}
			}
			
			ISalaPersistente persistentRoom = sp.getISalaPersistente();
			List rooms = persistentRoom.readForRoomReservation();
			
			List allInfoRooms = (List) CollectionUtils.collect(
					rooms,
					TRANSFORM_TO_INFOROOM);
		
			availableRooms =
				(List) CollectionUtils.subtract(
					allInfoRooms,
					occupiedInfoRooms);
			
//			availableWithoutLabsRooms =
//				(List) CollectionUtils.subtract(
//					availableRooms,
//					labsInfoRooms);
			
			
		}
		catch(ExcepcaoPersistencia e){
			throw new FenixServiceException(e);
		}
		
		return availableRooms;
	}

}
