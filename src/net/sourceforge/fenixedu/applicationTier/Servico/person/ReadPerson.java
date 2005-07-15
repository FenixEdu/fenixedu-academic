/*
 * Created on 19/01/2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class ReadPerson extends ReadDomainObjectService implements IService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPessoaPersistente();
    }

    protected InfoObject newInfoFromDomain(IDomainObject domainObject) {
        return InfoPersonWithInfoCountry.newInfoFromDomain((IPerson) domainObject);
    }

    protected Class getDomainObjectClass() {
        return Person.class;
    }

}
