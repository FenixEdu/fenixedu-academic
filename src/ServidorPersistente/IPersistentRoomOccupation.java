/*
 * Created on 21/Out/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IRoomOccupation;

/**
 * @author Ana e Ricardo
 *
 */
public interface IPersistentRoomOccupation extends IPersistentObject{
	//public List readBy(Sala room) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void delete(IRoomOccupation roomOccupation) throws ExcepcaoPersistencia;
	//public void deleteAll() throws ExcepcaoPersistencia;
}
