package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPersonAccount;
import Dominio.IPessoa;
import Dominio.PersonAccount;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonAccount;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class PersonAccountOJB extends ObjectFenixOJB implements IPersistentPersonAccount {

    public PersonAccountOJB() {
    }

    public IPersonAccount readByPerson(IPessoa person) throws ExcepcaoPersistencia {

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