/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.publication;

import java.util.List;

import Dominio.publication.Author;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentAuthor extends IPersistentObject
{

    Author readAuthorByKeyPerson(Integer keyPerson)
        throws ExcepcaoPersistencia;
    
    List  readAuthorsBySubName(String stringSubName)
		throws ExcepcaoPersistencia;
}
