package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
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

    public PersonAccountOJB() {
    }

    public IPersonAccount readByPerson(IPerson person) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        IPersonAccount personAccount = (IPersonAccount) queryObject(PersonAccount.class, crit);

        // the creation of the new account is required,
        // because everyone is supposed to have one
        if (personAccount == null) {
            personAccount = new PersonAccount(person);
            lockWrite(personAccount);
        }

        return personAccount;
    }
}