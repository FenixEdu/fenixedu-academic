/*
 * Created on 17/Ago/2004
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IAttendInAttendsSet;

/**
 * @author joaosa & rmalo
 */
public interface IPersistentAttendInAttendsSet extends IPersistentObject{

	public List readAll() throws ExcepcaoPersistencia;

	public void delete(IAttendInAttendsSet attendInAttendsSet) 
		throws ExcepcaoPersistencia;
		
}
