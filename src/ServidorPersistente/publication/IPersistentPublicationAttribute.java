/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.publication;

import java.util.List;

import Dominio.publication.PublicationType;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentPublicationAttribute extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;

    List readAllByPublicationType(PublicationType publicationType) throws ExcepcaoPersistencia;
}