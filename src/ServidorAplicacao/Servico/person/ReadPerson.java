/*
 * Created on 19/01/2004
 * 
 */
package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.InfoPersonWithInfoCountry;
import Dominio.IDomainObject;
import Dominio.IPerson;
import Dominio.Person;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadPerson extends ReadDomainObjectService implements IService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPessoaPersistente();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return InfoPersonWithInfoCountry.newInfoFromDomain((IPerson) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return Person.class;
    }
}