/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentAuthor extends IPersistentObject {

    Author readAuthorByKeyPerson(Integer keyPerson) throws ExcepcaoPersistencia;

    List readAuthorsBySubName(String stringSubName) throws ExcepcaoPersistencia;
}