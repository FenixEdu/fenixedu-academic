/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentPublication extends IPersistentObject
{

    List readAllByPersonAndPublicationType(IPerson pessoa, Integer publicationType)
        throws ExcepcaoPersistencia;
    
    List readAll() throws ExcepcaoPersistencia;

    List readByPublicationsTypeId(Integer publicationTypeId) throws ExcepcaoPersistencia;
}