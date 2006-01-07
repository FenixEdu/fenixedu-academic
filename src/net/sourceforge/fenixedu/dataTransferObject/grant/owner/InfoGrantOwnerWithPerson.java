/*
 * Created on Jun 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.owner;

import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantOwnerWithPerson extends InfoGrantOwner {
    public void copyFromDomain(GrantOwner grantOwner) {
        super.copyFromDomain(grantOwner);
        if (grantOwner != null) {
            setPersonInfo(InfoPersonWithInfoCountry.newInfoFromDomain(grantOwner.getPerson()));
        }
    }

    public static InfoGrantOwner newInfoFromDomain(GrantOwner grantOwner) {
        InfoGrantOwnerWithPerson infoGrantOwner = null;
        if (grantOwner != null) {
            infoGrantOwner = new InfoGrantOwnerWithPerson();
            infoGrantOwner.copyFromDomain(grantOwner);
        }
        return infoGrantOwner;
    }

    public void copyToDomain(InfoGrantOwner infoGrantOwner, GrantOwner grantOwner)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantOwner, grantOwner);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person person = (Person) sp.getIPessoaPersistente().readByOID(Person.class,
                infoGrantOwner.getPersonInfo().getIdInternal());
        grantOwner.setPerson(person);
    }

}
