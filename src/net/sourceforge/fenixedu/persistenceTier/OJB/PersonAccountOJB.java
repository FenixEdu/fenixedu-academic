package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonAccount;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class PersonAccountOJB extends ObjectFenixOJB implements IPersistentPersonAccount {

    public PersonAccount readByPerson(final Integer personOID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", personOID);
        return (PersonAccount) queryObject(PersonAccount.class, crit);
    }

}