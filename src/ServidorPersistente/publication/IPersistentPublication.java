/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.publication;

import java.util.List;

import Dominio.IPessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentPublication extends IPersistentObject
{

    List readAllByPersonAndPublicationType(IPessoa pessoa, Integer publicationType)
        throws ExcepcaoPersistencia;
    
    List readAll() throws ExcepcaoPersistencia;
}
