/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.publication;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author TJBF & PFON
 * 
 */
public interface IPersistentPublicationType extends IPersistentObject
{
	List readAll() throws ExcepcaoPersistencia;
}
