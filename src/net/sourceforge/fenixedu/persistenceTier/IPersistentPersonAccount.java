package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.PersonAccount;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentPersonAccount extends IPersistentObject {

    public PersonAccount readByPerson(final Integer personOID) throws ExcepcaoPersistencia;

}